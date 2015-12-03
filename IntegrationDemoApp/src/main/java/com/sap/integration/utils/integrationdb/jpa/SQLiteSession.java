package com.sap.integration.utils.integrationdb.jpa;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Login;
import org.eclipse.persistence.sessions.Session;

/**
 * Eclipselink tweaker class, used in persistence definition.
 * 
 * Primarily lowers the eclipselink parameters "delayBetweenConnectionAttempts" and "queryRetryAttemptCount" for better
 * performance during initial schema creation.
 */
public class SQLiteSession implements SessionCustomizer {

    public void customize(Session session) throws Exception {
        if (session == null) {
            return;
        }

        Login login = session.getDatasourceLogin();
        if (login instanceof DatabaseLogin) {
            DatabaseLogin databaseLogin = (DatabaseLogin) login;
            databaseLogin.setDelayBetweenConnectionAttempts(100);
            databaseLogin.setQueryRetryAttemptCount(1);
        }
    }
}
