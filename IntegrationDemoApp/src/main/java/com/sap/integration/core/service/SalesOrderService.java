package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwSalesOrderDto;
import com.sap.integration.core.transformation.SalesOrderTransformation;
import com.sap.integration.erp.dto.ErpSalesOrderDto;
import com.sap.integration.erp.dummy.conversion.SalesOrderConversion;
import com.sap.integration.erp.dummy.entity.SalesOrder;
import com.sap.integration.erp.dummy.jpa.JpaLayer;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;

/**
 * Class used for getting and posting of sales order data to SAP Anywhere / ERP.
 */
public class SalesOrderService {

    public static final String SALES_ORDERS = "SalesOrders";

    private static final Logger LOG = Logger.getLogger(SalesOrderService.class);

    private static IntegrationState integrationState;

    /**
     * Get SAP Anywhere sales order data transfer object
     * 
     * @param id Sales order's id in SAP Anywhere
     * @return AnwSalesOrderDto
     * @throws Exception
     */
    public static AnwSalesOrderDto getAnwSalesOrder(String id) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_ORDERS)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "id eq " + id)
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder);
        List<AnwSalesOrderDto> anwSalesOrder;
        try {
            anwSalesOrder = JsonUtil.getObjects(response.getContent(), AnwSalesOrderDto.class);
            if (anwSalesOrder.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwSalesOrder.get(0);
    }
    
    /**
     * Get SAP Anywhere sales order data transfer object with specific value of dicument number
     * 
     * @param id Sales order's id in SAP Anywhere
     * @return AnwSalesOrderDto
     * @throws Exception
     */
    public static AnwSalesOrderDto getAnwSalesOrderWithDocNumber(String docNumber) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_ORDERS)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "docNumber eq '" + docNumber + "'")
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder);
        List<AnwSalesOrderDto> anwSalesOrder;
        try {
            anwSalesOrder = JsonUtil.getObjects(response.getContent(), AnwSalesOrderDto.class);
            if (anwSalesOrder.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwSalesOrder.get(0);
    }

    /**
     * Get list of SAP Anywhere sales order data transfer objects ordered by updateTime.
     * 
     * @return List<AnwSalesOrderDto>
     * @throws Exception
     */
    public static List<AnwSalesOrderDto> getAnwSalesOrders() throws Exception {
        // read last update time of products
        integrationState = IntegrationState.getIntegrationStateFor(AnwSalesOrderDto.class);

        // get SAP Anywhere sales order
        List<AnwSalesOrderDto> anwSalesOrders = new ArrayList<AnwSalesOrderDto>();
        List<AnwSalesOrderDto> anwSalesOrdersPage;
        int offset = 0;
        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(SALES_ORDERS)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter("orderby", "updateTime")
                    .parameter(
                            "filter",
                            "updateTime gt '" + DateUtil.convertDateTimeToString(integrationState.getLastSyncTime())
                                    + "'")
                    .parameter("expand", "productLines")
                    .parameter("access_token", Property.getAccessToken());

            anwSalesOrdersPage = JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(), AnwSalesOrderDto.class);
            anwSalesOrders.addAll(anwSalesOrdersPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwSalesOrdersPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwSalesOrders;
    }
  
    /**
     * Evoke CREATE / UPDATE of SAP Anywhere sales orders and persist timestamp of last successfully transfered SAP Anywhere sales
     * order
     * 
     * @param erpSalesOrders ERP sales order data transfer objects
     * @throws Exception
     */
    public static void postAnwSalesOrders(List<ErpSalesOrderDto> erpSalesOrders) throws Exception {
        // read last update time of sales order
        integrationState = IntegrationState.getIntegrationStateFor(ErpSalesOrderDto.class);

        // post sales orders to SAP Anywhere
        int createdCount = 0;
        int updatedCount = 0;
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_ORDERS)
                .parameter("access_token", Property.getAccessToken());
        for (ErpSalesOrderDto erpSalesOrder : erpSalesOrders) {
        	AnwSalesOrderDto anwSalesOrder = null;
        	if (erpSalesOrder.getAnwId() != null) {
        		anwSalesOrder = SalesOrderService.getAnwSalesOrder(erpSalesOrder.getAnwId().toString());
        	}
            AnwSimpleResponse response = null;
            if (anwSalesOrder == null) { // create
                AnwSalesOrderDto anwSalesOrderToCreate = SalesOrderTransformation.run(erpSalesOrder);
                response = AnwServiceCall.post(urlBuilder, anwSalesOrderToCreate);
                if (response != null) {

                    // Update sales order data in ERP - insert id of sales order just created in SAP Anywhere (parameter anwId)
                    // into ERP
                    if ((response.getId() == null) || (response.getStatusCode() != 201)) {
                        throw new Exception(
                                "Problem with POSTing sales order into SAP Anywhere. Response from server was: " + response.getContent());
                    } else {           
                    	List<SalesOrder> sos = JpaLayer.find(SalesOrder.class,"SELECT so FROM SalesOrder so WHERE so.id = ?1",erpSalesOrder.getId());
                        SalesOrder so = sos.get(0);
                        if (so != null) {
                        	so.setAnwId(response.getId());
                        	so.merge();
                        }
                    }
                    createdCount++;
                }
            } else { // update
                if (erpSalesOrder.getLastUpdateTime().isAfter(anwSalesOrder.getUpdateTime())) {
                	urlBuilder.append("/" + erpSalesOrder.getAnwId());
                    response = AnwServiceCall.patch(urlBuilder, SalesOrderTransformation.run(erpSalesOrder));
                    if (response != null) {
                        updatedCount++;
                    }
                }
            }

            // update last sync time of successfully transfered sales order
            if (response != null && erpSalesOrder.getLastUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(erpSalesOrder.getLastUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " sales orders");
        LOG.info("Successfully updated " + updatedCount + " sales orders");
        LOG.info("Failed " + (erpSalesOrders.size() - createdCount - updatedCount) + " sales orders");
    }

    /**
     * Get ERP sales order data transfer object
     * 
     * @param anwId Sales Order's ID in SAP Anywhere - stored in ERP as parameter for join records between SAP Anywhere and ERP
     * @return ErpSalesOrderDto
     */
    public static ErpSalesOrderDto getErpSalesOrder(String anwId) {
        return SalesOrderConversion.run(
                SalesOrder.findFirst(SalesOrder.class, "SELECT so FROM SalesOrder so WHERE so.anwId = ?1", new Long(anwId)));
    }

    /**
     * Get list of ERP sales order data transfer objects ordered by lastUpdateTime.
     * 
     * @return List<ErpSales OrderDto>
     */
    @SuppressWarnings("unchecked")
    public static List<ErpSalesOrderDto> getErpSalesOrders() throws Exception {
        // read last update time of products
        integrationState = IntegrationState.getIntegrationStateFor(ErpSalesOrderDto.class);

        // get ERP Sales Orders
        List<ErpSalesOrderDto> erpSalesOrders = new ArrayList<ErpSalesOrderDto>();
        List<SalesOrder> salesOrders = JpaLayer
                .find(SalesOrder.class,
                        "SELECT so FROM SalesOrder so WHERE (so.lastUpdateTime > ?1) or (so.lastUpdateTime is NULL) ORDER BY so.lastUpdateTime",
                        integrationState.getLastSyncTime());

        // convert db items into ERP product data transfer objects
        erpSalesOrders = (List<ErpSalesOrderDto>) SalesOrderConversion.run(SalesOrder.class, salesOrders);

        return erpSalesOrders;
    }

    /**
     * Evoke CREATE / UPDATE of ERP sales orders and persist timestamp of last successfully transfered ERP sales order
     * 
     * @param anwSalesOrders Sap Anywhere sales order data transfer objects
     */
    public static void postErpSalesOrders(List<AnwSalesOrderDto> anwSalesOrders) throws Exception {
        // read last update time of sales order
        integrationState = IntegrationState.getIntegrationStateFor(AnwSalesOrderDto.class);

        // post sales orders to ERP
        int createdCount = 0;
        int updatedCount = 0;
        for (AnwSalesOrderDto anwSalesOrder : anwSalesOrders) {
            ErpSalesOrderDto erpSalesOrder = SalesOrderService.getErpSalesOrder(anwSalesOrder.getId().toString());
            if (erpSalesOrder == null) { // create
                SalesOrderConversion.run(SalesOrderTransformation.run(anwSalesOrder)).persist();
                createdCount++;
            } else { // update
                if (anwSalesOrder.getUpdateTime().isAfter(erpSalesOrder.getLastUpdateTime())) {
                	ErpSalesOrderDto updatedErpSalesOrder = SalesOrderTransformation.run(anwSalesOrder);
                	updatedErpSalesOrder.setId(erpSalesOrder.getId());
                	
                    SalesOrderConversion.run(updatedErpSalesOrder).merge();
                    updatedCount++;
                }
            }

            // update last sync time of successfully transfered Sales Order
            if (anwSalesOrder.getUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(anwSalesOrder.getUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " sales orders");
        LOG.info("Successfully updated " + updatedCount + " sales orders");
        LOG.info("Failed " + (anwSalesOrders.size() - createdCount - updatedCount) + " sales orders");
    }
}
