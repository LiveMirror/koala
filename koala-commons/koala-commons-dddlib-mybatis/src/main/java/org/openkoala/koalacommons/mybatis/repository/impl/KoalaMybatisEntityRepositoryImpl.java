package org.openkoala.koalacommons.mybatis.repository.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dayatang.domain.PositionalParameters;
import org.dayatang.domain.Entity;
import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.QueryParameters;
import org.openkoala.koala.commons.domain.MybatisEntityRepository;
import org.openkoala.koala.commons.domain.mybatis.MybatisNamedQuery;

/**
 * 支持DDDLIB，为其提供Mybatis实现
 * 
 * @author lingen
 * 
 */
public class KoalaMybatisEntityRepositoryImpl implements MybatisEntityRepository {

	private SqlSessionFactory sqlSessionFactory;

	public <T extends Entity> T save(T entity) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			int i = 0;
			if (entity.getId() != null) {
				i = session.update(entity.getClass().getName() + ".update",
						entity);
			}
			if (i == 0) {
				session.insert(entity.getClass().getName() + ".insert", entity);
			}
		} finally {
			session.close();
		}
		return entity;
	}

	public void remove(Entity entity) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.delete(entity.getClass().getName() + ".remove", entity);
		} finally {
			session.close();
		}
	}

	public <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
		T t = get(clazz, id);
		if (t != null)
			return true;
		return false;
	}

	public <T extends Entity> T get(Class<T> clazz, Serializable id) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			T t = (T) session.selectOne(clazz.getName() + ".get", id);
			return t;
		} finally {
			session.close();
		}
	}

	public <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return get(clazz, id);
	}

    @Override
    public <T extends Entity> T getByBusinessKeys(Class<T> clazz, NamedParameters keyValues) {
        return null;
    }

    public <T extends Entity> List<T> findAll(Class<T> clazz) {
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		try {
			lists = session.selectList(clazz.getName() + ".getAll", null);
		} finally {
			session.close();
		}
		return lists;
	}

	public <T> List<T> find(MybatisNamedQuery namedQuery) {
		return findByNamedQueryList(namedQuery.getNamespaceQueryName(), findParameters(namedQuery));
	}

	public <T> T getSingleResult(MybatisNamedQuery namedQuery) {
		return this.findByNamedQuerySingleResult(namedQuery.getNamespaceQueryName(), findParameters(namedQuery));
	}

	public int executeUpdate(MybatisNamedQuery namedQuery) {
		return executeNamedUpdate(namedQuery.getNamespaceQueryName(), findParameters(namedQuery));
	}
	
	private int executeNamedUpdate(String queryName,
			Map<String,Object> paramsMap) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			return session.update(queryName, paramsMap);
		} finally {
			session.close();
		}
	}

	private <T> List<T> findByNamedQueryList(String queryName,
			Map<String,Object> paramsMap) {
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		try {
			lists = session.selectList(queryName, paramsMap);
		} finally {
			session.close();
		}
		return lists;
	}

	private <T extends Object> T findByNamedQuerySingleResult(String queryName,Map<String,Object> paramsMap) {
		SqlSession session = sqlSessionFactory.openSession();
		T result = null;
		try {
			result = session.selectOne(queryName, paramsMap);
		} finally {
			session.close();
		}
		return result;
	}
	
	private Map<String,Object> findParameters(MybatisNamedQuery namedQuery) {
		QueryParameters params = namedQuery.getParameters();
		Map<String, Object> paramMap = null;
		paramMap = new HashMap<String,Object>();
        if (params instanceof PositionalParameters) {
            Object[] paramArray = ((PositionalParameters) params).getParams();
            
            paramMap.put("params", paramArray);
        } else if (params instanceof NamedParameters) {
            paramMap.putAll(((NamedParameters) params).getParams());
        } else {
            throw new UnsupportedOperationException("不支持的参数形式");
        }
        if(namedQuery.getFirstResult()!=0 || namedQuery.getMaxResults()!=0){
        	paramMap.put("firstRow", namedQuery.getFirstResult());
        	paramMap.put("pageSize", namedQuery.getMaxResults());
        }
        
        return paramMap;
    }

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public MybatisNamedQuery createNamedQuery(Class claszz, String queryName) {
		return new MybatisNamedQuery(this,queryName,claszz);
	}

}
