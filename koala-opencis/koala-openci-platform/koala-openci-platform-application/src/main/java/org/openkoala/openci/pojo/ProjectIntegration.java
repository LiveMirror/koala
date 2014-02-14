package org.openkoala.openci.pojo;

import java.io.File;
import java.util.Set;

import org.openkoala.openci.core.CasUserConfiguration;
import org.openkoala.openci.core.Tool;
import org.openkoala.openci.dto.ScmConfig;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;


public class ProjectIntegration {

	private String artifactId;

	private String groupId;

	private String projectName;

	private Set<Tool> tools;

	// 集成CAS用户管理
	private CasUserConfiguration casUserConfiguration;

	// 项目存放路径
	private String projectSavePath;

	// 项目版本控制
	private ScmConfig scmConfig;

	// 开发者列表
	private Set<Developer> developers;

	public Project toCISProject() {
		Project project = new Project();
		project.setArtifactId(artifactId);
		project.setGroupId(groupId);
		project.setProjectName(projectName);
		project.setPhysicalPath(projectSavePath + File.separator + projectName);
		return project;
	}

	public boolean isIntegrationCas() {
		if (casUserConfiguration == null) {
			return false;
		}
		return true;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Set<Tool> getTools() {
		return tools;
	}

	public void setTools(Set<Tool> tools) {
		this.tools = tools;
	}

	public CasUserConfiguration getCasUserConfiguration() {
		return casUserConfiguration;
	}

	public void setCasUserConfiguration(CasUserConfiguration casUserConfiguration) {
		this.casUserConfiguration = casUserConfiguration;
	}

	public String getProjectSavePath() {
		return projectSavePath;
	}

	public void setProjectSavePath(String projectSavePath) {
		this.projectSavePath = projectSavePath;
	}

	public ScmConfig getScmConfig() {
		return scmConfig;
	}

	public void setScmConfig(ScmConfig scmConfig) {
		this.scmConfig = scmConfig;
	}

	public Set<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<Developer> developers) {
		this.developers = developers;
	}

}
