package org.openkoala.koala.mybatis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dayatang.domain.CriteriaQuery;
import org.dayatang.domain.Entity;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.ExampleSettings;
import org.dayatang.domain.JpqlQuery;
import org.dayatang.domain.MapParameters;
import org.dayatang.domain.NamedQuery;
import org.dayatang.domain.SqlQuery;
/**
 * EntityRepository的Mybatis实现版本
 * @author lingen
 *
 */
public class EntityRepositoryMybatis implements EntityRepository{

	@Override
	public <T extends Entity> T save(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends Entity> T get(Class<T> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> T load(Class<T> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> T getByBusinessKeys(Class<T> clazz,
			MapParameters keyValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> List<T> findAll(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> CriteriaQuery createCriteriaQuery(
			Class<T> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> find(CriteriaQuery criteriaQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getSingleResult(CriteriaQuery criteriaQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpqlQuery createJpqlQuery(String jpql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> find(JpqlQuery jpqlQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getSingleResult(JpqlQuery jpqlQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(JpqlQuery jpqlQuery) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NamedQuery createNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> find(NamedQuery namedQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getSingleResult(NamedQuery namedQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(NamedQuery namedQuery) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SqlQuery createSqlQuery(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> find(SqlQuery sqlQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getSingleResult(SqlQuery sqlQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(SqlQuery sqlQuery) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T extends Entity, E extends T> List<T> findByExample(E example,
			ExampleSettings<T> settings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> List<T> findByProperty(Class<T> clazz,
			String propertyName, Object propertyValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> List<T> findByProperties(Class<T> clazz,
			MapParameters properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryStringOfNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	
}
