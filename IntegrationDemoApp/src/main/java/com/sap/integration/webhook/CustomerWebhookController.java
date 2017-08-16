package com.sap.integration.webhook;

import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
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
import com.sap.integration.customer.CustomerIntegration;
import com.sap.integration.customer.model.AnwCustomerDto;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.webhook.definition.BoChangeNotification;
import com.sap.integration.webhook.util.AnwWebhoookVerifier;

@RestController
@RequestMapping(value = "/Webhook")
public class CustomerWebhookController {
    private static Logger logger = Logger.getLogger(CustomerWebhookController.class);

    @RequestMapping(value = "/ListenCustomerCreated", method = RequestMethod.POST)
    public void ListenCustomerCreated(@RequestBody BoChangeNotification boPayload) {
        try {
            if (AnwWebhoookVerifier.verifyWebhook(boPayload.getTimestamp(), boPayload.getHmac())) {
                logger.info(
                        "-------------------------------------------------------------------------------------------------------");
                logger.info(String.format("Event Type: %s", boPayload.getPayload().getEvent_type()));
                logger.info(String.format("Resource Id: %s", boPayload.getPayload().getResource_id()));
                logger.info(String.format("Tenant Code: %s", boPayload.getPayload().getTenant_code()));
                List<AnwCustomerDto> customers = queryCustomer(boPayload.getPayload().getResource_id());
                if (!CollectionUtils.isEmpty(customers)) {
                    logger.info(String.format("Customer %s created", customers.get(0).getDisplayName()));
                }
            } else {
                throw new Exception("illegal webhook request!");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @RequestMapping(value = "/ListenCustomerUpdated", method = RequestMethod.POST)
    public void ListenCustomerUpdated(@RequestBody BoChangeNotification boPayload) throws Exception {
        CustomerIntegration.syncFromSapAnywhere(boPayload.getPayload().getResource_id());
        CustomerIntegration.syncToSapAnywhere();
    }

    // example "in"
    private List<AnwCustomerDto> queryCustomer(String resourceId) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Customers")
                .parameter("filter", "id in [" + resourceId + "]");

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
        return (List<AnwCustomerDto>) JsonUtil.getObjects(response.getContent(),
                AnwCustomerDto.class);
    }

}
