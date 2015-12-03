package com.sap.integration.anywhere;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Utility class used for constructing URL for HTTP requests.
 */
public class AnwUrlUtil {

    private static final Logger LOG = Logger.getLogger(AnwUrlUtil.class);
    
    /** OPENAPI_PATH is path, on which is exposed OPEN API. It can be changed, so probably best way is to store it in config.properties file. */
    public static final String OPENAPI_PATH = "api-gateway/v1";
    public static final int OPENAPI_QEURY_OPTION_LIMIT = 100;

    /**
     * Method construct and return base Open API URL. Sample of returned URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/</code> <br>
     * <br>
     * <b>Warning:</b> Method logs sensitive information for testing and demo purposes. In real application logger should be
     * deleted, not just disabled by configuration! <br>
     * 
     * @return base URL to the Open API
     * @throws Exception possible exception during loading of configuration data
     */
    public static String getOpenApiBaseUrl() throws Exception {
        StringBuilder url = new StringBuilder();
        if (StringUtils.isNotBlank(Property.getAnwProtocol())) {
            url.append(Property.getAnwProtocol());
            url.append("://");
        }
        url.append(Property.getAnwServer());
        if (StringUtils.isNotBlank(Property.getAnwPort())) {
            url.append(":");
            url.append(Property.getAnwPort());
        }
        url.append("/");
        url.append(OPENAPI_PATH);
        url.append("/");

        String baseUrl = url.toString();
        LOG.debug("Open API - base path = " + baseUrl);
        return baseUrl;
    }

    /**
     * Method construct and return base OAuth URL. Sample of returned URL: <br>
     * <br>
     * <code>https://anywhereserver:port/sld/oauth2/token</code> <br>
     * <br>
     * <b>Warning:</b> Method logs sensitive information for testing and demo purposes. In real application logger should be
     * deleted, not just disabled by configuration! <br>
     * 
     * @return base URL to the OAuth
     * @throws Exception possible exception during loading of configuration data
     */
    public static String getOauthBaseUrl() throws Exception {
        StringBuilder url = new StringBuilder();
        if (StringUtils.isNotBlank(Property.getIdpProtocol())) {
            url.append(Property.getIdpProtocol());
            url.append("://");
        }
        url.append(Property.getIdpServer());
        if (StringUtils.isNotBlank(Property.getIdpPort())) {
            url.append(":");
            url.append(Property.getIdpPort());
        }
        url.append("/");
        url.append(Property.getIdpPath());
        String baseUrl = url.toString();
        LOG.debug("OAuth - base path = " + baseUrl);
        return baseUrl;
    }

    /**
     * Method returns URL, which is used for getting Access Token. Sample of returned URL as string: <br>
     * <br>
     * <code>https://idpserver:idpport/sld/oauth2/token?client_id=client_id&client_secret=client_secret&refresh_token=refresh_token&grant_type=refresh_token</code>
     * <br>
     * <br>
     * <b>Warning:</b> Method logs sensitive information for testing and demo purposes. In real application logger should be
     * deleted, not just disabled by configuration! <br>
     * 
     * @return string URL used for getting Access Token
     * @throws Exception possible exception during loading of configuration data
     */
    public static String getAccessTokenUrl() throws Exception {
        UrlBuilder url = new UrlBuilder(getOauthBaseUrl());
        url.parameter("client_id", Property.getAppId());
        url.parameter("client_secret", Property.getAppSecret());
        url.parameter("refresh_token", Property.getRefreshToken());
        url.parameter("grant_type", "refresh_token");
        String baseUrl = url.get();
        LOG.debug("Access Token - created URL = " + baseUrl);
        return baseUrl;
    }
}
