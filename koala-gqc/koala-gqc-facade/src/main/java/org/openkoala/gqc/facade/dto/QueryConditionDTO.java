package org.openkoala.gqc.facade.dto;

import java.io.Serializable;

public abstract class QueryConditionDTO implements Serializable {

	private static final long serialVersionUID = -3244024590117914634L;
	
	private String fieldName;
	
	private QueryOperationDTO queryOperationDTO;
	
	private Integer fieldType;
	
	private DataSourceDTO dataSourceDTO;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public QueryOperationDTO getQueryOperationDTO() {
		return queryOperationDTO;
	}

	public void setQueryOperationDTO(QueryOperationDTO queryOperationDTO) {
		this.queryOperationDTO = queryOperationDTO;
	}

	public Integer getFieldType() {
		return fieldType;
	}

	public void setFieldType(Integer fieldType) {
		this.fieldType = fieldType;
	}

	public DataSourceDTO getDataSourceDTO() {
		return dataSourceDTO;
	}

	public void setDataSourceDTO(DataSourceDTO dataSourceDTO) {
		this.dataSourceDTO = dataSourceDTO;
	}
	
}
