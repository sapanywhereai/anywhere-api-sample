package com.sap.integration.salesorder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.sap.integration.anywhere.AccessTokenGetter;
import com.sap.integration.anywhere.AnwErrorCode;
import com.sap.integration.anywhere.AnwErrorObject;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.IntegrationState;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.salesorder.model.AnwSalesOrderDto;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.HttpsCallUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class used for getting sales order data from SAP Anywhere.
 */
public class SalesOrderService {

    public static final String SALES_ORDERS = "SalesOrders";

    private static final Logger LOG = Logger.getLogger(SalesOrderService.class);

    private static IntegrationState integrationState;

    /**
     * Get list of SAP Anywhere sales order data transfer objects ordered by updateTime.
     * 
     * @return List<AnwSalesOrderDto>
     * @throws Exception
     */
    public static List<AnwSalesOrderDto> getAnwSalesOrders() throws Exception {
        // read last update time of products
        integrationState = IntegrationState.getIntegrationStateFor(AnwSalesOrderDto.class);

        // get SAP Anywhere sales order
        List<AnwSalesOrderDto> anwSalesOrders = new ArrayList<AnwSalesOrderDto>();
        List<AnwSalesOrderDto> anwSalesOrdersPage = new ArrayList<AnwSalesOrderDto>();
        int offset = 0;
        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(SALES_ORDERS)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter("orderby", "updateTime")
                    .parameter(
                            "filter",
                            "updateTime gt '" + DateUtil.convertDateTimeToString(integrationState.getLastSyncTime())
                                    + "'")
                    .parameter("expand", "productLines");

            String url = urlBuilder.get();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
            httpGet.setHeader("Access-Token", Property.getAccessToken());

            AnwSimpleResponse response = HttpsCallUtil.executeRequest(httpGet, new AnwSimpleResponse(url));

            if (response.getStatusCode() == AnwErrorCode.HTTP_STATUS_TOO_MANY_REQEUEST) {
                /*
                 * Http status: 429, too many request over the rate limiter.
                 * You can send request again wait for a while, or wait for next job schedule
                 * This return statement is for sample, you can do what you want to do.
                 */
                return anwSalesOrders;
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
                    continue;
                }
            } else if (response.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                /*
                 * handle error > 400
                 */
            }
            if (response.getStatusCode() == HttpStatus.SC_OK) {
                anwSalesOrdersPage = JsonUtil.getObjects(response.getContent(), AnwSalesOrderDto.class);
                if (CollectionUtils.isEmpty(anwSalesOrdersPage)) {
                    return anwSalesOrders;
                }
                anwSalesOrders.addAll(anwSalesOrdersPage);
                offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
            }
        } while (anwSalesOrdersPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);
        LOG.info("Get salesorder count:" + anwSalesOrders.size());
        return anwSalesOrders;
    }
}
