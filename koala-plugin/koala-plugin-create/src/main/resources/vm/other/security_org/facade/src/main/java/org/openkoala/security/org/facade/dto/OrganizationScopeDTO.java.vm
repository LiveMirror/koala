package org.openkoala.security.org.facade.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class OrganizationScopeDTO implements Serializable{

	private static final long serialVersionUID = 46891448382729505L;

	private Long id;
	
	private int version;
	
	private String name;
	
	private String description;
	
	private Long parentId;
	
	private Set<OrganizationScopeDTO> children = new HashSet<OrganizationScopeDTO>();
	
	public OrganizationScopeDTO() {
	}
	
	public OrganizationScopeDTO(Long id, String name, String description, Long parentId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Set<OrganizationScopeDTO> getChildren() {
		return children;
	}

	public void setChildren(Set<OrganizationScopeDTO> children) {
		this.children = children;
	}
	
}
