package org.openkoala.gqc.application;

import java.util.List;
import java.util.Map;

import org.openkoala.gqc.core.domain.DataSource;

/**
 * 数据源应用层接口，处理数据源的增删改查
 * 
 * @author xmfang
 *
 */
public interface DataSourceApplication {

	/**
	 * 查询数据源
	 * 
	 * @param id
	 *            主键id
	 * @return
	 */
	DataSource getById(Long id);

	/**
	 * 查询数据源
	 * 
	 * @param dataSourceId
	 *            数据源id
	 * @return
	 */
	DataSource getByDataSourceId(String dataSourceId);

	/**
	 * 保存数据源
	 * 
	 * @param dataSource
	 *            待保存的数据源
	 * @return
	 */
	void createDataSource(DataSource dataSource);

	/**
	 * 更新数据源
	 * 
	 * @param dataSource
	 *            待更新的数据源
	 */
	void updateDataSource(DataSource dataSource);

	/**
	 * 删除数据源
	 * 
	 * @param id
	 *            主键id
	 */
	void removeDataSource(Long id);

	/**
	 * 批量删除数据源
	 * 
	 * @param ids
	 *            数据源主键数组
	 */
	public void removeDataSources(Long[] ids);

	/**
	 * 查询所有数据源
	 * 
	 * @return
	 */
	List<DataSource> findAllDataSource();

	boolean checkDataSourceCanConnect(DataSource dataSource);

	/**
	 * 测试数据源连接，列表页面修改数据源时候使用
	 * 
	 * @param id
	 *            数据源主键
	 * @return
	 */
	boolean checkDataSourceCanConnect(Long id);
	
	/**
	 * 
	 * @param id
	 * @return 查找某个查询器下面的tables
	 */
	List<String> findAllTable(Long id);
	
	/**
	 * 
	 * @param id
	 * @param tableName
	 * @return 查找某个table下的所有columns
	 */
	Map<String, Integer> findAllColumn(Long id, String tableName);

}