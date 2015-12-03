package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwPaymentDto;
import com.sap.integration.core.transformation.PaymentTransformation;
import com.sap.integration.erp.dto.ErpPaymentDto;
import com.sap.integration.erp.dummy.conversion.PaymentConversion;
import com.sap.integration.erp.dummy.entity.Payment;
import com.sap.integration.erp.dummy.jpa.JpaLayer;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;

/**
 * Class used for getting and posting of payment data to SAP Anywhere / ERP.
 */
public class PaymentService {

    public static final String PAYMENTS = "Payments";

    private static final Logger LOG = Logger.getLogger(PaymentService.class);

    private static IntegrationState integrationState;

    /**
     * Get SAP Anywhere payment data transfer object
     * 
     * @param id
     *            Payment's id in SAP Anywhere
     * @return AnwPaymentDto
     * @throws Exception
     */
    public static AnwPaymentDto getAnwPayment(String id) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(PAYMENTS)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "id eq " + id)
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder);
        List<AnwPaymentDto> anwPayments;
        try {
            anwPayments = JsonUtil.getObjects(response.getContent(), AnwPaymentDto.class);
            if (anwPayments.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwPayments.get(0);
    }

    /**
     * Get list of all SAP Anywhere payment data transfer objects between from and to date.
     * 
     * @param fromDate Date before first payment creation date
     * @param toDate Date after last payment creation date
     * @return List<AnwPaymentDto>
     * @throws Exception
     */
    public static List<AnwPaymentDto> getAnwPaymentsPeriod(DateTime fromDate, DateTime toDate) throws Exception {
        // get SAP Anywhere payment
        List<AnwPaymentDto> anwPayments = new ArrayList<AnwPaymentDto>();
        List<AnwPaymentDto> anwPaymentsPage;
        int offset = 0;
        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(PAYMENTS)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter("filter",
                            "creationTime gt '" + DateUtil.convertDateTimeToString(fromDate) + "' and "
                                    + "creationTime lt '" + DateUtil.convertDateTimeToString(toDate) + "' ")
                    .parameter("expand", "paymentMethodLines")
                    .parameter("access_token", Property.getAccessToken());

            anwPaymentsPage = JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(), AnwPaymentDto.class);
            anwPayments.addAll(anwPaymentsPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwPaymentsPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwPayments;
    }

    /**
     * Get list of SAP Anywhere payment data transfer objects ordered by updateTime.
     * 
     * @return List<AnwPaymentDto>
     * @throws Exception
     */
    public static List<AnwPaymentDto> getAnwPayments() throws Exception {
        // read last update time of payments
        integrationState = IntegrationState.getIntegrationStateFor(AnwPaymentDto.class);

        // get SAP Anywhere payment
		List<AnwPaymentDto> anwPayments = new ArrayList<AnwPaymentDto>();
        List<AnwPaymentDto> anwPaymentsPage;
        int offset = 0;
        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(PAYMENTS)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter(
                            "filter",
                            "updateTime gt '" + DateUtil.convertDateTimeToString(integrationState.getLastSyncTime())
                                    + "'")
                    .parameter("expand", "paymentLines, paymentMethodLines")
                    .parameter("orderby", "updateTime")                    
                    .parameter("access_token", Property.getAccessToken());

            anwPaymentsPage = JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(), AnwPaymentDto.class);
            anwPayments.addAll(anwPaymentsPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwPaymentsPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwPayments;
    }

    /**
     * Evoke CREATE / UPDATE of SAP Anywhere payments and persist timestamp of last successfully transfered SAP
     * Anywhere payment
     * 
     * @param erpPayments
     *            ERP payment data transfer objects
     * @throws Exception
     */
    public static void postAnwPayments(List<ErpPaymentDto> erpPayments) throws Exception {
        // read last update time of payment
        integrationState = IntegrationState.getIntegrationStateFor(ErpPaymentDto.class);

        // post payments to SAP Anywhere
        int createdCount = 0;
        int updatedCount = 0;
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(PAYMENTS)
                .parameter("access_token", Property.getAccessToken());
        for (ErpPaymentDto erpPayment : erpPayments) {
            AnwPaymentDto anwPayment = PaymentService.getAnwPayment(erpPayment.getAnwId().toString());
            AnwSimpleResponse response = null;
            if (anwPayment == null) { // create
            	AnwPaymentDto anwPaymentToCreate = PaymentTransformation.run(erpPayment);
                response = AnwServiceCall.post(urlBuilder, anwPaymentToCreate);
                if (response != null) {
                	
                	//Update payment data in ERP - insert id of payment just created in SAP Anywhere (parameter anwId) into ERP
                    if (response.getId() == null) {
                        throw new Exception(
                                "Problem with POSTing payment into SAP Anywhere - incorrect response from server.");
                    } else {
        				anwPaymentToCreate.setId(response.getId());
                        Payment pm = PaymentConversion.run(PaymentTransformation.run(anwPaymentToCreate));
        				pm.merge();
        			}
                    createdCount++;
                }
            } else { // update
                if (erpPayment.getLastUpdateTime().isAfter(anwPayment.getUpdateTime())) {
                    response = AnwServiceCall.post(urlBuilder, PaymentTransformation.run(erpPayment));
                    if (response != null) {
                        updatedCount++;
                    }
                }
            }

            // update last sync time of successfully transfered payment
            if (response != null && erpPayment.getLastUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(erpPayment.getLastUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " payments");
        LOG.info("Successfully updated " + updatedCount + " payments");
        LOG.info("Failed " + (erpPayments.size() - createdCount - updatedCount) + " payments");
    }

    /**
     * Get ERP payment data transfer object
     * 
     * @param anwId
     *            Payment's ID in SAP Anywhere - stored in ERP as parameter for join records between SAP Anywhere and ERP
     * @return ErpPaymentDto
     */
    public static ErpPaymentDto getErpPayment(String anwId) throws Exception {
        return PaymentConversion.run(
                Payment.findFirst(Payment.class, "SELECT p FROM Payment p WHERE p.anwId = ?1", new Long(anwId)));
    }

    /**
     * Get list of ERP payment data transfer objects ordered by lastUpdateTime.
     * 
     * @return List<ErpPaymentDto>
     */
    @SuppressWarnings("unchecked")
    public static List<ErpPaymentDto> getErpPayments() throws Exception {
        // read last update time of payments
        integrationState = IntegrationState.getIntegrationStateFor(ErpPaymentDto.class);

        // get ERP Payments
        List<ErpPaymentDto> erpPayments = new ArrayList<ErpPaymentDto>();
        List<Payment> payments = JpaLayer
                .find(Payment.class,
                        "SELECT p FROM Payment p WHERE (p.lastUpdateTime > ?1) or (p.lastUpdateTime is NULL) ORDER BY p.lastUpdateTime",
                        integrationState.getLastSyncTime());

        // convert db items into ERP product data transfer objects
        erpPayments = (List<ErpPaymentDto>) PaymentConversion.run(Payment.class, payments);

        return erpPayments;
    }

    /**
     * Evoke CREATE / UPDATE of ERP payments and persist timestamp of last successfully transfered ERP payment
     * 
     * @param anwPayments
     *            Sap Anywhere payment data transfer objects
     */
    public static void postErpPayments(List<AnwPaymentDto> anwPayments) throws Exception {
        // read last update time of payment
        integrationState = IntegrationState.getIntegrationStateFor(AnwPaymentDto.class);

        // post payments to ERP
        int createdCount = 0;
        int updatedCount = 0;
        for (AnwPaymentDto anwPayment : anwPayments) {
            ErpPaymentDto erpPayment = PaymentService.getErpPayment(anwPayment.getId().toString());
            if (erpPayment == null) { // create
                PaymentConversion.run(PaymentTransformation.run(anwPayment)).persist();
                createdCount++;
            } else { // update
                if (anwPayment.getUpdateTime().isAfter(erpPayment.getLastUpdateTime())) {
                	
                	ErpPaymentDto updatedErpPayment = PaymentTransformation.run(anwPayment);
                	updatedErpPayment.setId(erpPayment.getId());
                	
                    PaymentConversion.run(updatedErpPayment).merge();
                    updatedCount++;
                }
            }

            // update last sync time of successfully transfered payment
            if (anwPayment.getUpdateTime().isAfter(integrationState.getLastSyncTime())) {
                integrationState.setLastSyncTime(anwPayment.getUpdateTime());
                integrationState.merge();
            }
        }

        LOG.info("Successfully created " + createdCount + " payments");
        LOG.info("Successfully updated " + updatedCount + " payments");
        LOG.info("Failed " + (anwPayments.size() - createdCount - updatedCount) + " payments");
    }
}