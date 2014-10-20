package org.openkoala.gqc.facade.dto;

public enum DataSourceType {

	SYSTEM_DATA_SOURCE("系统数据源"),
	CUSTOM_DATA_SOURCE("自定义数据源");
	
	/**
	 * 描述
	 */
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	private DataSourceType(String description) {
		this.description = description;
	}
	
}
