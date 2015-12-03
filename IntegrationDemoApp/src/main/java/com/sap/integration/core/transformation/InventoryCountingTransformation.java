package com.sap.integration.core.transformation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.dto.AnwInventoryCountingPostDto;
import com.sap.integration.anywhere.dto.AnwInventoryCountingPostLineDto;
import com.sap.integration.anywhere.dto.AnwSkuDto;
import com.sap.integration.anywhere.dto.AnwWarehouseDto;
import com.sap.integration.core.service.SkuService;
import com.sap.integration.core.service.WarehouseService;
import com.sap.integration.erp.dto.ErpStockDataDto;
import com.sap.integration.erp.dummy.entity.StockData;

public class InventoryCountingTransformation {

    private static final Logger LOG = Logger.getLogger(InventoryCountingTransformation.class);

    /**
     * Method, which will: <br>
     * <ul>
     * <li>creates AnwInventoryCountingDto payload from entered warehouse code and list of inventory countings from ERP</li>
     * <li>verifies, whether warehouse and item/SKU exists in SAP Anywhere. If not, they will not be attached to the payload,
     * which will be returned. This provides check against data inconsistencies.</li>
     * </ul>
     * 
     * @param warehouseCode - warehouse code, which exists in SAP Anywhere <br>
     * @param listOfInventoryCountings - list of inventory counting from ERP <br>
     * @return
     * @throws Exception - in case when some error
     */
    public static AnwInventoryCountingPostDto transform(String warehouseCode,
            List<ErpStockDataDto> listOfInventoryCountings) throws Exception {

        boolean hasAtLeastOneSku = false;

        // entered value can not be null or empty
        if (warehouseCode != null && warehouseCode.trim().length() > 0
                && listOfInventoryCountings != null && listOfInventoryCountings.size() > 0) {

            // identify warehouse data - check whether warehouse exists in SAP Anywhere - check, whether data are consistent
            AnwWarehouseDto warehouse = WarehouseService.findWarehouse(warehouseCode);

            // if warehouse exists in SAP Anywhere
            if (warehouse != null && warehouse.getId() != null) {
                // create object of inventory counting for SAP Anywhere
                AnwInventoryCountingPostDto icdto = new AnwInventoryCountingPostDto();

                icdto.setWarehouse(warehouse); // warehouse used for inventory counting
                icdto.setCreationTime("2015-09-14T10:55:37.000Z"); // for demo purposes it is hardcoded - it should be real value
                icdto.setCreatorName("Demo APP creator"); // for demo purposes it is hardcoded - it should be real value
                icdto.setOwnerCode(null);
                icdto.setStatus("OPEN");
                icdto.setUpdateTime(null);
                icdto.setRemark("Posted from demo application"); // for demo purposes it is hardcoded - it should be something
                                                                 // meaningful

                // for every inventory counting from ERP create inventory counting line for SAP Anywhere
                for (ErpStockDataDto inventoryCounting : listOfInventoryCountings) {

                    // get information about SKU from SAP Anywhere
                    AnwSkuDto sku = SkuService.findSku(inventoryCounting.getProductCode());

                    // if SKU exists in SAP Anywhere, line will be added to the payload
                    if (sku != null && sku.getId() != null) {

                        // identify SKU data and add SKU data to the inventory counting payload
                        AnwInventoryCountingPostLineDto line = new AnwInventoryCountingPostLineDto();
                        line.setSku(sku);
                        line.setId(null);
                        line.setCountedQuantity(inventoryCounting.getQuantity().toString());
                        line.setInventoryUoM(inventoryCounting.getUnitOfMeasure());
                        line.setVariance(null);
                        line.setVariancePercent(null);
                        icdto.addLine(line);

                        hasAtLeastOneSku = true;
                        // if SKU do not exists in SAP Anywhere, just log will be printed and no line will be added to the payload
                    } else {
                        LOG.info("Inventory counting - SKU with code " + inventoryCounting.getProductCode()
                                + " do not exists in SAP Anywhere");
                    }
                }

                return hasAtLeastOneSku ? icdto : null;
            } else {
                LOG.info("Inventory counting - Warehouse with code " + warehouseCode + " do not exists in SAP Anywhere");
                return null;
            }
        } else {
            LOG.info("Inventory counting - null or empty values on input of createPayload() method");
            return null;
        }
    }

    public static final List<ErpStockDataDto> transform(List<StockData> listOfStockData) {
        if (listOfStockData == null) {
            return null;
        } else if (listOfStockData.size() == 0) {
            return new ArrayList<ErpStockDataDto>(0);
        } else {
            List<ErpStockDataDto> listOfErpStockData = new ArrayList<ErpStockDataDto>(listOfStockData.size());

            if (listOfStockData.get(0) instanceof StockData) {
                for (StockData stockData : listOfStockData) {
                    ErpStockDataDto stockStatusDto = new ErpStockDataDto()
                            .setId(stockData.getId())
                            .setProductCode(stockData.getProductCode())
                            .setQuantity(stockData.getQuantity())
                            .setUnitOfMeasure(stockData.getUnitOfMeasure())
                            .setWhsCode(stockData.getWhsCode());
                    listOfErpStockData.add(stockStatusDto);
                }
            } else {
                try {
                    for (Object stockData : listOfStockData) {

                        Object[] o = (Object[]) stockData;
                        Long id = (Long) o[0];
                        String productCode = (String) o[1];
                        Long quantity = (Long) o[2];
                        String unitOfMeasure = (String) o[3];
                        String whsCode = (String) o[4];

                        ErpStockDataDto stockStatusDto = new ErpStockDataDto()
                                .setId(id)
                                .setProductCode(productCode)
                                .setQuantity(quantity)
                                .setUnitOfMeasure(unitOfMeasure)
                                .setWhsCode(whsCode);
                        listOfErpStockData.add(stockStatusDto);
                    }
                } catch (Exception e) {
                    LOG.error("Exception: " + e.getMessage());
                }
            }

            return listOfErpStockData;
        }
    }
}
