package com.sap.integration.erp.dummy.jpa;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;

import com.sap.integration.utils.integrationdb.jpa.AbstractJpaLayer;
import com.sap.integration.utils.integrationdb.jpa.JodaDateTimeConverter;

/**
 * Base class for entities within Dummy ERP DB covers the connection initialization, simplifies access to the EntityManager, etc.
 * It also adds and handles the "lastUpdateTime" property for each entity.
 */
@MappedSuperclass
@Converter(name = "DummyJodaDate", converterClass = JodaDateTimeConverter.class)
public class JpaLayer extends AbstractJpaLayer {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "INTEGER")
    private Long id;

    @Convert("DummyJodaDate")
    private DateTime lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    protected String getPersistenceUnitName() {
        return "dummyERP";
    }

    public void persist() {
        setLastUpdateTime(DateTime.now());
        super.persist();
    }

    public void merge() {
        setLastUpdateTime(DateTime.now());
        super.merge();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime);
        sb.append("]");
        return sb.toString();
    }
}
