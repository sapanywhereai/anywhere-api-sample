package com.sap.integration.anywhere;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.oauth.AccessTokenLoader;

public class AccessTokenGetter {

    /**
     * Logger for logging purposes. Similar definition is used in all classes of this application. You may configure it
     * in log4j.properties configuration file.
     */
    private static final Logger LOG = Logger.getLogger(AccessTokenGetter.class);

    /**
     * Method, which calls classes and method for retrieving Access Token.
     */
    public static void runAccessToken() {
        LOG.info("Retrieving Access Token");
        try {
            AccessTokenLoader.load();
        } catch (Exception e) {
            LOG.error("Exception " + e.getMessage(), e);
        }
        LOG.info("Access Token retrieved");
    }
}
