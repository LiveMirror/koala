package org.openkoala.koala.commons.domain.jpa;

import org.dayatang.domain.*;
import org.dayatang.persistence.jpa.NamedQueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class KoalaEntityRepositoryJpa implements
        EntityRepository {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(KoalaEntityRepositoryJpa.class);


    public String getEntityManagerName() {
        return entityManagerName;
    }

    public void setEntityManagerName(String entityManagerName) {
        this.entityManagerName = entityManagerName;
    }

    private String entityManagerName;


    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        if(entityManager==null){
            return InstanceFactory.getInstance(EntityManager.class, entityManagerName);
        }
        return entityManager;
    }

    private NamedQueryParser namedQueryParser;

    public KoalaEntityRepositoryJpa() {
    }

    public NamedQueryParser getNamedQueryParser() {
        if (namedQueryParser == null) {
            namedQueryParser = InstanceFactory
                    .getInstance(NamedQueryParser.class);
        }
        return namedQueryParser;
    }

    public void setNamedQueryParser(NamedQueryParser namedQueryParser) {
        this.namedQueryParser = namedQueryParser;
    }

    @Override
    public <T extends Entity> T save(T entity) {
        if (existed(entity)==false || entity.notExisted()) {

            getEntityManager().persist(entity);
            LOGGER.info("create a entity: " + entity.getClass() + "/"
                    + entity.getId() + ".");
            return entity;
        }else{
            entity = getEntityManager().merge(entity);
            LOGGER.info("update a entity: " + entity.getClass() + "/"
                    + entity.getId() + ".");
        }
        return entity;
    }

    public boolean existed(Entity entity) {
        Object id = entity.getId();
        if (id == null) {
            return false;
        }
        if (id instanceof Number && ((Number)id).intValue() == 0) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.dayatang.domain.EntityRepository#remove(org.dayatang.domain.Entity)
     */
    @Override
    public void remove(Entity entity) {
        getEntityManager().remove(get(entity.getClass(), entity.getId()));
        LOGGER.info("remove a entity: " + entity.getClass() + "/"
                + entity.getId() + ".");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.dayatang.domain.EntityRepository#exists(java.io.Serializable)
     */
    @Override
    public <T extends Entity> boolean exists(final Class<T> clazz,
                                             final Serializable id) {
        T entity = getEntityManager().find(clazz, id);
        return entity != null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.dayatang.domain.EntityRepository#get(java.io.Serializable)
     */
    @Override
    public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
        return getEntityManager().find(clazz, id);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.dayatang.domain.EntityRepository#load(java.io.Serializable)
     */
    @Override
    public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
        return getEntityManager().getReference(clazz, id);
    }

    @Override
    public <T extends Entity> T getUnmodified(final Class<T> clazz,
                                              final T entity) {
        getEntityManager().detach(entity);
        return get(clazz, entity.getId());
    }

    @Override
    public <T extends Entity> T getByBusinessKeys(Class<T> clazz,
                                                  NamedParameters keyValues) {
        List<T> results = findByProperties(clazz, keyValues);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public <T extends Entity> List<T> findAll(final Class<T> clazz) {
        String queryString = "select o from " + clazz.getName() + " as o";
        return getEntityManager().createQuery(queryString).getResultList();
    }

    @Override
    public <T extends Entity> CriteriaQuery createCriteriaQuery(
            Class<T> entityClass) {
        return new CriteriaQuery(this, entityClass);
    }

    @Override
    public <T> List<T> find(CriteriaQuery dddQuery) {
        Query query = getEntityManager().createQuery(dddQuery.getQueryString());
        Map<String, Object> params = dddQuery.getParameters().getParams();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setFirstResult(dddQuery.getFirstResult());
        if (dddQuery.getMaxResults() > 0) {
            query.setMaxResults(dddQuery.getMaxResults());
        }
        return query.getResultList();
    }

    @Override
    public <T> T getSingleResult(CriteriaQuery dddQuery) {
        List<T> results = find(dddQuery);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public JpqlQuery createJpqlQuery(String jpql) {
        return new JpqlQuery(this, jpql);
    }

    @Override
    public <T> List<T> find(JpqlQuery jpqlQuery) {
        return getQuery(jpqlQuery).getResultList();
    }

    @Override
    public <T> T getSingleResult(JpqlQuery jpqlQuery) {
        List<T> results = getQuery(jpqlQuery).getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public int executeUpdate(JpqlQuery jpqlQuery) {
        return getQuery(jpqlQuery).executeUpdate();

    }

    private Query getQuery(JpqlQuery jpqlQuery) {
        Query query = getEntityManager().createQuery(jpqlQuery.getJpql());
        processQuery(query, jpqlQuery);
        return query;
    }

    @Override
    public NamedQuery createNamedQuery(String queryName) {
        return new NamedQuery(this, queryName);
    }

    @Override
    public <T> List<T> find(NamedQuery namedQuery) {
        return getQuery(namedQuery).getResultList();
    }

    @Override
    public <T> T getSingleResult(NamedQuery namedQuery) {
		try {
			return (T) getQuery(namedQuery).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
    }

    @Override
    public int executeUpdate(NamedQuery namedQuery) {
        return getQuery(namedQuery).executeUpdate();
    }

    private Query getQuery(NamedQuery namedQuery) {
        Query query = getEntityManager().createNamedQuery(
                namedQuery.getQueryName());
        processQuery(query, namedQuery);
        return query;
    }

    @Override
    public SqlQuery createSqlQuery(String sql) {
        return new SqlQuery(this, sql);
    }

    @Override
    public <T> List<T> find(SqlQuery sqlQuery) {
        return getQuery(sqlQuery).getResultList();
    }

    @Override
    public <T> T getSingleResult(SqlQuery sqlQuery) {
        return (T) getQuery(sqlQuery).getSingleResult();
    }

    @Override
    public int executeUpdate(SqlQuery sqlQuery) {
        return getQuery(sqlQuery).executeUpdate();
    }

    private Query getQuery(SqlQuery sqlQuery) {
        Query query;
        if (sqlQuery.getResultEntityClass() == null) {
            query = getEntityManager().createNativeQuery(sqlQuery.getSql());
        } else {
            query = getEntityManager().createNativeQuery(sqlQuery.getSql(),
                    sqlQuery.getResultEntityClass());
        }
        processQuery(query, sqlQuery);
        Class resultEntityClass = sqlQuery.getResultEntityClass();
        return query;
    }

    @Override
    public <T extends Entity, E extends T> List<T> findByExample(
            final E example, final ExampleSettings<T> settings) {
        throw new RuntimeException("not implemented yet!");
    }

    @Override
    public <T extends Entity> List<T> findByProperty(Class<T> clazz,
                                                     String propertyName, Object propertyValue) {
        return find(new CriteriaQuery(this, clazz).eq(propertyName,
                propertyValue));
    }

    @Override
    public <T extends Entity> List<T> findByProperties(Class<T> clazz,
                                                       NamedParameters properties) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(this, clazz);
        for (Map.Entry<String, Object> each : properties.getParams().entrySet()) {
            criteriaQuery = criteriaQuery.eq(each.getKey(), each.getValue());
        }
        return find(criteriaQuery);
    }

    @Override
    public String getQueryStringOfNamedQuery(String queryName) {
        return getNamedQueryParser().getQueryStringOfNamedQuery(queryName);
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public void refresh(Entity entity) {
        getEntityManager().refresh(entity);
    }

    @Override
    public void clear() {
        getEntityManager().clear();
    }

    private void processQuery(Query query, BaseQuery originQuery) {
        fillParameters(query, originQuery.getParameters());
        query.setFirstResult(originQuery.getFirstResult());
        if (originQuery.getMaxResults() > 0) {
            query.setMaxResults(originQuery.getMaxResults());
        }
    }

    private void fillParameters(Query query, QueryParameters params) {
        if (params == null) {
            return;
        }
        if (params instanceof PositionalParameters) {
            fillParameters(query, (PositionalParameters) params);
        } else if (params instanceof NamedParameters) {
            fillParameters(query, (NamedParameters) params);
        } else {
            throw new UnsupportedOperationException("不支持的参数形式");
        }
    }

    private void fillParameters(Query query, PositionalParameters params) {
        Object[] paramArray = params.getParams();
        for (int i = 0; i < paramArray.length; i++) {
            query = query.setParameter(i + 1, paramArray[i]);
        }
    }

    private void fillParameters(Query query, NamedParameters params) {
        for (Map.Entry<String, Object> each : params.getParams().entrySet()) {
            query = query.setParameter(each.getKey(), each.getValue());
        }
    }
}
