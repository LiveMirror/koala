package org.openkoala.openci.factory;

import java.util.HashSet;
import java.util.Set;

import org.openkoala.openci.CISClientNotInstanceException;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.pojo.GitConfigurationPojo;
import org.openkoala.openci.pojo.JenkinsConfigurationPojo;
import org.openkoala.openci.pojo.JiraConfigurationPojo;
import org.openkoala.openci.pojo.SonarConfigurationPojo;
import org.openkoala.openci.pojo.SvnConfigurationPojo;
import org.openkoala.openci.pojo.ToolConfigurationPojo;
import org.openkoala.openci.pojo.TracConfigurationPojo;
import org.openkoala.opencis.api.CISClient;

public class CISClientFactory {

	private static Set<ToolConfigurationPojo> toolConfigurationPojos = new HashSet<ToolConfigurationPojo>();
	
	// TODO 这种方式无法解决各工具配置的差异，比如Jenkins需要ScmConfig
	static {
		toolConfigurationPojos.add(new SvnConfigurationPojo());
		toolConfigurationPojos.add(new GitConfigurationPojo());
		toolConfigurationPojos.add(new JenkinsConfigurationPojo());
		toolConfigurationPojos.add(new SonarConfigurationPojo());
		toolConfigurationPojos.add(new JiraConfigurationPojo());
		toolConfigurationPojos.add(new TracConfigurationPojo());
	}
	
	public synchronized static CISClient getInstance(ToolConfiguration toolConfiguration, Set<ToolConfigurationPojo> toolConfigurationPojos) {
		for (ToolConfigurationPojo each : toolConfigurationPojos) {
			each.createCISClient(toolConfiguration);
			if (each.isInstance()) {
				CISClient cisClient = each.getCISClient();
				each.destory();
				return cisClient;
			}
		}
		throw new CISClientNotInstanceException();
	}
	
}
