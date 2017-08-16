package com.sap.integration.utils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;

/**
 * Class, which handles database connection, EntityManager and Transaction life cycle(s) for the given persistence unit. You may
 * implement your own database connector, JPA layer and JPA Manager.
 */
public class JpaManager {

    private static final Logger LOG = Logger.getLogger(JpaManager.class);

    private static Map<String, JpaManager> instances = new HashMap<String, JpaManager>();

    private EntityManagerFactory emf = null;

    private EntityManager em = null;

    private String persistenceUnitName = null;

    public static JpaManager getInstance(final String persistenceUnitName) {
        if (instances.containsKey(persistenceUnitName)) {
            return instances.get(persistenceUnitName);
        }
        synchronized (instances) {
            instances.put(persistenceUnitName, new JpaManager(persistenceUnitName));
        }
        return instances.get(persistenceUnitName);
    }

    protected JpaManager(final String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    private EntityManagerFactory getEntityManagerFactory() {
        if (this.emf == null) {
            synchronized (this) {
                this.emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            }
        }
        return this.emf;
    }

    public EntityManager getEntityManager() {
        if (this.em == null) {
            synchronized (this) {
                this.em = getEntityManagerFactory().createEntityManager();
            }
        }
        return this.em;
    }

    private void transactionBegin(EntityTransaction transaction) {
        synchronized (this) {
            transaction.begin();
        }
    }

    public EntityTransaction transactionBegin() {
        return transactionBegin(false);
    }

    public EntityTransaction transactionBegin(boolean startNew) {
        EntityTransaction transaction = getEntityManager().getTransaction();
        if (transaction.isActive()) {
            if (startNew) {
                transactionBegin(transaction);
            }
        } else {
            transactionBegin(transaction);
        }
        return transaction;
    }

    public EntityTransaction transactionCommit() {
        return transactionCommit(true);
    }

    public EntityTransaction transactionCommit(boolean handleException) {
        EntityTransaction transaction = getEntityManager().getTransaction();
        try {
            transaction.commit();
        } catch (RollbackException e) {
            if (handleException) {
                LOG.error("RollbackException " + e.getMessage(), e);
            } else {
                throw e;
            }
        }
        return transaction;
    }

    public EntityTransaction transactionRollback() {
        return transactionRollback(true);
    }

    public EntityTransaction transactionRollback(boolean handleException) {
        EntityTransaction transaction = getEntityManager().getTransaction();
        try {
            transaction.rollback();
        } catch (PersistenceException e) {
            if (handleException) {
                LOG.error("PersistenceException " + e.getMessage(), e);
            } else {
                throw e;
            }
        }
        return transaction;
    }

    public void close() {
        EntityTransaction transaction = getEntityManager().getTransaction();
        while (transaction.isActive()) {
            transactionRollback();
        }
        this.em.close();
        this.emf.close();
    }
}
