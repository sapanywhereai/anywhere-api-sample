package com.sap.integration.erp.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 * Payment data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpPaymentDto {

    private Long id;
    private Long anwId;
    private String docNumber;
    private String status;
    private ErpCustomerDto customer;
    private DateTime lastUpdateTime;
    private List<ErpPaymentLineDto> paymentLines;
    private ErpAmountDto amount;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnwId() {
        return this.anwId;
    }

    public void setAnwId(Long anwId) {
        this.anwId = anwId;
    }

    public String getDocNumber() {
        return this.docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ErpCustomerDto getCustomer() {
        return this.customer;
    }

    public void setCustomer(ErpCustomerDto customer) {
        this.customer = customer;
    }

    public DateTime getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<ErpPaymentLineDto> getPaymentLines() {
        return this.paymentLines;
    }

    public void setPaymentLines(List<ErpPaymentLineDto> paymentLines) {
        this.paymentLines = paymentLines;
    }

    public ErpAmountDto getAmount() {
        return this.amount;
    }

    public void setAmount(ErpAmountDto amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", anwId=");
        sb.append(this.anwId);
        sb.append(", docNumber=");
        sb.append(this.docNumber);
        sb.append(", status=");
        sb.append(this.status);
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append(", paymentLines=");
        sb.append(this.paymentLines);
        sb.append(", amount=");
        sb.append(this.amount);
        sb.append("]");
        return sb.toString();
    }
}
