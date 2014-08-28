package org.openkoala.gqc.facade.assembler;

import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.facade.dto.DataSourceDTO;

public class DataSourceAssembler {

	public static DataSource  getEntity(DataSourceDTO dto){
		DataSource result = new DataSource();
		result.setConnectUrl(dto.getConnectUrl());
		result.setDataSourceDescription(dto.getDataSourceDescription());
		result.setDataSourceId(dto.getDataSourceId());
		result.setDataSourceType(dto.getDataSourceType());
		result.setId(dto.getId());
		result.setJdbcDriver(dto.getJdbcDriver());
		result.setPassword(dto.getPassword());
		result.setUsername(dto.getUsername());
		result.setVersion(dto.getVersion());
		return result;
	}
	
public static DataSourceDTO getDTO(DataSource dataSource){
		DataSourceDTO result = new DataSourceDTO();
		result.setConnectUrl(dataSource.getConnectUrl());
		result.setDataSourceDescription(dataSource.getDataSourceDescription());
		result.setDataSourceId(dataSource.getDataSourceId());
		result.setDataSourceType(dataSource.getDataSourceType());
		result.setId(dataSource.getId());
		result.setJdbcDriver(dataSource.getJdbcDriver());
		result.setPassword(dataSource.getPassword());
		result.setUsername(dataSource.getUsername());
		result.setVersion(dataSource.getVersion());
		return result;
	}
	
}
