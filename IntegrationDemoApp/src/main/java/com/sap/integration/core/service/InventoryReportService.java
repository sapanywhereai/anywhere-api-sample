package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwProductDto;
import com.sap.integration.anywhere.dto.AnwSkuDto;
import com.sap.integration.anywhere.dto.AnwSkuWarehouseInfoDto;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dummy.entity.InventoryReport;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Class used for creating payment reports of SAP Anywhere.
 */
public class InventoryReportService {

    private static final Logger LOG = Logger.getLogger(InventoryReportService.class);

    // inventory reports, where warehouse code is key and values are list of inventory reports
    private static Map<String, List<InventoryReport>> inventoryReportsByWarehouseCodeMap;

    public static void doReports() throws Exception {
        DateTime fromDate = new DateTime("2015-01-01");
        DateTime toDate = new DateTime("2016-01-01");

        LOG.info("Inventory reports from: " + fromDate + " to: " + toDate);

        loadReportsFromSapAnywhere(fromDate, toDate);
        saveReportsToErpDB();
        // loadReportsFromErpDB(fromDate, toDate, fromDate);
        showReports();
    }
    
    /**
     * Get inventory reports from SAP Anywhere, where products creation date is between from and to date.
     * 
     * @param fromDate Date of first SAP Anywhere product creation date
     * @param toDate Date of last SAP Anywhere product creation date
     * @throws Exception
     */
    public static void loadReportsFromSapAnywhere(DateTime fromDate, DateTime toDate) throws Exception {
        inventoryReportsByWarehouseCodeMap = new HashMap<String, List<InventoryReport>>();

        // get all active products
        ProductService productService = new ProductService(ProductService.PRODUCTS, AnwProductDto.class,
                ErpProductDto.class);
        for (IAnwDto anwObject : productService.getAllAnwObjectsInPeriod(fromDate, toDate, "skus")) {
            // no active product check
            AnwProductDto anwProduct = (AnwProductDto) anwObject;
            if (!anwProduct.getStatus().equals("Active")) {
                continue;
            }
            // get sku ids by product
            for (AnwSkuDto anwSku : anwProduct.getSkus()) {
                // create inventory reports
                for (AnwSkuWarehouseInfoDto anwSkuWarehouseInfos : SkuService.getWarehouseInfo(anwSku.getId())
                        .getSkuWarehouseInfoList()) {
                    InventoryReport inventoryReport = new InventoryReport();
                    inventoryReport.setWarehouseCode(anwSkuWarehouseInfos.getWarehouseCode());
                    inventoryReport.setWarehouseName(anwSkuWarehouseInfos.getWarehouseName());
                    inventoryReport.setProductCode(anwProduct.getCode());
                    inventoryReport.setProductName(anwProduct.getName());
                    inventoryReport.setInStock(anwSkuWarehouseInfos.getInStock());
                    inventoryReport.setFromDate(fromDate);
                    inventoryReport.setToDate(toDate);

                    List<InventoryReport> inventoryReports = inventoryReportsByWarehouseCodeMap
                            .get(anwSkuWarehouseInfos.getWarehouseCode());
                    if (inventoryReports == null) {
                        inventoryReports = new ArrayList<InventoryReport>();
                    }
                    inventoryReports.add(inventoryReport);

                    inventoryReportsByWarehouseCodeMap.put(anwSkuWarehouseInfos.getWarehouseCode(),
                            inventoryReports);
                }
            }
        }
    }

    /**
     * Get inventory reports from ERP DB newer than last update date.
     * 
     * @param fromDate Date of first SAP Anywhere product creation date
     * @param toDate Date of last SAP Anywhere product creation date
     * @param lastUpdateDate Date of last inventory report update
     */
    public static void loadReportsFromErpDB(DateTime fromDate, DateTime toDate, DateTime lastUpdateDate) {
        inventoryReportsByWarehouseCodeMap = new HashMap<String, List<InventoryReport>>();

        // get inventory reports by lastUpdateDate
        List<InventoryReport> inventoryReports = JpaLayer.find(InventoryReport.class,
                        "SELECT ir FROM InventoryReport ir WHERE ir.fromDate >= ?1 AND ir.toDate <= ?2 AND ir.lastUpdateTime >= ?3",
                        fromDate, toDate, lastUpdateDate);

        // construct inventory report hash map by warehouse code
        for (InventoryReport inventoryReport : inventoryReports) {
            List<InventoryReport> irFromMap = inventoryReportsByWarehouseCodeMap.get(inventoryReport.getWarehouseCode());
            if (irFromMap == null) {
                irFromMap = new ArrayList<InventoryReport>();
            }
            irFromMap.add(inventoryReport);

            inventoryReportsByWarehouseCodeMap.put(inventoryReport.getWarehouseCode(), irFromMap);
        }
    }

    /**
     * Save loaded inventory reports to ERP DB.
     * 
     * @param lastUpdateDate
     */
    public static void saveReportsToErpDB() {
        if (inventoryReportsByWarehouseCodeMap == null || inventoryReportsByWarehouseCodeMap.isEmpty()) {
            LOG.warn("No inventory reports to save.");
            return;
        }

        // save inventory reports
        for (String warehouseCode : inventoryReportsByWarehouseCodeMap.keySet()) {
            for (InventoryReport inventoryReport : inventoryReportsByWarehouseCodeMap.get(warehouseCode)) {
                inventoryReport.persist();
            }
        }
    }

    /**
     * Show loaded inventory reports.
     */
    public static void showReports() {
        if (inventoryReportsByWarehouseCodeMap == null || inventoryReportsByWarehouseCodeMap.isEmpty()) {
            LOG.warn("No inventory reports to show.");
            return;
        }

        // show inventory reports
        for (String warehouseCode : inventoryReportsByWarehouseCodeMap.keySet()) {
            List<InventoryReport> inventoryReports = inventoryReportsByWarehouseCodeMap.get(warehouseCode);
            // no inventory report for warehouse check
            if (inventoryReports.isEmpty()) {
                LOG.info("No data for warehouse code: " + warehouseCode);
                continue;
            }
            // show header info
            LOG.info("Warehouse code: " + warehouseCode + ", Warehouse name "
                    + inventoryReports.get(0).getWarehouseName());
            // show body info
            for (InventoryReport inventoryReport : inventoryReports) {
                LOG.info("Product code: " + inventoryReport.getProductCode()
                        + ", Product name: " + inventoryReport.getProductName()
                        + ", Stock: " + inventoryReport.getInStock());
            }
        }
    }
}
