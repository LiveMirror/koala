package org.openkoala.opencis.redmine;

import org.openkoala.opencis.api.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjzhai on 3/18/14.
 */
@Ignore
public class MainTest {

    String url = "http://192.168.1.104:9003";
    String username = "admin";
    String password = "admin";

    String leaderName = "leadjjggerbjbXX";

    @Test
    public void testName() throws Exception {
        RedmineClient client = new RedmineClient(url, username, password);


        for (int i = 600; i >= 0; i--) {

            client.createUserIfNecessary(null, createDeveloper(leaderName));

            assert client.isExist(createDeveloper(leaderName));

            Project project = getProject("xxxxxx" + i);

            project.setProjectLead(leaderName);

            client.createProject(project);

            assert client.isMemberOfProject(project, leaderName, RedmineClient.PROJECT_MANAGER_ROLE);
            assert client.isExist(project);

            // create some developers
            for (Developer developer : createDevelopers(new String[]{"qweqqbjbwjjnne1qwge", "qwebjjqjbqw1genn1", "qjjxxxbgj1nnbxxx"})) {
                client.createUserIfNecessary(null, developer);
                assert client.isExist(developer);
                client.assignUsersToRole(project, null, developer);
                assert client.isMemberOfProject(project, developer.getId(), RedmineClient.DEVELOPER_ROLE);
            }

            Developer otherDeveloper = createDeveloper("nunnjj1njuqnbbunu1gniu");
            client.createUserIfNecessary(null, otherDeveloper);
            assert client.isExist(otherDeveloper);
            assert !client.isMemberOfProject(project, otherDeveloper.getId(), RedmineClient.DEVELOPER_ROLE);
            System.out.println(i);
        }


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
