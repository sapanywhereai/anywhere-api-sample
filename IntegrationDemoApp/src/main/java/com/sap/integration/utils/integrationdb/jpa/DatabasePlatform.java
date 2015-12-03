package com.sap.integration.utils.integrationdb.jpa;

/**
 * Configuration class for Eclipselink JPA. Particularly handling of foreign keys constraints usage with SQLite.
 */
public class DatabasePlatform extends org.eclipse.persistence.platform.database.DatabasePlatform {

    private static final long serialVersionUID = 6889316678847660384L;

    public boolean supportsForeignKeyConstraints() {
        return false;
    }
}
