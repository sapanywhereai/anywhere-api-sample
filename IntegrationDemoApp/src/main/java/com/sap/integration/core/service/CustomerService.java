package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.dto.AnwCustomerDto;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Class used for for getting / posting of SAP Anywhere / ERP data.
 */
public class CustomerService extends BaseService {

    public static final String CUSTOMERS = "Customers";

    private final Logger LOG = Logger.getLogger(CustomerService.class);

    public CustomerService(String service, Class<? extends IAnwDto> anwClass, Class<? extends IErpDto> erpClass) {
        super(service, anwClass, erpClass);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    public IErpDto getErpObject(IAnwDto anwDto) {
        AnwCustomerDto anwCustomer = (AnwCustomerDto) anwDto;
        Customer customer = JpaLayer.findFirst(Customer.class, "SELECT c FROM Customer c WHERE c.code = ?1",
                anwCustomer.getCustomerCode());
        if (customer != null) {
            return customer.convert();
        }
        return null;
    }

    @Override
    public List<IErpDto> getErpObjects() {
        List<Customer> customers = JpaLayer
                .find(Customer.class,
                        "SELECT c FROM Customer c where (c.lastUpdateTime > ?1) or (c.lastUpdateTime is NULL) ORDER BY c.lastUpdateTime",
                        erpIntegrationState.getLastSyncTime());

        return (List<IErpDto>) Customer.convert(customers, new ArrayList<IErpDto>());
    }

    @Override
    public void postToErp(List<? extends IAnwDto> anwObjects) {
        int createdCount = 0, updatedCount = 0, skippedUpdate = 0;

        for (IAnwDto anwObject : anwObjects) {
            IErpDto erpObject = getErpObject(anwObject);
            if (erpObject == null) { // create
                Customer c = (Customer) (anwObject.transform()).convert();
                c.setId(null);
                c.persist();
                createdCount++;
            } else { // update
                IErpDto dtoToUpdate = anwObject.transform();
                if (dtoToUpdate.isDifferent(erpObject) && anwObject.getLastSyncTime().isAfter(erpObject.getLastSyncTime())) {
                    Customer c = (Customer) dtoToUpdate.convert();
                    c.setId(erpObject.getId());
                    c.merge();
                    updatedCount++;
                } else {
                    skippedUpdate++;
                }
            }

            if (anwObject.getLastSyncTime().isAfter(anwIntegrationState.getLastSyncTime())) {
                anwIntegrationState.setLastSyncTime(anwObject.getLastSyncTime());
                anwIntegrationState.merge();
            }
        }

        getLogger().info("Successfully created " + createdCount);
        getLogger().info("Successfully updated " + updatedCount);
        getLogger().info("Failed " + (anwObjects.size() - createdCount - updatedCount - skippedUpdate));
    }

    @Override
    public String getAnwQueryFilter(IErpDto erpObject) {
        ErpCustomerDto erpCustomer = (ErpCustomerDto) erpObject;

        return "customerCode eq '" + erpCustomer.getCode() + "'";
    }

}
