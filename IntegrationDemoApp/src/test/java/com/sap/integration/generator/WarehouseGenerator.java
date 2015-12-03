package com.sap.integration.generator;

import java.util.List;

import com.sap.integration.erp.dummy.entity.Warehouse;

public class WarehouseGenerator {

    public static void main(String[] args) {
        createDummyData();
        printAllData();
    }

    private static void createDummyData() {
        new Warehouse()
                .setCode("WH01")
                .setName("WH01")
                .persist();

        new Warehouse()
                .setCode("WH02")
                .setName("WH02")
                .persist();

        new Warehouse()
                .setCode("WH03")
                .setName("WH03")
                .persist();
    }

    private static void printAllData() {
        List<Warehouse> listOfWarehouses = Warehouse.getAll(Warehouse.class);

        if (listOfWarehouses != null && listOfWarehouses.size() > 0) {
            for (Warehouse whs : listOfWarehouses) {
                System.out.println(whs);
            }
        }
    }
}
