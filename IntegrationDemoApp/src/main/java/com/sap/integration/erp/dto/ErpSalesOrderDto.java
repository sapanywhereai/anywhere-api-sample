package com.sap.integration.erp.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 * Sales Order data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpSalesOrderDto {

	private Long id;
	private Long anwId;
	private String docNumber;
	private ErpCustomerDto customer;
	private ErpChannelDto channel;
	private String pricingMethod;
	private Double docTotal;
	private List<ErpSalesOrderLineDto> productLines;
	private DateTime orderTime;
	private DateTime lastUpdateTime;

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

	public ErpCustomerDto getCustomer() {
        return this.customer;
	}

	public void setCustomer(ErpCustomerDto customer) {
		this.customer = customer;
	}

	public ErpChannelDto getChannel() {
        return this.channel;
	}

	public void setChannel(ErpChannelDto channel) {
		this.channel = channel;
	}

	public String getPricingMethod() {
        return this.pricingMethod;
	}

	public void setPricingMethod(String pricingMethod) {
		this.pricingMethod = pricingMethod;
	}

	public Double getDocTotal() {
		return docTotal;
	}

	public void setDocTotal(Double docTotal) {
		this.docTotal = docTotal;
	}
	
	public List<ErpSalesOrderLineDto> getProductLines() {
        return this.productLines;
	}

	public void setProductLines(List<ErpSalesOrderLineDto> productLines) {
		this.productLines = productLines;
	}

	public DateTime getOrderTime() {
        return this.orderTime;
	}

	public void setOrderTime(DateTime orderTime) {
		this.orderTime = orderTime;
	}

	public DateTime getLastUpdateTime() {
        return this.lastUpdateTime;
	}

	public void setLastUpdateTime(DateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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
        sb.append(", customer=");
        sb.append(this.customer);
        sb.append(", channel=");
        sb.append(this.channel);
        sb.append(", pricingMethod=");
        sb.append(this.pricingMethod);
        sb.append(", docTotal=");
        sb.append(this.docTotal);
        sb.append(", productLines=");
        sb.append(this.productLines);
        sb.append(", orderTime=");
        sb.append(this.orderTime);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append("]");
        return sb.toString();
    }
}
