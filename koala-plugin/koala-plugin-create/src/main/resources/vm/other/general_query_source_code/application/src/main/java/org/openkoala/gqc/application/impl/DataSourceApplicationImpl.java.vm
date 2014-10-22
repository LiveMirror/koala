package org.openkoala.gqc.application.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.infra.util.DatabaseUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源应用层实现，处理数据源的增删改查
 * 
 * @author xmfang
 *
 */
@Named
@Transactional(value = "transactionManager_gqc")
public class DataSourceApplicationImpl implements DataSourceApplication {

	public DataSource getById(Long id) {
		try {
			return DataSource.get(DataSource.class, id);
		} catch (Exception e) {
			throw new RuntimeException("查询数据源失败！", e);
		}
	}

	public DataSource getByDataSourceId(String dataSourceId) {
		try {
			return DataSource.getByDataSourceId(dataSourceId);
		} catch (Exception e) {
			throw new RuntimeException("查询指定数据源失败！", e);
		}
	}

	public void createDataSource(DataSource dataSource) {
		dataSource.create();
	}

	public void updateDataSource(DataSource dataSource) {
		if (checkDataSourceCanConnect(dataSource)) {
			dataSource.save();
		} else {
			throw new RuntimeException("数据源无法连接，更新失败");
		}
	}

	public void removeDataSource(Long id) {
		removeDataSources(new Long[] { id });
	}

	public void removeDataSources(Long[] ids) {
		for (long id : ids) {
			DataSource.load(DataSource.class, id).remove();
		}
	}

	public List<DataSource> findAllDataSource() {
		try {
			return DataSource.findAll(DataSource.class);
		} catch (Exception e) {
			throw new RuntimeException("查询数据源列表失败！", e);
		}
	}

	/**
	 * 新增数据源时候的测试功能
	 */
	
	public boolean checkDataSourceCanConnect(DataSource dataSource) {
		return dataSource.testConnection();
	}

	/**
	 * 
	 * 列表页面修改数据源时候的测试功能
	 * 
	 * @return
	 */
	
	public boolean checkDataSourceCanConnect(Long id) {
		return getById(id).testConnection();
	}

	
	public List<String> findAllTable(Long id) {
		DataSource dataSource = null;
		Connection conn = null;
		try {
			dataSource = DataSource.get(DataSource.class, id);
			conn = dataSource.generateConnection();
			List<String> tableList = DatabaseUtils.getTables(conn);
			return tableList;
		} catch (Exception e) {
			throw new RuntimeException("查询所有表失败", e);
		} finally {
			if (conn != null) {
				try {
					closeConnection(dataSource, conn);
				} catch (SQLException e) {
					throw new RuntimeException("关闭自定义数据源连接失败", e);
				}
			}
		}
	}

	
	public Map<String, Integer> findAllColumn(Long id, String tableName) {
		DataSource dataSource = null;
		Connection conn = null;
		try {
			dataSource = getById(id);
			conn = dataSource.generateConnection();
			Map<String, Integer> tableMap = DatabaseUtils.getColumns(conn, tableName);
			return tableMap;
		} catch (Exception e) {
			throw new RuntimeException("查询所有列失败", e);
		} finally {
			if (conn != null) {
				try {
					closeConnection(dataSource, conn);
				} catch (SQLException e) {
					throw new RuntimeException("关闭自定义数据源连接失败", e);
				}
			}
		}
	}

	/**
	 * 非系统数据源的连接才需要close
	 */
	private void closeConnection(DataSource dataSource, Connection conn) throws SQLException {
		if (!DataSourceType.SYSTEM_DATA_SOURCE.equals(dataSource.getDataSourceType())) {
			conn.close();
		}
	}

}
