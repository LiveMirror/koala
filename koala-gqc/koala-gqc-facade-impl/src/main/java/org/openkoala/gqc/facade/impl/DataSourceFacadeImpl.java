package org.openkoala.gqc.facade.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;







import org.apache.commons.beanutils.BeanUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.facade.DataSourceFacade;
import org.openkoala.gqc.facade.assembler.DataSourceAssembler;
import org.openkoala.gqc.facade.dto.DataSourceDTO;

/**
 * 数据源应用层实现，处理数据源的增删改查
 *
 */

//@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
//@Stateless(name = "DataSourceApplication")
//@Remote
@Named
public class DataSourceFacadeImpl implements DataSourceFacade {

	@Inject
	private DataSourceApplication dataSourceApplication ;
	
	/**
	 * 查询通道
	 */
	private static QueryChannelService queryChannel;
	
	/**
	 * 锁对象
	 */
	private static byte[] lock = new byte[0];

	/**
	 * 获取查询通道实例
	 * @return
	 */
	private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			synchronized (lock) {
				if (queryChannel == null) {
					queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
				}
			}
		}
		return queryChannel;
	}
	
	
	public DataSourceDTO getDataSourceDTOById(Long id) {
		return	DataSourceAssembler.getDTO(dataSourceApplication.getDataSourceById(id));
	}

	public DataSourceDTO getDataSourceDTOByDataSourceId(String dataSourceId) {
		return  DataSourceAssembler.getDTO( dataSourceApplication.getDataSourceByDataSourceId(dataSourceId));
	}

	public String saveDataSource(DataSourceDTO dataSourceDTO) {
		DataSource dataSource = DataSourceAssembler.getEntity(dataSourceDTO);
		return  dataSourceApplication.saveDataSource(dataSource);
		
	}

	public void updateDataSource(DataSourceDTO dataSourceDTO) {
		dataSourceApplication.updateDataSource(DataSourceAssembler.getEntity(dataSourceDTO));
	}

	public void removeDataSource(Long id) {
		dataSourceApplication.removeDataSource(id);
	}

	public void removeDataSources(Long[] ids) {
		dataSourceApplication.removeDataSources(ids);
	}

	public List<DataSourceDTO> findAllDataSource() {
			List<DataSourceDTO> list = new ArrayList<DataSourceDTO>();
			for (DataSource dataSource : dataSourceApplication.findAllDataSource()) {
				list.add(DataSourceAssembler.getDTO(dataSource));
			}
			return list;
	}

	public List<String> findAllTable(Long id) {
		return dataSourceApplication.findAllTable(id);
	}

	public Map<String, Integer> findAllColumn(Long id, String tableName) {
		return dataSourceApplication.findAllColumn(id, tableName);
	}
	
	public Page<DataSourceDTO> pageQueryDataSource(DataSourceDTO queryVo, int currentPage,
			int pageSize) {
		try {
			List<DataSourceDTO> result = new ArrayList<DataSourceDTO>();
			List<Object> conditionVals = new ArrayList<Object>();

			StringBuilder jpql = new StringBuilder(
					" select _dataSource from DataSource _dataSource where 1=1 ");

			if (queryVo.getDataSourceId() != null && !"".equals(queryVo.getDataSourceId())) {
				jpql.append(" and _dataSource.dataSourceId like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDataSourceId()));
			}

			if (queryVo.getDataSourceDescription() != null
					&& !"".equals(queryVo.getDataSourceDescription())) {
				jpql.append(" and _dataSource.dataSourceDescription like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDataSourceDescription()));
			}

			if (queryVo.getConnectUrl() != null && !"".equals(queryVo.getConnectUrl())) {
				jpql.append(" and _dataSource.connectUrl like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getConnectUrl()));
			}

			if (queryVo.getJdbcDriver() != null && !"".equals(queryVo.getJdbcDriver())) {
				jpql.append(" and _dataSource.jdbcDriver like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getJdbcDriver()));
			}

			if (queryVo.getDriverUri() != null && !"".equals(queryVo.getDriverUri())) {
				jpql.append(" and _dataSource.driverUri like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDriverUri()));
			}

			if (queryVo.getUsername() != null && !"".equals(queryVo.getUsername())) {
				jpql.append(" and _dataSource.username like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getUsername()));
			}

			if (queryVo.getPassword() != null && !"".equals(queryVo.getPassword())) {
				jpql.append(" and _dataSource.password like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getPassword()));
			}

			Page<DataSource> pages = getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pageSize).pagedList();
			for (DataSource dataSource : pages.getData()) {
				DataSourceDTO dataSourceDTO = new DataSourceDTO();
				// 将domain转成VO
				try {
					BeanUtils.copyProperties(dataSourceDTO, dataSource);
				} catch (Exception e) {
				}

				dataSourceDTO.setDataSourceTypeDesc(dataSourceDTO.getDataSourceType().getDescription());

				result.add(dataSourceDTO);
			}
			
			return new Page<DataSourceDTO>(pages.getStart(), pages.getResultCount(),
					pages.getPageSize(), result);
		} catch (Exception e) {
			throw new RuntimeException("查询数据源列表失败！", e);
		}
	}
	
	public boolean checkDataSourceCanConnect(DataSource dataSource) {
		return dataSourceApplication.checkDataSourceCanConnect(dataSource);
	}
	
	/**
	 * 测试数据源连接
	 * 
	 * @return
	 */
	public boolean testConnection(Long id) {
		return dataSourceApplication.testConnection(id);
	}
	
	/**
	 * 非系统数据源的连接才需要close
	 * @param dataSource
	 * @param conn
	 * @throws SQLException
	 */
	private void closeConnection(DataSource dataSource, Connection conn){
		dataSourceApplication.checkDataSourceCanConnect(dataSource);
	}
}
