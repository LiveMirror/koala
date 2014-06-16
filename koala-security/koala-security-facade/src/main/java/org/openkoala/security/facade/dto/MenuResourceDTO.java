package org.openkoala.security.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuResourceDTO implements Serializable {

	private static final long serialVersionUID = 3329310376086930435L;

	private Long id;
	
	private String identifier;
	
	private String name;
	
	private String url;
	
	private String icon;
	
	private String description;
	
	private Long parentId;
	
	private boolean isValid;
	
	private List<MenuResourceDTO> children = new ArrayList<MenuResourceDTO>();

	public MenuResourceDTO() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<MenuResourceDTO> getChildren() {
		return children;
	}

	public void setChildren(List<MenuResourceDTO> children) {
		this.children = children;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
}
