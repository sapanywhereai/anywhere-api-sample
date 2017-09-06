package com.sap.integration.webhook.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.utils.HttpsCall;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.webhook.CustomerWebhookController;
import com.sap.integration.webhook.definition.AnwWebhook;
import com.sap.integration.webhook.definition.WebhookEventType;

/*reference 
 * https://doc.sapanywhere.com/api/spec/use_webhook#create_webhooks
 * https://doc.sapanywhere.com/api/api_ref/Webhook#/definitions/Webhook
 */

public class AnwWebhookRegister {
    private static Logger logger = Logger.getLogger(CustomerWebhookController.class);

    private static String ALREADY_EXIST_ERROR_CODE = "P100D00007";

    public static Long RegisterWebhook(String eventType, String url) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Webhooks");

        AnwWebhook webhook = new AnwWebhook(eventType, url);
        AnwSimpleResponse response = AnwServiceCall.post(urlBuilder, webhook, null);
        Long id = response.getId();
        if ((response.getId() == null) || (response.getStatusCode() != 201)) {
            // already exist this eventType.
            if (ALREADY_EXIST_ERROR_CODE.equals(response.getErrorCode())) {
                id = Property.getWebhookEventId(eventType);
                updateRegisterURL(id, webhook);
                logger.info("successful is updated " + eventType + " on" + url);
            } else {
                throw new Exception(String.format("Create webhook failure!, Response from server is: %s, response code is: %s, url is %s",
                        response.getContent(), response.getStatusCode(), url));
            }
        }
        logger.info("successful register event " + eventType + " on " + url);
        return id;
    }

    public static void updateRegisterURL(long id, AnwWebhook webhook) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Webhooks")
                .append("/")
                .append(String.valueOf(id));
        AnwSimpleResponse response = AnwServiceCall.patch(urlBuilder, webhook, null);
        if (response.getStatusCode() != 200) {
            throw new Exception("Create webhook failure!, Response from server was: " + response.getContent());
        }
    }

    public static void UnregisterWebhook(long id) throws URISyntaxException, Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Webhooks")
                .append("/")
                .append(String.valueOf(id));
        String url = urlBuilder.get();
        HttpsCall.delete(new URI(url), new AnwSimpleResponse(url), null);
    }

    public static void registerWebhook(@RequestBody String url) throws Exception {
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        doRegister(WebhookEventType.CUSTOMER_CREATE, url + "Webhook/ListenCustomerCreated");
        doRegister(WebhookEventType.CUSTOMER_UPDATE, url + "Webhook/ListenCustomerUpdated");
        doRegister(WebhookEventType.USER_LOGIN, url + "Webhook/ListenUserLogin");
        doRegister(WebhookEventType.SALESORDER_CREATE, url + "Webhook/ListenSalesOrderCreated");
    }

    private static void doRegister(String eventType, String url) throws Exception {
        Long id = AnwWebhookRegister.RegisterWebhook(eventType, url);
        Property.setWebhookEventId(eventType, id);
    }
}
