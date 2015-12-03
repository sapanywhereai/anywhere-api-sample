package com.sap.integration.anywhere.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.enumeration.AnwPaymentStatusDto;

/**
 * Payment object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwPaymentDto {

    private Long id;
    private String docNumber;
    private AnwPaymentStatusDto status;
    private AnwMarketingDocumentCustomerDto customer;
    private DateTime updateTime;
    private List<AnwPaymentLineDto> paymentLines;
    private AnwAmountDto amount;

    // For following properties is not implemented integration, added only because of need in reports
    private DateTime creationTime;
    private List<AnwPaymentMethodLineDto> paymentMethodLines;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocNumber() {
        return this.docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public AnwPaymentStatusDto getStatus() {
        return this.status;
    }

    public void setStatus(AnwPaymentStatusDto status) {
        this.status = status;
    }

    public AnwMarketingDocumentCustomerDto getCustomer() {
        return this.customer;
    }

    public void setCustomer(AnwMarketingDocumentCustomerDto customer) {
        this.customer = customer;
    }

    public DateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<AnwPaymentLineDto> getPaymentLines() {
        return this.paymentLines;
    }

    public void setPaymentLines(List<AnwPaymentLineDto> paymentLines) {
        this.paymentLines = paymentLines;
    }

    public AnwAmountDto getAmount() {
        return this.amount;
    }

    public void setAmount(AnwAmountDto amount) {
        this.amount = amount;
    }

    public List<AnwPaymentMethodLineDto> getPaymentMethodLines() {
        return paymentMethodLines;
    }

    public void setPaymentMethodLines(List<AnwPaymentMethodLineDto> paymentMethodLines) {
        this.paymentMethodLines = paymentMethodLines;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", docNumber=");
        sb.append(this.docNumber);
        sb.append(", status=");
        sb.append(this.status);
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append(", paymentLines=");
        sb.append(this.paymentLines);
        sb.append(", amount=");
        sb.append(this.amount);
        sb.append(", paymentMethodLines=");
        sb.append(this.paymentMethodLines);
        sb.append(", creationTime=");
        sb.append(this.creationTime);
        sb.append("]");
        return sb.toString();
    }
}