package org.openkoala.koalacommons.mybatis.querychannel.impl;

import java.util.List;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.domain.MybatisEntityRepository;
import org.openkoala.koalacommons.mybatis.MybatisQueryChannelService;
import org.openkoala.koalacommons.mybatis.querychannel.MybatisChannelQuery;
import org.openkoala.koalacommons.mybatis.querychannel.MybatisNamedChannelQuery;

public class KoalaMybatisQueryChannelServiceImpl implements
		MybatisQueryChannelService {

	private MybatisEntityRepository repository;
	
	public MybatisChannelQuery createNamedQuery( Class c,String queryName) {
		return new MybatisNamedChannelQuery(repository,queryName,c);
	}

	public <T> List<T> list(MybatisChannelQuery query) {
		return query.list();
	}

	public <T> Page<T> pagedList(MybatisChannelQuery query) {
		return query.pagedList();
	}

	public <T> T getSingleResult(MybatisChannelQuery query) {
		return (T) query.singleResult();
	}

	public long getResultCount(MybatisChannelQuery query) {
		return query.queryResultCount();
	}

	public MybatisEntityRepository getRepository() {
		return repository;
	}

	public void setRepository(MybatisEntityRepository repository) {
		this.repository = repository;
	}
	

}
