package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

import org.eclipse.persistence.annotations.Convert;
import org.joda.time.DateTime;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Inventory report ERP database entity used by database layer of ERP. This class can be adjusted to reflect your
 * solution.
 */
@Entity
public class InventoryReport extends JpaLayer implements Serializable {

    private static final long serialVersionUID = 4939088948645042440L;

    private String warehouseCode;

    private String warehouseName;

    private String productCode;

    private String productName;

    private BigDecimal inStock;

    @Convert("DummyJodaDate")
    private DateTime fromDate;

    @Convert("DummyJodaDate")
    private DateTime toDate;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getInStock() {
        return inStock;
    }

    public void setInStock(BigDecimal inStock) {
        this.inStock = inStock;
    }

    public DateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate() {
        return toDate;
    }

    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [warehouseCode=");
        sb.append(this.warehouseCode);
        sb.append(", warehouseName=");
        sb.append(this.warehouseName);
        sb.append(", productCode=");
        sb.append(this.productCode);
        sb.append(", productName=");
        sb.append(this.productName);
        sb.append(", inStock=");
        sb.append(this.inStock);
        sb.append(" fromDate=");
        sb.append(this.fromDate);
        sb.append(", toDate=");
        sb.append(this.toDate);
        sb.append("]");
        return sb.toString();
    }
}
