package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Payment Line ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class PaymentLine extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -4299433296921833166L;

    @ManyToOne
    @JoinColumn(name = "PAYMENTID", nullable = false)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "INVOICEID", nullable = false)
    private Invoice invoice;

    private Double appliedAmount;

    public PaymentLine() {
        super();
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Double getAppliedAmount() {
        return this.appliedAmount;
    }

    public void setAppliedAmount(Double appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [payment=");
        sb.append(this.payment);
        sb.append(", invoice=");
        sb.append(this.invoice);
        sb.append(", appliedAmount=");
        sb.append(this.appliedAmount);
        sb.append("]");
        return sb.toString();
    }
}
