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

import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Sales Delivery ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class SalesDelivery extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -2493395488100041074L;

    private Long anwId;

    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BPID")
    private Customer bp;

    @ManyToOne(optional = false)
    private SalesOrder salesOrder;

    @OneToMany(mappedBy = "salesDelivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(value = "id")
    private List<SalesDeliveryLine> lines = new ArrayList<SalesDeliveryLine>();

    private Long docTotal;

    public SalesDelivery() {
        super();
    }

    public Long getAnwId() {
        return this.anwId;
    }

    public void setAnwId(Long anwId) {
        this.anwId = anwId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getBp() {
        return this.bp;
    }

    public void setBp(Customer bp) {
        this.bp = bp;
    }

    public SalesOrder getSalesOrder() {
        return this.salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public List<SalesDeliveryLine> getLines() {
        return this.lines;
    }

    public void setLines(List<SalesDeliveryLine> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public void addLine(SalesDeliveryLine sol) {
        this.lines.add(sol);
        sol.setSalesDelivery(this);
    }

    public void removeLine(SalesDeliveryLine sol) {
        if (this.lines.contains(sol)) {
            this.lines.remove(sol);
        } else if (sol.getId() != null) {
            for (SalesDeliveryLine line : this.lines) {
                if (sol.getId().equals(line.getId())) {
                    this.lines.remove(line);
                    break;
                }
            }
        }
    }

    public Long getDocTotal() {
        return this.docTotal;
    }

    public void setDocTotal(Long docTotal) {
        this.docTotal = docTotal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(" [anwId=");
        builder.append(this.anwId);
        builder.append(", status=");
        builder.append(this.status);
        builder.append(", bp=");
        builder.append(this.bp);
        builder.append(", salesOrder=");
        builder.append(this.salesOrder);
        builder.append(", lines=");
        builder.append(this.lines);
        builder.append(", docTotal=");
        builder.append(this.docTotal);
        builder.append("]");
        return builder.toString();
    }
}
