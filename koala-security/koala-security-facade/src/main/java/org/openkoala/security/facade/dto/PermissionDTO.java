package org.openkoala.security.facade.dto;

public class PermissionDTO {

	private Long permissionId;

	private String userName;

	private String roleName;

	private String permissionName;

	private String description;

	public PermissionDTO() {
	}

	public PermissionDTO(Long permissionId, String permissionName, String description) {
		this.permissionId = permissionId;
		this.permissionName = permissionName;
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
