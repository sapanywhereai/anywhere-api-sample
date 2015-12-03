package com.sap.integration.utils.integrationdb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sap.integration.utils.integrationdb.jpa.JpaLayer;

/**
 * Entity Range stores information about processed ranges of transaction in StockData table. This entity and table belongs to
 * integration application.
 */
@Entity
public class Range extends JpaLayer implements Serializable {

    private static final long serialVersionUID = 8250646757562818936L;

    @Id
    @GeneratedValue
    private Long id;

    private Long rangeFrom;

    private Long rangeTo;

    public Range() {
        super();
    }

    public Long getId() {
        return this.id;
    }

    public Range setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getRangeFrom() {
        return this.rangeFrom;
    }

    public Range setRangeFrom(Long rangeFrom) {
        this.rangeFrom = rangeFrom;
        return this;
    }

    public Long getRangeTo() {
        return rangeTo;
    }

    public Range setRangeTo(Long rangeTo) {
        this.rangeTo = rangeTo;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", rangeFrom=");
        sb.append(this.rangeFrom);
        sb.append(", rangeTo=");
        sb.append(this.rangeTo);
        sb.append("]");
        return sb.toString();
    }
}
