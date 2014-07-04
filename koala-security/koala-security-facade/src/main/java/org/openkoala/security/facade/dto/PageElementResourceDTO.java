package org.openkoala.security.facade.dto;

import java.io.Serializable;

public class PageElementResourceDTO implements Serializable{
	
	private static final long serialVersionUID = -1461134111548409793L;
	
	private Long id;
	
	private int version;
	
	private String name;
	
	private boolean disabled;
	
	private String identifier;
	
	/**
	 * 页面元素类型。
	 */
	private String pageElementType;
	
	private String description;
	
	public PageElementResourceDTO() {
	}
	
	public PageElementResourceDTO(Long id, int version, String name, boolean disabled, String identifier,
			String description,String pageElementType) {
		this.id = id;
		this.version = version;
		this.name = name;
		this.disabled = disabled;
		this.identifier = identifier;
		this.description = description;
		this.pageElementType = pageElementType;
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPageElementType() {
		return pageElementType;
	}

	public void setPageElementType(String pageElementType) {
		this.pageElementType = pageElementType;
	}
}
