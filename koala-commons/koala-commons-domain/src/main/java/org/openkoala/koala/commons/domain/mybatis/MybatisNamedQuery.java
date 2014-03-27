package org.openkoala.koala.commons.domain.mybatis;

import org.openkoala.koala.commons.domain.MybatisEntityRepository;

import java.util.List;

public class MybatisNamedQuery extends org.openkoala.koala.commons.domain.mybatis.MybatisBaseQuery<org.openkoala.koala.commons.domain.mybatis.MybatisBaseQuery> {
	
	
	private String queryName;

	public MybatisNamedQuery(MybatisEntityRepository repository,String queryName,Class<? extends Object> entityClass) {
		super(repository,entityClass);
		this.queryName = queryName;
		
	}

	public String getNamespaceQueryName(){
		return getEntityClass().getName()+"."+getQueryName();
	}
	public String getQueryName() {
		return queryName;
	}
	
	

	@Override
	public <T> List<T> list() {
		return getRepository().find(this);
	}

	@Override
	public <T> T singleResult() {
		return getRepository().getSingleResult(this);
	}

	@Override
	public int executeUpdate() {
		return getRepository().executeUpdate(this);
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

}
