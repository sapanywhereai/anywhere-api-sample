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
 * Payment ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class Payment extends JpaLayer implements Serializable {

    private static final long serialVersionUID = -2493395488100041074L;

    private Long anwId;

    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BPID")
    private Customer bp;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(value = "id")
    private List<PaymentLine> lines = new ArrayList<PaymentLine>();

    private Double docTotal;

    public Payment() {
        super();
    }

    public Long getAnwId() {
        return anwId;
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

    public List<PaymentLine> getLines() {
        return this.lines;
    }

    public void setLines(List<PaymentLine> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public void addLine(PaymentLine pl) {
        this.lines.add(pl);
        pl.setPayment(this);
    }

    public void removeLine(SalesDeliveryLine sol) {
        if (this.lines.contains(sol)) {
            this.lines.remove(sol);
        } else if (sol.getId() != null) {
            for (PaymentLine line : this.lines) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [anwId=");
        sb.append(this.anwId);
        sb.append(", status=");
        sb.append(this.status);
        sb.append(", bp=");
        sb.append(this.bp);
        sb.append(", lines=");
        sb.append(this.lines);
        sb.append(", docTotal=");
        sb.append(this.docTotal);
        sb.append("]");
        return sb.toString();
    }
}
