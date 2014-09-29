package org.openkoala.koalacommons.mybatis.querychannel;

import java.util.List;

import org.dayatang.utils.Assert;
import org.dayatang.utils.Page;
import org.openkoala.koala.commons.domain.MybatisEntityRepository;
import org.openkoala.koala.commons.domain.mybatis.MybatisNamedQuery;

public class MybatisNamedChannelQuery extends
		MybatisChannelQuery<MybatisNamedChannelQuery> {

	private MybatisNamedQuery query;

	public MybatisNamedChannelQuery(MybatisEntityRepository repository,
			String queryName, Class c) {
		super(repository);
		Assert.notBlank(queryName, "Query name must be set!");
		query = new MybatisNamedQuery(repository, queryName, c);
		setQuery(query);
	}

	@Override
	public <T> List<T> list() {
		return query.list();
	}

	@Override
	public <T> Page<T> pagedList() {
		  return new Page<T>(query.getFirstResult(), queryResultCount(),
					query.getMaxResults(), (List<T>) query.list());
	}

	@Override
	public <T> T singleResult() {
		return (T) query.singleResult();
	}

}
