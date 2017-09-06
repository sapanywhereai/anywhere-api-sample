package com.sap.integration.salesorder;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.salesorder.model.AnwSalesOrderDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of Sales Orders is defined in this class. Do not run both sync methods to avoid circle updating!
 */
public class SalesOrderIntegration {

    private static final Logger LOG = Logger.getLogger(SalesOrderIntegration.class);

    /**
     * Integration of sales order from SAP Anywhere to APP.
     * 
     * @throws Exception
     *             possible exception during the processing
     */
    public static void syncFromSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of SalesOrder integration SAP Anywhere to APP");

        // read Sales Orders from SAP Anywhere and get their DTO object representation
        List<AnwSalesOrderDto> anwSalesOrderDtos = SalesOrderService.getAnwSalesOrders();

        /*
         * convert SAP Anywhere's DTO value into APP native format, and persist it or what you want to do
         */

        // log end
        LOG.info("End of SalesOrder integration SAP Anywhere to APP");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }
}
