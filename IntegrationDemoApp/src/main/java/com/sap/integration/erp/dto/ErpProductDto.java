package com.sap.integration.erp.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.core.transformation.ProductTransformation;
import com.sap.integration.erp.dummy.conversion.ProductConversion;
import com.sap.integration.erp.dummy.entity.IErpDB;

/**
 * Product data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
public class ErpProductDto implements IErpDto {

    private Long id;
    private String itemCode;
    private String itemName;
    private Integer stockCount;
    private DateTime lastUpdateTime;

    public ErpProductDto() {
        super();
    }

    public Long getId() {
        return this.id;
    }

    public ErpProductDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public ErpProductDto setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemName() {
        return this.itemName;
    }

    public ErpProductDto setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public Integer getStockCount() {
        return this.stockCount;
    }

    public ErpProductDto setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
        return this;
    }

    public DateTime getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public ErpProductDto setLastUpdateTime(DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    public int compareTo(ErpProductDto o) {
        return this.lastUpdateTime.compareTo(o.lastUpdateTime);
    }

    public DateTime getLastSyncTime() {
        return getLastUpdateTime();
    }

    public IAnwDto transform() {
        return ProductTransformation.run(this);
    }

    public IErpDB convert() {
        return ProductConversion.run(this);
    }

	public boolean isDifferent(IErpDto other) {
        if (other == null || !(other instanceof ErpProductDto)) {
            return false;
        }
        ErpProductDto product = (ErpProductDto) other;
        return !new EqualsBuilder()
                .append(this.itemCode, product.itemCode)
                .append(this.itemName, product.itemName)
                .append(this.stockCount, product.stockCount)
                .isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", itemCode=");
        sb.append(this.itemCode);
        sb.append(", itemName=");
        sb.append(this.itemName);
        sb.append(", stockCount=");
        sb.append(this.stockCount);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append("]");
        return sb.toString();
    }}

