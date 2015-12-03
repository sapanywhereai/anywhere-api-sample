package com.sap.integration.core.integration;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwSalesOrderDto;
import com.sap.integration.core.service.SalesOrderService;
import com.sap.integration.erp.dto.ErpSalesOrderDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of Sales Orders is defined in this class. Do not run both sync methods to avoid circle updating!
 */
public class SalesOrderIntegration {

    private static final Logger LOG = Logger.getLogger(SalesOrderIntegration.class);

    /**
     * Integration of sales order from SAP Anywhere to ERP.
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncFromSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration SAP Anywhere to ERP");

        // read Sales Orders from SAP Anywhere and get their DTO object representation
        List<AnwSalesOrderDto> anwSalesOrderDtos = SalesOrderService.getAnwSalesOrders();

        // convert SAP Anywhere's DTO value into ERP native format and persist it in ERP
        SalesOrderService.postErpSalesOrders(anwSalesOrderDtos);

        // log end
        LOG.info("End of integration SAP Anywhere to ERP");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

    /**
     * Integration of Sales Order from ERP to SAP Anywhere.
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncToSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration ERP to SAP Anywhere");

        // read Sales Orders from ERP and get their DTO object representation
        List<ErpSalesOrderDto> erpSalesOrders = SalesOrderService.getErpSalesOrders();

        // post ERP sales orders
        SalesOrderService.postAnwSalesOrders(erpSalesOrders);

        // log end
        LOG.info("End of integration ERP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }
}
