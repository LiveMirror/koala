package org.openkoala.openci.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("jenkins")
public class JenkinsConfiguration extends ToolConfiguration {

	private static final long serialVersionUID = -6711849762118486849L;

	public JenkinsConfiguration() {
		super();
	}

	public JenkinsConfiguration(String name, String serviceUrl, String username, String password) {
		super(name, serviceUrl, username, password);
	}

}
