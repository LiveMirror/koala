package org.openkoala.security.core.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "KS_SECURITYRESOURCES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
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
	@Column(name = "IS_VALID")
	private boolean isValid;

	@Column(name = "URL")
	private String url;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	SecurityResource() {

	}

	public SecurityResource(String name, boolean isValid) {
		this.name = name;
		this.isValid = isValid;
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


	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name","description" };
	}

	@Override
	public String toString() {
		return "SecurityResource [name=" + name + ", description=" + description + "]";
	}
}