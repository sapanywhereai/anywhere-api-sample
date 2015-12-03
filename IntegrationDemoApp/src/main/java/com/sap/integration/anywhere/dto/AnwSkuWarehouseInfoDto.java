package com.sap.integration.anywhere.dto;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * SKU warhouse info object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwSkuWarehouseInfoDto {

    private String warehouseCode;

    private String warehouseName;

    private BigDecimal inStock;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public BigDecimal getInStock() {
        return inStock;
    }

    public void setInStock(BigDecimal inStock) {
        this.inStock = inStock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [warehouseCode=");
        sb.append(this.warehouseCode);
        sb.append(", warehouseName=");
        sb.append(this.warehouseName);
        sb.append(", inStock=");
        sb.append(this.inStock);
        sb.append("]");
        return sb.toString();
    }

}
