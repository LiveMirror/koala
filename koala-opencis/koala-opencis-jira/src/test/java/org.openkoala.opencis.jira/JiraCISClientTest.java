package org.openkoala.opencis.jira;

import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

/**
 * User: zjzhai
 * Date: 2/11/14
 * Time: 11:19 AM
 */
public class JiraCISClientTest {

    public String username = "jira";

    @Test
    public void testName() throws Exception {


        Project project = new Project();
        project.setArtifactId("dddlib");
        project.setGroupId("org.dayatang");
        project.setProjectName("dddlib");
        project.setProjectLead("projectLeader");
        project.setDescription("description");
        project.setProjectLead(username);

        JiraCISClient client = new JiraCISClient("http://localhost:8080/", username, "12345678");
        assert client.authenticate();

        client.createProject(project);

        assert client.isProjectExist(project);


        client.createUserIfNecessary(project, createDeveloper());


    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setName("name");
        developer.setPassword("opopopopop");
        developer.setEmail("kkkkkkk@1dfdf.com");
        developer.setId("username");
        developer.setFullName("fullname");
        return developer;
    }

}
