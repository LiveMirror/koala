package org.openkoala.security.core.domain;

import java.util.Collections;
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.UrlIsExistedException;

@Entity
@Table(name = "KS_SECURITYRESOURCES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
		@NamedQuery(name = "SecurityResource.findAllByType", query = "SELECT _securityResource  FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :securityResourceType  AND _securityResource.disabled = :disabled"),
		@NamedQuery(name = "SecurityResource.findByName", query = "SELECT _securityResource  FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :securityResourceType AND _securityResource.name = :name AND _securityResource.disabled = :disabled") })
public abstract class SecurityResource extends SecurityAbstractEntity {

	private static final long serialVersionUID = 6064565786784560656L;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	protected String name;

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

	protected SecurityResource() {
	}

	public SecurityResource(String name) {
		this.name = name;
	}

	public SecurityResource(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public abstract void update();

	public abstract SecurityResource findByName(String name);

	public void disable() {
		disabled = true;
	}

	public void enable() {
		disabled = false;
	}

	protected void isNameExisted() {
		if (isExistName(this.getName())) {
			throw new NameIsExistedException("securityResource.name.exist");
		}
	}

	protected void isUrlExisted() {
		if (isExistUrl(this.getUrl())) {
			throw new UrlIsExistedException("securityResource.name.exist");
		}
	}

	protected SecurityResource findByUrl(String url) {
		return null;
	}

	private boolean isExistUrl(String url) {
		return findByUrl(url) != null;
	}

	private boolean isExistName(String name) {
		return findByName(name) != null;
	}
	
	@Override
	public String[] businessKeys() {
		return new String[] { "name" };
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(name)//
				.append(url)//
				.append(identifier)//
				.append(disabled)//
				.append(description)//
				.build();
	}

	public boolean isDisabled() {
		return disabled;
	}

	public String getName() {
		return name;
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
		return Collections.unmodifiableSet(authorities);
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

}