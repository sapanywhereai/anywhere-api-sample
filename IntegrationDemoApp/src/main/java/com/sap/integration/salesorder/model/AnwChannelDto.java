package com.sap.integration.salesorder.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Channel object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwChannelDto {

    private Number id = null;

    public AnwChannelDto() {
    }

    public AnwChannelDto(Number id) {
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
