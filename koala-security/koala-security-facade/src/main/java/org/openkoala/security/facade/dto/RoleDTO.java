package org.openkoala.security.facade.dto;

public class RoleDTO {

	private Long roleId;

	private String roleName;

	private String description;

	private boolean isMaster;

	public RoleDTO() {
	}

	public RoleDTO(Long roleId, String roleName, String description, boolean isMaster) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.description = description;
		this.isMaster = isMaster;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}
}
