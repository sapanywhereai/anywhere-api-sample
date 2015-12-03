package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;

import javax.persistence.Entity;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Warehouse ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class Warehouse extends JpaLayer implements Serializable {

    private static final long serialVersionUID = 8306899213027151325L;


    private String code;

    private String name;

    public String getCode() {
        return this.code;
    }

    public Warehouse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Warehouse setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [code=");
        sb.append(this.code);
        sb.append(", name=");
        sb.append(this.name);
        sb.append("]");
        return sb.toString();
    }
}
