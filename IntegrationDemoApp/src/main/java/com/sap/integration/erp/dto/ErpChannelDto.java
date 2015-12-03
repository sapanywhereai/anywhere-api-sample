package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Channel data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpChannelDto {

    private Number id = null;

    public ErpChannelDto() {
    }

    public ErpChannelDto(Number id) {
        this.id = id;
    }

    public Number getId() {
        return this.id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append("]");
        return sb.toString();
    }
}
