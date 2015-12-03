package com.sap.integration.utils.integrationdb.jpa;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Converter between class for persistence and JPA type conversion. Particularly converts the Joda DateTime so the time stamp
 * value is stored as human readable string.
 */
public class JodaDateTimeConverter implements Converter {

    private static final long serialVersionUID = -8170012396341100060L;

    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
        return objectValue == null ? null : ((DateTime) objectValue).toDateTime(DateTimeZone.UTC).toString();
    }

    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        return dataValue == null ? null : new DateTime((String) dataValue);
    }

    public boolean isMutable() {
        return false;
    }

    public void initialize(DatabaseMapping mapping, Session session) {
    }
}
