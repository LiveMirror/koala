package org.openkoala.gqc.facade.impl.assembler;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.facade.dto.DataSourceDTO;

public class DataSourceAssembler {

	public static DataSource toEntity(DataSourceDTO dto){
		DataSource result = new DataSource();
		result.setConnectUrl(dto.getConnectUrl());
		result.setDataSourceDescription(dto.getDataSourceDescription());
		result.setDataSourceId(dto.getDataSourceId());
		result.setDataSourceType(DataSourceType.valueOf(dto.getDataSourceType().toString()));
		result.setId(dto.getId());
		result.setJdbcDriver(dto.getJdbcDriver());
		result.setPassword(dto.getPassword());
		result.setUsername(dto.getUsername());
		result.setVersion(dto.getVersion());
		return result;
	}
	
	public static DataSourceDTO toDTO(DataSource dataSource){
		DataSourceDTO result = new DataSourceDTO();
		result.setConnectUrl(dataSource.getConnectUrl());
		result.setDataSourceDescription(dataSource.getDataSourceDescription());
		result.setDataSourceId(dataSource.getDataSourceId());
		result.setDataSourceType(org.openkoala.gqc.facade.dto.DataSourceType.valueOf(dataSource.getDataSourceType().toString()));
		result.setId(dataSource.getId());
		result.setJdbcDriver(dataSource.getJdbcDriver());
		result.setPassword(dataSource.getPassword());
		result.setUsername(dataSource.getUsername());
		result.setVersion(dataSource.getVersion());
		return result;
	}
	
	public static List<DataSourceDTO> toDTOs(List<DataSource> list){
		ArrayList<DataSourceDTO> dtos = new ArrayList<DataSourceDTO>(list.size());
		for(DataSource ds : list)
			dtos.add(toDTO(ds));
		return dtos;
	}
}
