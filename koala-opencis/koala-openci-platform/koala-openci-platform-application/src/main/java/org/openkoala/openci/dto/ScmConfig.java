package org.openkoala.openci.dto;

import org.openkoala.openci.core.ScmType;


public class ScmConfig {

	private ScmType scmType;

	private String repositoryUrl;

	private String username;

	private String password;

	public ScmType getScmType() {
		return scmType;
	}

	public void setScmType(ScmType scmType) {
		this.scmType = scmType;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
