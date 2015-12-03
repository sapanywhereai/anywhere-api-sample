package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Sales Delivery Line ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class SalesDeliveryLine extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -4299433296921833166L;

    @ManyToOne
    @JoinColumn(name = "SALESDELIVERYID", nullable = false)
    private SalesDelivery salesDelivery;

    @ManyToOne
    @JoinColumn(name = "SALESORDERID", nullable = true)
    private SalesOrder salesOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMID")
    private Item item;

    private Integer quantity;

    private Integer unitPrice;

    private String taxCode;

    private Integer lineTotal;

    public SalesDeliveryLine() {
        super();
    }

    public SalesDelivery getSalesDelivery() {
        return this.salesDelivery;
    }

    public void setSalesDelivery(SalesDelivery salesDelivery) {
        this.salesDelivery = salesDelivery;
    }

    public SalesOrder getSalesOrder() {
        return this.salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTaxCode() {
        return this.taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getLineTotal() {
        return this.lineTotal;
    }

    public void setLineTotal(Integer lineTotal) {
        this.lineTotal = lineTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [salesDelivery=");
        sb.append(this.salesDelivery);
        sb.append(", salesOrder=");
        sb.append(this.salesOrder);
        sb.append(", item=");
        sb.append(this.item);
        sb.append(", quantity=");
        sb.append(this.quantity);
        sb.append(", unitPrice=");
        sb.append(this.unitPrice);
        sb.append(", taxCode=");
        sb.append(this.taxCode);
        sb.append(", lineTotal=");
        sb.append(this.lineTotal);
        sb.append("]");
        return sb.toString();
    }
}
