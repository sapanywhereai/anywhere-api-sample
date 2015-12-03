package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.erp.dummy.conversion.CustomerConversion;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Customer ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class Customer extends JpaLayer implements IErpDB, Serializable {

    private static final long serialVersionUID = 5702878429398108300L;

    private String name;

    private String code;

    public Customer() {
        super();
    }

    public String getName() {
        return this.name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Customer setCode(String code) {
        this.code = code;
        return this;
    }

    public IErpDto convert() {
        return CustomerConversion.run(this);
    }

    public static <O, I> List<O> convert(List<I> itemsToConvert, List<O> toListType) {
        return CustomerConversion.run(itemsToConvert, toListType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [name=");
        sb.append(this.name);
        sb.append(", code=");
        sb.append(this.code);
        sb.append("]");
        return sb.toString();
    }
}
