package org.openkoala.opencis.jira;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * User: zjzhai
 * Date: 2/11/14
 * Time: 11:19 AM
 */
@Ignore
public class JiraCISClientTest {

    private String username = "xxxx";

    private JiraCISClient client = new JiraCISClient("http://localhost:8080/", username, "12345678");

    @Before
    public void setUp() throws Exception {
        assert client.authenticate();
        client.createUserIfNecessary(null, getDeveloper());
        assert client.isDeveloperExist(getDeveloper());


    }

    @After
    public void tearDown() throws Exception {


        client.removeUser(null, getDeveloper());
        assert !client.isDeveloperExist(getDeveloper());

        JiraCISClient developClient = new JiraCISClient("http://localhost:8080/", getDeveloper().getId(), getDeveloper().getPassword());
        assert !developClient.authenticate();

        client.removeProject(getProject());
        assert !client.isProjectExist(getProject());


    }

    @Test
    public void testName() throws Exception {


        // 注意project key的问题
        client.createProject(getProject());

        assert client.isProjectExist(getProject());

        String role = "admin111";

      /*  client.createRoleIfNecessary(getProject(), role);

        client.assignUsersToRole(getProject(), null, getDeveloper());

      assert client.isRoleExist(role);
*/

    }

    private Project getProject() {
        Project project = new Project();
        project.setArtifactId("dddlib");
        project.setGroupId("org.dayatang");
        project.setProjectName("dddlib");
        project.setProjectLead("projectLeader");
        project.setDescription("description");
        project.setProjectLead(username);
        return project;
    }

    private Developer getDeveloper() {
        Developer developer = new Developer();
        developer.setName("name");
        developer.setPassword("12345678");
        developer.setEmail("kkkkkkk@1dfdf.com");
        developer.setId("username");
        developer.setFullName("fullname");
        return developer;
    }

}
