package org.openkoala.security.facade.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class PermissionDTO {

	private Long id;
	
	private int version;

	private String name;
	
	private String userAccount;

	private String identifier;

	private String description;

	private String url;

    private List<MenuResourceDTO> menuResources = new ArrayList<MenuResourceDTO>();

    private List<UrlAccessResourceDTO> urlAccessResources = new ArrayList<UrlAccessResourceDTO>();

    private  List<PageElementResourceDTO> pageElementResources = new ArrayList<PageElementResourceDTO>();

	protected PermissionDTO() {}

	public PermissionDTO(Long id, String name, String identifier, String description) {
		this.id = id;
		this.name = name;
		this.identifier = identifier;
		this.description = description;
	}

	public PermissionDTO(Long id, String name, String identifier, String description, String url) {
		this.id = id;
		this.name = name;
		this.identifier = identifier;
		this.description = description;
		this.url = url;
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

	public void setName(String permissionName) {
		this.name = permissionName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long permissionId) {
		this.id = permissionId;
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

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public List<MenuResourceDTO> getMenuResources() {
        return menuResources;
    }

    public void setMenuResources(List<MenuResourceDTO> menuResources) {
        this.menuResources = menuResources;
    }

    public List<UrlAccessResourceDTO> getUrlAccessResources() {
        return urlAccessResources;
    }

    public void setUrlAccessResources(List<UrlAccessResourceDTO> urlAccessResources) {
        this.urlAccessResources = urlAccessResources;
    }

    public List<PageElementResourceDTO> getPageElementResources() {
        return pageElementResources;
    }

    public void setPageElementResources(List<PageElementResourceDTO> pageElementResources) {
        this.pageElementResources = pageElementResources;
    }

    @Override
	public int hashCode() {
		return new HashCodeBuilder()//
				.append(name)//
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
				.append(this.getName(), that.getName())//
				.append(this.getIdentifier(), that.getIdentifier())//
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getId())//
				.append(getName())//
				.append(getIdentifier())//
				.append(getDescription())//
				.build();
	}
}
