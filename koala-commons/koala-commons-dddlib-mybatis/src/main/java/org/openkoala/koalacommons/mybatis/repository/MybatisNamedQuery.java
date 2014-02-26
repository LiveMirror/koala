package org.openkoala.koalacommons.mybatis.repository;

import java.util.List;

import org.dayatang.domain.BaseQuery;
import org.dayatang.domain.EntityRepository;
import org.openkoala.koalacommons.mybatis.MybatisEntityRepository;

public class MybatisNamedQuery extends MybatisBaseQuery<MybatisBaseQuery> {
	
	
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
