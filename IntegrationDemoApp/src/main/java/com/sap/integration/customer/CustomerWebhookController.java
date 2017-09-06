package com.sap.integration.customer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.integration.anywhere.AccessTokenGetter;
import com.sap.integration.anywhere.AnwErrorCode;
import com.sap.integration.anywhere.AnwErrorObject;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.SimpleResponse;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.customer.model.AnwCustomerDto;
import com.sap.integration.utils.HttpsCallUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.webhook.definition.BoChangeNotification;
import com.sap.integration.webhook.util.AnwWebhoookVerifier;

@RestController
@RequestMapping(value = "/Webhook")
public class CustomerWebhookController {
    private static Logger LOG = Logger.getLogger(CustomerWebhookController.class);

    @RequestMapping(value = "/ListenCustomerAction", method = RequestMethod.POST)
    public void ListenCustomerCreated(@RequestBody BoChangeNotification boPayload) {
        try {
            if (AnwWebhoookVerifier.verifyWebhook(boPayload.getTimestamp(), boPayload.getHmac())) {
                LOG.info(
                        "-------------------------------------------------------------------------------------------------------");
                LOG.info(String.format("Event Type: %s", boPayload.getPayload().getEvent_type()));
                LOG.info(String.format("Resource Id: %s", boPayload.getPayload().getResource_id()));
                LOG.info(String.format("Tenant Code: %s", boPayload.getPayload().getTenant_code()));
                /*
                 * TODO needs review string
                 * Webhook needs quick response, so here needs async to get data from SAP anywhere.
                 * It can be persist id, and put into a pool to get data together.
                 */
                new customerAnwGetter(boPayload.getPayload().getResource_id()).start();
            } else {
                throw new Exception("illegal webhook request!");
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private class customerAnwGetter extends Thread {
        private String resourceId;

        public customerAnwGetter(String resourceId) {
            this.resourceId = resourceId;
        }

        public void run() {
            try {
                LOG.info("Thread start call customer " + resourceId);
                StringBuilder strBuilder = new StringBuilder()
                        .append(AnwUrlUtil.getOpenApiBaseUrl())
                        .append("Customers")
                        .append("/")
                        .append(resourceId);

                String url = strBuilder.toString();

                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
                httpGet.setHeader("Access-Token", Property.getAccessToken());

                AnwSimpleResponse response = HttpsCallUtil.executeRequest(httpGet, new AnwSimpleResponse(url));

                if (response.getStatusCode() == AnwErrorCode.HTTP_STATUS_TOO_MANY_REQEUEST) {
                    /*
                     * Http status: 429, too many request over the rate limiter.
                     * You can send request again wait for a while, or wait for next job schedule
                     */
                    Thread.sleep(500l);
                    response = HttpsCallUtil.executeRequest(httpGet, new AnwSimpleResponse(url));
                } else if (response.getStatusCode() == HttpStatus.SC_UNAUTHORIZED
                        && StringUtils.isNotEmpty(response.getContent())) {
                    AnwErrorObject error = JsonUtil.getObject(response.getContent(),
                            AnwErrorObject.class);
                    if (error.getErrorCode().equals(AnwErrorCode.OAUTH_EXPIRED)) {
                        /*
                         * Http status:401, and error code is APP00001.
                         * Get access token again.
                         * If necessary, you can send request again, or wait for next job schedule.
                         */
                        AccessTokenGetter.runAccessToken();
                        response = HttpsCallUtil.executeRequest(httpGet, new AnwSimpleResponse(url));
                        ;
                    }
                } else if (response.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                    /*
                     * handle error > 400
                     */
                    LOG.error(response.getContent());
                }
                if (response.getStatusCode() == HttpStatus.SC_OK) {

                    AnwCustomerDto customer = JsonUtil.getObject(response.getContent(),
                            AnwCustomerDto.class);
                    /*
                     * It can be persist data in APP or what you want to do.
                     */
                    LOG.info("Thread end call customer " + resourceId + " successful");
                    return;
                }
            } catch (JsonParseException e) {
                // handle error
            } catch (JsonMappingException e) {
                // handle error
            } catch (IOException e) {
                // handle error
            } catch (Exception e) {
                // handle error
            }
            LOG.info("Thread end call customer " + resourceId + " failure");
        }
    }
}
