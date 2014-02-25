package org.openkoala.openci.application.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.openci.EntityNullException;
import org.openkoala.openci.application.ToolConfigurationApplication;
import org.openkoala.openci.core.CasUserConfiguration;
import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.core.JiraConfiguration;
import org.openkoala.openci.core.SonarConfiguration;
import org.openkoala.openci.core.SvnConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.core.TracConfiguration;
import org.openkoala.openci.factory.CISClientFactory;
import org.openkoala.openci.pojo.GitConfigurationPojo;
import org.openkoala.openci.pojo.JenkinsConfigurationPojo;
import org.openkoala.openci.pojo.JiraConfigurationPojo;
import org.openkoala.openci.pojo.SonarConfigurationPojo;
import org.openkoala.openci.pojo.SvnConfigurationPojo;
import org.openkoala.openci.pojo.ToolConfigurationPojo;
import org.openkoala.openci.pojo.TracConfigurationPojo;
import org.openkoala.opencis.api.CISClient;
import org.springframework.transaction.annotation.Transactional;

@Named("toolConfigurationApplication")
@Transactional("transactionManager_opencis")
public class ToolConfigurationApplicationImpl implements ToolConfigurationApplication {

	@Inject
	private QueryChannelService queryChannel;

	public void createConfiguration(ToolConfiguration toolConfiguration) {
		if (toolConfiguration == null) {
			throw new EntityNullException();
		}
		toolConfiguration.save();
	}

	public void updateConfiguration(ToolConfiguration toolConfiguration) {
		createConfiguration(toolConfiguration);
	}

	public void setToolUsabled(ToolConfiguration toolConfiguration) {
		if (toolConfiguration == null) {
			throw new EntityNullException();
		}
		toolConfiguration.usabled();
	}

	public void setToolUnusabled(ToolConfiguration toolConfiguration) {
		if (toolConfiguration == null) {
			throw new EntityNullException();
		}
		toolConfiguration.unusabled();
	}

	public boolean canConnect(ToolConfiguration toolConfiguration) {
		try {
			CISClient cisClient = CISClientFactory.getInstance(toolConfiguration, initToolConfigurationPojos());
			if (cisClient.authenticate()) {
				setToolUsabled(toolConfiguration);
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}
	
	private Set<ToolConfigurationPojo> initToolConfigurationPojos() {
		Set<ToolConfigurationPojo> toolConfigurationPojos = new HashSet<ToolConfigurationPojo>();
		toolConfigurationPojos.add(new SvnConfigurationPojo());
		toolConfigurationPojos.add(new GitConfigurationPojo());
		toolConfigurationPojos.add(new JenkinsConfigurationPojo());
		toolConfigurationPojos.add(new SonarConfigurationPojo());
		toolConfigurationPojos.add(new JiraConfigurationPojo());
		toolConfigurationPojos.add(new TracConfigurationPojo());
		return toolConfigurationPojos;
	}

	public List<ToolConfiguration> getAllUsable() {
		return ToolConfiguration.findByUsable();
	}

	public Page<JenkinsConfiguration> pagingQeuryJenkinsConfigurations(int currentPage, int pagesize) {
		StringBuilder jpql = new StringBuilder("select _toolconfiguration from JenkinsConfiguration _toolconfiguration");
		return queryChannel.createJpqlQuery(jpql.toString()).setPage(currentPage, pagesize).pagedList();
	}

	public Page<SvnConfiguration> pagingQeurySvnConfigurations(int currentPage, int pagesize) {
		StringBuilder jpql = new StringBuilder("select _toolconfiguration from SvnConfiguration _toolconfiguration");
		return queryChannel.createJpqlQuery(jpql.toString()).setPage(currentPage, pagesize).pagedList();
	}

	public Page<GitConfiguration> pagingQeuryGitConfigurations(int currentPage, int pagesize) {
		StringBuilder jpql = new StringBuilder("select _toolconfiguration from GitConfiguration _toolconfiguration");
		return queryChannel.createJpqlQuery(jpql.toString()).setPage(currentPage, pagesize).pagedList();
		}

	public Page<SonarConfiguration> pagingQeurySonarConfigurations(int currentPage, int pagesize) {
		StringBuilder jpql = new StringBuilder("select _toolconfiguration from SonarConfiguration _toolconfiguration");
		return queryChannel.createJpqlQuery(jpql.toString()).setPage(currentPage, pagesize).pagedList();
	}

	public Page<JiraConfiguration> pagingQeuryJiraConfigurations(int currentPage, int pagesize) {
		StringBuilder jpql = new StringBuilder("select _toolconfiguration from JiraConfiguration _toolconfiguration");
		return queryChannel.createJpqlQuery(jpql.toString()).setPage(currentPage, pagesize).pagedList();
	}

	public Page<TracConfiguration> pagingQeuryTracConfigurations(int currentPage, int pagesize) {
		StringBuilder jpql = new StringBuilder("select _toolconfiguration from TracConfiguration _toolconfiguration");
		return queryChannel.createJpqlQuery(jpql.toString()).setPage(currentPage, pagesize).pagedList();
	}

	public CasUserConfiguration getUniqueInstance() {
		return CasUserConfiguration.getUniqueInstance();
	}

}
