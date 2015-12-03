package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Sales Delivery data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpSalesDeliveryLineDto {

    private Long id;
    private ErpSalesDeliveryDto salesDelivery;
    private ErpSalesOrderDto baseDocument;
    private Integer requiredQuantity;
    private Integer deliveryQuantity;
    private ErpProductDto sku;

    public ErpProductDto getSku() {
        return this.sku;
    }

    public void setSku(ErpProductDto sku) {
        this.sku = sku;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpSalesDeliveryDto getSalesDelivery() {
        return this.salesDelivery;
    }

    public void setSalesDelivery(ErpSalesDeliveryDto salesDelivery) {
        this.salesDelivery = salesDelivery;
    }

    public ErpSalesOrderDto getBaseDocument() {
        return this.baseDocument;
    }

    public void setBaseDocument(ErpSalesOrderDto baseDocument) {
        this.baseDocument = baseDocument;
    }

    public Integer getRequiredQuantity() {
        return this.requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public Integer getDeliveryQuantity() {
        return this.deliveryQuantity;
    }

    public void setDeliveryQuantity(Integer deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", salesDelivery=");
        sb.append(this.salesDelivery);
        sb.append(", baseDocument=");
        sb.append(this.baseDocument);
        sb.append(", requiredQuantity=");
        sb.append(this.requiredQuantity);
        sb.append(", deliveryQuantity=");
        sb.append(this.deliveryQuantity);
        sb.append(", sku=");
        sb.append(this.sku);
        sb.append("]");
        return sb.toString();
    }
}
