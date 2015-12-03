package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwWarehouseDto;
import com.sap.integration.core.transformation.WarehouseTransformation;
import com.sap.integration.erp.dto.ErpWarehouseDto;
import com.sap.integration.erp.dummy.entity.Warehouse;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class, which provides methods for create/read/update/delete data about Warehouses from/to SAP Anywhere. <br>
 */
public class WarehouseService {

    private static final Logger LOG = Logger.getLogger(WarehouseService.class);

    // ****************************** PART FOR SAP ANYWHERE ****************************** //
    
    /**
     * Method, which find warehouse in SAP Anywhere by using warehouse code. <br>
     * 
     * @param whsCode - warehouse code used as filter <br>
     * @return AnwWarehouseDto - warehouse from SAP Anywhere <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final AnwWarehouseDto findWarehouse(String whsCode) throws Exception {

        if (StringUtils.isNotBlank(whsCode)) {
            AnwSimpleResponse result = AnwServiceCall.get(getWarehouseLinkFind(whsCode));
            LOG.info("Warehouse - result of finding warehouse with wshCode " + whsCode + ": " + result.getContent());

            if (result != null) {
                List<AnwWarehouseDto> warehouses = JsonUtil.getObjects(result.getContent(), AnwWarehouseDto.class);

                if (warehouses != null && warehouses.size() > 0) {
                    return warehouses.get(0);
                } else {
                    LOG.info("Warehouse - result of parsing information about warehouse is null object of AnwWarehouseDto. Input value: "
                            + result);
                    return null;
                }
            } else {
                LOG.info("Warehouse - result of finding is empty/null warehouse. Input value: " + whsCode);
                return null;
            }
        } else {
            LOG.info("Warehouse - empty/null parameter on input of method findWarehouse()");
            return null;
        }
    }

    /**
     * Method, which returns list of all warehouses. It iterates over all pages with warehouses and creates payload of warehouses.<br>
     * 
     * @return List<AnwWarehouseDto> list of all warehouses <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final List<AnwWarehouseDto> findAllWarehouses() throws Exception {

        List<AnwWarehouseDto> listOfAllWarehouses = new ArrayList<AnwWarehouseDto>();
        List<AnwWarehouseDto> listOfFoundWarehouses = null;
        int cycle = 0;

        do {
            AnwSimpleResponse result = AnwServiceCall.get(getWarehouseLinkSelect(cycle * AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT));

            if (!result.isError()) {

                listOfFoundWarehouses = JsonUtil.getObjects(result.getContent(), AnwWarehouseDto.class);

                if (listOfFoundWarehouses != null && listOfFoundWarehouses.size() > 0) {
                    listOfAllWarehouses.addAll(listOfFoundWarehouses);
                    cycle++;
                } else {
                    listOfFoundWarehouses = null;
                }
            } else {
                listOfFoundWarehouses = null;
            }
        } while (listOfFoundWarehouses != null && listOfFoundWarehouses.size() > AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        LOG.debug("Warehouse - list of all warehouses:");
        LOG.info(JsonUtil.getJson(listOfFoundWarehouses));

        return listOfAllWarehouses;
    }

    /**
     * Method returns URL, which is used for selecting of Warehouses. <br>
     * Sample of URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/Warehouses?limit=100&offset=1&access_token=access_token_value</code>
     * 
     * @param limit
     * @param offset
     * @return URL for selection of Warehouses
     * @throws Exception possible exception thrown by class, which loads configuration
     */
    public static final String getWarehouseLinkSelect(Number offset) throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Warehouses")
                .parameter("access_token", Property.getAccessToken())
                .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                .parameter("offset", offset)
                .get();
        LOG.debug("Warehouse - select link: " + link);
        return link;
    }

    /**
     * Method returns URL, which is used for finding an existing Warehouse in SAP Anywhere and getting information from it. <br>
     * Sample of URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/Warehouses?$select=id,whsCode,whsName&filter=whsCode eq 'warehouseCode'&limit=1&offset=0&access_token=access_token_value</code>
     * 
     * @return URL for finding Warehouses
     * @throws Exception possible exception thrown by class, which loads configuration
     */
    public static final String getWarehouseLinkFind(String whsCode) throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Warehouses")
                .parameter("access_token", Property.getAccessToken())
                .parameter("$select", "id,whsCode,whsName")
                .parameter("filter", "code eq '" + whsCode + "'")
                .parameter("limit", 1)
                .parameter("offset", 0)
                .get();
        LOG.debug("Warehouse - find by code link: " + link);
        return link;
    }
    
    /** 
     * Method, which returns information about default warehouse defined in configuration file from SAP Anywhere. <br>
     * 
     * @return AnwWarehouseDto - default warehouse with data from SAP Anywhere defined in configuration file config.properties <br>
     * null - in case when some error occurs <br>
     */
    public static final AnwWarehouseDto getDefaultAnwWarehouse() {
        try {
            return findWarehouse(Property.getDefaultAnwWarehouse());
        } catch (Exception e) {
            LOG.error("Exception - Problem with retrieving default warehouse from configuration file", e);
            return null;
        }
    }
    
    // ****************************** PART FOR CUSTOM ERP ****************************** //
    
    /**
     * Method, which returns list of warehouses from ERP. <br>
     * 
     * @return List&lt;ErpWarehouseDto&gt; representing list of warehouses from ERP
     * @throws Exception
     */
    public static final List<ErpWarehouseDto> getListOfWarehousesFromErp() throws Exception {
        List<Warehouse> listOfWarehouses = Warehouse.getAll(Warehouse.class);
        List<ErpWarehouseDto> listOfErpWarehouses = WarehouseTransformation.transform(listOfWarehouses);
        return listOfErpWarehouses;
    }
}
