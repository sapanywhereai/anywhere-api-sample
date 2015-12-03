package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Stock Data data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpStockDataDto {

    private Long id;
    private String productCode;
    private Long quantity;
    private String unitOfMeasure;
    private String whsCode;

    public Long getId() {
        return this.id;
    }

    public ErpStockDataDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public ErpStockDataDto setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public ErpStockDataDto setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public ErpStockDataDto setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public String getWhsCode() {
        return whsCode;
    }

    public ErpStockDataDto setWhsCode(String whsCode) {
        this.whsCode = whsCode;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(" , productCode=");
        sb.append(this.productCode);
        sb.append(", quantity=");
        sb.append(this.quantity);
        sb.append(", unitOfMeasure=");
        sb.append(this.unitOfMeasure);
        sb.append(", whsCode=");
        sb.append(this.whsCode);
        sb.append("]");
        return sb.toString();
    }
}
