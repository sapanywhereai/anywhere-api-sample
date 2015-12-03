package com.sap.integration.generator;

import java.util.List;

import com.sap.integration.erp.dummy.entity.StockData;

/**
 * Class, which contains methods, which generates sample data.  
 */
public class StockDataGenerator {

    public static void main(String[] args) {
        createDummyData();
        printAllData();
    }

    private static void createDummyData() {
        new StockData()
                .setProductCode("ITEMCODE1")
                .setQuantity(10L)
                .setUnitOfMeasure("Unit")
                .setWhsCode("WH01")
                .persist();
        new StockData()
                .setProductCode("ITEMCODE2")
                .setQuantity(20L)
                .setUnitOfMeasure("Unit")
                .setWhsCode("WH02")
                .persist();
        new StockData()
                .setProductCode("ITEMCODE3")
                .setQuantity(30L)
                .setUnitOfMeasure("Unit")
                .setWhsCode("WH03")
                .persist();
        new StockData()
                .setProductCode("ITEMCODE4")
                .setQuantity(40L)
                .setUnitOfMeasure("Unit")
                .setWhsCode("DONOTEXISTSINSAPANYWHERE")
                .persist();
    }

    private static void printAllData() {
        List<StockData> listOfStockData = StockData.getAll(StockData.class);

        if (listOfStockData != null && listOfStockData.size() > 0) {
            for (StockData ss : listOfStockData) {
                System.out.println(ss);
            }
        }
    }
}
