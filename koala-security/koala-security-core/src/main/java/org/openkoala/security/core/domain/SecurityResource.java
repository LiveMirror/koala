package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "KS_SECURITYRESOURCES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({ 
	@NamedQuery(name = "SecurityResource.findAllByType", query = "SELECT _securityResource  FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :_securityResourceType  AND _securityResource.disabled = :disabled")
})
public abstract class SecurityResource extends SecurityAbstractEntity {

	private static final long serialVersionUID = 6064565786784560656L;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 是否有效
	 */
	@Column(name = "DISABLED")
	private boolean disabled = false;

	@Column(name = "URL")
	private String url;

	@Column(name = "IDENTIFIER")
	private String identifier;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 查询的时候禁止懒加载。
	 */
	@ManyToMany(mappedBy = "securityResources", fetch = FetchType.EAGER)
	private Set<Authority> authorities = new HashSet<Authority>();

	SecurityResource() {
	}

	public SecurityResource(String name) {
		this.name = name;
	}

	public abstract void update();

	public void disable() {
		disabled = true;
	}

	public void enable() {
		disabled = false;
	}

	/**
	 * TODO 用get 还是IS
	 * 
	 * @return
	 */
	public boolean isDisabled() {
		return disabled;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name", "description" };
	}

	@Override
	public String toString() {
		return "SecurityResource [name=" + name + ", description=" + description + "]";
	}

}