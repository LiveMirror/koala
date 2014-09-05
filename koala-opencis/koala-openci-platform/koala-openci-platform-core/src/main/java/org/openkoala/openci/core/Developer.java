package org.openkoala.openci.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "developers")
public class Developer extends TimeIntervalEntity {

	private static final long serialVersionUID = -3733063134487603001L;

	
	@Column(name = "developer_id", nullable = false, unique = true)
	private String developerId;

	private String name;

	
	@Column(nullable = false, unique = true)
	private String email;

	private String password;

	public Developer(String developerId, String name, String password, String email) {
		super(new Date());
		this.developerId = developerId;
		this.name = name;
		this.password = password;
		this.email = email;
	}

	Developer() {
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Developer)) {
			return false;
		}
		Developer that = (Developer) other;
		return new EqualsBuilder().append(getName(), that.getName()).append(getDeveloperId(), that.getDeveloperId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(getDeveloperId()).hashCode();
	}

	public String toString() {
		return getDeveloperId();
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
