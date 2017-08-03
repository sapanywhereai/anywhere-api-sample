package com.sap.integration.anywhere.oauth;

import java.net.URI;

//import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.utils.HttpsCallUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class stores methods used for retrieving, processing and saving access token from SAP Anywhere into configuration file.
 */
@Component
public class AccessTokenLoader {

    private static final Logger LOG = Logger.getLogger(AccessTokenLoader.class);

    /**
     * Default Method, which retrieves access token from SAP Anywhere, save it and return it.
     * 
     * @param offset Type of SAP Anywhere call - GET, or POST
     * @return value of access token
     * @throws Exception possible exception during retrieving, processing and saving access token
     */
    public static String load() throws Exception {
    	return load("POST");
    	//return load("GET");    -- OLD FASHION STYLE...
    }
    
    /**
     * Method, which retrieves access token from SAP Anywhere, save it and return it.
     * 
     * @param offset Type of SAP Anywhere call - GET, or POST
     * @return value of access token
     * @throws Exception possible exception during retrieving, processing and saving access token
     */
    public static String load(String type) throws Exception {
    	String content = "";
    	if ("GET".equalsIgnoreCase(type)) {
	        URI uri = new UrlBuilder(AnwUrlUtil.getAccessTokenUrlWithUrlParams()).getURI();
	        content = HttpsCallUtil.get(uri).getContent();
    	}
    	else if ("POST".equalsIgnoreCase(type)) {
    		content = AnwUrlUtil.getAccessTokenViaPost(); 
    	}
        LOG.info("Access Token - retrieved response: " + content);
        AccessTokenDto accessToken = JsonUtil.getObject(content, AccessTokenDto.class);
        Property.saveAccessToken(accessToken.getAccess_token());
        LOG.info("Access Token - loaded access token = " + accessToken.getAccess_token());
        return accessToken.getAccess_token();
    }
}
