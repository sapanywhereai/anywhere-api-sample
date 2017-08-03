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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;

public class HttpsCallUtil {
	private static final Logger LOG = Logger.getLogger(HttpsCallUtil.class);

	private static HttpClientBuilder httpClientBuilder = null;
	
	private static int HTTP_STATUS_TOO_MANY_REQEUEST = 429;

	/**
	 * HTTP GET request, retrieving data by object URL.
	 * 
	 * @param uri
	 *            - URL used for connection
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public static SimpleResponse get(final URI uri)
			throws Exception {
		return get(uri, new SimpleResponse());
	}

	/**
	 * HTTP GET request, retrieving data by object URL.
	 * 
	 * @param uri
	 *            - URL used for connection
	 * @param response
	 *            - response object, which stores information about content and
	 *            HTTP code
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public static <T extends SimpleResponse> T get(final URI uri, final T response)
			throws Exception {
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
		return executeRequest(httpGet, response);
	}
	
	/**
	 * Method, which executes request and saves it into response object.
	 * 
	 * @param httpUriRequest
	 * @param response
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws InterruptedException 
	 */
	public static <T extends SimpleResponse> T executeRequest(final HttpUriRequest httpUriRequest, final T response)
			throws NoSuchAlgorithmException, KeyManagementException, InterruptedException {

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
		// 429 over rate limiter
		if (response.getStatusCode() == HTTP_STATUS_TOO_MANY_REQEUEST) {
			Thread.sleep(1000l);
			return executeRequest(httpUriRequest, response);
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
	private static CloseableHttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException {

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
	 * @param httpClient
	 *            - HTTP Client which will be closed
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
