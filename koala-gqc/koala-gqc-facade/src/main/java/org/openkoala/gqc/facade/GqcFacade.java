package org.openkoala.gqc.facade;

import java.util.Map;

import org.dayatang.utils.Page;

import org.openkoala.gqc.facade.dto.GeneralQueryDTO;
import org.openkoala.koala.commons.InvokeResult;
/**
 * 
 * 通用查询器应用层接口，提供增删改查功能
 *
 */
public interface GqcFacade {

	/**
	 * 获取Querier实例
	 * @param GeneralQueryDTO
	 * @param id 通用查询主键
	 * @return
	 */
	GeneralQueryDTO getQuerier(Long id);

	
	/**
	 * 获取Querier实例
	 * @param GeneralQueryDTO
	 * @param id 通用查询主键
	 * @return
	 */
	Map<String, Object> getForDisplay(Long id);	
	
	/**
	 * 创建Querier实例
	 * @param GeneralQueryDTO
	 */
	InvokeResult createQuerier(GeneralQueryDTO generalQueryDTO);
	
	/**
	 * 更新Querier实例
	 * @param GeneralQueryDTO
	 */
	InvokeResult updateQuerier(GeneralQueryDTO GeneralQueryDTO);

	/**
	 * 根据GeneralQueryDTO对象，删除Querier实例
	 * @param GeneralQueryDTO.
	 */
	InvokeResult removeQuerier(Long id);
	
	/**
	 * 根据id，删除Querier实例
	 * @param GeneralQueryDTO.
	 */
	InvokeResult removeQueriers(Long[] ids);
	
	
	/**
	 * 分页查询Querier实例
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<GeneralQueryDTO> getQueriers(int currentPage, int pagesize);
	
	/**
	 * 分页查询Querier实例
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<GeneralQueryDTO> getQueriersByName(String queryName, int currentPage, int pagesize);
	
	
	/**
	 * 通过Querier进行具体的查询
	 */
	Page<Map<String, Object>> queryWithQuerier(long querierID, Map<String, Object[]> params, int currentPage, int pagesize);

}
