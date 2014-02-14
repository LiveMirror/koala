package org.openkoala.openci.dto;

import java.io.Serializable;

public class ProjectDeveloperDto implements Serializable {

	private static final long serialVersionUID = 6130656959923923075L;

	private Long developerId;

	private Long[] roleIds;

	public Long getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

}
