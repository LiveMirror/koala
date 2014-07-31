package org.openkoala.security.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MenuResourceDTO implements Serializable {

	private static final long serialVersionUID = 3329310376086930435L;

	private Long id;
	
	private int version;
	
	private String identifier;
	
	private String name;
	
	private String url;
	
	private String icon;
	
	private String description;
	
	private Long parentId;
	
	private boolean disabled;
	
	private int level;
	
	private boolean checked;
	
	private List<MenuResourceDTO> children = new ArrayList<MenuResourceDTO>();

	protected MenuResourceDTO() {}
	
	public MenuResourceDTO(Long id,String name) {
		this.id = id;
		this.name = name;
	}

	public MenuResourceDTO(Long id, String identifier, String name, String url, String icon, String description, Long parentId, boolean disabled,int level) {
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

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean isChecked) {
		this.checked = isChecked;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()//
				.append(name)//
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MenuResourceDTO)) {
			return false;
		}
		MenuResourceDTO that = (MenuResourceDTO) other;
		return new EqualsBuilder()//
				.append(this.getName(), that.getName())//
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getId())//
				.append(getName())//
				.append(getDescription())//
				.append(getIcon())//
				.append(getIdentifier())//
				.append(getLevel())//
				.append(getUrl())//
				.append(getParentId())//
				.build();//
	}
}
