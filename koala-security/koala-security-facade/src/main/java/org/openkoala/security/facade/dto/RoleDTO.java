package org.openkoala.security.facade.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.facade.SecurityAccessFacade;

public class RoleDTO implements Serializable {

	private static final long serialVersionUID = -8008875711416716934L;

	private Long roleId;

	private int version;

	private String roleName;

	private String description;

	private String url;

	private Set<PermissionDTO> permissionDTOs = new HashSet<PermissionDTO>();

	protected RoleDTO() {
	}

	public RoleDTO(Long roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public RoleDTO(Long roleId, String roleName, String description) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.description = description;
	}

	public RoleDTO(Long roleId, String roleName, String description, String url) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.description = description;
		this.url = url;
	}

	public void add(PermissionDTO permissionDTO) {
		this.permissionDTOs.add(permissionDTO);
	}

	public void add(Collection<PermissionDTO> permissionDTOs) {
		this.permissionDTOs.addAll(permissionDTOs);
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public Set<PermissionDTO> getPermissionDTOs() {
		return permissionDTOs;
	}

	public void setPermissionDTOs(Set<PermissionDTO> permissionDTOs) {
		this.permissionDTOs = permissionDTOs;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 因为{@link SecurityAccessFacade}findRolesByMenuOrUrl需要url 所以比领域层多一个url。
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()//
				.append(roleName)//
				.append(url).hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RoleDTO)) {
			return false;
		}
		RoleDTO that = (RoleDTO) other;
		return new EqualsBuilder()//
				.append(this.getRoleName(), that.getRoleName())//
				.append(this.getUrl(), that.getUrl()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getRoleId())//
				.append(getRoleName())//
				.append(getDescription())//
				.append(getUrl())//
				.build();
	}

}
