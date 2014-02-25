package org.openkoala.openci.pojo;

import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.opencis.gitlab.GitlabClient;
import org.openkoala.opencis.gitlab.GitlabConfiguration;


public class GitConfigurationPojo extends ToolConfigurationPojo {

	@Override
	public void createCISClient(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof GitConfiguration) {
			GitlabClient gitlabCISClient = new GitlabClient(createGitlabConfiguration((GitConfiguration) toolConfiguration));
			cisClient = gitlabCISClient;
			isInstance = true;
		}
	}

	private GitlabConfiguration createGitlabConfiguration(GitConfiguration gitConfiguration) {
		GitlabConfiguration gitlabConfiguration = new GitlabConfiguration();
		gitlabConfiguration.setGitlabHostURL(gitConfiguration.getServiceUrl());
		gitlabConfiguration.setToken(gitConfiguration.getToken());
		gitlabConfiguration.setUsername(gitConfiguration.getUsername());
		gitlabConfiguration.setPassword(gitConfiguration.getPassword());
		gitlabConfiguration.setEmail(gitConfiguration.getEmail());
		return gitlabConfiguration;
	}

	
}
