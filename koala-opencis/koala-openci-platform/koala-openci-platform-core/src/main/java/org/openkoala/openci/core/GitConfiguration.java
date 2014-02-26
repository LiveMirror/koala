package org.openkoala.openci.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("git")
public class GitConfiguration extends ToolConfiguration {

	private static final long serialVersionUID = 2960979556527118613L;

	private String token;
	
	private String email;

	public GitConfiguration(String name, String serviceUrl, String username, String password, String token, String email) {
		super(name, serviceUrl, username, password);
		this.token = token;
		this.email = email;
	}

	public GitConfiguration() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
