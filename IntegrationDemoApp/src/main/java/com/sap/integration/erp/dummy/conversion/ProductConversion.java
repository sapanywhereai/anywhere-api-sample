package com.sap.integration.erp.dummy.conversion;

import java.util.List;

import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.erp.dummy.entity.Item;

/**
 * Class used for converting ERP product objects to DB product objects and vice versa.
 */
public class ProductConversion {

    /**
     * Convert ERP product object to DB product object.
     * 
     * @param erpDto ERP product data transfer object
     * @return null - if entered parameter is null <br>
     *         Item - converted product object <br>
     */
    public static Item run(IErpDto erpDto) {
        if (erpDto == null) {
            return null;
        } else {
            ErpProductDto erpProduct = (ErpProductDto) erpDto;
            return new Item()
                    .setItemCode(erpProduct.getItemCode())
                    .setItemName(erpProduct.getItemName());
        }
    }

    /**
     * Convert DB item object to ERP product data transfer object.
     * 
     * @param item DB product object
     * @return null - if entered parameter is null <br>
     *         IErpDto - converted ERP product data transfer object <br>
     */
    public static IErpDto run(Item item) {
        if (item == null) {
            return null;
        } else {
            return new ErpProductDto()
                    .setId(item.getId())
                    .setItemCode(item.getItemCode())
                    .setItemName(item.getItemName())
                    .setLastUpdateTime(item.getLastUpdateTime());
        }
    }

    /**
     * Generic conversion of DB product list to ERP product list and vice versa.
     * 
     * @param itemsToConvert DB / ERP product object list
     * @param toListType - type on which will be conversion provided
     * @return List<?> of converted objects
     */
    @SuppressWarnings("unchecked")
    public static <O, I> List<O> run(List<I> itemsToConvert, List<O> toListType) {

        if (toListType == null || itemsToConvert == null || itemsToConvert.isEmpty()) {
            return toListType;
        }

        Class<?> itemClassToConvert = itemsToConvert.get(0).getClass();

        // conversion items to erpProducts
        if (itemClassToConvert.equals(Item.class)) {
            for (Item item : (List<Item>) itemsToConvert) {
                toListType.add((O) run(item));
            }
        }
        // conversion erpProducts to items
        else if (itemClassToConvert.equals(IErpDto.class)) {
            for (IErpDto erpItems : (List<IErpDto>) itemsToConvert) {
                toListType.add((O) run(erpItems));
            }
        }

        return toListType;
    }
}
