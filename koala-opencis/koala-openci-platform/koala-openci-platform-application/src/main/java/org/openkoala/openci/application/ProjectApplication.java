package org.openkoala.openci.application;

import org.dayatang.querychannel.Page;
import org.openkoala.openci.core.Project;
import org.openkoala.openci.core.Tool;
import org.openkoala.openci.dto.ProjectDto;
import org.openkoala.openci.dto.ProjectQueryDto;

public interface ProjectApplication {

	/**
	 * 创建项目
	 * 
	 * @param projectDto
	 */
	void createProject(ProjectDto projectDto);

	/**
	 * 分页查找项目
	 * 
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<Project> pagingQueryProject(ProjectQueryDto projectQueryDto, int currentPage, int pagesize);

	/**
	 * 获取项目详情
	 * 
	 * @param projectId
	 * @return
	 */
	Project getDetail(long projectId);

	/**
	 * 根据项目名检测项目是否存在
	 * 
	 * @param name
	 * @return
	 */
	boolean isExistByName(String name);

	/**
	 * 添加整合工具
	 * 
	 * @param project
	 * @param tool
	 */
	void addIntegrationTool(Project project, Tool tool);

}