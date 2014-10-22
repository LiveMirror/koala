package org.openkoala.gqc.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.dayatang.utils.Page;
import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.facade.GqcFacade;
import org.openkoala.gqc.facade.dto.GeneralQueryDTO;
import org.openkoala.gqc.facade.impl.assembler.DynamicQueryConditionAssembler;
import org.openkoala.gqc.facade.impl.assembler.FieldDetailAssembler;
import org.openkoala.gqc.facade.impl.assembler.GqcAssembler;
import org.openkoala.gqc.facade.impl.assembler.PreQueryConditionAssembler;
import org.openkoala.koala.commons.InvokeResult;

/**
 * 通用查询器应用层实现，提供增删改查功能
 *
 */

@Named
public class GqcFacadeImpl implements GqcFacade {

	@Inject
	private GqcApplication gqcApplication;

	@Inject
	private DataSourceApplication dsApplication;
	/**
	 * 查询通道
	 */
	private static QueryChannelService queryChannel;

	/**
	 * 获取查询通道实例
	 * 
	 * @return
	 */
	private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
		}
		return queryChannel;
	}

	
	@SuppressWarnings("unchecked")
	public Page<GeneralQueryDTO> getQueriers(int currentPage, int pagesize) {
		StringBuilder jpql = null;
		List<Object> conditionVals = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			return getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals)
					.setPage(currentPage, pagesize).pagedList();
		} catch (Exception e) {
			throw new RuntimeException("查询失败！", e);
		}
	}

	
	@SuppressWarnings("unchecked")
	public Page<GeneralQueryDTO> getQueriersByName(String queryName, int currentPage, int pagesize) {
		StringBuilder jpql = null;
		List<Object> conditionVals = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();

			if (queryName != null && !queryName.isEmpty()) {
				jpql = jpql.append(" where _generalQuery.queryName like ?");
				conditionVals.add("%" + queryName + "%");
			}
			return getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals)
					.setPage(currentPage, pagesize).pagedList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败！", e);
		}
	}

	
	public Page<Map<String, Object>> queryWithQuerier(long querierID, Map<String, Object[]> params, int currentPage,
			int pagesize) {
		GeneralQuery querier = gqcApplication.getQuerier(querierID);
		// 需要将查询条件的值，传递到所属的GeneralQuery
		String startValueTag = "Start@";
		String endValueTag = "End@";
		Set<Entry<String, Object[]>> keyValues = params.entrySet();
		for (Entry<String, Object[]> keyValue : keyValues) {
			String key = keyValue.getKey();
			if (!"page".equals(key) && !"pagesize".equals(key)) {
				DynamicQueryCondition dqc = null;
				if (key.endsWith(startValueTag)) {
					String fieldName = key.replace(startValueTag, "");
					dqc = querier.getDynamicQueryConditionByFieldName(fieldName);
					if (dqc != null) {
						dqc.setStartValue((String) keyValue.getValue()[0]);
					}
					continue;
				}
				if (key.endsWith(endValueTag)) {
					String fieldName = key.replace(endValueTag, "");
					dqc = querier.getDynamicQueryConditionByFieldName(fieldName);
					if (dqc != null) {
						dqc.setEndValue((String) keyValue.getValue()[0]);
					}
					continue;
				}
				dqc = querier.getDynamicQueryConditionByFieldName(key);
				if (dqc != null) {
					dqc.setValue((String) keyValue.getValue()[0]);
				}
			}
		}
		return gqcApplication.pagingQueryWithPage(querier, currentPage, pagesize);
	}

	
	public InvokeResult createQuerier(GeneralQueryDTO dto) {
		/*gqcApplication.saveQuerier(GqcAssembler.toEntity(dto));
		return InvokeResult.success();*/
		try {
			gqcApplication.saveQuerier(GqcAssembler.toEntity(dto));
			return InvokeResult.success();
		} catch (Exception e) {
			return InvokeResult.failure(e.getMessage());
		}
	}

	
	public GeneralQueryDTO getQuerier(Long id) {
		return GqcAssembler.toDTO(gqcApplication.getQuerier(id));
	}

	
	public InvokeResult updateQuerier(GeneralQueryDTO dto) {
		try {
			GeneralQuery realQuerier = gqcApplication.getQuerier(dto.getId());
			//GeneralQuery querier = GqcAssembler.toEntity(dto);
			realQuerier.setQueryName(dto.getQueryName());
			realQuerier.setPreQueryConditions(PreQueryConditionAssembler.toEntityList(dto.getPreQueryConditions()));;
			realQuerier.setDynamicQueryConditions(DynamicQueryConditionAssembler.toEntityList(dto.getDynamicQueryConditions()));
			realQuerier.setFieldDetails(FieldDetailAssembler.toEntityList(dto.getFieldDetails()));
			gqcApplication.updateQuerier(realQuerier);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("修改查询器失败");
		}
	}

	
	public InvokeResult removeQuerier(Long id) {
		try {
			gqcApplication.removeQueier(id);
			return InvokeResult.success();
		} catch (Exception e) {
			return InvokeResult.failure("删除查询器失败");
		}
	}

	
	public InvokeResult removeQueriers(Long[] ids) {
		try {
			gqcApplication.removeQueiers(ids);
			return InvokeResult.success();
		} catch (Exception e) {
			return InvokeResult.failure("删除查询器失败");
		}
	}

	
	public Map<String, Object> getForDisplay(Long id) {
		// 查询出该实体
		GeneralQuery querier = gqcApplication.getQuerier(id);
		// 表中所有列，供查询条件选择
		Map<String, Integer> queryConditionColumns = dsApplication.findAllColumn(id, querier.getTableName());
		// 表中所有列，供显示列选择
		Map<String, Integer> displayColumns = getCloneMap(queryConditionColumns);
		// 把已被选择了的列从列池中去除
		this.removeTableMapLeftDiv(querier, queryConditionColumns);
		this.removeTableMapRightDiv(querier, displayColumns);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("generalQuery", querier);
		dataMap.put("queryConditionColumns", queryConditionColumns);
		dataMap.put("showColumns", displayColumns);
		return dataMap;
	}

	/**
	 * 从条件列池中移除已经在静态/动态条件中的列
	 * 
	 * @param generalQuery
	 * @param tableMapLeftDiv
	 */
	private void removeTableMapLeftDiv(GeneralQuery generalQuery, Map<String, Integer> tableMapLeftDiv) {
		// 把已被选择了的列从列池中去除
		List<PreQueryCondition> list = generalQuery.getPreQueryConditions();
		for (PreQueryCondition bean : list) {
			if (tableMapLeftDiv.containsKey(bean.getFieldName())) {
				// 把列类型赋值给PreQueryCondition
				bean.setFieldType(tableMapLeftDiv.get(bean.getFieldName()));
				tableMapLeftDiv.remove(bean.getFieldName());
			}
		}
		// 把已被选择了的列从列池中去除
		List<DynamicQueryCondition> list2 = generalQuery.getDynamicQueryConditions();
		for (DynamicQueryCondition bean : list2) {
			if (tableMapLeftDiv.containsKey(bean.getFieldName())) {
				// 把列类型赋值给DynamicQueryCondition
				bean.setFieldType(tableMapLeftDiv.get(bean.getFieldName()));
				tableMapLeftDiv.remove(bean.getFieldName());
			}
		}
	}

	/**
	 * 从显示列池中移除已经用作显示的列
	 * 
	 * @param generalQuery
	 * @param tableMapRightDiv
	 */
	private void removeTableMapRightDiv(GeneralQuery generalQuery, Map<String, Integer> tableMapRightDiv) {
		// 把已被选择了的列从列池中去除
		List<FieldDetail> list3 = generalQuery.getFieldDetails();
		for (FieldDetail bean : list3) {
			if (tableMapRightDiv.containsKey(bean.getFieldName())) {
				tableMapRightDiv.remove(bean.getFieldName());
			}
		}
	}

	private static Map<String, Integer> getCloneMap(Map<String, Integer> map) {
		Map<String, Integer> mapClone = new HashMap<String, Integer>();
		for (Entry<String, Integer> e : map.entrySet()) {
			mapClone.put(e.getKey(), e.getValue());
		}
		return mapClone;
	}
}
