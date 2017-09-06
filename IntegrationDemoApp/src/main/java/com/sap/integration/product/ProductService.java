package com.sap.integration.product;

import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.sap.integration.anywhere.AccessTokenGetter;
import com.sap.integration.anywhere.AnwErrorCode;
import com.sap.integration.anywhere.AnwErrorObject;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.product.model.AnwProductDto;
import com.sap.integration.utils.HttpsCallUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class used for posting APP data to SAP Anywhere.
 */
public class ProductService {

    public static final String PRODUCTS = "Products";

    private final Logger LOG = Logger.getLogger(ProductService.class);

    public ProductService() {
    }

    public AnwProductDto getData() {
        /*
         * This is sample code.
         */
        return new AnwProductDto("sampleCode", "sampleName");
    }

    public void postToAnywhere(AnwProductDto localObj) throws Exception {
        StringBuilder strBuilder = new StringBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(PRODUCTS);

        AnwSimpleResponse response = null;
        AnwProductDto anwObj = getExistAnwObject(localObj);
        if (anwObj == null) {
            String url = strBuilder.toString();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
            httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            httpPost.setHeader("Access-Token", Property.getAccessToken());

            String entity = JsonUtil.getJson(localObj, Visibility.ANY);
            httpPost.setEntity(new StringEntity(entity, ContentType.APPLICATION_JSON));

            response = HttpsCallUtil.executeRequest(httpPost, new AnwSimpleResponse(url));
        } else {
            strBuilder.append("/").append(anwObj.getId().toString());
            String url = strBuilder.toString();
            HttpPatch httpPatch = new HttpPatch(url);

            httpPatch.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
            httpPatch.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            httpPatch.setHeader("Access-Token", Property.getAccessToken());

            String entity = JsonUtil.getJson(anwObj, Visibility.ANY);
            httpPatch.setEntity(new StringEntity(entity, ContentType.APPLICATION_JSON));

            response = HttpsCallUtil.executeRequest(httpPatch, new AnwSimpleResponse(url));
        }

        if (response.getStatusCode() == AnwErrorCode.HTTP_STATUS_TOO_MANY_REQEUEST) {
            /*
             * Http status: 429, too many request over the rate limiter.
             * You can send request again wait for a while, or wait for next job schedule
             */
        } else if (response.getStatusCode() == HttpStatus.SC_UNAUTHORIZED
                && StringUtils.isNotEmpty(response.getContent())) {
            AnwErrorObject error = JsonUtil.getObject(response.getContent(),
                    AnwErrorObject.class);
            if (error.getErrorCode().equals(AnwErrorCode.OAUTH_EXPIRED)) {
                /*
                 * Http status:401, and error code is APP00001.
                 * Get access token again.
                 * If necessary, you can send request again, or wait for next job schedule.
                 */
                AccessTokenGetter.runAccessToken();
            }
        } else if (response.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
            /*
             * handle error which status code > 400
             */
        } else if(response.getStatusCode() == HttpStatus.SC_OK || response.getStatusCode() == HttpStatus.SC_CREATED){
            LOG.info("successful sync data to sap anywhere");
        }
    }

    private AnwProductDto getExistAnwObject(AnwProductDto dto) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(PRODUCTS)
                .parameter("filter", "name  eq '" + dto.getName() + "'");

        String url = urlBuilder.get();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpGet.setHeader("Access-Token", Property.getAccessToken());

        AnwSimpleResponse response = HttpsCallUtil.executeRequest(httpGet, new AnwSimpleResponse(url));
        List<AnwProductDto> result = JsonUtil.getObjects(response.getContent(), AnwProductDto.class);
        /*
         * Product's name can be the same.
         * Update them all or the 1st one.
         */
        if (result.size() == 0) {
            return null;
        }
        return result.get(0);

    }

}
