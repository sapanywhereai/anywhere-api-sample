package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwInvoiceDto;
import com.sap.integration.core.transformation.InvoiceTransformation;
import com.sap.integration.erp.dto.ErpInvoiceDto;
import com.sap.integration.erp.dummy.conversion.InvoiceConversion;
import com.sap.integration.erp.dummy.entity.Invoice;
import com.sap.integration.erp.dummy.entity.SalesOrder;
import com.sap.integration.erp.dummy.jpa.JpaLayer;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;

/**
 * Class used for getting and posting of invoice data to SAP Anywhere / ERP.
 */
public class InvoiceService {

    public static final String INVOICES = "SalesInvoices";

    private static final Logger LOG = Logger.getLogger(InvoiceService.class);

    private static IntegrationState integrationState;

    /**
     * Get SAP Anywhere invoice data transfer object
     * 
     * @param id
     *            Invoice's id in SAP Anywhere
     * @return AnwInvoiceDto
     * @throws Exception
     */
    public static AnwInvoiceDto getAnwInvoice(String id) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(INVOICES)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "id eq " + id)
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder);
        List<AnwInvoiceDto> anwInvoices;
        try {
            anwInvoices = JsonUtil.getObjects(response.getContent(), AnwInvoiceDto.class);
            if (anwInvoices.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwInvoices.get(0);
    }

    /**
     * Get list of SAP Anywhere invoice data transfer objects ordered by updateTime.
     * 
     * @return List<AnwInvoiceDto>
     * @throws Exception
     */
    public static List<AnwInvoiceDto> getAnwInvoices() throws Exception {
        // read last update time of invoices
        integrationState = IntegrationState.getIntegrationStateFor(AnwInvoiceDto.class);

        // get SAP Anywhere invoice
		List<AnwInvoiceDto> anwInvoices = new ArrayList<AnwInvoiceDto>();
        List<AnwInvoiceDto> anwInvoicesPage;
        int offset = 0;
        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(INVOICES)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter(
                            "filter",
                            "updateTime gt '" + DateUtil.convertDateTimeToString(integrationState.getLastSyncTime())
                                    + "'")
                    .parameter("expand", "invoiceLines")
                    .parameter("orderby", "updateTime")                    
                    .parameter("access_token", Property.getAccessToken());

            anwInvoicesPage = JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(), AnwInvoiceDto.class);
            anwInvoices.addAll(anwInvoicesPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwInvoicesPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwInvoices;
    }

    /**
     * Evoke CREATE / UPDATE of SAP Anywhere invoices and persist timestamp of last successfully transfered SAP
     * Anywhere invoice
     * 
     * @param erpInvoices
     *            ERP invoice data transfer objects
     * @throws Exception
     */
    public static void postAnwInvoices(List<ErpInvoiceDto> erpInvoices) throws Exception {
        // read last update time of invoice
        integrationState = IntegrationState.getIntegrationStateFor(ErpInvoiceDto.class);

        // post invoices to SAP Anywhere
        int createdCount = 0;
        int updatedCount = 0;
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(INVOICES)
                .parameter("access_token", Property.getAccessToken());
        for (ErpInvoiceDto erpInvoice : erpInvoices) {
            AnwInvoiceDto anwInvoice = InvoiceService.getAnwInvoice(erpInvoice.getAnwId().toString());
            AnwSimpleResponse response = null;
            if (anwInvoice == null) { // create
            	AnwInvoiceDto anwInvoiceToCreate = InvoiceTransformation.run(erpInvoice);
                response = AnwServiceCall.post(urlBuilder, anwInvoiceToCreate);
                if (response != null) {
                	
                    // Update invoice data in ERP - insert id of invoice just created in SAP Anywhere (parameter anwId)
                    // into ERP
                    if ((response.getId() == null) || (response.getStatusCode() != 201)) {
                        throw new Exception(
                                "Problem with POSTing invoice into SAP Anywhere. Response from server was: " + response.getContent());
                    } else {           
                    	List<Invoice> invs = JpaLayer.find(Invoice.class,"SELECT i FROM Invoice i WHERE i.id = ?1",erpInvoice.getId());
                        Invoice inv = invs.get(0);
                        if (inv != null) {
                        	inv.setAnwId(response.getId());
                        	inv.merge();
                        }
                    }
                    createdCount++;
                }
            } else { // update
                if (erpInvoice.getLastUpdateTime().isAfter(anwInvoice.getUpdateTime())) {
                	urlBuilder.append("/" + erpInvoice.getAnwId());
                    response = AnwServiceCall.post(urlBuilder, InvoiceTransformation.run(erpInvoice));
                    if (response != null) {
                        updatedCount++;
                    }
                }
            }

            // update last sync time of successfully transfered invoice
            if (response != null && erpInvoice.getLastUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(erpInvoice.getLastUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " invoices");
        LOG.info("Successfully updated " + updatedCount + " invoices");
        LOG.info("Failed " + (erpInvoices.size() - createdCount - updatedCount) + " invoices");
    }

    /**
     * Get ERP invoice data transfer object
     * 
     * @param anwId
     *            Invoice's ID in SAP Anywhere - stored in ERP as parameter for join records between SAP Anywhere and ERP
     * @return ErpInvoiceDto
     */
    public static ErpInvoiceDto getErpInvoice(String anwId) throws Exception {
        return InvoiceConversion.run(
                Invoice.findFirst(Invoice.class, "SELECT i FROM Invoice i WHERE i.anwId = ?1", new Long(anwId)));
    }

    /**
     * Get list of ERP invoice data transfer objects ordered by lastUpdateTime.
     * 
     * @return List<ErpInvoiceDto>
     */
    @SuppressWarnings("unchecked")
    public static List<ErpInvoiceDto> getErpInvoices() throws Exception {
        // read last update time of invoices
        integrationState = IntegrationState.getIntegrationStateFor(ErpInvoiceDto.class);

        // get ERP Invoices
        List<ErpInvoiceDto> erpInvoices = new ArrayList<ErpInvoiceDto>();
        List<Invoice> invoices = JpaLayer
                .find(Invoice.class,
                        "SELECT i FROM Invoice i WHERE (i.lastUpdateTime > ?1) or (i.lastUpdateTime is NULL) ORDER BY i.lastUpdateTime",
                        integrationState.getLastSyncTime());

        // convert db items into ERP product data transfer objects
        erpInvoices = (List<ErpInvoiceDto>) InvoiceConversion.run(Invoice.class, invoices);

        return erpInvoices;
    }

    /**
     * Evoke CREATE / UPDATE of ERP invoices and persist timestamp of last successfully transfered ERP invoice
     * 
     * @param anwInvoices
     *            Sap Anywhere invoice data transfer objects
     */
    public static void postErpInvoices(List<AnwInvoiceDto> anwInvoices) throws Exception {
        // read last update time of invoice
        integrationState = IntegrationState.getIntegrationStateFor(AnwInvoiceDto.class);

        // post invoices to ERP
        int createdCount = 0;
        int updatedCount = 0;
        for (AnwInvoiceDto anwInvoice : anwInvoices) {
            ErpInvoiceDto erpInvoice = InvoiceService.getErpInvoice(anwInvoice.getId().toString());
            if (erpInvoice == null) { // create
                InvoiceConversion.run(InvoiceTransformation.run(anwInvoice)).persist();
                createdCount++;
            } else { // update
                if (anwInvoice.getUpdateTime().isAfter(erpInvoice.getLastUpdateTime())) {
                	ErpInvoiceDto updatedErpInvoice = InvoiceTransformation.run(anwInvoice);
                	updatedErpInvoice.setId(erpInvoice.getId());
                	
                    InvoiceConversion.run(updatedErpInvoice).merge();
                    updatedCount++;
                }
            }

            // update last sync time of successfully transfered invoice
            if (anwInvoice.getUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(anwInvoice.getUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " invoices");
        LOG.info("Successfully updated " + updatedCount + " invoices");
        LOG.info("Failed " + (anwInvoices.size() - createdCount - updatedCount) + " invoices");
    }
}