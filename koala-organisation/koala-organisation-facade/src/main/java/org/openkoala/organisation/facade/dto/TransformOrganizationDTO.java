package org.openkoala.organisation.facade.dto;

import java.io.Serializable;

public class TransformOrganizationDTO implements Serializable {
	
	private static final long serialVersionUID = 7894829542701979243L;

	private Long organizationId;
	
	private String organizationSn;
	
	private String organizationName;
	
	private String organizationDescription;
	
	private boolean principal;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationSn() {
		return organizationSn;
	}

	public void setOrganizationSn(String organizationSn) {
		this.organizationSn = organizationSn;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationDescription() {
		return organizationDescription;
	}

	public void setOrganizationDescription(String organizationDescription) {
		this.organizationDescription = organizationDescription;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
}
