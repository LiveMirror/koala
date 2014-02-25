package org.openkoala.openci.pojo;

import org.openkoala.openci.core.JiraConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.opencis.jira.JiraCISClient;


public class JiraConfigurationPojo extends ToolConfigurationPojo {

	@Override
	public void createCISClient(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof JiraConfiguration) {
			JiraCISClient jiraCISClient = new JiraCISClient(toolConfiguration.getServiceUrl(), toolConfiguration.getUsername(), toolConfiguration.getPassword());
			cisClient = jiraCISClient;
			isInstance = true;
		}
	}

}
