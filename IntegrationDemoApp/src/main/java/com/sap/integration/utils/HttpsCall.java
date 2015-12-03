package com.sap.integration.utils;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * Utility class used for HTTPS GET, POST and PATCH requests. For demo purpose all trusting managers are used.
 */
public class HttpsCall {

    private static final Logger LOG = Logger.getLogger(HttpsCall.class);

    private static HttpClientBuilder httpClientBuilder = null;

    /**
     * HTTP GET request, retrieving data by object URL.
     * 
     * @param uri - URL used for connection
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static SimpleResponse get(final URI uri)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {

        return get(uri, new SimpleResponse());
    }

    /**
     * HTTP GET request, retrieving data by object URL.
     * 
     * @param uri - URL used for connection
     * @param response - response object, which stores information about content and HTTP code
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static <T extends SimpleResponse> T get(final URI uri, final T response)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {

        return executeRequest(new HttpGet(uri), response);
    }

    /**
     * Create data witch POST request, by object URL and object payload.
     * 
     * @param url - URL used for connection
     * @param payload - data which will be send
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static SimpleResponse post(final URI url, final Object payload)
            throws JsonGenerationException, JsonMappingException, IOException, KeyManagementException, NoSuchAlgorithmException {

        return post(url, payload, new SimpleResponse());
    }

    /**
     * Create data by POST request, by object URL and object payload.
     * 
     * @param url - URL used for connection
     * @param payload - data which will be send
     * @param response - object used for storing information about HTTP response
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static <T extends SimpleResponse> T post(final URI url, final Object payload, final T response)
            throws JsonGenerationException, JsonMappingException, IOException, KeyManagementException, NoSuchAlgorithmException {

        return post(url, JsonUtil.getJson(payload, Visibility.ANY), response);
    }

    /**
     * Create data by POST request, by object URL and object payload.
     * 
     * @param url - URL used for connection
     * @param json - data which will be send
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static SimpleResponse post(URI url, String json)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {

        return post(url, json, new SimpleResponse());
    }

    /**
     * Create data by POST request, by object URL and object payload.
     * 
     * @param url - URL used for connection
     * @param json - data which will be send
     * @param response - object used for storing information about HTTP response
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static <T extends SimpleResponse> T post(final URI url, final String json, final T response)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setEntity(new StringEntity(json == null ? "" : json, ContentType.APPLICATION_JSON));

        return executeRequest(httpPost, response);
    }

    /**
     * Update data witch PATCH request, by object URL and object payload.
     * 
     * @param url - URL used for connection
     * @param payload - data which will be send
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static SimpleResponse patch(final URI url, final Object payload)
            throws JsonGenerationException, JsonMappingException, IOException, KeyManagementException, NoSuchAlgorithmException {

        return patch(url, payload, new SimpleResponse());
    }

    /**
     * Update data witch PATCH request, by object URL and object payload.
     * 
     * @param url - URL used for connection
     * @param payload - data which will be send
     * @param response - object used for storing information about HTTP response
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static <T extends SimpleResponse> T patch(final URI url, final Object payload, final T response)
            throws JsonGenerationException, JsonMappingException, IOException, KeyManagementException, NoSuchAlgorithmException {

        return patch(url, JsonUtil.getJson(payload, Visibility.ANY), response);
    }

    /**
     * Update data witch PATCH request, by object URL and JSON string.
     * 
     * @param url - URL used for connection
     * @param json - data which will be send
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static SimpleResponse patch(final URI url, final String json)
            throws KeyManagementException, NoSuchAlgorithmException {

        return patch(url, json, new SimpleResponse());
    }

    /**
     * Update data witch PATCH request, by object URL and JSON string and returns data in response object.
     * 
     * @param url - URL used for connection
     * @param json - data which will be send
     * @param SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static <T extends SimpleResponse> T patch(URI url, String json, T response)
            throws KeyManagementException, NoSuchAlgorithmException {

        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpPatch.setEntity(new StringEntity(json == null ? "" : json, ContentType.APPLICATION_JSON));

        return executeRequest(httpPatch, response);
    }

    /**
     * Method, which executes request and returns response in SimpleResponse object.
     * 
     * @param httpUriRequest
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SimpleResponse executeRequest(final HttpUriRequest httpUriRequest)
            throws NoSuchAlgorithmException, KeyManagementException {

        return executeRequest(httpUriRequest, new SimpleResponse());
    }

    /**
     * Method, which executes request and saves it into response object.
     * 
     * @param httpUriRequest
     * @param response
     * @return SimpleResponse - object represents HTTP response with HTTP code and content
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static <T extends SimpleResponse> T executeRequest(final HttpUriRequest httpUriRequest, final T response)
            throws NoSuchAlgorithmException, KeyManagementException {

        CloseableHttpClient closeableHttpClient = getHttpClient();

        try {
            final HttpResponse httpResponse = closeableHttpClient.execute(httpUriRequest);
            response.setResponse(httpResponse);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(closeableHttpClient);
        }

        return response;
    }

    /**
     * Method, which returns instance of CloseableHttpClient.
     * 
     * @return instance of CloseableHttpClient
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static CloseableHttpClient getHttpClient()
            throws NoSuchAlgorithmException, KeyManagementException {

        if (httpClientBuilder != null) {
            return httpClientBuilder.build();
        }
        synchronized (LOG) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();
            } catch (KeyStoreException e) {
                LOG.error("Error while customizing SSL for HTTPS: " + e.getMessage());
            }

            httpClientBuilder = HttpClients.custom().setSSLContext(sslContext);
            return httpClientBuilder.build();
        }
    }

    /**
     * Method, which close HttpClient.
     * 
     * @param httpClient - HTTP Client which will be closed
     */
    private static void close(final CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOG.warn("IOException: " + e.getMessage());
            }
        }
    }
}
