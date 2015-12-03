package com.sap.integration.core.integration;

import static com.sap.integration.utils.JsonUtil.getJson;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwInventoryCountingPostDto;
import com.sap.integration.anywhere.dto.AnwWarehouseDto;
import com.sap.integration.core.service.InventoryCountingService;
import com.sap.integration.core.service.WarehouseService;
import com.sap.integration.core.transformation.InventoryCountingTransformation;
import com.sap.integration.erp.dto.ErpStockDataDto;
import com.sap.integration.erp.dto.ErpWarehouseDto;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.Range;
import com.sap.integration.utils.integrationdb.entity.RangeService;

/**
 * Class, which contains methods for integration of inventory counting.
 */
public class InventoryCountingIntegration {

    private static final Logger LOG = Logger.getLogger(InventoryCountingIntegration.class);

    /**
     * Method, which starts integration of inventory counting from ERP to SAP Anywhere. It will: <br>
     * <ul>
     * <li>read data from another ERP</li>
     * <li>process data</li>
     * <li>validate data - check data inconsistencies</li>
     * <li>create payloads of inventory counting for SAP Anywhere</li>
     * <li>send payloads to the SAP Anywhere</li>
     * </ul>
     * 
     * @throws Exception possible exception during the processing
     */
    public static final void syncToSapAnywhere() throws Exception {

        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration ERP to SAP Anywhere");
        LOG.debug("Inventory counting - used set up: USE_DEFAULT_WAREHOUSE=" + Property.getUseDefaultWarehouse()
                + ", DEFAULT_ANW_WAREHOUSE=" + Property.getDefaultAnwWarehouse());

        if (Property.getUseDefaultWarehouse()) {
            syncByDefaultWarehouse();
        } else {
            syncByErpWarehouses();
        }

        // log end
        LOG.info("End of integration ERP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

    /**
     * Method, which synchronize stock status by using default warehouse defined in configuration file config.properties. It
     * suppose, that information about warehouses between custom ERP and SAP Anywhere are not synchronized. All information about
     * stock status are saved on defined default warehouse.
     * 
     * @throws Exception possible exception during the processing
     */
    private static final void syncByDefaultWarehouse() throws Exception {

        LOG.debug("Inventory counting - synchronization by using default warehouses");

        // load warehouse code from configuration file
        String defaultWarehouseCode = Property.getDefaultAnwWarehouse();

        // if warehouse code is not null or is not empty, it is possible to continue
        if (StringUtils.isNotBlank(defaultWarehouseCode)) {

            // find information about default warehouse in SAP Anywhere/verify whether warehouse exists in SAP Anywhere
            AnwWarehouseDto warehouse = WarehouseService.getDefaultAnwWarehouse();

            // identify which range of data will be synchronized
            Range rangeForProcessing = RangeService.getRangeForProcessing();

            if (rangeForProcessing != null
                    && rangeForProcessing.getRangeFrom() != null
                    && rangeForProcessing.getRangeTo() != null
                    && rangeForProcessing.getRangeFrom() <= rangeForProcessing.getRangeTo()) {

                // if default warehouse exists, it is possible to continue
                if (warehouse != null && warehouse.getId() != null && warehouse.getCode() != null) {

                    // get data about inventory countings from custom ERP
                    List<ErpStockDataDto> listOfInventoryCountings = InventoryCountingService
                            .getLastInventoryCountingsFromErp(rangeForProcessing);

                    // if data about inventory counting from ERP are not null or empty payload will be created and posted
                    if (listOfInventoryCountings != null && listOfInventoryCountings.size() > 0) {

                        // create payload, which will be posted
                        AnwInventoryCountingPostDto payload = InventoryCountingTransformation
                                .transform(warehouse.getCode(), listOfInventoryCountings);

                        // if payload is not null, it will be posted
                        if (payload != null) {
                            // log for demo purposes
                            LOG.debug("Inventory counting - created payload: \n" + getJson(payload));

                            // post payload to SAP Anywhere
                            InventoryCountingService.sendPayloadToSapAnywhere(payload);
                        } else {
                            LOG.debug("Inventory counting - created payload for default warehouse " + warehouse.getCode()
                                    + " is null");
                        }
                    } else {
                        LOG.debug("Inventory counting - data about inventory countings from ERP are null or empty");
                    }
                } else {
                    LOG.error("Inventory counting - default warehouse was not possible to find in SAP Anywhere");
                }

                // save ranges, which was processed. these processed ranges of data will not be processed again in another
                // iteration of integration
                RangeService.saveRange(rangeForProcessing);
            } else {
                LOG.info("Inventory counting - nothing to integrate");
            }
        } else {
            LOG.error("Inventory counting - default warehouse is null or empty");
        }
    }

    /**
     * Method which synchronize data between custom ERP and SAP Anywhere. It suppose, that information about warehouses between
     * custom ERP and SAP Anywhere are synchronized.
     * 
     * @throws Exception possible exception during the processing
     */
    private static final void syncByErpWarehouses() throws Exception {

        LOG.debug("Inventory counting - synchronization by using ERP warehouses");

        // read/get list of warehouses from ERP system
        List<ErpWarehouseDto> listOfWarehouses = WarehouseService.getListOfWarehousesFromErp();

        // if list of warehouses is not null or empty, process items/transaction on the warehouses
        if (listOfWarehouses != null && listOfWarehouses.size() > 0) {

            // identify which range of data will be synchronized
            Range rangeForProcessing = RangeService.getRangeForProcessing();

            if (rangeForProcessing != null
                    && rangeForProcessing.getRangeFrom() != null
                    && rangeForProcessing.getRangeTo() != null
                    && rangeForProcessing.getRangeFrom() <= rangeForProcessing.getRangeTo()) {

                // for every warehouse get items with actual count
                for (ErpWarehouseDto warehouse : listOfWarehouses) {

                    // get data about inventory countings from custom ERP
                    List<ErpStockDataDto> listOfInventoryCountings = InventoryCountingService
                            .getInventoryCountingsFromErp(warehouse.getCode(), rangeForProcessing);

                    // if data about inventory counting on selected warehouses are not null or empty payload will be created
                    // and posted
                    if (listOfInventoryCountings != null && listOfInventoryCountings.size() > 0) {

                        // create payload, which will be posted
                        AnwInventoryCountingPostDto payload = InventoryCountingTransformation
                                .transform(warehouse.getCode(), listOfInventoryCountings);

                        // if payload is not null, it will be posted
                        if (payload != null) {
                            // log for demo purposes
                            LOG.debug("Inventory counting - created payload: \n" + getJson(payload));

                            // post payload to SAP Anywhere
                            InventoryCountingService.sendPayloadToSapAnywhere(payload);
                        } else {
                            LOG.debug("Inventory counting - created payload for warehouse " + warehouse.getCode() + " is null");
                        }
                    } else {
                        LOG.debug("Inventory counting - data for warehouse " + warehouse.getCode() + " are null or empty");
                    }
                }

                // save ranges, which was processed. these processed ranges of data will not be processed again in another
                // iteration of integration
                RangeService.saveRange(rangeForProcessing);
            } else {
                LOG.info("Inventory counting - nothing to integrate");
            }
        } else {
            LOG.info("Inventory counting - list of warehouses is null or empty");
        }
    }
}
