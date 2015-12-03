package com.sap.integration.erp.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 * Product data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpSalesDeliveryDto {

    private Long id;
    private Long anwId;
    private Long baseId;
    private String docNumber;
    private String status;
    private ErpCustomerDto customer;
    private String salesOrderNumber;
    private DateTime lastUpdateTime;
    private List<ErpSalesDeliveryLineDto> lines;

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

    public Long getBaseId() {
        return this.baseId;
    }

    public void setSalesOrderId(Long baseId) {
        this.baseId = baseId;
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

    public String getSalesOrderNumber() {
        return this.salesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }

    public DateTime getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<ErpSalesDeliveryLineDto> getLines() {
        return this.lines;
    }

    public void setLines(List<ErpSalesDeliveryLineDto> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", anwId=");
        sb.append(this.anwId);
        sb.append(", baseId=");
        sb.append(this.baseId);
        sb.append(", docNumber=");
        sb.append(this.docNumber);
        sb.append(", status=");
        sb.append(this.status);
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", salesOrderNumber=");
        sb.append(this.salesOrderNumber);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append(", lines=");
        sb.append(this.lines);
        sb.append("]");
        return sb.toString();
    }
}
