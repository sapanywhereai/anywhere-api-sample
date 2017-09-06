package com.sap.integration.customer;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.web.bind.annotation.RequestBody;

import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.utils.HttpsCall;
import com.sap.integration.utils.HttpsCallUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.webhook.definition.AnwWebhook;
import com.sap.integration.webhook.definition.WebhookEventType;

/**
 * Class used for getting SAP Anywhere data.
 */
public class CustomerWebhookRegister {

    private static final Logger LOG = Logger.getLogger(CustomerWebhookRegister.class);

    private static String ALREADY_EXIST_ERROR_CODE = "P100D00007";

    public static Long RegisterWebhook(String eventType, String listenUrl) throws Exception {
        StringBuilder strBuilder = new StringBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Webhooks");

        AnwWebhook webhook = new AnwWebhook(eventType, listenUrl);
        
        String url = strBuilder.toString();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("Access-Token", Property.getAccessToken());

        String entity = JsonUtil.getJson(webhook, Visibility.ANY);
        httpPost.setEntity(new StringEntity(entity, ContentType.APPLICATION_JSON));

        AnwSimpleResponse response = HttpsCallUtil.executeRequest(httpPost, new AnwSimpleResponse(url));
        Long id = response.getId();
        if ((response.getId() == null) || (response.getStatusCode() != 201)) {
            // already exist this eventType.
            if (ALREADY_EXIST_ERROR_CODE.equals(response.getErrorCode())) {
                id = Property.getWebhookEventId(eventType);
                updateRegisterURL(id, webhook);
                LOG.info("successful update " + eventType + " on" + listenUrl);
            } else {
                throw new Exception(String.format(
                        "Create webhook failure!, Response from server is: %s, response code is: %s, url is %s",
                        response.getContent(), response.getStatusCode(), listenUrl));
            }
        }
        LOG.info("successful register event " + eventType + " on " + listenUrl);
        return id;
    }

    public static void updateRegisterURL(long id, AnwWebhook webhook) throws Exception {
        StringBuilder strBuilder = new StringBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("Webhooks")
                .append("/")
                .append(String.valueOf(id));

        String url = strBuilder.toString();
        HttpPatch httpPatch = new HttpPatch(url);

        httpPatch.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpPatch.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        httpPatch.setHeader("Access-Token", Property.getAccessToken());

        String entity = JsonUtil.getJson(webhook, Visibility.ANY);

        httpPatch.setEntity(new StringEntity(entity, ContentType.APPLICATION_JSON));
        AnwSimpleResponse response = HttpsCallUtil.executeRequest(httpPatch, new AnwSimpleResponse(url));

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
        doRegister(WebhookEventType.CUSTOMER_CREATE, url + "Webhook/ListenCustomerAction");
        doRegister(WebhookEventType.CUSTOMER_UPDATE, url + "Webhook/ListenCustomerAction");
    }

    private static void doRegister(String eventType, String url) throws Exception {
        Long id = CustomerWebhookRegister.RegisterWebhook(eventType, url);
        Property.setWebhookEventId(eventType, id);
    }
}
