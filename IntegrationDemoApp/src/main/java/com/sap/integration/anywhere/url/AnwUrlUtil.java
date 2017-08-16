package com.sap.integration.anywhere.url;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sap.integration.anywhere.oauth.SSLContextUtils;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Utility class used for constructing URL for HTTP requests.
 */
public class AnwUrlUtil {

	private static final Logger LOG = Logger.getLogger(AnwUrlUtil.class);

	/**
	 * OPENAPI_PATH is path, on which is exposed OPEN API. It can be changed, so
	 * probably best way is to store it in config.properties file.
	 */
	public static final String OPENAPI_PATH = "v1";
	public static final int OPENAPI_QEURY_OPTION_LIMIT = 100;

	/**
	 * Method construct and return base Open API URL. Sample of returned URL:
	 * <br>
	 * <code>https://anywhereserver:port/api-gateway/v1/</code> <br>
	 * <br>
	 * <b>Warning:</b> Method logs sensitive information for testing and demo
	 * purposes. In real application logger should be deleted, not just disabled
	 * by configuration! <br>
	 * 
	 * @return base URL to the Open API
	 * @throws Exception
	 *             possible exception during loading of configuration data
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
	 * <b>Warning:</b> Method logs sensitive information for testing and demo
	 * purposes. In real application logger should be deleted, not just disabled
	 * by configuration! <br>
	 * 
	 * @return base URL to the OAuth
	 * @throws Exception
	 *             possible exception during loading of configuration data
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
	 * Method returns URL with URL parameters, which is used for getting Access
	 * Token. Sample of returned URL as string: <br>
	 * <br>
	 * <code>https://idpserver:idpport/sld/oauth2/token?client_id=client_id&client_secret=client_secret&refresh_token=refresh_token&grant_type=refresh_token</code>
	 * <br>
	 * <br>
	 * <b>Warning:</b> Method logs sensitive information for testing and demo
	 * purposes. In real application logger should be deleted, not just disabled
	 * by configuration! <br>
	 * 
	 * @return string URL used for getting Access Token
	 * @throws Exception
	 *             possible exception during loading of configuration data
	 */
	public static String getAccessTokenUrlWithUrlParams() throws Exception {
		UrlBuilder url = new UrlBuilder(getOauthBaseUrl());
		url.parameter("client_id", Property.getAppId());
		url.parameter("client_secret", Property.getAppSecret());
		url.parameter("refresh_token", Property.getRefreshToken());
		url.parameter("grant_type", "refresh_token");
		String baseUrl = url.get();
		LOG.debug("Access Token - created URL = " + baseUrl);
		return baseUrl;
	}

	public static String getAccessTokenViaPost() throws Exception {
		HttpsURLConnection https = null;
		try {
			SSLSocketFactory sslFactory = getSSLSocketFactory();
			if (sslFactory == null) {
				// should be set byPass before create connection.
				SSLContextUtils.byPassCert();
			}
			String params = "client_id=" + URLEncoder.encode(Property.getAppId(), "UTF-8");
			params += "&";
			params += "client_secret=" + URLEncoder.encode(Property.getAppSecret(), "UTF-8");
			params += "&";
			params += "grant_type=refresh_token";
			params += "&";
			params += "refresh_token=" + URLEncoder.encode(Property.getRefreshToken(), "UTF-8");

			URL urlPost = new URL(getOauthBaseUrl());
			https = (HttpsURLConnection) urlPost.openConnection();

			if (sslFactory != null) {
				((HttpsURLConnection) https).setSSLSocketFactory(sslFactory);
			}
			https.setInstanceFollowRedirects(false);
			https.setRequestMethod("POST");
			https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			https.setDoOutput(true);
			https.setDoInput(true);
			DataOutputStream output = new DataOutputStream(https.getOutputStream());
			output.writeBytes(params);
			output.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(https.getInputStream()));
			StringBuffer inputLine = new StringBuffer();
			String tmp;
			while ((tmp = br.readLine()) != null) {
				inputLine.append(tmp);
				System.out.println(tmp);
			}
			br.close();

			return inputLine.toString();
		} catch (Exception e) {
			if (https != null) {
				https.disconnect();
			}
			throw e;
		} finally {
			if (https != null) {
				https.disconnect();
			}
		}
	}

	private static SSLSocketFactory getSSLSocketFactory() throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		String certificate = Property.getAnwCertificate();
		SSLSocketFactory sslFactory = null;
		if (StringUtils.isNotEmpty(certificate) && !"notDefined".equals(certificate)) {
			keyStore.load(new FileInputStream(Property.getAnwCertificate()), "changeit".toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tmf.getTrustManagers(), null);
			sslFactory = ctx.getSocketFactory();
		}
		return sslFactory;
	}
}
