package org.openkoala.security.facade.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoleDTO implements Serializable{

	private static final long serialVersionUID = -8008875711416716934L;

	private Long roleId;
	
	private int version;

	private String roleName;

	private String description;
	
	private String url;
	
	private Set<PermissionDTO> permissionDTOs = new HashSet<PermissionDTO>();

	RoleDTO() {
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
	
	public RoleDTO(Long roleId, String roleName, String description,String url) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.description = description;
		this.url = url;
	}
	
	public void add(PermissionDTO permissionDTO){
		this.permissionDTOs.add(permissionDTO);
	}
	
	public void add(Collection<PermissionDTO> permissionDTOs){
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

	@Override
	public String toString() {
		return "RoleDTO [roleId=" + roleId + ", version=" + version + ", roleName=" + roleName + ", description="
				+ description + ", url=" + url + ", permissionDTOs=" + permissionDTOs + "]";
	}
	
}
