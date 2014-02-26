package org.openkoala.openci.web.controller.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.koala.queryvo.TypeDef;
import org.openkoala.koala.util.ModuleDependencyUtils;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Project;
import org.openkoala.openci.application.ProjectApplication;
import org.openkoala.openci.core.CasUserConfiguration;
import org.openkoala.openci.core.ProjectDeveloper;
import org.openkoala.openci.core.Role;
import org.openkoala.openci.dto.ProjectDto;
import org.openkoala.openci.dto.ProjectQueryDto;
import org.openkoala.openci.executor.ToolIntegrationExecutor;
import org.openkoala.openci.pojo.ProjectIntegration;
import org.openkoala.openci.web.controller.BaseController;
import org.openkoala.openci.web.dto.ResultDto;
import org.openkoala.opencis.api.Developer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

	@Inject
	private ProjectApplication projectApplication;

	@Inject
	private ToolIntegrationExecutor toolIntegrationExecutor;
	
	@ResponseBody
	@RequestMapping("/create")
	public ResultDto createProject(@RequestBody ProjectDto projectDto) {
		projectApplication.createProject(projectDto);
		integrateProjectToTools(projectDto);
		return ResultDto.createSuccess();
	}
	
	private void integrateProjectToTools(ProjectDto projectDto) {
		org.openkoala.koala.widget.Project project = projectDto.getProjectForCreate();
		ProjectIntegration projectIntegration = new ProjectIntegration();
		projectIntegration.setGroupId(project.getGroupId());
		projectIntegration.setArtifactId(project.getArtifactId());
		projectIntegration.setProjectName(project.getAppName());
		projectIntegration.setTools(projectDto.getProjectForCis().getTools());
		projectIntegration.setProjectSavePath(project.getPath());
		projectIntegration.setDevelopers(transformDevelopers(projectDto.getProjectForCis().getDevelopers()));
		projectIntegration.setScmConfig(projectDto.getScmConfig());
		if (projectDto.isUserCas()) {
			projectIntegration.setCasUserConfiguration(CasUserConfiguration.getUniqueInstance());
		}

		toolIntegrationExecutor.execute(projectIntegration);
	}

	private Set<Developer> transformDevelopers(Set<ProjectDeveloper> projectDevelopers) {
		Set<Developer> results = new HashSet<Developer>();
		for (ProjectDeveloper each : projectDevelopers) {
			Developer developer = new Developer();
			developer.setId(each.getDeveloper().getDeveloperId());
			developer.setName(each.getDeveloper().getName());
			developer.setEmail(each.getDeveloper().getEmail());
			developer.setPassword(each.getDeveloper().getPassword());

			List<String> roles = new ArrayList<String>();
			for (Role role : each.getRoles()) {
				roles.add(role.getName());
			}
			developer.setRoles(roles);
			results.add(developer);
		}
		return results;
	}


	@ResponseBody
	@RequestMapping("/get-functions")
	public Map<String, Map<String, String>> getFunctions(String moduleType) {
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		TypeDef typeDef = TypeDef.getInstance();
		result.put("functions", typeDef.getFunctions(moduleType));
		return result;
	}

	@ResponseBody
	@RequestMapping("/get-dependables")
	public List<Module> getDependables(@RequestBody Project project, String moduleType) {
		ModuleDependencyUtils moduleDependencyUtils = new ModuleDependencyUtils(project, moduleType);
		return moduleDependencyUtils.getCouldDependencyModules();
	}

	@ResponseBody
	@RequestMapping("/generate-default-modules")
	public ProjectDto generateDefaultModules(ProjectDto projectDto) {
		Project project = projectDto.getProjectForCreate();
		project.initSSJProject();
		project.initModulePrefix(project.getAppName());
		return projectDto;
	}

	@ResponseBody
	@RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(ProjectQueryDto projectQueryDto, int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<org.openkoala.openci.core.Project> projectPage = projectApplication.pagingQueryProject(projectQueryDto, page, pagesize);
		dataMap.put("Rows", projectPage.getData());
		dataMap.put("start", projectPage.getStart());
		dataMap.put("limit", pagesize);
		dataMap.put("Total", projectPage.getResultCount());
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/detail/{projectId}")
	public org.openkoala.openci.core.Project getProjectDetail(@PathVariable long projectId) {
		return projectApplication.getDetail(projectId);
	}
	
	@ResponseBody
	@RequestMapping("/is-exist/{name}") 
	public boolean isExistByName(@PathVariable String name) {
		return projectApplication.isExistByName(name);
	}
	
}
