package com.sap.integration.salesorder.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Unit of Measure object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwUomDto {

    private Long id;
    private String name;

    public AnwUomDto() {
    }

    public AnwUomDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
        sb.append(", name=");
        sb.append(this.name);
        sb.append("]\n");
        return sb.toString();
    }
}
