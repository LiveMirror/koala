package org.openkoala.openci.dto;

import org.openkoala.openci.core.ToolType;


public class ToolConfigurationDto {

	private Long id;
	
	private ToolType toolType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ToolType getToolType() {
		return toolType;
	}

	public void setToolType(ToolType toolType) {
		this.toolType = toolType;
	}
	
}
