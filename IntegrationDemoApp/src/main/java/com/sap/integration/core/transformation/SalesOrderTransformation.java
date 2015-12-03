package com.sap.integration.core.transformation;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwAmountDto;
import com.sap.integration.anywhere.dto.AnwChannelDto;
import com.sap.integration.anywhere.dto.AnwCurrencyDto;
import com.sap.integration.anywhere.dto.AnwCustomerDto;
import com.sap.integration.anywhere.dto.AnwMarketingDocumentCustomerDto;
import com.sap.integration.anywhere.dto.AnwProductDto;
import com.sap.integration.anywhere.dto.AnwSalesOrderDto;
import com.sap.integration.anywhere.dto.AnwSalesOrderLineDto;
import com.sap.integration.anywhere.dto.AnwSkuDto;
import com.sap.integration.anywhere.dto.AnwUomDto;
import com.sap.integration.anywhere.dto.enumeration.AnwSalesOrderLineCalculationBaseDto;
import com.sap.integration.anywhere.dto.enumeration.AnwSalesOrderPricingMethodDto;
import com.sap.integration.core.service.CustomerService;
import com.sap.integration.core.service.ProductService;
import com.sap.integration.erp.dto.ErpChannelDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.ErpSalesOrderDto;
import com.sap.integration.erp.dto.ErpSalesOrderLineDto;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;

/**
 * Class for transform SAP Anywhere's Sales Order to custom solution object
 */
public class SalesOrderTransformation {

    public static final AnwCurrencyDto DEFAULT_CURRENCY = new AnwCurrencyDto(new Long(1), "", new Double(1));
    public static final AnwUomDto DEFAULT_SALES_UOM = new AnwUomDto(new Long(1), "\u65e0");

    /**
     * Method for transformation SAP Anywhere's Sales Order DTO object into ERP's Sales Order DTO object
     * 
     * @param anwSo SAP Anywhere's Sales Order DTO object
     * @return ERP's Sales Order object
     */
    public static ErpSalesOrderDto run(AnwSalesOrderDto anwSo) {
        ErpSalesOrderDto erpSo = new ErpSalesOrderDto();
        
        erpSo.setAnwId(anwSo.getId());
        erpSo.setDocNumber(anwSo.getDocNumber());
        erpSo.setOrderTime(anwSo.getOrderTime());
        erpSo.setLastUpdateTime(anwSo.getUpdateTime());
        erpSo.setPricingMethod((anwSo.getPricingMethod() != null) ? anwSo.getPricingMethod().toString() : null);
        erpSo.setDocTotal((anwSo.getGrossTotal() != null) ? ((anwSo.getGrossTotal().getAmount() != null) ? anwSo.getGrossTotal()
                .getAmount().doubleValue() : null) : null);
        erpSo.setChannel(new ErpChannelDto(1));

        ErpCustomerDto customer = new ErpCustomerDto();
        customer.setName((anwSo.getCustomer() != null) ? anwSo.getCustomer().getName() : "");
        customer.setCode((anwSo.getCustomer() != null) ? anwSo.getCustomer().getCode() : "");
        customer.setId((anwSo.getCustomer() != null) ? anwSo.getCustomer().getId() : null);
        erpSo.setCustomer(customer);

        if (anwSo.getProductLines() != null) {
            ArrayList<ErpSalesOrderLineDto> erpProductLines = new ArrayList<ErpSalesOrderLineDto>();
            for (AnwSalesOrderLineDto anwSoLine : anwSo.getProductLines()) {
                ErpSalesOrderLineDto erpProductLine = new ErpSalesOrderLineDto();

                erpProductLine.setId(anwSoLine.getId());
                erpProductLine.setOrderId(anwSoLine.getOrderId());
                erpProductLine.setQuantity(anwSoLine.getQuantity());
                erpProductLine.setInventoryUomQuantity(anwSoLine.getInventoryUomQuantity());
                erpProductLine.setUnitPrice(anwSoLine.getGrossUnitPrice());
                erpProductLine
                        .setLineTotal((anwSoLine.getGrossAmount() != null) ? ((anwSoLine.getGrossAmount().getAmount() != null) ? anwSoLine
                                .getGrossAmount().getAmount().doubleValue()
                                : null)
                                : null);
                erpProductLine.setCalculationBase((anwSoLine.getCalculationBase() != null) ? anwSoLine.getCalculationBase()
                        .toString() : null);

                ErpProductDto erpProduct = new ErpProductDto();
                erpProduct.setId((anwSoLine.getSku() != null) ? new Long(anwSoLine.getSku().getId().longValue()) : null);
                erpProduct.setItemCode((anwSoLine.getSku() != null) ? anwSoLine.getSku().getCode() : "");
                erpProduct.setItemName((anwSoLine.getSku() != null) ? anwSoLine.getSku().getName() : "");
                erpProductLine.setSku(erpProduct);

                erpProductLines.add(erpProductLine);
            }

            erpSo.setProductLines(erpProductLines);

        } else {
            erpSo.setProductLines(null);
        }

        return erpSo;
    }

    /**
     * Method for transformation ERP's Sales Order DTO object into SAP Anywhere's Sales Order DTO object
     * 
     * @param erpSo ERP's Sales Order object
     * @return SAP Anywhere's Sales Order DTO object
     */
    public static AnwSalesOrderDto run(ErpSalesOrderDto erpSo) {
        AnwSalesOrderDto anwSo = new AnwSalesOrderDto();

        anwSo.setDocNumber(erpSo.getDocNumber());
        anwSo.setOrderTime(erpSo.getOrderTime());
        anwSo.setUpdateTime(erpSo.getLastUpdateTime());
        anwSo.setPricingMethod(AnwSalesOrderPricingMethodDto.valueOf(erpSo.getPricingMethod().toUpperCase()));
        anwSo.setCurrency(DEFAULT_CURRENCY);
        anwSo.setChannel(new AnwChannelDto(erpSo.getChannel().getId()));

        AnwMarketingDocumentCustomerDto customer = new AnwMarketingDocumentCustomerDto();
        customer.setName((erpSo.getCustomer() != null) ? erpSo.getCustomer().getName() : "");
        customer.setCode((erpSo.getCustomer() != null) ? erpSo.getCustomer().getCode() : "");

        // To get Anw Id of customer
        try {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(CustomerService.CUSTOMERS)
                    .parameter("limit", 100)
                    .parameter("offset", 0)
                    .parameter("select", "id")
                    .parameter("filter",
                            "customerCode eq '" + ((erpSo.getCustomer() != null) ? erpSo.getCustomer().getCode() : "") + "'")
                    .parameter("access_token", Property.getAccessToken());

            AnwSimpleResponse datasetJson = AnwServiceCall.get(urlBuilder);
            if (datasetJson.hasContent()) {
                List<AnwCustomerDto> anwCustomerDtos = (new ObjectMapper()).readValue(datasetJson.getContent(),
                        new TypeReference<List<AnwCustomerDto>>() {
                        });
                customer.setId(((AnwCustomerDto) anwCustomerDtos.get(0)).getId());
            }
        } catch (Exception e) {
            customer.setId(null);
        }
        anwSo.setCustomer(customer);

        if (erpSo.getProductLines() != null) {
            ArrayList<AnwSalesOrderLineDto> anwProductLines = new ArrayList<AnwSalesOrderLineDto>();
            for (ErpSalesOrderLineDto erpSoLine : erpSo.getProductLines()) {
                AnwSalesOrderLineDto anwProductLine = new AnwSalesOrderLineDto();

                anwProductLine.setOrderId(erpSoLine.getOrderId());
                anwProductLine.setQuantity(erpSoLine.getQuantity());
                anwProductLine.setGrossUnitPrice(erpSoLine.getUnitPrice());
                anwProductLine.setGrossAmount(new AnwAmountDto(erpSoLine.getLineTotal(), erpSoLine.getLineTotal()));
                anwProductLine.setInventoryUomQuantity(erpSoLine.getInventoryUomQuantity());
                anwProductLine.setCalculationBase(AnwSalesOrderLineCalculationBaseDto.valueOf(erpSoLine.getCalculationBase().toUpperCase()));

                AnwSkuDto anwSku = new AnwSkuDto();
                anwSku.setCode((erpSoLine.getSku() != null) ? erpSoLine.getSku().getItemCode() : "");
                anwSku.setName((erpSoLine.getSku() != null) ? erpSoLine.getSku().getItemName() : "");
                try {

                    UrlBuilder urlBuilder = new UrlBuilder()
                            .append(AnwUrlUtil.getOpenApiBaseUrl())
                            .append(ProductService.PRODUCTS)
                            .parameter("limit", 100)
                            .parameter("offset", 0)
                            .parameter("select", "id")
                            .parameter("filter", "code eq '" + anwSku.getCode() + "' and name eq '" + anwSku.getName() + "'")
                            .parameter("access_token", Property.getAccessToken());

                    AnwSimpleResponse datasetJson = AnwServiceCall.get(urlBuilder);
                    if (datasetJson.hasContent()) {
                        List<AnwProductDto> anwProductDtos = (new ObjectMapper()).readValue(datasetJson.getContent(),
                                new TypeReference<List<AnwProductDto>>() {
                                });
                        anwSku.setId(((AnwProductDto) anwProductDtos.get(0)).getId());
                    }
                } catch (Exception e) {
                    anwSku.setId(null);
                }
                anwProductLine.setSku(anwSku);
                anwProductLine.setSalesUom(DEFAULT_SALES_UOM);

                anwProductLines.add(anwProductLine);
            }

            anwSo.setProductLines(anwProductLines);

        } else {
            anwSo.setProductLines(null);
        }

        return anwSo;
    }
}