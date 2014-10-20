package org.openkoala.gqc.core.domain.utils;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.TIMESTAMP;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.IocException;
import org.dayatang.domain.IocInstanceNotFoundException;
import org.openkoala.gqc.core.SystemDataSourceNotExistException;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;

/**
 * 抽象查询器
 * 
 * @author xmfang
 *
 */
public abstract class Querier {

	/**
	 * 查询SQL语句
	 */
	private SqlStatmentMode querySql;

	/**
	 * 数据源
	 */
	private DataSource dataSource;

	public SqlStatmentMode getQuerySql() {
		return querySql;
	}

	public void setQuerySql(SqlStatmentMode querySql) {
		this.querySql = querySql;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Querier() {
	}

	Querier(SqlStatmentMode querySql, DataSource dataSource) {
		this.querySql = querySql;
		this.dataSource = dataSource;
	}

	/**
	 * 生成查询SQL语句
	 * 
	 * @return
	 */
	public abstract SqlStatmentMode generateQuerySql();

	/**
	 * 查询
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> query() {
		List<Map<String, Object>> results = null;
		Connection connection = null;
		SqlStatmentMode sqlStatment = generateQuerySql();

		try {
			connection = getConnection();
			QueryRunner queryRunner = new QueryRunner();
			List<Object> parameters = sqlStatment.getValues();
			ResultSetHandler<List<Map<String, Object>>> resultSetHandler = new FormatDateResultSetHandler();

			if (parameters.isEmpty()) {
				results = (List<Map<String, Object>>) queryRunner.query(connection, sqlStatment.getStatment(),
						resultSetHandler);
			} else {
				results = (List<Map<String, Object>>) queryRunner.query(connection, sqlStatment.getStatment(),
						resultSetHandler, parameters.toArray());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}

		return results;
	}

	final Connection getConnection() throws SQLException {
		Connection result = null;

		if (dataSource.getDataSourceType().equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
			try {
				javax.sql.DataSource systemDataSource = InstanceFactory.getInstance(javax.sql.DataSource.class,
						dataSource.getDataSourceId());
				result = systemDataSource.getConnection();
			} catch (IocException exception) {
				if (exception instanceof IocInstanceNotFoundException) {
					throw new SystemDataSourceNotExistException("系统数据源不存在！", exception);
				}
			}
		} else {
			DbUtils.loadDriver(dataSource.getJdbcDriver());
			result = DriverManager.getConnection(dataSource.getConnectUrl(), dataSource.getUsername(),
					dataSource.getPassword());
		}

		return result;
	}

	final void closeConnection(Connection connection) {
		//if (dataSource.getDataSourceType().equals(DataSourceType.CUSTOM_DATA_SOURCE)) {
			DbUtils.closeQuietly(connection);
		//}
	}

	private class FormatDateResultSetHandler implements ResultSetHandler<List<Map<String, Object>>> {
		public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				ResultSetMetaData resultSetMetaData = rs.getMetaData();
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
					int columnIndex = i + 1;
					if (rs.getObject(columnIndex) instanceof java.sql.Date) {
						if (resultSetMetaData.getColumnTypeName(columnIndex).equals("YEAR")) {
							map.put(resultSetMetaData.getColumnName(columnIndex),
									new SimpleDateFormat("yyyy").format(rs.getDate(columnIndex)));
						} else {
							map.put(resultSetMetaData.getColumnName(columnIndex),
									new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate(columnIndex)));
						}
					} else if (rs.getObject(columnIndex) instanceof Timestamp) {
						map.put(resultSetMetaData.getColumnName(columnIndex), new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp(columnIndex)));
					} else if (rs.getObject(columnIndex) instanceof TIMESTAMP) {
						TIMESTAMP ts = (TIMESTAMP) rs.getObject(columnIndex);
						map.put(resultSetMetaData.getColumnName(columnIndex), new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(ts.dateValue()));
					} else if (rs.getObject(columnIndex) instanceof Clob) {
						map.put(resultSetMetaData.getColumnName(columnIndex),
								"Clob data is not support display in the list!");
					} else {
						map.put(resultSetMetaData.getColumnName(columnIndex), rs.getObject(columnIndex));
					}
				}
				result.add(map);
			}
			return result;
		}
	}
}
