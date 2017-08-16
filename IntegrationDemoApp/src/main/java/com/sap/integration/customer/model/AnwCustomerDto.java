package com.sap.integration.customer.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;

import com.sap.integration.salesorder.model.IAnwDto;
import com.sap.integration.salesorder.model.enumeration.AnwMarketingStatusDto;

/**
 * Customer object used by JSON for communication with SAP Anywhere.
 */
public class AnwCustomerDto implements IAnwDto {

    private Long id;
    private String customerCode;
    private String customerName;
    private AnwCustomerTypeDto customerType;
    private AnwCustomerStageDto stage;
    private AnwCustomerStatusDto status;
    private AnwMarketingStatusDto marketingStatus;
    private DateTime updateTime;
    private String displayName;

    public AnwCustomerDto() {
    }

    public AnwCustomerDto(String customerName, String customerCode, AnwCustomerTypeDto customerType,
            AnwCustomerStageDto stage, AnwCustomerStatusDto status, AnwMarketingStatusDto marketingStatus, DateTime updateTime) {
        this.setCustomerName(customerName);
        this.setCustomerCode(customerCode);
        this.customerType = customerType;
        this.stage = stage;
        this.status = status;
        this.marketingStatus = marketingStatus;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public AnwCustomerTypeDto getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(AnwCustomerTypeDto customerType) {
        this.customerType = customerType;
    }

    public AnwCustomerStageDto getStage() {
        return this.stage;
    }

    public void setStage(AnwCustomerStageDto stage) {
        this.stage = stage;
    }

    public AnwCustomerStatusDto getStatus() {
        return this.status;
    }

    public void setStatus(AnwCustomerStatusDto status) {
        this.status = status;
    }

    public AnwMarketingStatusDto getMarketingStatus() {
        return this.marketingStatus;
    }

    public void setMarketingStatus(AnwMarketingStatusDto marketingStatus) {
        this.marketingStatus = marketingStatus;
    }

    public DateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    public DateTime getLastSyncTime() {
        return getUpdateTime();
    }

    public boolean isDifferent(IAnwDto other) {
        if (other == null || !(other instanceof AnwCustomerDto)) {
            return false;
        }
        AnwCustomerDto r = (AnwCustomerDto) other;
        return !new EqualsBuilder()
                .append(customerCode, r.customerCode)
                .append(customerName, r.customerName)
                .isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", customerName=");
        sb.append(this.customerName);
        sb.append(", customerCode=");
        sb.append(this.customerCode);
        sb.append(", customerType=");
        sb.append(this.customerType);
        sb.append(", stage=");
        sb.append(this.stage);
        sb.append(", status=");
        sb.append(this.status);
        sb.append(", marketingStatus=");
        sb.append(this.marketingStatus);
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append("]");
        return sb.toString();
    }

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}