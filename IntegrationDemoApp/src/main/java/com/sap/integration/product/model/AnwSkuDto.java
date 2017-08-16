package com.sap.integration.product.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * SKU object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwSkuDto {

    private Number id;
    private String code;
    private String name;

    public AnwSkuDto() {
    }

    public AnwSkuDto(Number id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Number getId() {
        return this.id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", code=");
        sb.append(this.code);
        sb.append(", name=");
        sb.append(this.name);
        sb.append("]");
        return sb.toString();
    }
}
