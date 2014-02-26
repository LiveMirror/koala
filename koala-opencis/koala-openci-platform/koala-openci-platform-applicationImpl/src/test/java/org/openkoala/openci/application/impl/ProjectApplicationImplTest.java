package org.openkoala.openci.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.openci.AbstractIntegrationTest;
import org.openkoala.openci.EntityNullException;
import org.openkoala.openci.application.ProjectApplication;
import org.openkoala.openci.core.Developer;
import org.openkoala.openci.core.Project;
import org.openkoala.openci.core.ProjectDeveloper;
import org.openkoala.openci.core.Role;
import org.openkoala.openci.core.ScmType;
import org.openkoala.openci.core.Tool;
import org.openkoala.openci.dto.ProjectDto;
import org.openkoala.openci.dto.ProjectQueryDto;
import org.openkoala.openci.dto.ScmConfig;

public class ProjectApplicationImplTest extends AbstractIntegrationTest {
	
	@Inject
	private ProjectApplication projectApplication;

	private static final String NAME = "test";
	
	private Developer developer;
	private Role role;
	
	@Test
	public void testCreateProject() {
		ProjectDto projectDto = getProjectDtoInstance();
		projectApplication.createProject(projectDto);
		assertEquals(1, projectDto.getProjectForCis().getDevelopers().size());
		assertEquals(2, projectDto.getProjectForCis().getTools().size());
		projectDto.getProjectForCis().remove();
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreateProjectIfNull() {
		projectApplication.createProject(null);
	}
	
	@Test
	public void testPagingQueryProject() {
		ProjectDto projectDto = getProjectDtoInstance();
		projectApplication.createProject(projectDto);
		
		ProjectQueryDto projectQueryDto = new ProjectQueryDto();
		projectQueryDto.setName("demo");
		List<Project> projects = projectApplication.pagingQueryProject(projectQueryDto, 0, 10).getData();
		assertEquals(0, projects.size());
		
		projectQueryDto.setName("es");
		projects = projectApplication.pagingQueryProject(projectQueryDto, 0, 10).getData();
		assertEquals(1, projects.size());
		
		projectDto.getProjectForCis().remove();
	}
	
	@Test
	public void testPagingQueryProject2() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		ProjectDto projectDto = getProjectDtoInstance();
		projectApplication.createProject(projectDto);
		
		ProjectQueryDto projectQueryDto = new ProjectQueryDto();
		projectQueryDto.setName("test");
		projectQueryDto.setStartDate(dateFormat.parse("8888-1-1"));
		List<Project> projects = projectApplication.pagingQueryProject(projectQueryDto, 0, 10).getData();
		assertEquals(0, projects.size());
		
		projectQueryDto.setStartDate(dateFormat.parse("2013-1-1"));
		projects = projectApplication.pagingQueryProject(projectQueryDto, 0, 10).getData();
		assertEquals(1, projects.size());
		
		projectDto.getProjectForCis().remove();
	}
	
	@Test
	public void testGetDetail() {
		ProjectDto projectDto = getProjectDtoInstance();
		projectApplication.createProject(projectDto);

		Project project = projectApplication.getDetail(projectDto.getProjectForCis().getId());
		assertNotNull(project);
		assertEquals(project.getName(), projectDto.getProjectForCis().getName());
		
		projectDto.getProjectForCis().remove();
	}
	
	@Test
	public void testGetDetail2() {
		Project project = projectApplication.getDetail(0);
		assertNull(project);
	}
	
	@Test
	public void testIsExistByName() {
		ProjectDto projectDto = getProjectDtoInstance();
		projectApplication.createProject(projectDto);
		
		assertTrue(projectApplication.isExistByName(NAME));
		assertFalse(projectApplication.isExistByName("tt"));
		projectDto.getProjectForCis().remove();
	}
	
	@Test
	public void testAddIntegrationTool() {
		ProjectDto projectDto = getProjectDtoInstance();
		projectApplication.createProject(projectDto);
		projectApplication.addIntegrationTool(projectDto.getProjectForCis(), new Tool(null, projectDto.getProjectForCis()));
		assertEquals(3, projectDto.getProjectForCis().getTools().size());
	}
	
	@Test(expected = EntityNullException.class)
	public void testAddIntegrationToolIfNull() {
		Project project = getProjectInstance();
		projectApplication.addIntegrationTool(project, null);
	}

	
	private ProjectDto getProjectDtoInstance() {
		ProjectDto projectDto = new ProjectDto(NAME);
		projectDto.getProjectForCis().setDevelopers(createProjectDeveloper(projectDto.getProjectForCis()));
		projectDto.getProjectForCis().setTools(createTool(projectDto.getProjectForCis()));
		projectDto.setScmConfig(createScmConfig());
		
		org.openkoala.koala.widget.Project projectForCreate = projectDto.getProjectForCreate();
		projectForCreate.setAppName("demo");
		projectForCreate.setGroupId("org.openkoala");
		projectForCreate.setArtifactId("demo");
		projectForCreate.setPackaging("pom");
		projectForCreate.initSSJProject();
		
		return projectDto;
	}
	
	private ScmConfig createScmConfig() {
		ScmConfig scmConfig = new ScmConfig();
		scmConfig.setRepositoryUrl("xxxxxxx");
		scmConfig.setScmType(ScmType.GIT);
		return scmConfig;
	}

	private Project getProjectInstance() {
		Project project = new Project(NAME);
		project.setDevelopers(createProjectDeveloper(project));
		project.setTools(createTool(project));
		return project;
	}

	private Set<Tool> createTool(Project project) {
		Set<Tool> tools = new HashSet<Tool>();
		tools.add(new Tool(null, project));
		tools.add(new Tool(null, project));
		return tools;
	}

	private Set<ProjectDeveloper> createProjectDeveloper(Project project) {
		Set<ProjectDeveloper> projectDevelopers = new HashSet<ProjectDeveloper>();
		projectDevelopers.add(new ProjectDeveloper(createRoles(), developer, project));
		return projectDevelopers;
	}

	private Set<Role> createRoles() {
		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		return roles;
	}

	@Before
	public void init() {
		developer = new Developer(NAME, NAME, NAME, NAME);
		developer.save();
		role = new Role(NAME, NAME);
		role.save();
	}
	
	@After
	public void destory() {
		List<Developer> developers = Developer.findAll(Developer.class);
		if (developers.size() > 0) {
			for (Developer each : developers) {
				each.remove();
			}
		}
	}
	
}
