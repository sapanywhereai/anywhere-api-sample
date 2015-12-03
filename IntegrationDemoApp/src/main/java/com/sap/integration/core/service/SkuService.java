package com.sap.integration.core.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwSkuAllWarehouseInfoDto;
import com.sap.integration.anywhere.dto.AnwSkuDto;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class, which provides methods for create/read/update/delete data about Warehouses from/to SAP Anywhere. <br>
 */
public class SkuService {

    private static final Logger LOG = Logger.getLogger(SkuService.class);

    /**
     * Method, which find SKU in SAP Anywhere by using SKU code. <br>
     * 
     * @param skuCode - SKU code used as filter <br>
     * @return AnwSkuDto - SKU from SAP Anywhere <br>
     * @throws Exception possible exception during the processing <br>
     */
    public static final AnwSkuDto findSku(String skuCode) throws Exception {

        if (skuCode != null) {
            AnwSimpleResponse result = AnwServiceCall.get(getSkuLinkFind(skuCode));
            LOG.info("SKU - result of finding sku with code " + skuCode + ": " + result.getContent());

            if (result != null) {
                List<AnwSkuDto> skus = (new ObjectMapper()).readValue(result.getContent(), new TypeReference<List<AnwSkuDto>>() {
                });

                if (skus != null && skus.size() > 0) {
                    return skus.get(0);
                } else {
                    LOG.info("SKU - result of parsing information about sku is null object of AnwSkuDto. Input value: "
                            + result);
                    return null;
                }
            } else {
                LOG.info("SKU - result of finding is empty/null warehouse. Input value: " + skuCode);
                return null;
            }
        } else {
            LOG.info("SKU - empty/null parameter on input of method findSku()");
            return null;
        }
    }

    /**
     * Method returns URL, which is used for selecting of SKUs. <br>
     * Sample of URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/SKUs?limit=100&offset=1&access_token=access_token_value</code>
     * 
     * @param offset
     * @return URL for selection of SKU
     * @throws Exception possible exception thrown by class, which loads configuration
     */
    public static final String getSkuLinkSelect(Number offset) throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("SKUs")
                .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                .parameter("offset", offset)
                .parameter("access_token", Property.getAccessToken())
                .get();

        LOG.info("SKU - select link: " + link);
        return link;
    }

    /**
     * Method returns URL, which is used for finding an existing SKU in SAP Anywhere and getting information from it. <br>
     * Sample of URL: <br>
     * <code>https://anywhereserver:port/api-gateway/v1/SKUs?$select=id,code,name&filter=code eq 'skuCode' &limit=1&offset=0&access_token=access_token_value</code>
     * 
     * @return URL for finding SKUs
     * @throws Exception possible exception thrown by class, which loads configuration
     */
    public static final String getSkuLinkFind(String skuCode) throws Exception {
        String link = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("SKUs")
                .parameter("access_token", Property.getAccessToken())
                .parameter("$select", "id,code,name")
                .parameter("filter", "code eq '" + skuCode + "'")
                .parameter("limit", 1)
                .parameter("offset", 0)
                .get();

        LOG.info("SKU - find by code link: " + link);
        return link;
    }

    /**
     * Get warehouse info data by sku id<br>
     * 
     * @param skuId - SKU id used as filter <br>
     * @return List<AnwSkuAllWarehouseInfoDto><br>
     * @throws Exception
     */
    public static AnwSkuAllWarehouseInfoDto getWarehouseInfo(Number skuId) throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append("SKUs/" + skuId + "/getWarehouseInfos")
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.post(urlBuilder, "");
        AnwSkuAllWarehouseInfoDto anwAllSkuWarehouseInfos;
        try {
            anwAllSkuWarehouseInfos = JsonUtil.getObject(response.getContent(), AnwSkuAllWarehouseInfoDto.class);
            return anwAllSkuWarehouseInfos;
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            LOG.warn(urlBuilder.getURL().toString(), e);
            return null;
        }
    }
}
