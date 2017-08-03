package com.sap.integration.anywhere;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sap.integration.anywhere.oauth.AccessTokenLoader;
import com.sap.integration.utils.HttpsCall;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class, which contains helper methods used for making HTTP calls to SAP Anywhere.
 */
@Service
public class AnwServiceCall {

    private final static Logger LOG = Logger.getLogger(AnwServiceCall.class);

    /**
     * Method, which make HTTP GET requests.
     * 
     * @param urlBuilder - URL Builder with created URL used for connection
     * @return instance of AnwSimpleResponse containing response
     */
    public static AnwSimpleResponse get(final UrlBuilder urlBuilder, final Map<String, String> headers) {
        return get(urlBuilder.get(), headers);
    }

    /**
     * Method, which make HTTP GET requests.
     * 
     * @param url - string URL for connection
     * @return instance of AnwSimpleResponse containing response
     */
    public static AnwSimpleResponse get(String url, final Map<String, String> headers) {
        AnwSimpleResponse response = null;
        try {
            response = HttpsCall.get(new URI(url), new AnwSimpleResponse(url), headers);
            if (responseRequiresAccessTokenRefresh(response)) {
                url = loadAndUpdateAccessToken(url);
                response = HttpsCall.get(new URI(url), new AnwSimpleResponse(url), headers);
            }
        } catch (Exception e) {
            LOG.error("Error while executing: " + url);
            LOG.error("Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * Method, which make HTTP POST requests.
     * 
     * @param urlBuilder - URL Builder with created URL used for connection
     * @param data - data which will be send via HTTP POST
     * @return instance of AnwSimpleResponse containing response
     */
    public static AnwSimpleResponse post(final UrlBuilder urlBuilder, final Object data, final Map<String, String> headers) {
        return post(urlBuilder.get(), data, headers);
    }

    /**
     * Method, which make HTTP POST requests.
     * 
     * @param url - string URL for connection
     * @param data - data which will be send via HTTP POST
     * @return instance of AnwSimpleResponse containing response
     */
    public static AnwSimpleResponse post(String url, final Object data, final Map<String, String> headers) {
        AnwSimpleResponse response = null;
        try {
            response = HttpsCall.post(new URI(url), data, new AnwSimpleResponse(url), headers);
            if (responseRequiresAccessTokenRefresh(response)) {
                url = loadAndUpdateAccessToken(url);
                response = HttpsCall.post(new URI(url), data, new AnwSimpleResponse(url), headers);
            }
        } catch (Exception e) {
            LOG.error("Error while executing: " + url);
            LOG.error("Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * Method, which make HTTP PATCH requests.
     * 
     * @param urlBuilder - URL Builder with created URL used for connection
     * @param data - data which will be send via HTTP PATCH
     * @return instance of AnwSimpleResponse containing response
     */
    public static AnwSimpleResponse patch(final UrlBuilder urlBuilder, final Object data, final Map<String, String> headers) {
        return patch(urlBuilder.get(), data, headers);
    }

    /**
     * Method, which make HTTP PATCH requests.
     * 
     * @param url - string URL for connection
     * @param data - data which will be send via HTTP PATCH
     * @return instance of AnwSimpleResponse containing response
     */
    public static AnwSimpleResponse patch(String url, final Object data, Map<String, String> headers) {
        AnwSimpleResponse response = null;
        try {
            response = HttpsCall.patch(new URI(url), data, new AnwSimpleResponse(url), headers);
            if (responseRequiresAccessTokenRefresh(response)) {
                url = loadAndUpdateAccessToken(url);
                response = HttpsCall.patch(new URI(url), data, new AnwSimpleResponse(url), headers);
            }
        } catch (Exception e) {
            LOG.error("Error while executing: " + url);
            LOG.error("Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * Method, which identifies, whether it is necessary to refresh access token or not. Blank value of access token might cause
     * returning of HTTP STATUS 500 - Internal Server Error.
     * 
     * @param response - response with response data used for verification of access token validity
     * @return true - it is necessary to refresh access token <br>
     *         false - it not necessary to refresh access token <br>
     */
    private static boolean responseRequiresAccessTokenRefresh(final AnwSimpleResponse response) {
        return (response.getStatusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR && isBlankAccessToken(response.getUrl()))
                || response.getStatusCode() == HttpURLConnection.HTTP_FORBIDDEN;
    }

    /**
     * Method, which loads new access token and attach it to the entered URL.
     * 
     * @param oldUrl - old url with expired access token
     * @return new URL with new valid access token
     */
    private static String loadAndUpdateAccessToken(final String oldUrl) {
        String updatedUrl = oldUrl;
        try {
            AccessTokenLoader.load();
            Matcher m = Pattern.compile("(.*access_token=).*?(&.*|$)").matcher(oldUrl);
            if (m.matches() && m.groupCount() >= 2) {
                updatedUrl = m.group(1);
                updatedUrl += Property.getAccessToken();
                if (m.groupCount() > 2) {
                    updatedUrl += m.group(2);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception: " + e.getMessage());
        }
        return updatedUrl;
    }

    /**
     * Method, which identifies, whether access token is missing or not.
     * 
     * @param url - URL used for identification whether access token is missing or not
     * @return true - access token is missing <br>
     *         false - access token is not missing <br>
     */
    private static boolean isBlankAccessToken(final String access_token) {
        Matcher m = Pattern.compile(".*access_token=(.*?)(&.*|$)").matcher(access_token);
        if (m.matches() && m.groupCount() >= 2) {
            String accessToken = m.group(1);
            return StringUtils.isBlank(accessToken);
        }
        return true;
    }
    
    public Map<String, String> preparedHeaders() throws Exception {
		return new HashMap<String, String>() {
			{
				put("Access-Token", Property.getAccessToken());
			}
		};
	}
}
