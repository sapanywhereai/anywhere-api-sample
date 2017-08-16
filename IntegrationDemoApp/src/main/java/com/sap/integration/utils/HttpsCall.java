package com.sap.integration.utils;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.stereotype.Service;

import com.sap.integration.anywhere.SimpleResponse;
import com.sap.integration.utils.configuration.Property;

/**
 * Utility class used for HTTPS GET, POST and PATCH requests. For demo purpose
 * all trusting managers are used.
 */
@Service
public class HttpsCall {

	private static final Logger LOG = Logger.getLogger(HttpsCall.class);
	
	/**
	 * HTTP GET request, retrieving data by object URL.
	 * 
	 * @param uri
	 *            - URL used for connection headers - request header, such as
	 *            "Access-Token"
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public SimpleResponse get(final URI uri, Map<String, String> headers)
			throws Exception {

		return get(uri, new SimpleResponse(), headers);
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
	public static <T extends SimpleResponse> T get(final URI uri, final T response, final Map<String, String> headers)
			throws Exception {
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
		httpGet.setHeader("Access-Token", Property.getAccessToken());
		if (headers != null) {
			for (Entry<String, String> header : headers.entrySet()) {
				httpGet.addHeader(header.getKey(), header.getValue());
			}
		}
		return HttpsCallUtil.executeRequest(httpGet, response);
	}

	/**
	 * Create data witch POST request, by object URL and object payload.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param payload
	 *            - data which will be send
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public SimpleResponse post(final URI url, final Object payload, final Map<String, String> headers)
			throws Exception {

		return post(url, payload, new SimpleResponse(), headers);
	}

	/**
	 * Create data by POST request, by object URL and object payload.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param payload
	 *            - data which will be send
	 * @param response
	 *            - object used for storing information about HTTP response
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public static <T extends SimpleResponse> T post(final URI url, final Object payload, final T response,
			final Map<String, String> headers) throws Exception {

		return post(url, JsonUtil.getJson(payload, Visibility.ANY), response, headers);
	}

	/**
	 * Create data by POST request, by object URL and object payload.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param json
	 *            - data which will be send
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public SimpleResponse post(URI url, String json, final Map<String, String> headers)
			throws Exception {

		return post(url, json, new SimpleResponse(), headers);
	}

	/**
	 * Create data by POST request, by object URL and object payload.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param json
	 *            - data which will be send
	 * @param response
	 *            - object used for storing information about HTTP response
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public static <T extends SimpleResponse> T post(final URI url, final String json, final T response,
			final Map<String, String> headers) throws Exception {

		HttpPost httpPost = new HttpPost(url);
		/*
		 * And all webhook requests must include the following in their HTTP headers:
     			Accept: application/json
     			Content-Type: application/json
		 */
		httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
		httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
		httpPost.setHeader("Access-Token", Property.getAccessToken());
		if (headers != null) {
			for (Entry<String, String> header : headers.entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
		}
		httpPost.setEntity(new StringEntity(json == null ? "" : json, ContentType.APPLICATION_JSON));

		return HttpsCallUtil.executeRequest(httpPost, response);
	}

	/**
	 * Update data witch PATCH request, by object URL and object payload.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param payload
	 *            - data which will be send
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public SimpleResponse patch(final URI url, final Object payload, final Map<String, String> headers)
			throws Exception {

		return patch(url, payload, new SimpleResponse(), headers);
	}

	/**
	 * Update data witch PATCH request, by object URL and object payload.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param payload
	 *            - data which will be send
	 * @param response
	 *            - object used for storing information about HTTP response
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public static <T extends SimpleResponse> T patch(final URI url, final Object payload, final T response,
			Map<String, String> headers) throws Exception {

		return patch(url, JsonUtil.getJson(payload, Visibility.ANY), response, headers);
	}

	/**
	 * Update data witch PATCH request, by object URL and JSON string.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param json
	 *            - data which will be send
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws Exception 
	 */
	public SimpleResponse patch(final URI url, final String json, Map<String, String> headers)
			throws Exception {

		return patch(url, json, new SimpleResponse(), headers);
	}

	/**
	 * Update data witch PATCH request, by object URL and JSON string and
	 * returns data in response object.
	 * 
	 * @param url
	 *            - URL used for connection
	 * @param json
	 *            - data which will be send
	 * @param SimpleResponse
	 *            - object represents HTTP response with HTTP code and content
	 * @throws Exception 
	 */
	public static <T extends SimpleResponse> T patch(URI url, String json, T response, Map<String, String> headers)
			throws Exception {

		HttpPatch httpPatch = new HttpPatch(url);
		// system predefined header
		httpPatch.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
		httpPatch.setHeader("Access-Token", Property.getAccessToken());
		
		// load customized header
		if (headers != null) {
			for (Entry<String, String> header : headers.entrySet()) {
				httpPatch.addHeader(header.getKey(), header.getValue());
			}
		}
		httpPatch.setEntity(new StringEntity(json == null ? "" : json, ContentType.APPLICATION_JSON));

		return HttpsCallUtil.executeRequest(httpPatch, response);
	}

	/**
	 * Method, which executes request and returns response in SimpleResponse
	 * object.
	 * 
	 * @param httpUriRequest
	 * @return SimpleResponse - object represents HTTP response with HTTP code
	 *         and content
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws InterruptedException 
	 */
	public SimpleResponse executeRequest(final HttpUriRequest httpUriRequest)
			throws NoSuchAlgorithmException, KeyManagementException, InterruptedException {

		return HttpsCallUtil.executeRequest(httpUriRequest, new SimpleResponse());
	}

	public static <T extends SimpleResponse> T delete(final URI url, final T response,
			final Map<String, String> headers) throws Exception {

		HttpDelete httpDelete = new HttpDelete(url);
		/*
		 * And all webhook requests must include the following in their HTTP headers:
     			Accept: application/json
     			Content-Type: application/json
		 */
		httpDelete.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
		httpDelete.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
		httpDelete.setHeader("Access-Token", Property.getAccessToken());
		if (headers != null) {
			for (Entry<String, String> header : headers.entrySet()) {
				httpDelete.addHeader(header.getKey(), header.getValue());
			}
		}

		return HttpsCallUtil.executeRequest(httpDelete, response);
	}
}
