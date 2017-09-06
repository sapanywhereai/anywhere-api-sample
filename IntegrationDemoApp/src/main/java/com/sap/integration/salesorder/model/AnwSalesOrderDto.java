package com.sap.integration.salesorder.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import com.sap.integration.salesorder.model.enumeration.AnwSalesOrderPricingMethodDto;

/**
 * Sales Order object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwSalesOrderDto {

    private Long id;
    private String docNumber;
    private AnwMarketingDocumentCustomerDto customer;
    private AnwSalesOrderPricingMethodDto pricingMethod;
    private List<AnwSalesOrderLineDto> productLines;
    private AnwAmountDto grossTotal;
    private DateTime orderTime;
    private DateTime updateTime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaseId() {
        return this.id;
    }

    public void setBaseId(Long baseId) {
        this.id = baseId;
    }

    public String getDocNumber() {
        return this.docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public AnwMarketingDocumentCustomerDto getCustomer() {
        return this.customer;
    }

    public void setCustomer(AnwMarketingDocumentCustomerDto customer) {
        this.customer = customer;
    }

    public AnwSalesOrderPricingMethodDto getPricingMethod() {
        return this.pricingMethod;
    }

    public void setPricingMethod(AnwSalesOrderPricingMethodDto pricingMethod) {
        this.pricingMethod = pricingMethod;
    }

    public List<AnwSalesOrderLineDto> getProductLines() {
        return this.productLines;
    }

    public void setProductLines(List<AnwSalesOrderLineDto> productLines) {
        this.productLines = productLines;
    }

    public AnwAmountDto getGrossTotal() {
        return this.grossTotal;
    }

    public void setGrossTotal(AnwAmountDto grossTotal) {
        this.grossTotal = grossTotal;
    }

    public DateTime getOrderTime() {
        return this.orderTime;
    }

    public void setOrderTime(DateTime orderTime) {
        this.orderTime = orderTime;
    }

    public DateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", docNumber=");
        sb.append(this.docNumber);
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", pricingMethod=");
        sb.append(this.pricingMethod);
        sb.append(", productLines=");
        sb.append(this.productLines);
        sb.append(", grossTotal=");
        sb.append(this.grossTotal);
        sb.append(", orderTime=");
        sb.append(this.orderTime);
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append("]");
        return sb.toString();
    }
}
