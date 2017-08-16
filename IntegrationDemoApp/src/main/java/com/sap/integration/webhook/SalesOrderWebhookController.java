package com.sap.integration.webhook;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.integration.anywhere.AccessTokenGetter;
import com.sap.integration.anywhere.AnwErrorCode;
import com.sap.integration.anywhere.AnwErrorObject;
import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.SimpleResponse;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.salesorder.model.AnwSalesOrderDto;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.webhook.definition.BoChangeNotification;
import com.sap.integration.webhook.util.AnwWebhoookVerifier;

@RestController
@RequestMapping(value = "/Webhook")
public class SalesOrderWebhookController {
    private static Logger logger = Logger.getLogger(SalesOrderWebhookController.class);

    @RequestMapping(value = "/ListenSalesOrderCreated", method = RequestMethod.POST)
    public void ListenSalesOrderCreated(@RequestBody BoChangeNotification boPayload) {
        try {
            if (AnwWebhoookVerifier.verifyWebhook(boPayload.getTimestamp(), boPayload.getHmac())) {
                logger.info(
                        "-------------------------------------------------------------------------------------------------------");
                logger.info(String.format("Event Type: %s", boPayload.getPayload().getEvent_type()));
                logger.info(String.format("Resource Id: %s", boPayload.getPayload().getResource_id()));
                logger.info(String.format("Tenant Code: %s", boPayload.getPayload().getTenant_code()));
                AnwSalesOrderDto salesOrder = querySalesOrder(boPayload.getPayload().getResource_id());
                if (salesOrder != null) {
                    logger.info(String.format("SalesOrder has %d lines", salesOrder.getProductLines().size()));
                }
            } else {
                throw new Exception("illegal webhook request!");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private AnwSalesOrderDto querySalesOrder(String resourceId) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("SalesOrders/").append(resourceId)
                .parameter("select", "customer,productLines")
                .parameter("expand", "productLines");
        // 429 over rate limiter
        SimpleResponse response = AnwServiceCall.get(urlBuilder, null);
        if (response.getStatusCode() == AnwErrorCode.HTTP_STATUS_TOO_MANY_REQEUEST) {
            Thread.sleep(1000l);
            response = AnwServiceCall.get(urlBuilder, null);
        } else if (response.getStatusCode() == HttpStatus.SC_UNAUTHORIZED
                && StringUtils.isNotEmpty(response.getContent())) {
            AnwErrorObject error = JsonUtil.getObject(response.getContent(),
                    AnwErrorObject.class);
            // check oauth expired, get access token again.
            if (error.getErrorCode().equals(AnwErrorCode.OAUTH_EXPIRED)) {
                AccessTokenGetter.runAccessToken();
                response = AnwServiceCall.get(urlBuilder, null);
            }
        }
        return (AnwSalesOrderDto) JsonUtil.getObject(response.getContent(),
                AnwSalesOrderDto.class);
    }
}
