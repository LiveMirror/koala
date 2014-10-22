package org.openkoala.gqc.facade;

import java.util.List;
import java.util.Map;

import org.dayatang.utils.Page;
import org.openkoala.gqc.facade.dto.DataSourceDTO;
import org.openkoala.koala.commons.InvokeResult;

/**
 * 数据源应用层接口，处理数据源的增删改查
 *
 */

public interface DataSourceFacade {

	/**
	 * 查询数据源
	 * @param id 主键id
	 * @return
	 */
	DataSourceDTO getById(Long id);
	
	/**
	 * 查询数据源
	 * @param dataSourceId 数据源id
	 * @return
	 */
	DataSourceDTO getByDataSourceId(String dataSourceId);
	
	/**
	 * 保存数据源
	 * @param dataSource 待保存的数据源
	 * @return
	 */
	InvokeResult createDataSource(DataSourceDTO dataSource);
	
	/**
	 * 更新数据源
	 * @param dataSource 待更新的数据源
	 */
	InvokeResult updateDataSource(DataSourceDTO dataSource);
	
	/**
	 * 删除数据源
	 * @param id 主键id
	 */
	InvokeResult removeDataSource(Long id);
	
	/**
	 * 批量删除数据源
	 * @param ids 数据源主键数组
	 */
	InvokeResult removeDataSources(Long[] ids);
	
	/**
	 * 查询所有数据源
	 * @return
	 */
	List<DataSourceDTO> findAllDataSource();
	
	/**
	 * 查询所有表
	 * @param id 数据源主键
	 * @return
	 */
	List<String> findAllTable(Long id);
	
	/**
	 * 查询所有列
	 * @param id 数据源主键
	 * @param tableName 表名
	 * @return
	 */
	Map<String, Integer> findAllColumn(Long id, String tableName);
	
	/**
	 * 分页查询数据源
	 * @param dataSource 查询条件
	 * @param currentPage 当前页
	 * @param pageSize 页面大小
	 * @return
	 */
	Page<DataSourceDTO> pageQueryDataSource(DataSourceDTO dataSource, int currentPage, int pageSize);
	
	/**
	 * 检测数据源是否可用，新增数据源时候使用
	 * @param dataSourceDTO
	 * @return
	 */
	InvokeResult checkDataSourceCanConnect(DataSourceDTO dataSourceDTO);
	
	/**
	 * 检测数据源是否可用，修改数据源时候使用
	 * @param id 数据源主键
	 * @return
	 */
	InvokeResult checkDataSourceCanConnect(Long id);
	

}

