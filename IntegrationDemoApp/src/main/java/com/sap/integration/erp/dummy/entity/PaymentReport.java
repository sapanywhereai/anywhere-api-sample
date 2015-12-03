package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;

import javax.persistence.Entity;

import org.eclipse.persistence.annotations.Convert;
import org.joda.time.DateTime;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Payment report ERP database entity used by database layer of ERP. This class can be adjusted to reflect your
 * solution.
 */
@Entity
public class PaymentReport extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -5172556604388101066L;

    private String methodInfo;

    private Integer numberOfPayments;

    private Double amount;

    private Double amountLC;

    @Convert("DummyJodaDate")
    private DateTime fromDate;

    @Convert("DummyJodaDate")
    private DateTime toDate;


    public String getMethodInfo() {
        return methodInfo;
    }

    public void setMethodInfo(String methodInfo) {
        this.methodInfo = methodInfo;
    }

    public Integer getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(Integer numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountLC() {
        return amountLC;
    }

    public void setAmountLC(Double amountLC) {
        this.amountLC = amountLC;
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
        sb.append(" [methodInfo=");
        sb.append(this.methodInfo);
        sb.append(", numberOfPayments=");
        sb.append(this.numberOfPayments);
        sb.append(", amount=");
        sb.append(this.amount);
        sb.append(", amountLC=");
        sb.append(this.amountLC);
        sb.append(", fromDate=");
        sb.append(this.fromDate);
        sb.append(", toDate=");
        sb.append(this.toDate);
        sb.append("]");
        return sb.toString();
    }
}
