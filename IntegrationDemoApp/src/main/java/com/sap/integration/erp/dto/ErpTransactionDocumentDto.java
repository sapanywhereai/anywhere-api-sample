package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Transaction Document data transfer object used when simulating communication with ERP. This class can be adjusted to reflect
 * your solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpTransactionDocumentDto {

    private Long id;
    private String type;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", type=");
        sb.append(this.type);
        sb.append("]");
        return sb.toString();
    }
}
