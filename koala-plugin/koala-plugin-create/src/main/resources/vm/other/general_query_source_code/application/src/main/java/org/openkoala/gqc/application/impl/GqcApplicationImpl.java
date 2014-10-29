package org.openkoala.gqc.application.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.dayatang.utils.Page;
import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional(value = "transactionManager_gqc")
public class GqcApplicationImpl implements GqcApplication {

	@Override
	public GeneralQuery getQuerier(Long id) {
		return GeneralQuery.get(GeneralQuery.class, id);
	}

	@Override
	public void saveQuerier(GeneralQuery querier) {
		querier.save();
	}

	@Override
	public void updateQuerier(GeneralQuery querier) {
		querier.save();
	}

	@Override
	public void removeQueier(Long id) {
		removeQueiers(new Long[] { id });
	}

	@Override
	public void removeQueiers(Long[] ids) {
		for (Long id : ids)
			getQuerier(id).remove();
	}

	@Override
	public List<Map<String, Object>> pagingQuery(int currentPage, int pagesize, GeneralQuery querier) {
		return querier.pagingQuery(currentPage, pagesize);
	}

	@Override
	public Page<Map<String, Object>> pagingQueryWithPage(GeneralQuery querier, int currentPage, int pagesize) {
		return querier.pagingQueryPage(currentPage, pagesize);
	}

	@Override
	public Page<Map<String, Object>> pagingQuery(GeneralQuery generalQuery, int currentPage, int pagesize) {
		return generalQuery.pagingQueryPage(currentPage, pagesize);
	}

	@Override
	public void saveGeneralQuery(GeneralQuery generalQuery) {
		generalQuery.save();
	}
}
