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
	
	private boolean disabled;
	
	private int level;
	
	private List<MenuResourceDTO> children = new ArrayList<MenuResourceDTO>();

	public MenuResourceDTO() {
	}
	
	public MenuResourceDTO(Long id, String identifier, String name, String url, String icon, String description,
			Long parentId, boolean disabled,int level) {
		this.id = id;
		this.identifier = identifier;
		this.name = name;
		this.url = url;
		this.icon = icon;
		this.description = description;
		this.parentId = parentId;
		this.disabled = disabled;
		this.level = level;
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean isValid) {
		this.disabled = isValid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "MenuResourceDTO [id=" + id + ", identifier=" + identifier + ", name=" + name + ", url=" + url
				+ ", icon=" + icon + ", description=" + description + ", parentId=" + parentId + ", disabled="
				+ disabled + ", children=" + children + "]";
	}
}
