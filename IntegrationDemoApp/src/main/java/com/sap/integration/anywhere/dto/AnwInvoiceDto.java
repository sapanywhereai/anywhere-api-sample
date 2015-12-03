package com.sap.integration.anywhere.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.enumeration.AnwInvoicePaymentStatusDto;
import com.sap.integration.anywhere.dto.enumeration.AnwInvoiceStatusDto;

/**
 * Invoice object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwInvoiceDto {

    private Long id;
    private String docNumber;
    private AnwInvoiceStatusDto status;
    private AnwInvoicePaymentStatusDto paymentStatus;
    private AnwMarketingDocumentCustomerDto customer;
    private DateTime updateTime;
    private AnwAmountDto grossTotal;
    private List<AnwInvoiceLineDto> invoiceLines;

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

    public AnwInvoiceStatusDto getStatus() {
        return this.status;
    }

    public void setStatus(AnwInvoiceStatusDto status) {
        this.status = status;
    }

    public AnwInvoicePaymentStatusDto getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(AnwInvoicePaymentStatusDto paymentStatus) {
        this.paymentStatus = paymentStatus;
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

	public AnwAmountDto getGrossTotal() {
		return grossTotal;
	}

	public void setGrossTotal(AnwAmountDto grossTotal) {
		this.grossTotal = grossTotal;
	}
	
    public List<AnwInvoiceLineDto> getInvoiceLines() {
        return this.invoiceLines;
    }

    public void setInvoiceLines(List<AnwInvoiceLineDto> invoiceLines) {
        this.invoiceLines = invoiceLines;
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
        sb.append(", paymentStatus=");
        sb.append(this.paymentStatus);
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append(", grossTotal=");
        sb.append(this.grossTotal);
        sb.append(", invoiceLines=[");
        sb.append(this.invoiceLines);
        sb.append("]");
        return sb.toString();
    }


}
