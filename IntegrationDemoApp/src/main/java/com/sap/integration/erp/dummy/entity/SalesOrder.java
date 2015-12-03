package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.eclipse.persistence.annotations.Convert;
import org.joda.time.DateTime;

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Sales Order ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class SalesOrder extends JpaLayer implements Serializable {

    private static final long serialVersionUID = 8911729198093454953L;

    private Long anwId;

    private Long channelId;

    private String pricingMethod;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BPID")
    private Customer bp;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(value = "id")
    private List<SalesOrderLine> lines = new ArrayList<SalesOrderLine>();

    private Double docTotal;

    @Convert("DummyJodaDate")
    private DateTime orderTime;

    public SalesOrder() {
        super();
    }

    public Long getAnwId() {
        return this.anwId;
    }

    public void setAnwId(Long anwId) {
        this.anwId = anwId;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getPricingMethod() {
        return this.pricingMethod;
    }

    public void setPricingMethod(String pricingMethod) {
        this.pricingMethod = pricingMethod;
    }

    public Customer getBusinessPartner() {
        return this.bp;
    }

    public void setBusinessPartner(Customer bp) {
        this.bp = bp;
    }

    public List<SalesOrderLine> getLines() {
        return this.lines;
    }

    public void setLines(List<SalesOrderLine> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public void addLine(SalesOrderLine sol) {
        this.lines.add(sol);
        sol.setSalesOrder(this);
    }

    public void removeLine(SalesOrderLine sol) {
        if (this.lines.contains(sol)) {
            this.lines.remove(sol);
        } else if (sol.getId() != null) {
            for (SalesOrderLine line : this.lines) {
                if (sol.getId().equals(line.getId())) {
                    this.lines.remove(line);
                    break;
                }
            }
        }
    }

    public Double getDocTotal() {
        return this.docTotal;
    }

    public void setDocTotal(Double docTotal) {
        this.docTotal = docTotal;
    }

    public DateTime getOrderTime() {
        return this.orderTime;
    }

    public void setOrderTime(DateTime orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [anwId=");
        sb.append(this.anwId);
        sb.append(", channelId=");
        sb.append(this.channelId);
        sb.append(", pricingMethod=");
        sb.append(this.pricingMethod);
        sb.append(", bp=");
        sb.append(this.bp);
        sb.append(", lines=");
        sb.append(this.lines);
        sb.append(", docTotal=");
        sb.append(this.docTotal);
        sb.append(", orderTime=");
        sb.append(this.orderTime);
        sb.append("]");
        return sb.toString();
    }
}
