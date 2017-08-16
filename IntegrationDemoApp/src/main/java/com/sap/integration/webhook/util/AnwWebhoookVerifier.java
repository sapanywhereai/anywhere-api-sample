package com.sap.integration.webhook.util;

import com.sap.integration.anywhere.oauth.SHA256Algorithm;
import com.sap.integration.utils.configuration.Property;

public class AnwWebhoookVerifier {
    public static boolean verifyWebhook(String timestamp, String hmac) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("apiKey=").append(Property.getAppId()).append("&timestamp=").append(timestamp);

        String encodedCode = SHA256Algorithm.encode(Property.getAppSecret(), builder.toString());
        if (encodedCode.equals(hmac)) {
            return true;
        }
        return false;
    }

}
