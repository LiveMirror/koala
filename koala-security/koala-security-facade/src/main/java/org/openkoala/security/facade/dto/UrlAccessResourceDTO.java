package org.openkoala.security.facade.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UrlAccessResourceDTO implements Serializable {

	private static final long serialVersionUID = -2406552978317692278L;

	private Long id;
	
	private int version;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 是否有效
	 */
	private Boolean disabled;

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

	/**
	 * 该URL上所有的角色。 多个以,号分割。
	 * 
	 */
	private String roles;

	/**
	 * 该URL上所有的权限。 多个以,号分割
	 */
	private String permissions;
	
	protected UrlAccessResourceDTO() {}

	public UrlAccessResourceDTO(Long id, String name, boolean disabled, String url, String identifier,
			String description) {
		this.id = id;
		this.name = name;
		this.disabled = disabled;
		this.url = url;
		this.identifier = identifier;
		this.description = description;
	}

	public UrlAccessResourceDTO(Long id, String name, boolean disabled, String url, String identifier) {
		this.id = id;
		this.name = name;
		this.disabled = disabled;
		this.url = url;
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
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

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
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
				.append(url)//
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UrlAccessResourceDTO)) {
			return false;
		}
		UrlAccessResourceDTO that = (UrlAccessResourceDTO) other;
		return new EqualsBuilder()//
				.append(this.getName(), that.getName())//
				.append(this.getUrl(), that.getUrl())//
				.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getId())//
				.append(getName())//
				.append(getIdentifier())//
				.append(getUrl())//
				.append(getDisabled())//
				.build();
	}
	
}
