package org.openkoala.security.facade.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PermissionDTO {

	private Long permissionId;
	
	private int version;

	private String userName;

	private String roleName;

	private String permissionName;

	private String identifier;

	private String description;

	private String url;

	protected PermissionDTO() {}

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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
		return new HashCodeBuilder()//
				.append(permissionName)//
				.append(identifier)//
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PermissionDTO)) {
			return false;
		}
		PermissionDTO that = (PermissionDTO) other;
		return new EqualsBuilder()//
				.append(this.getPermissionName(), that.getPermissionName())//
				.append(this.getIdentifier(), that.getIdentifier())//
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getPermissionId())//
				.append(getPermissionName())//
				.append(getIdentifier())//
				.append(getDescription())//
				.build();
	}
}
