package org.openkoala.openci.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("sonar")
public class SonarConfiguration extends ToolConfiguration {

	private static final long serialVersionUID = 6699967170771265556L;

	public SonarConfiguration() {
		super();
	}

	public SonarConfiguration(String name, String serviceUrl, String username, String password) {
		super(name, serviceUrl, username, password);
	}
}
