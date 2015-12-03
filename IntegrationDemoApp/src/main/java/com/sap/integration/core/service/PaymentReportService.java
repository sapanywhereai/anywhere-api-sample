package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwAmountDto;
import com.sap.integration.anywhere.dto.AnwPaymentDto;
import com.sap.integration.anywhere.dto.AnwPaymentMethodLineDto;
import com.sap.integration.erp.dummy.entity.PaymentReport;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Class used for creating payment reports of SAP Anywhere.
 */
public class PaymentReportService {

    private static final Logger LOG = Logger.getLogger(PaymentReportService.class);

    private static List<PaymentReport> paymentReports;

    public static void doReports() throws Exception {
        DateTime fromDate = new DateTime("2015-01-01");
        DateTime toDate = new DateTime("2016-01-01");

        LOG.info("Payment reports from: " + fromDate + " to: " + toDate);

        loadReportsFromSapAnywhere(fromDate, toDate);
        saveReportsToErpDB();
        // loadReportsFromErpDB(fromDate, toDate, fromDate);
        showReports();
    }
    
    /**
     * Get payment reports from SAP Anywhere, where payment creation date is between from and to date.
     * 
     * @param fromDate Date before first payment creation date
     * @param toDate Date after last payment creation date
     * @throws Exception
     */
    public static void loadReportsFromSapAnywhere(DateTime fromDate, DateTime toDate) throws Exception {
        Map<String, List<AnwAmountDto>> paymentsByMethodInfoMap = new HashMap<String, List<AnwAmountDto>>();

        // get all payments in date period from SAP Anywhere
        for (AnwPaymentDto anwPayment : PaymentService.getAnwPaymentsPeriod(fromDate, toDate)) {
            for (AnwPaymentMethodLineDto paymentMethodLine : anwPayment.getPaymentMethodLines()) {
                List<AnwAmountDto> amounts = paymentsByMethodInfoMap
                        .get(paymentMethodLine.getPaymentMethod().getName());
                if (amounts == null) {
                    amounts = new ArrayList<AnwAmountDto>();
                }
                amounts.add(paymentMethodLine.getAmount());

                paymentsByMethodInfoMap.put(paymentMethodLine.getPaymentMethod().getName(), amounts);
            }
        }

        // create payment reports
        paymentReports = new ArrayList<PaymentReport>();
        for (String paymentMethodInfo : paymentsByMethodInfoMap.keySet()) {
            PaymentReport paymentReport = new PaymentReport();
            paymentReport.setMethodInfo(paymentMethodInfo);
            paymentReport.setAmount(getAmountSum(paymentsByMethodInfoMap.get(paymentMethodInfo)));
            paymentReport.setAmountLC(getAmountLocalCurrencySum(paymentsByMethodInfoMap.get(paymentMethodInfo)));
            paymentReport.setNumberOfPayments(getNumberOfPayments(paymentsByMethodInfoMap.get(paymentMethodInfo)));
            paymentReport.setFromDate(fromDate);
            paymentReport.setToDate(toDate);
            paymentReports.add(paymentReport);
        }
    }

    /**
     * Get payment reports from ERP DB newer than last update date.
     * 
     * @param fromDate Date of first SAP Anywhere payment creation date
     * @param toDate Date of last SAP Anywhere payment creation date
     * @param lastUpdateDate Date of last payment report update
     */
    public static void loadReportsFromErpDB(DateTime fromDate, DateTime toDate, DateTime lastUpdateDate) {
        paymentReports = JpaLayer.find(PaymentReport.class,
                        "SELECT pr FROM PaymentReport pr WHERE pr.fromDate >= ?1 AND pr.toDate <= ?2 AND pr.lastUpdateTime >= ?3",
                        fromDate, toDate, lastUpdateDate);
    }

    /**
     * Save loaded payment reports to ERP DB.
     * 
     * @param lastUpdateDate
     */
    public static void saveReportsToErpDB() {
        if (paymentReports == null || paymentReports.isEmpty()) {
            LOG.warn("No payment reports to save.");
            return;
        }

        // save payment reports
        for (PaymentReport paymentReport : paymentReports) {
            paymentReport.persist();
        }
    }

    /**
     * Show loaded payment reports
     */
    public static void showReports() {
        if (paymentReports == null || paymentReports.isEmpty()) {
            LOG.warn("No payment reports to show.");
            return;
        }

        // show payment reports
        for (PaymentReport paymentReport : paymentReports) {
            LOG.info("Payment type: " + paymentReport.getMethodInfo()
                    + ", Number of payments: " + paymentReport.getNumberOfPayments()
                    + ", Amount: " + paymentReport.getAmount()
                    + ", Amount local currency: " + paymentReport.getAmountLC());
        }
    }
    
    private static Double getAmountSum(List<AnwAmountDto> amounts) {
        Double amountSum = 0.;
        for (AnwAmountDto amount : amounts) {
            amountSum += amount.getAmount();
        }

        return amountSum;
    }

    private static Double getAmountLocalCurrencySum(List<AnwAmountDto> amounts) {
        Double amountLocalCurrency = 0.;
        for (AnwAmountDto amount : amounts) {
            amountLocalCurrency += amount.getAmountLocalCurrency();
        }

        return amountLocalCurrency;
    }

    private static Integer getNumberOfPayments(List<AnwAmountDto> amounts) {
        return amounts.size();
    }

}
