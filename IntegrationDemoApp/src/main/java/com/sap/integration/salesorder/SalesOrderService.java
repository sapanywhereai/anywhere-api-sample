package com.sap.integration.salesorder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.IntegrationState;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.salesorder.model.AnwSalesOrderDto;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;

/**
 * Class used for getting and posting of sales order data to SAP Anywhere / ERP.
 */
public class SalesOrderService {

    public static final String SALES_ORDERS = "SalesOrders";

    private static final Logger LOG = Logger.getLogger(SalesOrderService.class);

    private static IntegrationState integrationState;

    /**
     * Get SAP Anywhere sales order data transfer object
     * 
     * @param id Sales order's id in SAP Anywhere
     * @return AnwSalesOrderDto
     * @throws Exception
     */
    public static AnwSalesOrderDto getAnwSalesOrder(String id) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_ORDERS)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "id eq " + id);

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder, null);
        List<AnwSalesOrderDto> anwSalesOrder;
        try {
            anwSalesOrder = JsonUtil.getObjects(response.getContent(), AnwSalesOrderDto.class);
            if (anwSalesOrder.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwSalesOrder.get(0);
    }
    
    /**
     * Get SAP Anywhere sales order data transfer object with specific value of dicument number
     * 
     * @param id Sales order's id in SAP Anywhere
     * @return AnwSalesOrderDto
     * @throws Exception
     */
    public static AnwSalesOrderDto getAnwSalesOrderWithDocNumber(String docNumber) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(SALES_ORDERS)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", "docNumber eq '" + docNumber + "'");

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder, null);
        List<AnwSalesOrderDto> anwSalesOrder;
        try {
            anwSalesOrder = JsonUtil.getObjects(response.getContent(), AnwSalesOrderDto.class);
            if (anwSalesOrder.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return anwSalesOrder.get(0);
    }

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
        List<AnwSalesOrderDto> anwSalesOrdersPage;
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

            anwSalesOrdersPage = JsonUtil.getObjects(AnwServiceCall.get(urlBuilder, null).getContent(), AnwSalesOrderDto.class);
            if(CollectionUtils.isEmpty(anwSalesOrdersPage)){
                return anwSalesOrders;
            }
            anwSalesOrders.addAll(anwSalesOrdersPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwSalesOrdersPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwSalesOrders;
    }
}
