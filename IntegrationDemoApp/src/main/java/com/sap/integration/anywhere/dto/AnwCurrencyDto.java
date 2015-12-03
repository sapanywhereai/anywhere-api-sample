package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Currency object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwCurrencyDto {

    private Long id;
    private String code;
    private Double rate;

    public AnwCurrencyDto() {
    }

    public AnwCurrencyDto(Long id, String code, Double rate) {
        this.id = id;
        this.code = code;
        this.rate = rate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getRate() {
        return this.rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", code=");
        sb.append(this.code);
        sb.append(", rate=");
        sb.append(this.rate);
        sb.append("]");
        return sb.toString();
    }
}
