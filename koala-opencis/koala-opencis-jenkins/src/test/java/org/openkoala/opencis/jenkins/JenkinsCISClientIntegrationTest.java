package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.net.MalformedURLException;
import java.util.UUID;


@Ignore
public class JenkinsCISClientIntegrationTest {


    public static String jenkinsURL = "http://127.0.0.1:8989";
    //public static String jenkinsURL = "http://127.0.0.1:8989";
    public static String username = "admin";
    public static String apiToken = "9cfe23e07f7d5e3df3dd9d3207cfcc67";

    public static String svnUrl = "http://10.108.1.138/svn/projec";

    @Test
    public void test() throws MalformedURLException {

        Project project = getProject("uuuuu");

        JenkinsCISClient client = new JenkinsCISClient(jenkinsURL, username, apiToken);

        assert client.authenticate();

        client.setKoalaScmConfig(new KoalaSvnConfig(svnUrl));

        client.createProject(project);

        String name = "uuuuuu";
        String name1 = "xxxx1";


        client.createUserIfNecessary(project, getDeveloper(name));
        client.createUserIfNecessary(project, getDeveloper(name1));
        project.setProjectLead(name1);

        client.assignUsersToRole(project, "", getDeveloper(name));
        //client.removeProject(project);
        client.close();


    }

    public Developer getDeveloper(String name) {
        Developer developer = new Developer();
        developer.setName("中文名1");
        developer.setId(name);
        developer.setPassword("123");
        developer.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        return developer;
    }


    public Project getProject(String name) {
        Project project = new Project();
        project.setProjectName(name);
        project.setGroupId("com.c");
        project.setPhysicalPath("plpl");
        project.setDescription("中文描述");
        project.setArtifactId(name);
        return project;
    }


}
