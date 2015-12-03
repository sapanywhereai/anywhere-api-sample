package com.sap.integration.utils.integrationdb.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

/**
 * Abstract base class for a persistence unit entities. Covers the connection initialization, simplifies access to the
 * EntityManager, implements some helper methods etc. You may implement your own database connector and JPA layer.
 */
public abstract class AbstractJpaLayer {

    private static final Logger LOG = Logger.getLogger(JpaLayer.class);

    public void persist() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
            em.persist(this);
            tx.commit();
        } else {
            em.persist(this);
        }
    }

    public void merge() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
            em.merge(this);
            tx.commit();
        } else {
            em.merge(this);
        }
    }

    public static <T extends AbstractJpaLayer> List<T> getAll(final Class<T> clazz) {
        return find(clazz, "select e from " + clazz.getSimpleName() + " e");
    }

    public static <T extends AbstractJpaLayer> List<T> find(final Class<T> clazz, final String select, Object... params) {
        return getQuery(clazz, select, params).getResultList();
    }

    public static <T extends AbstractJpaLayer> T findFirst(final Class<T> clazz, final String select, Object... params) {
        TypedQuery<T> q = getQuery(clazz, select, params);
        q.setMaxResults(1);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            LOG.info("No record for " + clazz.getSimpleName() + " with entered query " + select + " was found.");
            return null;
        }
    }

    private static <T extends AbstractJpaLayer> TypedQuery<T> getQuery(Class<T> clazz, String select, Object... params) {
        TypedQuery<T> q = getStaticEntityManager(clazz).createQuery(select, clazz);
        int paramIndex = 1;
        for (Object param : params) {
            q.setParameter(paramIndex++, param);
        }
        return q;
    }

    private EntityManager getEntityManager() {
        String persistenceUnitName = getPersistenceUnitName();
        return getEntityManager(persistenceUnitName);
    }

    private static <T extends AbstractJpaLayer> EntityManager getStaticEntityManager(final Class<T> clazz) {
        String persistenceUnitName = getPersistenceUnitName(clazz);
        return getEntityManager(persistenceUnitName);
    }

    private static Map<String, EntityManager> entityManagers = new HashMap<String, EntityManager>();

    private static EntityManager getEntityManager(final String persistenceUnitName) {
        if (entityManagers.containsKey(persistenceUnitName)) {
            return entityManagers.get(persistenceUnitName);
        }
        synchronized (entityManagers) {
            entityManagers.put(persistenceUnitName, JpaManager.getInstance(persistenceUnitName).getEntityManager());
        }
        return entityManagers.get(persistenceUnitName);
    }

    private static <T extends AbstractJpaLayer> String getPersistenceUnitName(final Class<T> clazz) {
        String persistenceUnitName = null;
        try {
            T instance = findMyDirectDescendantFor(clazz).newInstance();
            persistenceUnitName = instance.getPersistenceUnitName();
        } catch (InstantiationException e) {
            throw new RuntimeException("Persistence Unit Name not found", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Persistence Unit Name not found", e);
        }
        if (persistenceUnitName == null) {
            throw new RuntimeException("Persistence Unit Name not found");
        }
        return persistenceUnitName;
    }

    @SuppressWarnings("unchecked")
    private static <T extends AbstractJpaLayer> Class<T> findMyDirectDescendantFor(final Class<T> clazz) {
        Class<?> descendant = clazz;
        while (descendant.getSuperclass() != null && !AbstractJpaLayer.class.equals(descendant.getSuperclass())) {
            descendant = descendant.getSuperclass();
        }
        return (Class<T>) descendant;
    }

    abstract protected String getPersistenceUnitName();
}
