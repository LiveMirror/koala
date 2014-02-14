package org.openkoala.openci.pojo;

import org.openkoala.openci.core.JiraConfiguration;
import org.openkoala.openci.core.ToolConfiguration;


public class JiraConfigurationPojo extends ToolConfigurationPojo {

	@Override
	public void createCISClient(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof JiraConfiguration) {
			
		}
	}

}
