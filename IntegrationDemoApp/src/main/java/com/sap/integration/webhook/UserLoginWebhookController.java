package com.sap.integration.webhook;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.integration.webhook.definition.LoginNotification;
import com.sap.integration.webhook.util.AnwWebhoookVerifier;

@RestController
@RequestMapping(value = "/Webhook")
public class UserLoginWebhookController {
    private static Logger logger = Logger.getLogger(UserLoginWebhookController.class);

    @RequestMapping(value = "ListenUserLogin", method = RequestMethod.POST)
    public void ListenUserLogin(@RequestBody LoginNotification loginNotification) {
        try {
            if (AnwWebhoookVerifier.verifyWebhook(loginNotification.getTimestamp(), loginNotification.getHmac())) {
                logger.info(
                        "-------------------------------------------------------------------------------------------------------");
                logger.info(String.format("Event Type: %s", loginNotification.getPayload().getEventType()));
                logger.info(String.format("Email: %s", loginNotification.getPayload().getEmail()));
                logger.info(String.format("Last login: %s", loginNotification.getPayload().getLastLogin()));
                logger.info(String.format("Tenant Code: %s", loginNotification.getPayload().getTenantCode()));
            } else {
                throw new Exception("illegal webhook request!");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
