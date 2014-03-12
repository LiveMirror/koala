package org.openkoala.opencis.jenkins;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.net.MalformedURLException;
import java.util.UUID;


@Ignore
public class JenkinsCISClientIntegrationTest {


    public static String jenkinsURL = "http://10.108.1.92:8080/jenkins";
    //public static String jenkinsURL = "http://127.0.0.1:8989";
    public static String username = "admin";
    public static String apiToken = "6672ca5d5fad881359029c7165eb57a6";

    public static String svnUrl = "http://10.108.1.138/svn/projec";

    @Test
    public void test() throws MalformedURLException {

        Project project = getProject("zzzxssx");

        JenkinsCISClient client = new JenkinsCISClient(jenkinsURL, username, apiToken);

        assert client.authenticate();

        client.setKoalaScmConfig(new KoalaSvnConfig(svnUrl));

        client.createProject(project);

        String name = "xddddfdfadasdf";
        String name1 = "xxxx1";


        client.createUserIfNecessary(project, getDeveloper(name));

        client.assignUsersToRole(project, "", getDeveloper(name));
        //client.removeProject(project);
        client.close();


    }

    public Developer getDeveloper(String name) {
        Developer developer = new Developer();
        developer.setName("中文名1");
        developer.setId(name);
        developer.setPassword("20140305");
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
