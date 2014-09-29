
package org.openkoala.gqc.facade;

import java.util.List;
import java.util.Map;

import org.dayatang.utils.Page;
import org.openkoala.gqc.facade.dto.DataSourceDTO;

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
	public DataSourceDTO getDataSourceDTOById(Long id);
	
	/**
	 * 查询数据源
	 * @param dataSourceId 数据源id
	 * @return
	 */
	public DataSourceDTO getDataSourceDTOByDataSourceId(String dataSourceId);
	
	/**
	 * 保存数据源
	 * @param dataSource 待保存的数据源
	 * @return
	 */
	public String saveDataSource(DataSourceDTO dataSource);
	
	/**
	 * 更新数据源
	 * @param dataSource 待更新的数据源
	 */
	public void updateDataSource(DataSourceDTO dataSource);
	
	/**
	 * 删除数据源
	 * @param id 主键id
	 */
	public void removeDataSource(Long id);
	
	/**
	 * 批量删除数据源
	 * @param ids 数据源主键数组
	 */
	public void removeDataSources(Long[] ids);
	
	/**
	 * 查询所有数据源
	 * @return
	 */
	public List<DataSourceDTO> findAllDataSource();
	
	/**
	 * 查询所有表
	 * @param id 数据源主键
	 * @return
	 */
	public List<String> findAllTable(Long id);
	
	/**
	 * 查询所有列
	 * @param id 数据源主键
	 * @param tableName 表名
	 * @return
	 */
	public Map<String, Integer> findAllColumn(Long id, String tableName);
	
	/**
	 * 分页查询数据源
	 * @param dataSource 查询条件
	 * @param currentPage 当前页
	 * @param pageSize 页面大小
	 * @return
	 */
	public Page<DataSourceDTO> pageQueryDataSource(DataSourceDTO dataSource, int currentPage, int pageSize);
	
	/**
	 * 检测数据源是否可用
	 * @param dataSourceDTO
	 * @return
	 */
	public boolean checkDataSourceCanConnect(DataSourceDTO dataSourceDTO);
	
	/**
	 * 测试数据源连接
	 * @param id 数据源主键
	 * @return
	 */
	public boolean testConnection(Long id);
	

}

