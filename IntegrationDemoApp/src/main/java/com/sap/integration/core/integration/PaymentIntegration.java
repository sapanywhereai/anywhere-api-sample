package com.sap.integration.core.integration;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwPaymentDto;
import com.sap.integration.core.service.PaymentService;
import com.sap.integration.erp.dto.ErpPaymentDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of Payments is defined in this class.
 */
public class PaymentIntegration {

    private static final Logger LOG = Logger.getLogger(PaymentIntegration.class);

    /**
     * Integration of Payment from SAP Anywhere to ERP.
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncFromSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration SAP Anywhere to ERP");

        // read Sales Invoices from SAP Anywhere and get their DTO object representation
        List<AnwPaymentDto> anwPaymentDtos = PaymentService.getAnwPayments();

        // convert SAP Anywhere's DTO value into ERP native format and persist it in ERP
        PaymentService.postErpPayments(anwPaymentDtos);

        // log end
        LOG.info("End of integration SAP Anywhere to ERP");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

    /**
     * Integration of Payment from ERP to SAP Anywhere.
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncToSAPAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration ERP to SAP Anywhere");

        // read Sales Orders from ERP and get their DTO object representation
        List<ErpPaymentDto> erpPayments = PaymentService.getErpPayments();

        // post ERP payments
        PaymentService.postAnwPayments(erpPayments);

        // log end
        LOG.info("End of integration ERP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }
}