package org.openkoala.openci.executor;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.openci.AbstractIntegrationTest;
import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.core.Tool;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.pojo.ProjectIntegration;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SSHConnectConfig;
import org.openkoala.opencis.svn.SvnCISClient;

@Ignore
public class ToolIntegrationExecutorTest extends AbstractIntegrationTest {

	@Inject
	private ToolIntegrationExecutor toolIntegrationExecutor;

	@Test
	public void testExecutor() {
		// toolIntegrationExecutor.execute(createProjectIntegration());
		// System.setProperty("webdriver.firefox.bin",
		// "D:\\Program Files\\Mozilla Firefox\\firefox.exe");
		// WebDriver driver = new FirefoxDriver();
		//
		// driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		ToolConfiguration toolConfiguration = createToolConfiguration();

//		JenkinsCISClient jenkinsCISClient = new JenkinsCISClient(toolConfiguration.getServiceUrl(), authentication);
//		jenkinsCISClient.setScmConfig(new SvnConfig("http://git.oschina.net/xiaokaceng/openci-platform.git", "test", "test"));
//		jenkinsCISClient.authenticate();
//		Project project = createProjectIntegration().toCISProject();
//		jenkinsCISClient.createProject(project);
//		jenkinsCISClient.createUserIfNecessary(project, createDeveloper());
//		jenkinsCISClient.createRoleIfNecessary(project, project.getArtifactId());
//		jenkinsCISClient.assignUsersToRole(project, project.getArtifactId(), createDeveloper());
		
//		SSHConnectConfig sshConnectConfig = new SSHConnectConfig("10.108.1.138", "apache", "apache", "/usr/share/trac/projects/openci-platform3");
//		TracCISClient tracCISClient = new TracCISClient(sshConnectConfig);
//		Project project = createProjectIntegration().toCISProject();
//		tracCISClient.createProject(project);
//		tracCISClient.createUserIfNecessary(project, createDeveloper());
//		tracCISClient.createRoleIfNecessary(project, project.getArtifactId());
//		tracCISClient.assignUsersToRole(project, project.getArtifactId(), createDeveloper());

		SSHConnectConfig sshConnectConfig = new SSHConnectConfig("10.108.1.138", "apache", "apache", "/home/svn");
		SvnCISClient tracCISClient = new SvnCISClient(null);
		Project project = createProjectIntegration().toCISProject();
		tracCISClient.createProject(project);
		tracCISClient.createUserIfNecessary(project, createDeveloper());
		tracCISClient.assignUsersToRole(project, project.getArtifactId(), createDeveloper());

		
	}

	private ProjectIntegration createProjectIntegration() {
		ProjectIntegration projectIntegration = new ProjectIntegration();
		projectIntegration.setGroupId("com.xiaokaceng.openci");
		projectIntegration.setArtifactId("openci-platform3");
		projectIntegration.setProjectName("openci-platform3");
		projectIntegration.setTools(createTools());
		Set<Developer> developers = new HashSet<Developer>();
		developers.add(createDeveloper());
		projectIntegration.setDevelopers(developers);
		return projectIntegration;
	}

	private Set<Tool> createTools() {
		Set<Tool> tools = new HashSet<Tool>();
		tools.add(new Tool(createToolConfiguration(), null));
		return tools;
	}

	private ToolConfiguration createToolConfiguration() {
		// ToolConfiguration toolConfiguration = new TracConfiguration("trac",
		// "10.108.1.138", "root", "luomin888", "/tmp");
		ToolConfiguration toolConfiguration = new JenkinsConfiguration("jenkins", "http://10.108.1.138:8080/jenkins", "admin", "admin");
		return toolConfiguration;
	}

	private Developer createDeveloper() {
		Developer developer = new Developer();
		developer.setId("test");
		developer.setName("test");
		developer.setEmail("test@test.com");
		developer.setPassword("test");
		developer.setFullName("test");
		return developer;
	}
}
