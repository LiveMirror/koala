package org.openkoala.openci.core;

public enum ToolType {
	CAS_USER_MANAGE(new CasUserConfiguration()),
	SVN(new SvnConfiguration()),
	GIT(new GitConfiguration()),
	JENKINS(new JenkinsConfiguration()),
	SONAR(new SonarConfiguration()),
	JIRA(new JiraConfiguration()),
	TRAC(new TracConfiguration());
	
	private ToolConfiguration toolConfiguration;

	private ToolType(ToolConfiguration toolConfiguration) {
		this.toolConfiguration = toolConfiguration;
	}

	public ToolConfiguration getToolConfiguration() {
		return toolConfiguration;
	}
	
}
