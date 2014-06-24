package org.openkoala.security.facade.dto;

import java.io.Serializable;

public class UrlAccessResourceDTO implements Serializable {

	private static final long serialVersionUID = -2406552978317692278L;

	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 是否有效
	 */
	private boolean disabled = false;

	/**
	 * URL 路径
	 */
	private String url;

	/**
	 * 标识
	 */
	private String identifier;

	/**
	 * 描述
	 */
	private String description;

	public UrlAccessResourceDTO() {
	}

	public UrlAccessResourceDTO(Long id, String name, boolean disabled, String url, String identifier,
			String description) {
		this.id = id;
		this.name = name;
		this.disabled = disabled;
		this.url = url;
		this.identifier = identifier;
		this.description = description;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
