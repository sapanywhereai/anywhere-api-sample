package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwSalesDeliveryDto;
import com.sap.integration.core.transformation.SalesDeliveryTransformation;
import com.sap.integration.erp.dto.ErpSalesDeliveryDto;
import com.sap.integration.erp.dummy.conversion.SalesDeliveryConversion;
import com.sap.integration.erp.dummy.entity.SalesDelivery;
import com.sap.integration.erp.dummy.entity.SalesOrder;
import com.sap.integration.erp.dummy.jpa.JpaLayer;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;

/**
 * Class used for getting and posting of sales delivery data to SAP Anywhere / ERP.
 */
public class SalesDeliveryService {

    public static final String SALES_DELIVERIES = "SalesDeliveries";

    private static final Logger LOG = Logger.getLogger(SalesDeliveryService.class);

    private static IntegrationState integrationState;

    /**
     * Get SAP Anywhere sales delivery data transfer object
     * 
     * @param id
     *            Sales delivery's id in SAP Anywhere
     * @return AnwSalesDeliveryDto
     * @throws Exception
     */
    public static AnwSalesDeliveryDto getAnwSalesDelivery(String id) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_DELIVERIES)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "id eq " + id)
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder);
        List<AnwSalesDeliveryDto> anwSalesDeliveries;
        try {
            anwSalesDeliveries = JsonUtil.getObjects(response.getContent(), AnwSalesDeliveryDto.class);
            if (anwSalesDeliveries.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwSalesDeliveries.get(0);
    }

    /**
     * Get list of SAP Anywhere sales delivery data transfer objects ordered by updateTime.
     * 
     * @return List<AnwSalesDeliveryDto>
     * @throws Exception
     */
    public static List<AnwSalesDeliveryDto> getAnwSalesDeliveries() throws Exception {
        // read last update time of products
        integrationState = IntegrationState.getIntegrationStateFor(AnwSalesDeliveryDto.class);

        // get SAP Anywhere sales delivery
		List<AnwSalesDeliveryDto> anwSalesDeliveries = new ArrayList<AnwSalesDeliveryDto>();
        List<AnwSalesDeliveryDto> anwSalesDeliveriesPage;
        int offset = 0;
        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(SALES_DELIVERIES)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter(
                            "filter",
                            "updateTime gt '" + DateUtil.convertDateTimeToString(integrationState.getLastSyncTime())
                                    + "'")
                    .parameter("expand", "lines")
                    .parameter("orderby", "updateTime")                    
                    .parameter("access_token", Property.getAccessToken());

            anwSalesDeliveriesPage = JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(), AnwSalesDeliveryDto.class);
            anwSalesDeliveries.addAll(anwSalesDeliveriesPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwSalesDeliveriesPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwSalesDeliveries;
    }

    /**
     * Evoke CREATE / UPDATE of SAP Anywhere sales deliveries and persist timestamp of last successfully transfered SAP
     * Anywhere sales delivery
     * 
     * @param erpSalesDeliveries
     *            ERP sales delivery data transfer objects
     * @throws Exception
     */
    public static void postAnwSalesDeliveries(List<ErpSalesDeliveryDto> erpSalesDeliveries) throws Exception {
        // read last update time of sales delivery
        integrationState = IntegrationState.getIntegrationStateFor(ErpSalesDeliveryDto.class);

        // post sales deliveries to SAP Anywhere
        int createdCount = 0;
        int updatedCount = 0;
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_DELIVERIES)
                .parameter("access_token", Property.getAccessToken());
        for (ErpSalesDeliveryDto erpSalesDelivery : erpSalesDeliveries) {
            AnwSalesDeliveryDto anwSalesDelivery = SalesDeliveryService.getAnwSalesDelivery(erpSalesDelivery.getAnwId().toString());
            AnwSimpleResponse response = null;
            if (anwSalesDelivery == null) { // create
            	AnwSalesDeliveryDto anwSalesDeliveryToCreate = SalesDeliveryTransformation.run(erpSalesDelivery);
                response = AnwServiceCall.post(urlBuilder, anwSalesDeliveryToCreate);
                if (response != null) {
                	
                    // Update sales delivery data in ERP - insert id of sales delivery just created in SAP Anywhere (parameter anwId)
                    // into ERP
                    if ((response.getId() == null) || (response.getStatusCode() != 201)) {
                        throw new Exception(
                                "Problem with POSTing sales delivery into SAP Anywhere. Response from server was: " + response.getContent());
                    } else {           
                    	List<SalesDelivery> sds = JpaLayer.find(SalesDelivery.class,"SELECT sd FROM SalesDelivery sd WHERE sd.id = ?1",erpSalesDelivery.getId());
                        SalesDelivery sd = sds.get(0);
                        if (sd != null) {
                        	sd.setAnwId(response.getId());
                        	sd.merge();
                        }
                    }
                    createdCount++;
                }
            } else { // update
                if (erpSalesDelivery.getLastUpdateTime().isAfter(anwSalesDelivery.getUpdateTime())) {
                    response = AnwServiceCall.patch(urlBuilder, SalesDeliveryTransformation.run(erpSalesDelivery));
                    if (response != null) {
                        updatedCount++;
                    }
                }
            }

            // update last sync time of successfully transfered sales delivery
            if (response != null && erpSalesDelivery.getLastUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(erpSalesDelivery.getLastUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " sales deliveries");
        LOG.info("Successfully updated " + updatedCount + " sales deliveries");
        LOG.info("Failed " + (erpSalesDeliveries.size() - createdCount - updatedCount) + " sales deliveries");
    }

    /**
     * Get ERP sales delivery data transfer object
     * 
     * @param anwId
     *            Sales Delivery's ID in SAP Anywhere - stored in ERP as parameter for join records between SAP Anywhere and ERP
     * @return ErpSalesDeliveryDto
     */
    public static ErpSalesDeliveryDto getErpSalesDelivery(String anwId) throws Exception {
        return SalesDeliveryConversion.run(
                SalesDelivery.findFirst(SalesDelivery.class, "SELECT sd FROM SalesDelivery sd WHERE sd.anwId = ?1", new Long(anwId)));
    }

    /**
     * Get list of ERP sales delivery data transfer objects ordered by lastUpdateTime.
     * 
     * @return List<ErpSalesDeliveryDto>
     */
    @SuppressWarnings("unchecked")
    public static List<ErpSalesDeliveryDto> getErpSalesDeliveries() throws Exception {
        // read last update time of sales deliveries
        integrationState = IntegrationState.getIntegrationStateFor(ErpSalesDeliveryDto.class);

        // get ERP Sales Deliveries
        List<ErpSalesDeliveryDto> erpSalesDeliveries = new ArrayList<ErpSalesDeliveryDto>();
        List<SalesDelivery> salesDeliveries = JpaLayer
                .find(SalesDelivery.class,
                        "SELECT sd FROM SalesDelivery sd WHERE (sd.lastUpdateTime > ?1) or (sd.lastUpdateTime is NULL) ORDER BY sd.lastUpdateTime",
                        integrationState.getLastSyncTime());

        // convert db items into ERP product data transfer objects
        erpSalesDeliveries = (List<ErpSalesDeliveryDto>) SalesDeliveryConversion.run(SalesDelivery.class,
                salesDeliveries);

        return erpSalesDeliveries;
    }

    /**
     * Evoke CREATE / UPDATE of ERP sales deliveries and persist timestamp of last successfully transfered ERP sales delivery
     * 
     * @param anwSalesDeliveries
     *            Sap Anywhere sales delivery data transfer objects
     */
    public static void postErpSalesDeliveries(List<AnwSalesDeliveryDto> anwSalesDeliveries) throws Exception {
        // read last update time of sales delivery
        integrationState = IntegrationState.getIntegrationStateFor(AnwSalesDeliveryDto.class);

        // post sales deliveries to ERP
        int createdCount = 0;
        int updatedCount = 0;
        for (AnwSalesDeliveryDto anwSalesDelivery : anwSalesDeliveries) {
            ErpSalesDeliveryDto erpSalesDelivery = SalesDeliveryService.getErpSalesDelivery(anwSalesDelivery.getId().toString());
            if (erpSalesDelivery == null) { // create
                SalesDeliveryConversion.run(SalesDeliveryTransformation.run(anwSalesDelivery)).persist();
                createdCount++;
            } else { // update
                if (anwSalesDelivery.getUpdateTime().isAfter(erpSalesDelivery.getLastUpdateTime())) {
                	ErpSalesDeliveryDto updatedErpSalesDelivery = SalesDeliveryTransformation.run(anwSalesDelivery);
                	updatedErpSalesDelivery.setId(erpSalesDelivery.getId());
                	
                    SalesDeliveryConversion.run(updatedErpSalesDelivery).merge();
                    updatedCount++;
                }
            }

            // update last sync time of successfully transfered Sales Delivery
            if (anwSalesDelivery.getUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(anwSalesDelivery.getUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " sales deliveries");
        LOG.info("Successfully updated " + updatedCount + " sales deliveries");
        LOG.info("Failed " + (anwSalesDeliveries.size() - createdCount - updatedCount) + " sales deliveries");
    }
}