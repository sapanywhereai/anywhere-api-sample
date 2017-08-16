package com.sap.integration.salesorder.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Amount object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwAmountDto {

    private Double amount;
    private Double amountLocalCurrency;

    public AnwAmountDto() {
    	
    }
    
    public AnwAmountDto(Double amount, Double amountLocalCurrency) {
    	this.amount = amount;
    	this.amountLocalCurrency = amountLocalCurrency;
    }
    
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