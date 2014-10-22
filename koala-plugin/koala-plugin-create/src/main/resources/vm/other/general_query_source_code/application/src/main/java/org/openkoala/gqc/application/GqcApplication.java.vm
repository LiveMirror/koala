package org.openkoala.gqc.application;

import java.util.List;
import java.util.Map;

import org.dayatang.utils.Page;
import org.openkoala.gqc.core.domain.GeneralQuery;

/**
 * 
 * 通用查询器应用层接口，提供增删改查功能
 *
 */
public interface GqcApplication {

	/**
	 * 获取通用查询实例
	 * @param clazz
	 * @param id 通用查询主键
	 * @return
	 */
	GeneralQuery getQuerier(Long id);

    /**
	 * 更新用查询实例
	 * @param querier
	 */
	void updateQuerier(GeneralQuery querier);

	/**
	 * 删除用查询实例
	 * @param querier
	 */
	void removeQueier(Long id);
	
	/**
	 * 根据ID删除用查询实例
	 * @param querier
	 */
	void removeQueiers(Long[] ids);
	
	/**
	 * 用查询器查询数据
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	List<Map<String, Object>> pagingQuery(int currentPage, int pagesize, GeneralQuery querier);
	
	/**
	 * 用查询器查询数据
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<Map<String, Object>> pagingQueryWithPage(GeneralQuery querier, int currentPage, int pagesize);

	//Page<GeneralQuery> pagingQueryGeneralQueriesByQueryName(String queryName, int currentPage, int pagesize);
	
	Page<Map<String, Object>> pagingQuery(GeneralQuery generalQuery, int currentPage, int pagesize);

	void saveGeneralQuery(GeneralQuery generalQuery);

	void saveQuerier(GeneralQuery entity);
}