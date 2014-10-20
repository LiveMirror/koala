package org.openkoala.gqc.core.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.dbutils.DbUtils;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.gqc.core.DataSourceBeingUsedException;
import org.openkoala.gqc.core.DataSourceIDExistException;
import org.openkoala.gqc.core.SystemDataSourceNotExistException;

/**
 * 查询数据源。
 * 
 * @author xmfang
 *
 */
@Entity
@Table(name = "KG_DATA_SOURCES")
public class DataSource extends AbstractGeneralQueryEntity {

	private static final long serialVersionUID = 7435451055102038439L;

	@Enumerated(EnumType.STRING)
	@Column(name = "DATA_SOURCE_TYPE")
	private DataSourceType dataSourceType;

	@Column(name = "DATA_SOURCE_ID")
	private String dataSourceId;

	@Column(name = "DATA_SOURCE_DESCRIPTION")
	private String dataSourceDescription;

	@Column(name = "CONNECT_URL")
	private String connectUrl;

	@Column(name = "JDBC_DRIVER")
	private String jdbcDriver;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	/**
	 * 根据数据源ID获取
	 * 
	 * @param dataSourceId
	 * @return
	 */
	public static DataSource getByDataSourceId(String dataSourceId) {
		return getRepository().createCriteriaQuery(DataSource.class).eq("dataSourceId", dataSourceId).singleResult();
	}

	/**
	 * 获取系统数据源
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DataSource getSystemDataSource(String dataSourceId)  {
		DataSource result = null;
		Connection conn = null;
		javax.sql.DataSource dataSource = null;
		try {
			dataSource = InstanceFactory.getInstance(javax.sql.DataSource.class, dataSourceId);
		} catch (Exception e) {
			throw new SystemDataSourceNotExistException("该系统数据源不存在！", e);
		}
		try {
			conn = dataSource.getConnection();
			result = new DataSource();
			result.setDataSourceId(dataSourceId);
			result.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
			result.setConnectUrl(conn.getMetaData().getURL());
			result.setUsername(conn.getMetaData().getUserName());
		} catch (Exception e) {
			throw new RuntimeException("获取系统数据源失败！", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException("获取系统数据源失败！", e);
				}
			}
		}
		return result;
	}

	/**
	 * 测试数据源连接
	 * 
	 * @return
	 */
	public boolean testConnection() {
		boolean result = false;
		Connection connection = null;
		if (dataSourceType.equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
			javax.sql.DataSource dataSource = null;
			try {
				dataSource = InstanceFactory.getInstance(javax.sql.DataSource.class, dataSourceId);
			} catch (Exception e) {
				throw new SystemDataSourceNotExistException("系统数据源不存在！", e);
			}
			try {
				connection = dataSource.getConnection();
				if (connection != null) {
					result = true;
					// 系统数据源连接不能手动关闭，事务会把该连接放回到连接池中
					 connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException("获取系统数据源连接失败！", e);
			}
			return result;
		}

		if(!DbUtils.loadDriver(jdbcDriver)){
			throw new RuntimeException("无法加载JDBC驱动");
		}
		try {
			connection = DriverManager.getConnection(connectUrl, username, password);
			if (connection != null) {
				result = true;
			}
		} catch (SQLException e) {
			throw new RuntimeException("获取自定义数据源连接失败！", e);
		} finally {
			DbUtils.closeQuietly(connection);
		}
		return result;
	}

	/**
	 * 获取数据源连接
	 * 
	 * @return
	 */
	public Connection generateConnection() {
		Connection connection = null;
		if (dataSourceType.equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
			javax.sql.DataSource dataSource;
			try {
				dataSource = InstanceFactory.getInstance(javax.sql.DataSource.class, dataSourceId);
			} catch (Exception e) {
				throw new SystemDataSourceNotExistException("系统数据源不存在！", e);
			}
			try {
				connection = dataSource.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException("获取系统数据源连接失败！", e);
			}
		} else {
			DbUtils.loadDriver(jdbcDriver);
			try {
				connection = DriverManager.getConnection(connectUrl, username, password);
			} catch (SQLException e) {
				throw new RuntimeException("获取自定义数据源连接失败！", e);
			}
		}
		return connection;
	}

	public void create()  {
		if (getByDataSourceId(dataSourceId) != null) {
			throw new DataSourceIDExistException("数据源ID已存在");
			//throw new RuntimeException("数据源ID已存在");
		}
		//新增之前要检查一下该数据源是否可以连接，如果不可以则不予以保存
		try{
			if(!testConnection()){throw new RuntimeException("该数据源不可用");}
		}catch(Exception e){
			throw new RuntimeException("该数据源不可用",e);
		}
		if (DataSourceType.SYSTEM_DATA_SOURCE.equals(getDataSourceType())) {
			DataSource sysDS = null;
			sysDS = DataSource.getSystemDataSource(getDataSourceId());
			sysDS.setDataSourceDescription(dataSourceDescription);
			sysDS.save();
		} else {
			setId(null);
			super.save();
		}
	}
	
	public void remove(){
		if (!GeneralQuery.findByDatasource(this).isEmpty()) {
			throw new DataSourceBeingUsedException("数据源已被使用！");
		}
		super.remove();
	}
	

	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(DataSourceType dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDataSourceDescription() {
		return dataSourceDescription;
	}

	public void setDataSourceDescription(String dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public String getConnectUrl() {
		return connectUrl;
	}

	public void setConnectUrl(String connectUrl) {
		if (connectUrl == null) {
			return;
		}
		this.connectUrl = connectUrl.trim();
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connectUrl == null) ? 0 : connectUrl.hashCode());
		result = prime * result + ((dataSourceId == null) ? 0 : dataSourceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DataSource other = (DataSource) obj;
		if (connectUrl == null) {
			if (other.connectUrl != null) {
				return false;
			}
		} else if (!connectUrl.equals(other.connectUrl)) {
			return false;
		}
		if (dataSourceId == null) {
			if (other.dataSourceId != null) {
				return false;
			}
		} else if (!dataSourceId.equals(other.dataSourceId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return dataSourceId;
	}

}
