package org.openkoala.security.facade.dto;

public class PermissionDTO {

	private Long permissionId;

	private String userName;

	private String roleName;

	private String permissionName;

	private String identifier;

	private String description;

	private String url;

	public PermissionDTO() {
	}

	public PermissionDTO(Long permissionId, String permissionName, String identifier, String description) {
		this.permissionId = permissionId;
		this.permissionName = permissionName;
		this.identifier = identifier;
		this.description = description;
	}

	public PermissionDTO(Long permissionId, String permissionName, String identifier, String description, String url) {
		this.permissionId = permissionId;
		this.permissionName = permissionName;
		this.identifier = identifier;
		this.description = description;
		this.url = url;
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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((permissionName == null) ? 0 : permissionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissionDTO other = (PermissionDTO) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (permissionName == null) {
			if (other.permissionName != null)
				return false;
		} else if (!permissionName.equals(other.permissionName))
			return false;
		return true;
	}

}
