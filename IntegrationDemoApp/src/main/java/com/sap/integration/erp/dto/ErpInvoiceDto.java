package com.sap.integration.erp.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 * Invoice data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpInvoiceDto {

    private Long id;
    private Long anwId;
    private String docNumber;
    private String status;
    private String paymentStatus;
    private ErpCustomerDto customer;
    private DateTime lastUpdateTime;
    private Double docTotal;
    private List<ErpInvoiceLineDto> invoiceLines;

    public ErpInvoiceDto() {
    }

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

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

	public Double getDocTotal() {
		return docTotal;
	}

	public void setDocTotal(Double docTotal) {
		this.docTotal = docTotal;
	}
	
    public List<ErpInvoiceLineDto> getInvoiceLines() {
        return this.invoiceLines;
    }

    public void setInvoiceLines(List<ErpInvoiceLineDto> invoiceLines) {
        this.invoiceLines = invoiceLines;
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
        sb.append(", paymentStatus=");
        sb.append(this.paymentStatus);
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append(", docTotal=");
        sb.append(this.docTotal);
        sb.append(", invoiceLines=");
        sb.append(this.invoiceLines);
        sb.append("]");
        return sb.toString();
    }
}
