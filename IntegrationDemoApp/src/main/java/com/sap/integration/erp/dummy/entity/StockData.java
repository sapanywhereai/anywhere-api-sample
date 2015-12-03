package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;

import javax.persistence.Entity;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Stock Data ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class StockData extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -5587832475306787724L;

    private String productCode;

    private Long quantity;

    private String unitOfMeasure;

    private String whsCode;

    public StockData() {
        super();
    }

    public String getProductCode() {
        return this.productCode;
    }

    public StockData setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public StockData setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public StockData setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public String getWhsCode() {
        return whsCode;
    }

    public StockData setWhsCode(String whsCode) {
        this.whsCode = whsCode;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [productCode=");
        sb.append(this.productCode);
        sb.append(", quantity=");
        sb.append(this.quantity);
        sb.append(", unitOfMeasure=");
        sb.append(this.unitOfMeasure);
        sb.append(", whsCode=");
        sb.append(this.whsCode);
        sb.append("]");
        return sb.toString();
    }
}
