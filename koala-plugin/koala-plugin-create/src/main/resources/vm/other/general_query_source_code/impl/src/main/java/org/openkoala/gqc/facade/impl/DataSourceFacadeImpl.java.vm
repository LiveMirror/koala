package org.openkoala.gqc.facade.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.dayatang.utils.Page;
import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.core.DataSourceBeingUsedException;
import org.openkoala.gqc.core.SystemDataSourceNotExistException;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.facade.DataSourceFacade;
import org.openkoala.gqc.facade.dto.DataSourceDTO;
import org.openkoala.gqc.facade.impl.assembler.DataSourceAssembler;
import org.openkoala.koala.commons.InvokeResult;

/**
 * 数据源应用层实现，处理数据源的增删改查
 *
 */

@Named
public class DataSourceFacadeImpl implements DataSourceFacade {

	@Inject
	private DataSourceApplication dataSourceApplication;

	private static QueryChannelService queryChannel;

	private static byte[] lock = new byte[0];

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

	
	public InvokeResult checkDataSourceCanConnect(DataSourceDTO dataSourceDTO) {
		DataSource ds = DataSourceAssembler.toEntity(dataSourceDTO);
			try{
				if (dataSourceApplication.checkDataSourceCanConnect(ds)) {
					return InvokeResult.success();
				}else{
					return InvokeResult.failure("该数据源无法连接");
				}
			}catch(SystemDataSourceNotExistException e) {
				return InvokeResult.failure(e.getMessage());
			} catch (RuntimeException e) {
				return InvokeResult.failure(e.getMessage());
			}
	
	}

	
	public InvokeResult checkDataSourceCanConnect(Long id) {
		try {
			if (dataSourceApplication.checkDataSourceCanConnect(id)){
				return InvokeResult.success();
			}else{
				return InvokeResult.failure("该数据源无法连接");
			}
		} catch(SystemDataSourceNotExistException e) {
			return InvokeResult.failure(e.getMessage());
		} catch (RuntimeException e) {
			return InvokeResult.failure(e.getMessage());
		}
	}

	public DataSourceDTO getById(Long id) {
		return DataSourceAssembler.toDTO(dataSourceApplication.getById(id));
	}

	public DataSourceDTO getByDataSourceId(String dataSourceId) {
		return DataSourceAssembler.toDTO(dataSourceApplication.getByDataSourceId(dataSourceId));
	}

	public InvokeResult createDataSource(DataSourceDTO dataSourceDTO) {
		DataSource dataSource = DataSourceAssembler.toEntity(dataSourceDTO);
		try {
			dataSourceApplication.createDataSource(dataSource);
			return InvokeResult.success();
		}catch (RuntimeException e) {
			return InvokeResult.failure(e.getMessage());
		}
	}

	public InvokeResult updateDataSource(DataSourceDTO dto) {
		try {
			if(dto.getDataSourceType().toString().equals(DataSourceType.CUSTOM_DATA_SOURCE.toString())){
				dataSourceApplication.updateDataSource(DataSourceAssembler.toEntity(dto));
			}else{
				dataSourceApplication.updateDataSource(dataSourceApplication.getById(dto.getId()));
			}
			return InvokeResult.success();
		} catch (Exception e) {
			return InvokeResult.failure(e.getMessage());
		}
	}

	public InvokeResult removeDataSource(Long id) {
		return this.removeDataSources(new Long[] { id });
	}

	public InvokeResult removeDataSources(Long[] ids) {
		try {
			dataSourceApplication.removeDataSources(ids);
			return InvokeResult.success();
		} catch (DataSourceBeingUsedException e) {
			return InvokeResult.failure("数据源已被使用");
		} catch (Exception e) {
			return InvokeResult.failure("删除数据源失败");
		}
	}

	public List<DataSourceDTO> findAllDataSource() {
		List<DataSourceDTO> list = new ArrayList<DataSourceDTO>();
		for (DataSource dataSource : dataSourceApplication.findAllDataSource()) {
			list.add(DataSourceAssembler.toDTO(dataSource));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public Page<DataSourceDTO> pageQueryDataSource(DataSourceDTO queryVo, int currentPage, int pageSize) {
		try {
			
			List<Object> conditionVals = new ArrayList<Object>();

			StringBuilder jpql = new StringBuilder(" select _dataSource from DataSource _dataSource where 1=1 ");

			if (queryVo.getDataSourceId() != null && !"".equals(queryVo.getDataSourceId())) {
				jpql.append(" and _dataSource.dataSourceId like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDataSourceId()));
			}

			if (queryVo.getDataSourceDescription() != null && !"".equals(queryVo.getDataSourceDescription())) {
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

			Page<DataSource> pages = getQueryChannelService().createJpqlQuery(jpql.toString())
					.setParameters(conditionVals)
					.setPage(currentPage, pageSize)
					.pagedList();
			 List<DataSourceDTO> result = DataSourceAssembler.toDTOs(pages.getData());
			return new Page<DataSourceDTO>(pages.getStart(), pages.getResultCount(), pages.getPageSize(), result);
		} catch (Exception e) {
			throw new RuntimeException("查询数据源列表失败！", e);
		}
	}

	public List<String> findAllTable(Long id) {
		return dataSourceApplication.findAllTable(id);
	}

	public Map<String, Integer> findAllColumn(Long id, String tableName) {
		return dataSourceApplication.findAllColumn(id, tableName);
	}

}
