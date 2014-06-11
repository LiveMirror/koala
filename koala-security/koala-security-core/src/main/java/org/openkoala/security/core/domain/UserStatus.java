package org.openkoala.security.core.domain;

public enum UserStatus {
	
	SUSPEND("禁用"), 
	ACTIVATE("激活");
	
	private String description;

	public String getDescription() {
		return description;
	}

	private UserStatus(String description) {
		this.description = description;
	}
}
