package org.openkoala.opencis.redmine;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjzhai on 3/18/14.
 */
@Ignore
public class MainTest {

    String url = "http://localhost:49153";
    String username = "admin";
    String password = "admin";
    String key = "7514e272dc5d4461987762b5ceb4f5d4b2757bb3";

    String leaderName = "leaderXX";

    @Test
    public void testName() throws Exception {
        //
        RedmineClient client = new RedmineClient(url, username, password);

        client.createUserIfNecessary(null, createDeveloper(leaderName));

        assert client.isExist(createDeveloper(leaderName));

        Project project = getProject("projectXXxxx");

        project.setProjectLead(leaderName);

        client.createProject(project);

        assert client.isMemberOfProject(project, leaderName, RedmineClient.PROJECT_MANAGER_ROLE);
        assert client.isExist(project);

        // create some developers
        for (Developer developer : createDevelopers(new String[]{"qweqweqwe", "qweqwe1", "xxxxxx"})) {
            client.createUserIfNecessary(null, developer);
            assert client.isExist(developer);
            client.assignUsersToRole(project, null, developer);
            assert client.isMemberOfProject(project, developer.getId(), RedmineClient.DEVELOPER_ROLE);
        }

        Developer otherDeveloper = createDeveloper("nununununiu");
        client.createUserIfNecessary(null, otherDeveloper);
        assert client.isExist(otherDeveloper);
        assert !client.isMemberOfProject(project, otherDeveloper.getId(), RedmineClient.DEVELOPER_ROLE);


    }

    public Project getProject(String name) {
        Project project = new Project();
        project.setProjectName(name);
        project.setDescription("description");
        return project;
    }

    private List<Developer> createDevelopers(String[] names) {
        List<Developer> developers = new ArrayList<Developer>();

        for (String each : names) {
            developers.add(createDeveloper(each));
        }

        return developers;
    }

    private Developer createDeveloper(String name) {
        Developer developer = new Developer();
        developer.setId(name);
        developer.setName(name);
        developer.setEmail(name + "gg@gmail.com");
        developer.setFullName(name);
        developer.setPassword("123123123");
        return developer;

    }

}
