package org.openkoala.openci.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.mojo.KoalaProjectCreate;
import org.openkoala.openci.EntityNullException;
import org.openkoala.openci.application.ProjectApplication;
import org.openkoala.openci.core.Project;
import org.openkoala.openci.core.ProjectDetail;
import org.openkoala.openci.core.ProjectStatus;
import org.openkoala.openci.core.Tool;
import org.openkoala.openci.dto.ProjectDto;
import org.openkoala.openci.dto.ProjectQueryDto;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional("transactionManager_opencis")
public class ProjectApplicationImpl implements ProjectApplication {

	@Inject
	private QueryChannelService queryChannel;

	public void createProject(ProjectDto projectDto) {
		Project projectForCis = projectDto.getProjectForCis();
		if (projectForCis == null) {
			throw new EntityNullException();
		}
		projectDto.getProjectForCreate().setPath(getProjectSavePath());
		persistProject(projectDto, projectForCis);
	}

	private void persistProject(ProjectDto projectDto, Project projectForCis) {
		projectForCis.setProjectDetail(createProjectDetail(projectDto));
		boolean createResult = createProjectFile(projectDto.getProjectForCreate());
		projectForCis.setProjectStatus(getProjectStatus(createResult));
		projectForCis.save();
	}

	private ProjectStatus getProjectStatus(boolean createResult) {
		if (createResult) {
			return ProjectStatus.INTEGRATION_TOOL;
		}
		return ProjectStatus.CREATE_MAVEN_PROJECT_FAILURE;
	}

	private String getProjectSavePath() {
		return System.getProperty("java.io.tmpdir");
	}

	private ProjectDetail createProjectDetail(ProjectDto projectDto) {
		ProjectDetail projectDetail = new ProjectDetail();
		org.openkoala.koala.widget.Project project = projectDto.getProjectForCreate();
		projectDetail.setArtifactId(project.getArtifactId());
		projectDetail.setGroupId(project.getGroupId());
		projectDetail.setIntegrationCas(projectDto.isUserCas());
		projectDetail.setProjectSavePath(getProjectSavePath());
		projectDetail.setScmRepositoryUrl(projectDto.getScmConfig().getRepositoryUrl());
		projectDetail.setScmType(projectDto.getScmConfig().getScmType());
		return projectDetail;
	}

	private boolean createProjectFile(org.openkoala.koala.widget.Project projectForCreate) {
		KoalaProjectCreate koalaProjectCreate = new KoalaProjectCreate();
		try {
			koalaProjectCreate.createProject(projectForCreate);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void addIntegrationTool(Project project, Tool tool) {
		if (project == null) {
			throw new EntityNullException();
		}
		project.addTool(tool);
	}

	public Page<Project> pagingQueryProject(ProjectQueryDto projectQueryDto, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();
		StringBuilder jpql = new StringBuilder("select _project from Project _project");
		
		if (projectQueryDto != null) {
			jpql.append(" where 1=1");
			if (!StringUtils.isBlank(projectQueryDto.getName())) {
				jpql.append(" and _project.name like ?");
				conditionVals.add(MessageFormat.format("%{0}%", projectQueryDto.getName()));
			}
			if (projectQueryDto.getStartDate() != null) {
				jpql.append(" and _project.createDate >= ?");
				conditionVals.add(projectQueryDto.getStartDate());
			}
			if (projectQueryDto.getEndDate() != null) {
				jpql.append(" and _project.createDate <= ?");
				conditionVals.add(projectQueryDto.getEndDate());
			}
		}
		return queryChannel.createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize).pagedList();
	}

	public Project getDetail(long projectId) {
		return Project.get(Project.class, projectId);
	}

	public boolean isExistByName(String name) {
		return Project.isExixtByName(name);
	}

}
