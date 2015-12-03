package com.sap.integration.core.transformation;

import java.util.ArrayList;
import java.util.List;

import com.sap.integration.erp.dto.ErpWarehouseDto;
import com.sap.integration.erp.dummy.entity.Warehouse;

/**
 * Class, which contains methods for transformation of objects regarded to warehouse objects. <br>
 */
public class WarehouseTransformation {

    /**
     * Method, which transforms listOfDbWarehouses which came from database to list of AnwWarehouseDto objects.
     * 
     * @param listOfWarehouses - list, which will be transformed to the
     * @return List&lt;AnwWarehouseDto&gt;
     */
    public static List<ErpWarehouseDto> transform(List<Warehouse> listOfWarehouses) {

        if (listOfWarehouses == null) {
            return null;
        } else if (listOfWarehouses.size() == 0) {
            return new ArrayList<ErpWarehouseDto>();
        } else {
            List<ErpWarehouseDto> listOfErpWarehouses = new ArrayList<ErpWarehouseDto>(listOfWarehouses.size());

            for (Warehouse warehouse : listOfWarehouses) {
                ErpWarehouseDto anwWarehouse = new ErpWarehouseDto().setCode(warehouse.getCode());
                listOfErpWarehouses.add(anwWarehouse);
            }

            return listOfErpWarehouses;
        }
    }
}
