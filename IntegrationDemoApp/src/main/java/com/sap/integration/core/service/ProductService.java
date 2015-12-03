package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.dto.AnwProductDto;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Class used for getting / posting of SAP Anywhere / ERP data.
 */
public class ProductService extends BaseService {

    public static final String PRODUCTS = "Products";

    private final Logger LOG = Logger.getLogger(ProductService.class);

    public ProductService(String service, Class<? extends IAnwDto> anwClass, Class<? extends IErpDto> erpClass) {
        super(service, anwClass, erpClass);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    public IErpDto getErpObject(IAnwDto anwDto) {
        AnwProductDto anwProduct = (AnwProductDto) anwDto;
        Item item = JpaLayer.findFirst(Item.class, "SELECT i FROM Item i WHERE i.itemCode = ?1",
                anwProduct.getCode());
        if (item != null) {
            return item.convert();
        }
        return null;
    }

    @Override
    public List<IErpDto> getErpObjects() {
        List<Item> items = JpaLayer
                .find(Item.class,
                        "SELECT i FROM Item i where (i.lastUpdateTime > ?1) or (i.lastUpdateTime is NULL) ORDER BY i.lastUpdateTime",
                        erpIntegrationState.getLastSyncTime());

        return (List<IErpDto>) Item.convert(items, new ArrayList<IErpDto>());
    }

    @Override
    public void postToErp(List<? extends IAnwDto> anwObjects) {
        int createdCount = 0, updatedCount = 0, skippedUpdate = 0;

        for (IAnwDto anwObject : anwObjects) {
            IErpDto erpObject = getErpObject(anwObject);
            if (erpObject == null) { // create
                Item c = (Item) (anwObject.transform()).convert();
                c.setId(null);
                c.persist();
                createdCount++;
            } else { // update
                IErpDto dtoToUpdate = anwObject.transform();
                if (dtoToUpdate.isDifferent(erpObject) && anwObject.getLastSyncTime().isAfter(erpObject.getLastSyncTime())) {
                    Item c = (Item) anwObject.transform().convert();
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
        ErpProductDto erpProduct = (ErpProductDto) erpObject;

        return "code eq '" + erpProduct.getItemCode() + "'";
    }

}
