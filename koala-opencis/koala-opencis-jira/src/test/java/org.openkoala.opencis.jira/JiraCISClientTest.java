package org.openkoala.opencis.jira;

import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 2/11/14
 * Time: 11:19 AM
 */
@Ignore
public class JiraCISClientTest {

    /*    private String username = "foreverosstest";

        private JiraCISClient client = new JiraCISClient("http://localhost:8080/", username, "f12345678");*/
    private String username = "admin";

    private String url = "http://10.108.1.92:8082/";

    private JiraCISClient client = new JiraCISClient(url, username, "admin");

    @Before
    public void setUp() throws Exception {
        assert client.authenticate();


    }

    //@After
    public void tearDown() throws Exception {


        client.removeProject(getProject());
        assert !client.isProjectExist(getProject());


    }

    @Test
    public void testName() throws Exception {

        // 注意project key的问题
        client.createProject(getProject());

        assert client.isProjectExist(getProject());


        for (Developer developer : createDevelopers(10)) {
            client.createUserIfNecessary(null, developer);
            client.assignUsersToRole(getProject(), "", developer);

            JiraCISClient developClient = new JiraCISClient(url, developer.getId(), developer.getPassword());
            assert developClient.authenticate();
            assert client.isUserAtProjectDevelopRole(getProject(), developer);
        }


        for (Developer developer : createDevelopers(10)) {
            client.createUserIfNecessary(null, developer);
            client.assignUsersToRole(getProject(), "", developer);
        }

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


    private List<Developer> createDevelopers(int count) {
        List<Developer> developers = new ArrayList<Developer>();
        for (int i = count; i > 0; i--) {
            developers.add(getDeveloper("usernamexx" + i));
        }
        return developers;

    }

    private Developer getDeveloper(String id) {
        Developer developer = new Developer();
        developer.setName("中文全名");
        developer.setPassword("12345678");
        developer.setEmail(id + "kkkkkkk@1dfdf.com");
        developer.setId(id);
        developer.setFullName("中文全名");
        return developer;
    }

}
