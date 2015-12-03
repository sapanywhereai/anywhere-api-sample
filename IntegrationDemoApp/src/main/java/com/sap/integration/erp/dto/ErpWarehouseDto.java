package com.sap.integration.erp.dto;

/**
 * Warehouse data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
public class ErpWarehouseDto {

    private String code;

    public ErpWarehouseDto() {
    }

    public ErpWarehouseDto(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public ErpWarehouseDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [code=");
        sb.append(this.code);
        sb.append("]");
        return sb.toString();
    }
}
