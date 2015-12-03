package com.sap.integration.core.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwInventoryCountingPostDto;
import com.sap.integration.core.transformation.InventoryCountingTransformation;
import com.sap.integration.erp.dto.ErpStockDataDto;
import com.sap.integration.erp.dummy.entity.StockData;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.Range;

public class InventoryCountingService {

    private static final Logger LOG = Logger.getLogger(InventoryCountingService.class);

    // ****************************** PART FOR SAP ANYWHERE ****************************** //

    /**
     * Method, which will send list of payloads of inventory countings to the SAP Anywhere. <br>
     * 
     * @param list - list of payloads which represent inventory counting <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final void sendPayloadToSapAnywhere(List<AnwInventoryCountingPostDto> list) throws Exception {

        // check if entered list is not null and size is greater then 0
        if (list != null && list.size() > 0) {

            // for every object from the list
            for (int i = 0; i < list.size(); i++) {
                sendPayloadToSapAnywhere(list.get(0));
            }

        } else {
            LOG.info("Inventory counting - empty/null parameter on input of method postDataToSapAnywhere()");
        }
    }

    /**
     * Method, which will send payload of inventory counting to the SAP Anywhere. Algorithm of sending inventory counting to the
     * SAP Anywhere is simple:
     * <ul>
     * <li>post payload</li>
     * <li>get id, which was associated to the posted payload</li>
     * <li>close posted inventory counting</li>
     * </ul>
     * 
     * @param payload - payload which represents inventory counting <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final void sendPayloadToSapAnywhere(AnwInventoryCountingPostDto payload) throws Exception {

        if (payload != null) {

            // send inventory counting to the SAP Anywhere
            String result = post(payload);
            LOG.info("Inventoy counting - result from HTTPS POST: " + result);

            // SAP Anywhere returns result, it should not be null and you have to parse id of posted inventory counting from it
            if (result != null) {

                // parse id of inventory counting from the result
                Long id = parse(result);
                LOG.info("Inventory counting - parsed id = " + id);

                // if id, which represents sent inventory counting is not null, you may close the inventory counting
                if (id != null) {

                    // close inventory counting
                    close(id);
                } else {
                    LOG.info("Inventory counting - result of parsing inventory counting id from result of HTTPS POST is null");
                }
            } else {
                LOG.info("Inventory counting - result of HTTPS POST is null");
            }
        } else {
            LOG.info("Inventory counting - empty/null parameter on input of method postDataToSapAnywhere()");
        }
    }

    /**
     * Method, which will send entered payload to the SAP Anywhere. <br>
     * 
     * @param payload - payload, which will be sent to the SAP Anywhere <br>
     * @return result of posting - it should contains an id of inventory counting <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final String post(AnwInventoryCountingPostDto payload) throws Exception {
        AnwSimpleResponse response = AnwServiceCall.post(getInventoryCountingLinkPost(), payload);
        // TODO: check for errors
        return response.getContent();
    }

    /**
     * Method, which will parse id of inventory counting from the result. <br>
     * 
     * @param result - result of posting inventory counting to the SAP Anywhere <br>
     * @return id - id of posted inventory counting <br>
     *         null - in case when some error occurs <br>
     */
    private static final Long parse(String result) {

        if (result != null) {
            try {
                Long id = Long.parseLong(result);
                return id;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            LOG.info("Inventory counting - empty/null parameter on input of method getId()");
            return null;
        }
    }

    /**
     * Method, which will close inventory counting. As an input should be id of posted inventory counting. <br>
     * 
     * @param id - id of inventory counting which will be closed <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final void close(Long id) throws Exception {
        AnwServiceCall.post(getInventoryCountingLinkClose(id), null);
    }

    /**
     * Method returns URL, which is used for selecting inventory counting. <br>
     * Sample of URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/InventoryCountings?limit=limitValue&offset=offsetValue&access_token=access_token_value</code>
     * 
     * @param limit
     * @param offset
     * @return URL used for selection of inventory counting
     * @throws Exception possible exception thrown by class, which loads configuration
     */

    public static final String getInventoryCountingsLinkFind(String warehouseCode, Number offset) throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("InventoryCountings")
                .parameter("access_token", Property.getAccessToken())
                .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                .parameter("offset", offset)
                .parameter("expand", "lines")
                .get();
        LOG.info("Inventory counting - select link: " + link);
        return link;
    }

    /**
     * Method, which returns URL which is used for posting Inventory counting to the SAP Anywhere. <br>
     * 
     * @return URL used for posting inventory counting to the SAP Anywhere
     * @throws Exception possible exception thrown by class, which loads configuration
     */
    public static final String getInventoryCountingLinkPost() throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("InventoryCountings")
                .parameter("access_token", Property.getAccessToken())
                .get();
        LOG.info("Inventory counting - post link: " + link);
        return link;
    }

    /**
     * Method returns URL, which is used for closing of inventory counting. <br>
     * Sample of URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/InventoryCountings/1/close?access_token=access_token_value</code>
     *
     * @return URL used for closing of inventory counting
     * @throws Exception possible exception thrown by class, which loads configuration
     */
    public static final String getInventoryCountingLinkClose(Number id) throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("InventoryCountings/" + id + "/close")
                .parameter("access_token", Property.getAccessToken())
                .get();
        LOG.info("Inventory counting - closing link: " + link);
        return link;
    }

    // ****************************** PART FOR CUSTOM ERP ****************************** //

    /**
     * Method which returns inventory counting data from ERP based on entered warehouse code. For every warehouses there is some
     * status of products on the stock. These data should be retrieved and collected within this method. <br>
     * 
     * @param whsCode - warehouse code
     * @return List<ErpInventoryCountingDto> - inventory counting data from custom ERP
     */
    public static final List<ErpStockDataDto> getInventoryCountingsFromErp(String whsCode, Range rangeForProcessing) {
        List<StockData> listOfStockData = StockData
                    .find(StockData.class, 
                            "SELECT s FROM StockData s WHERE s.whsCode = ?1 AND s.id >= ?2 AND s.id <= ?3 ORDER BY s.id ASC", 
                            whsCode, 
                            rangeForProcessing.getRangeFrom(), 
                            rangeForProcessing.getRangeTo());
        List<ErpStockDataDto> listOfErpStockData = InventoryCountingTransformation.transform(listOfStockData);
        return listOfErpStockData;
    }
    
    public static final List<ErpStockDataDto> getInventoryCountingsFromErp(Range rangeForProcessing) {
        List<StockData> listOfStockData = StockData
                .find(StockData.class, 
                        "SELECT s FROM StockData s WHERE s.id >= ?1 AND s.id <= ?2 ORDER BY s.id ASC", 
                        rangeForProcessing.getRangeFrom(), 
                        rangeForProcessing.getRangeTo());
        List<ErpStockDataDto> listOfErpStockData = InventoryCountingTransformation.transform(listOfStockData);
        return listOfErpStockData;
    }

    public static final List<ErpStockDataDto> getLastInventoryCountingsFromErp(Range rangeForProcessing) {
        List<StockData> listOfStockData = (List<StockData>) StockData
                .find(StockData.class,
                        "SELECT MAX(sd.id) as id, sd.productCode, sd.quantity, sd.unitOfMeasure, sd.whsCode FROM StockData sd WHERE sd.id >= ?1 AND sd.id <= ?2 GROUP BY sd.productCode ORDER BY sd.id ASC",
                        rangeForProcessing.getRangeFrom(),
                        rangeForProcessing.getRangeTo());

        List<ErpStockDataDto> listOfErpStockData = InventoryCountingTransformation.transform(listOfStockData);
        return listOfErpStockData;
    }

}
