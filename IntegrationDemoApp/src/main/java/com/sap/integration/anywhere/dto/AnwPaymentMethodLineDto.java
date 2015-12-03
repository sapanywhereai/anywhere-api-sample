package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Payment method line object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwPaymentMethodLineDto {

    private Long id;

    private AnwPaymentMethodInfoDto paymentMethod;

    private AnwAmountDto amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnwPaymentMethodInfoDto getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AnwPaymentMethodInfoDto paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AnwAmountDto getAmount() {
        return amount;
    }

    public void setAmount(AnwAmountDto amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", paymentMethod=");
        sb.append(this.paymentMethod);
        sb.append(", amount=");
        sb.append(this.amount);
        sb.append("]");
        return sb.toString();
    }
}