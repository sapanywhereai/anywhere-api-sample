package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Payment Line data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpPaymentLineDto {

    private Long id;
    private ErpTransactionDocumentDto transactionDocument;
    private ErpAmountDto appliedAmount;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpTransactionDocumentDto getTransactionDocument() {
        return this.transactionDocument;
    }

    public void setTransactionDocument(ErpTransactionDocumentDto transactionDocument) {
        this.transactionDocument = transactionDocument;
    }

    public ErpAmountDto getAppliedAmount() {
        return this.appliedAmount;
    }

    public void setAppliedAmount(ErpAmountDto appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", transactionDocument=[");
        sb.append(this.transactionDocument);
        sb.append("], appliedAmount=[");
        sb.append(this.appliedAmount);
        sb.append("]");
        return sb.toString();
    }
}
