package org.openkoala.openci.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.core.JiraConfiguration;
import org.openkoala.openci.core.SonarConfiguration;
import org.openkoala.openci.core.SvnConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.core.ToolType;
import org.openkoala.openci.core.TracConfiguration;

public class ToolConfigurationDto {

	private Long id;
	
	private String name;

	private String serviceUrl;

	private String username;

	private String password;

	private ToolType toolType;
	
	public ToolConfiguration toToolConfiguration() {
//		ToolConfiguration toolConfiguration = new ToolConfiguration(name, serviceUrl, username, password, toolType);
//		toolConfiguration.setId(id);
//		toolConfiguration.setVersion(version);
//		return toolConfiguration;
		return null;
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
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

	public ToolType getToolType() {
		return toolType;
	}

	public void setToolType(ToolType toolType) {
		this.toolType = toolType;
	}

	private static ToolConfigurationDto toolConfigurationToDto(ToolConfiguration toolConfiguration) {
		ToolConfigurationDto toolConfigurationDto = new ToolConfigurationDto();
		toolConfigurationDto.setId(toolConfiguration.getId());
		toolConfigurationDto.setName(toolConfiguration.getName());
		toolConfigurationDto.setServiceUrl(toolConfiguration.getServiceUrl());
		toolConfigurationDto.setUsername(toolConfiguration.getUsername());
		toolConfigurationDto.setPassword(toolConfiguration.getPassword());
		toolConfigurationDto.setToolType(getToolType(toolConfiguration));
		return toolConfigurationDto;
	}
	
	private static ToolType getToolType(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof SvnConfiguration) {
			return ToolType.SVN;
		}
		if (toolConfiguration instanceof GitConfiguration) {
			return ToolType.GIT;
		}
		if (toolConfiguration instanceof JenkinsConfiguration) {
			return ToolType.JENKINS;
		}
		if (toolConfiguration instanceof SonarConfiguration) {
			return ToolType.SONAR;
		}
		if (toolConfiguration instanceof JiraConfiguration) {
			return ToolType.JIRA;
		}
		if (toolConfiguration instanceof TracConfiguration) {
			return ToolType.TRAC;
		}
		return null;
	}

	public static List<ToolConfigurationDto> transform(List<ToolConfiguration> toolConfigurations) {
		List<ToolConfigurationDto> toolConfigurationDtos = new ArrayList<ToolConfigurationDto>();
		if (toolConfigurations != null && toolConfigurations.size() > 0) {
			for (ToolConfiguration each : toolConfigurations) {
				toolConfigurationDtos.add(toolConfigurationToDto(each));
			}
		}
		return toolConfigurationDtos;
	}

}
