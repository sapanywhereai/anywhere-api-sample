package com.sap.integration.erp.dummy.conversion;

import java.util.List;

import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.erp.dummy.entity.Customer;

/**
 * Class used for converting ERP customer objects to DB customer objects and vice versa.
 */
public class CustomerConversion {

    /**
     * Convert ERP customer object to DB customer object.
     * 
     * @param erpDto ERP customer data transfer object
     * @return null - if entered parameter is null <br>
     *         Customer - converted customer object <br>
     */
    public static Customer run(final IErpDto erpDto) {
        if (erpDto == null) {
            return null;
        } else {
            ErpCustomerDto erpCustomer = (ErpCustomerDto) erpDto;
            return new Customer()
                    .setName(erpCustomer.getName())
                    .setCode(erpCustomer.getCode());
        }
    }

    /**
     * Convert DB customer object to ERP customer data transfer object.
     * 
     * @param customer DB customer object
     * @return null - if entered parameter is null <br>
     *         IErpDto - converted ERP customer data transfer object <br>
     */
    public static IErpDto run(final Customer customer) {
        if (customer == null) {
            return null;
        } else {
            return new ErpCustomerDto()
                    .setId(customer.getId())
                    .setName(customer.getName())
                    .setCode(customer.getCode())
                    .setLastUpdateTime(customer.getLastUpdateTime());
        }
    }

    /**
     * Generic conversion of DB customer list to ERP customer list and vice versa.
     * 
     * @param customersToConvert DB / ERP customer object list
     * @param toListType - type on which will be conversion provided
     * @return List<?> of converted objects
     */
    @SuppressWarnings("unchecked")
    public static <O, I> List<O> run(final List<I> customersToConvert, final List<O> toListType) {

        if (toListType == null || customersToConvert == null || customersToConvert.isEmpty()) {
            return toListType;
        }

        Class<?> customerClassToConvert = customersToConvert.get(0).getClass();

        // conversion customers to erpCustomers
        if (customerClassToConvert.equals(Customer.class)) {
            for (Customer customer : (List<Customer>) customersToConvert) {
                toListType.add((O) run(customer));
            }
        }
        // conversion erpCustomers to customers
        else if (customerClassToConvert.equals(IErpDto.class)) {
            for (IErpDto erpCustomer : (List<IErpDto>) customersToConvert) {
                toListType.add((O) run(erpCustomer));
            }
        }

        return toListType;
    }
}
