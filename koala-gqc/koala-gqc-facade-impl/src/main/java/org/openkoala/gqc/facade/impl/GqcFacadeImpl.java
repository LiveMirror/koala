package org.openkoala.gqc.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.GeneralQueryEntity;
import org.openkoala.gqc.facade.GqcFacade;
import org.openkoala.gqc.facade.assembler.GqcAssembler;
import org.openkoala.gqc.facade.dto.GeneralQueryDTO;

/**
 * 通用查询器应用层实现，提供增删改查功能
 *
 */

@Named
//@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
//@Stateless(name = "GqcApplication")
//@Remote
public class GqcFacadeImpl implements GqcFacade {
	
	@Inject
	private  GqcApplication gqcApplication;
	/**
	 * 查询通道
	 */
	private static QueryChannelService queryChannel;

	/**
	 * 获取查询通道实例
	 * @return
	 */
	private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
		}
		return queryChannel;
	}
	
	public <T extends GeneralQueryEntity> T getEntity(Class<T> clazz, Long id) {
		try {
			return AbstractEntity.get(clazz, id);
		} catch (Exception e) {
			throw new RuntimeException("查询指定查询器失败！", e);
		}
	}
	
	public void saveEntity(GeneralQueryEntity entity) {
		gqcApplication.saveEntity(entity);
	}

	public void updateEntity(GeneralQueryEntity entity) {
		gqcApplication.saveEntity(entity);
	}

	public void removeEntity(GeneralQueryEntity entity) {
		gqcApplication.removeEntity(entity);
	}
	
	public void removeEntity(String ids) {
		gqcApplication.removeEntity(ids);
	}

	public Page<GeneralQuery> pagingQueryGeneralQueries(int currentPage, int pagesize) {
	   	StringBuilder jpql = null;
		List<Object> conditionVals = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			return getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize).pagedList();
		} catch (Exception e) {
			throw new RuntimeException("查询失败！", e);
		}
	}
	
	public GeneralQueryDTO getById(Long id){
			return  GqcAssembler.getDTO(gqcApplication.getById(id));
		
	}
	
	public <T extends GeneralQueryEntity> void removeEntities(Set<T> entities) {
		gqcApplication.removeEntities(entities);
	}

	public Page<GeneralQuery> pagingQueryGeneralQueriesByQueryName(String queryName, int currentPage, int pagesize) {
	   	StringBuilder jpql = null;
		List<Object> conditionVals = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			
			if (queryName != null && !queryName.isEmpty()) {
				jpql = jpql.append(" where _generalQuery.queryName like ?");
				conditionVals.add("%" + queryName + "%");
			}
			return getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize).pagedList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败！", e);
		}
	}

	@Override
	public Page<Map<String, Object>> pagingQuery(GeneralQueryDTO generalQuery, int currentPage, int pagesize) {
		
		return GqcAssembler.getEntity(generalQuery).pagingQueryPage(currentPage, pagesize);
	}

	@Override
	public void saveGeneralQuery(GeneralQueryDTO generalQuery) {
		gqcApplication.saveGeneralQuery(GqcAssembler.getEntity(generalQuery ));
	}
	
}
