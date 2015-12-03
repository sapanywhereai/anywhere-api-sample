package com.sap.integration.core.integration;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwSalesDeliveryDto;
import com.sap.integration.core.service.SalesDeliveryService;
import com.sap.integration.erp.dto.ErpSalesDeliveryDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of Sales Deliveries is defined in this class.
 */
public class SalesDeliveryIntegration {

    private static final Logger LOG = Logger.getLogger(SalesDeliveryIntegration.class);

    /**
     * Integration of Sales Delivery from SAP Anywhere to ERP.
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncFromSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration SAP Anywhere to ERP");

        // read Sales Deliveries from SAP Anywhere and get their DTO object representation
        List<AnwSalesDeliveryDto> anwSalesDeliveryDtos = SalesDeliveryService.getAnwSalesDeliveries();

        // convert SAP Anywhere's DTO value into ERP native format and persist it in ERP
        SalesDeliveryService.postErpSalesDeliveries(anwSalesDeliveryDtos);

        // log end
        LOG.info("End of integration SAP Anywhere to ERP");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

    /**
     * Integration of Sales Order from ERP to SAP Anywhere.
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncToSAPAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration ERP to SAP Anywhere");

        // read Sales Orders from ERP and get their DTO object representation
        List<ErpSalesDeliveryDto> erpSalesDeliveries = SalesDeliveryService.getErpSalesDeliveries();

        // post ERP sales deliveries
        SalesDeliveryService.postAnwSalesDeliveries(erpSalesDeliveries);

        // log end
        LOG.info("End of integration ERP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }
}