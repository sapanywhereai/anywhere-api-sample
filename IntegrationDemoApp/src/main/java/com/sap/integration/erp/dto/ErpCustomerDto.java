package com.sap.integration.erp.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.core.transformation.CustomerTransformation;
import com.sap.integration.erp.dummy.conversion.CustomerConversion;
import com.sap.integration.erp.dummy.entity.IErpDB;

/**
 * Customer data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your solution.
 */
public class ErpCustomerDto implements IErpDto {

    private Long id;
    private String code;
    private String name;
    private DateTime lastUpdateTime;

    public ErpCustomerDto() {
    }

    public ErpCustomerDto(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return this.id;
    }

    public ErpCustomerDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public ErpCustomerDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ErpCustomerDto setName(String name) {
        this.name = name;
        return this;
    }

    public DateTime getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public ErpCustomerDto setLastUpdateTime(DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    public DateTime getLastSyncTime() {
        return getLastUpdateTime();
    }

    public IAnwDto transform() {
        return CustomerTransformation.run(this);
    }

    public IErpDB convert() {
        return CustomerConversion.run(this);
    }

    public boolean isDifferent(IErpDto other) {
        if (other == null || !(other instanceof ErpCustomerDto)) {
            return false;
        }
        ErpCustomerDto r = (ErpCustomerDto) other;
        return !new EqualsBuilder()
                .append(code, r.code)
                .append(name, r.name)
                .isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", code=");
        sb.append(this.code);
        sb.append(", name=");
        sb.append(this.name);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append("]");
        return sb.toString();
    }
}
