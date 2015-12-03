package com.sap.integration.utils.integrationdb.jpa;

import javax.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Converter;

/**
 * Base class for entities within Integration DB. Defines the persistence unit name for Integration DB. You may implement your own
 * database connector and JPA layer.
 */
@MappedSuperclass
@Converter(name = "JodaDate", converterClass = JodaDateTimeConverter.class)
public class JpaLayer extends AbstractJpaLayer {

    protected String getPersistenceUnitName() {
        return "integrationDb";
    }
}
