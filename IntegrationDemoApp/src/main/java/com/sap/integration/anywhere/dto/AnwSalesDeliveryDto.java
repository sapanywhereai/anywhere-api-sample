package com.sap.integration.anywhere.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.enumeration.AnwSalesDeliveryStatusDto;

/**
 * Sales Delivery object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwSalesDeliveryDto {

    private Long id;
    private String docNumber;
    private AnwSalesDeliveryStatusDto status;
    private AnwMarketingDocumentCustomerDto customer;
    private String salesOrderNumber;
    private DateTime updateTime;
    private List<AnwSalesDeliveryLineDto> lines;

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

    public AnwSalesDeliveryStatusDto getStatus() {
        return this.status;
    }

    public void setStatus(AnwSalesDeliveryStatusDto status) {
        this.status = status;
    }

    public AnwMarketingDocumentCustomerDto getCustomer() {
        return this.customer;
    }

    public void setCustomer(AnwMarketingDocumentCustomerDto customer) {
        this.customer = customer;
    }

    public String getSalesOrderNumber() {
        return this.salesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }

    public DateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<AnwSalesDeliveryLineDto> getLines() {
        return this.lines;
    }

    public void setLines(List<AnwSalesDeliveryLineDto> lines) {
        this.lines = lines;
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
        sb.append(", salesOrderNumber=");
        sb.append(this.salesOrderNumber);
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append(", lines=");
        sb.append(this.lines);
        sb.append("]");
        return sb.toString();
    }
}
