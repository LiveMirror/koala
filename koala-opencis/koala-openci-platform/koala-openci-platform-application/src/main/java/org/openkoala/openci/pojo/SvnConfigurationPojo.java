package org.openkoala.openci.pojo;

import org.openkoala.openci.core.SvnConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.opencis.support.SvnConfig;
import org.openkoala.opencis.svn.SvnCISClient;


public class SvnConfigurationPojo extends ToolConfigurationPojo {

	@Override
	public void createCISClient(ToolConfiguration toolConfiguration) {
		if (toolConfiguration instanceof SvnConfiguration) {
			SvnConfig svnConfig = new SvnConfig(toolConfiguration.getServiceUrl(), toolConfiguration.getUsername(), toolConfiguration.getPassword(),
					((SvnConfiguration) toolConfiguration).getSavePath(), getSvnAddress((SvnConfiguration) toolConfiguration), null, null);
			cisClient = new SvnCISClient(svnConfig);
			isInstance = true;
		}
	}

	private String getSvnAddress(SvnConfiguration toolConfiguration) {
		return "http://" + toolConfiguration.getServiceUrl() + "/" + toolConfiguration.getRequestRootAddress();
	}
}
