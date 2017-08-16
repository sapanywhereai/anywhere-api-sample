package com.sap.integration.salesorder.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Warehouse object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwWarehouseDto {

    private Long id;
    private String code;
    private String name;

    public AnwWarehouseDto() {
    }

    public AnwWarehouseDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public AnwWarehouseDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public AnwWarehouseDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public AnwWarehouseDto setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(" [id=");
        builder.append(this.id);
        builder.append(", code=");
        builder.append(this.code);
        builder.append(", name=");
        builder.append(this.name);
        builder.append("]");
        return builder.toString();
    }
}
