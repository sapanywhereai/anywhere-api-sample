package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Payment Line object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwPaymentLineDto {

    private Long id;
    private AnwTransactionDocumentDto transactionDocument;
    private AnwAmountDto appliedAmount;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnwTransactionDocumentDto getTransactionDocument() {
        return this.transactionDocument;
    }

    public void setTransactionDocument(AnwTransactionDocumentDto transactionDocument) {
        this.transactionDocument = transactionDocument;
    }

    public AnwAmountDto getAppliedAmount() {
        return this.appliedAmount;
    }

    public void setAppliedAmount(AnwAmountDto appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", transactionDocument=");
        sb.append(this.transactionDocument);
        sb.append(", appliedAmount=");
        sb.append(this.appliedAmount);
        sb.append("]");
        return sb.toString();
    }
}