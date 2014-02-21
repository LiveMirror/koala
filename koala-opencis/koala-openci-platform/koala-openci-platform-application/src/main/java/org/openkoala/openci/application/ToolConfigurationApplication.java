package org.openkoala.openci.application;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.openci.core.CasUserConfiguration;
import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.core.JiraConfiguration;
import org.openkoala.openci.core.SonarConfiguration;
import org.openkoala.openci.core.SvnConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.core.TracConfiguration;

public interface ToolConfigurationApplication {

	/**
	 * 创建工具配置
	 * 
	 * @param toolConfiguration
	 */
	void createConfiguration(ToolConfiguration toolConfiguration);

	/**
	 * 更新工具配置
	 * 
	 * @param toolConfiguration
	 */
	void updateConfiguration(ToolConfiguration toolConfiguration);

	/**
	 * 设置工具可用
	 * 
	 * @param toolConfiguration
	 */
	void setToolUsabled(ToolConfiguration toolConfiguration);

	/**
	 * 设置工具不可用
	 * 
	 * @param toolConfiguration
	 */
	void setToolUnusabled(ToolConfiguration toolConfiguration);

	/**
	 * 测试工具是否可用
	 * 
	 * @param toolConfiguration
	 * @return
	 */
	boolean canConnect(ToolConfiguration toolConfiguration);

	/**
	 * 获取所有可用的工具配置列表
	 * 
	 * @return
	 */
	List<ToolConfiguration> getAllUsable();

	/**
	 * 分页查询Jenkins工具配置信息
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<JenkinsConfiguration> pagingQeuryJenkinsConfigurations(int currentPage, int pagesize);

	/**
	 * 分页查询Svn工具配置信息
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<SvnConfiguration> pagingQeurySvnConfigurations(int currentPage, int pagesize);

	/**
	 * 分页查询Git工具配置信息
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<GitConfiguration> pagingQeuryGitConfigurations(int currentPage, int pagesize);

	/**
	 * 分页查询Sonar工具配置信息
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<SonarConfiguration> pagingQeurySonarConfigurations(int currentPage, int pagesize);

	/**
	 * 分页查询Jira工具配置信息
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<JiraConfiguration> pagingQeuryJiraConfigurations(int currentPage, int pagesize);

	/**
	 * 分页查询Trac工具配置信息
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<TracConfiguration> pagingQeuryTracConfigurations(int currentPage, int pagesize);

	/**
	 * 获取CAS用户管理的配置
	 * 
	 * @return
	 */
	CasUserConfiguration getUniqueInstance();
}