package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Object used for Inventory counting. It represents line of Inventory Counting payload in which are stored information about
 * product and its quantity. Inventory Counting line is slightly different for HTTP GET and HTTP POST, that is the reason why
 * exists two objects AnwInventoryCountingGetLineDto and AnwInventoryCountingPostLineDto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwInventoryCountingPostLineDto {

    private String countedQuantity;
    private Number id;
    private String inventoryUoM;
    private AnwSkuDto sku;
    private String variance;
    private String variancePercent;

    public String getCountedQuantity() {
        return this.countedQuantity;
    }

    public void setCountedQuantity(String countedQuantity) {
        this.countedQuantity = countedQuantity;
    }

    public Number getId() {
        return this.id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getInventoryUoM() {
        return this.inventoryUoM;
    }

    public void setInventoryUoM(String inventoryUoM) {
        this.inventoryUoM = inventoryUoM;
    }

    public AnwSkuDto getSku() {
        return this.sku;
    }

    public void setSku(AnwSkuDto sku) {
        this.sku = sku;
    }

    public String getVariance() {
        return this.variance;
    }

    public void setVariance(String variance) {
        this.variance = variance;
    }

    public String getVariancePercent() {
        return this.variancePercent;
    }

    public void setVariancePercent(String variancePercent) {
        this.variancePercent = variancePercent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [countedQuantity=");
        sb.append(this.countedQuantity);
        sb.append(", id=");
        sb.append(this.id);
        sb.append(", inventoryUoM=");
        sb.append(this.inventoryUoM);
        sb.append(", sku=");
        sb.append(this.sku);
        sb.append(", variance=");
        sb.append(this.variance);
        sb.append(", variancePercent=");
        sb.append(this.variancePercent);
        sb.append("]");
        return sb.toString();
    }
}
