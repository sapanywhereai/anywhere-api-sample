package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Invoice Line ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class InvoiceLine extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -4299433296921833166L;

    @ManyToOne
    @JoinColumn(name = "INVOICEID", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "SALESORDERID", nullable = true)
    private SalesOrder salesOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMID")
    private Item item;

    private Integer quantity;

    private Double unitPrice;

    private String taxCode;

    private Double lineTotal;

    public InvoiceLine() {
        super();
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTaxCode() {
        return this.taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Double getLineTotal() {
        return this.lineTotal;
    }

    public void setLineTotal(Double lineTotal) {
        this.lineTotal = lineTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [invoice=");
        sb.append(this.invoice);
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
