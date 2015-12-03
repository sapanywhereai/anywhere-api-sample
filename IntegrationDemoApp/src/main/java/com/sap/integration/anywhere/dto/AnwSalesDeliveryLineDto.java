package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Sales Delivery Line object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwSalesDeliveryLineDto {

    private Long id;
    private AnwSalesOrderDto baseDocument;
    private Integer requiredQuantity;
    private Integer deliveryQuantity;
    private AnwSkuDto sku;

    public AnwSkuDto getSku() {
        return this.sku;
    }

    public void setSku(AnwSkuDto sku) {
        this.sku = sku;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnwSalesOrderDto getBaseDocument() {
        return this.baseDocument;
    }

    public void setBaseDocument(AnwSalesOrderDto baseDocument) {
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
