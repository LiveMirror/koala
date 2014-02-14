package org.openkoala.openci.pojo;

import org.openkoala.openci.core.SonarConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.opencis.sonar.SonarCISClient;
import org.openkoala.opencis.sonar.SonarConnectConfig;


public class SonarConfigurationPojo extends ToolConfigurationPojo {

	@Override
	public void createCISClient(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof SonarConfiguration) {
			SonarConnectConfig sonarConnectConfig = new SonarConnectConfig(toolConfiguration.getServiceUrl(), toolConfiguration.getUsername(), toolConfiguration.getPassword());
			cisClient = new SonarCISClient(sonarConnectConfig);
			isInstance = true;
		}
	}

}
