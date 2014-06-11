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

	public SecurityResource() {

	}

	public SecurityResource(String name, boolean isValid, String description) {
		this.name = name;
		this.isValid = isValid;
		this.description = description;
	}

	public SecurityResource(String name, boolean isValid, String description, String url) {
		this.name = name;
		this.isValid = isValid;
		this.description = description;
		this.url = url;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityResource other = (SecurityResource) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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