package com.sap.integration.anywhere.oauth;

import java.net.URI;

import org.apache.log4j.Logger;

import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.utils.HttpsCall;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class stores methods used for retrieving, processing and saving access token from SAP Anywhere into configuration file.
 */
public class AccessTokenLoader {

    private static final Logger LOG = Logger.getLogger(AccessTokenLoader.class);

    /**
     * Method, which retrieves access token from SAP Anywhere, save it and return it.
     * 
     * @return value of access token
     * @throws Exception possible exception during retrieving, processing and saving access token
     */
    public static String load() throws Exception {
        URI uri = new UrlBuilder(AnwUrlUtil.getAccessTokenUrl()).getURI();
        String content = HttpsCall.get(uri).getContent();
        LOG.info("Access Token - retrieved response: " + content);
        AccessTokenDto accessToken = JsonUtil.getObject(content, AccessTokenDto.class);
        Property.saveAccessToken(accessToken.getAccess_token());
        LOG.info("Access Token - loaded access token = " + accessToken.getAccess_token());
        return accessToken.getAccess_token();
    }
}
