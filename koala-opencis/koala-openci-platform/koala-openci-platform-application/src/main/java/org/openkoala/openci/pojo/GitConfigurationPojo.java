package org.openkoala.openci.pojo;

import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.opencis.git.impl.GitlabCISClient;
import org.openkoala.opencis.git.impl.GitlabConfiguration;


public class GitConfigurationPojo extends ToolConfigurationPojo {

	@Override
	public void createCISClient(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof GitConfiguration) {
			GitlabCISClient gitlabCISClient = new GitlabCISClient(createGitlabConfiguration(toolConfiguration));
			cisClient = gitlabCISClient;
			isInstance = true;
		}
	}

	private GitlabConfiguration createGitlabConfiguration(GitConfiguration gitConfiguration) {
		GitlabConfiguration gitlabConfiguration = new GitlabConfiguration();
		gitlabConfiguration.setGitHostURL(gitConfiguration.getServiceUrl());
		gitlabConfiguration.setToken(gitConfiguration.getToken());
		gitlabConfiguration.setAdminUsername(gitConfiguration.getUsername());
		gitlabConfiguration.setAdminPassword(gitConfiguration.getPassword());
		gitlabConfiguration.setAdminEmail(gitConfiguration.getEmail());
		return gitlabConfiguration;
	}
	
}
