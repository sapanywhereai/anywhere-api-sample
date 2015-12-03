package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Amount data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpAmountDto {

    private Double amount;
    private Double amountLocalCurrency;

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountLocalCurrency() {
        return this.amountLocalCurrency;
    }

    public void setAmountLocalCurrency(Double amountLocalCurrency) {
        this.amountLocalCurrency = amountLocalCurrency;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [amount=");
        sb.append(this.amount);
        sb.append(", amountLocalCurrency=");
        sb.append(this.amountLocalCurrency);
        sb.append("]");
        return sb.toString();
    }
}
