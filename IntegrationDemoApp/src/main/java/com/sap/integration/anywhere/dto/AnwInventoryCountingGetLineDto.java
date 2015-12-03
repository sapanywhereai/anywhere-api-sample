package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Object used for Inventory counting. It represents line of Inventory Counting payload in which are stored information about
 * product and its quantity. Inventory Counting line is slightly different for HTTP GET and HTTP POST, that is the reason why
 * exists two objects AnwInventoryCountingGetLineDto and AnwInventoryCountingPostLineDto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwInventoryCountingGetLineDto {

    private Long id;
    private String lineNumber;
    private String inventoryUoM;
    private AnwSkuDto sku;
    private Long inStockQuantity;
    private Boolean isCounted;
    private Long countedQuantity;
    private Long variance;
    private Long variancePercent;
    private String remark;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
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

    public Long getInStockQuantity() {
        return this.inStockQuantity;
    }

    public void setInStockQuantity(Long inStockQuantity) {
        this.inStockQuantity = inStockQuantity;
    }

    public Boolean getIsCounted() {
        return this.isCounted;
    }

    public void setIsCounted(Boolean isCounted) {
        this.isCounted = isCounted;
    }

    public Long getCountedQuantity() {
        return this.countedQuantity;
    }

    public void setCountedQuantity(Long countedQuantity) {
        this.countedQuantity = countedQuantity;
    }

    public Long getVariance() {
        return this.variance;
    }

    public void setVariance(Long variance) {
        this.variance = variance;
    }

    public Long getVariancePercent() {
        return this.variancePercent;
    }

    public void setVariancePercent(Long variancePercent) {
        this.variancePercent = variancePercent;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", lineNumber=");
        sb.append(this.lineNumber);
        sb.append(", inventoryUoM=");
        sb.append(this.inventoryUoM);
        sb.append(", sku=");
        sb.append(this.sku);
        sb.append(", inStockQuantity=");
        sb.append(this.inStockQuantity);
        sb.append(", isCounted=");
        sb.append(this.isCounted);
        sb.append(", countedQuantity=");
        sb.append(this.countedQuantity);
        sb.append(", variance=");
        sb.append(this.variance);
        sb.append(", variancePercent=");
        sb.append(this.variancePercent);
        sb.append(", remark=");
        sb.append(this.remark);
        sb.append("]");
        return sb.toString();
    }
}
