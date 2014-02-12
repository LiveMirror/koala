package org.openkoala.opencis.git.impl;

import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;


@Ignore
public class GitlabCISClientIntegrationTest {

    private String token;

    private GitlabCISClient cisClient;

    @Before
    public void setUp() throws Exception {
        token = getToken();
        cisClient = new GitlabCISClient(getConfiguration());
        assert cisClient.authenticate();
    }

    @After
    public void tearDown() throws Exception {
        for (Developer developer : createDevelops()) {
            assert !cisClient.isUserExist(developer);
        }

    }

    @Test
    public void test1() throws Exception {
        Project project = createProject();

        cisClient.createProject(project);
        assert cisClient.isProjectExist(project);


        for (Developer developer : createDevelops()) {
            cisClient.createUserIfNecessary(project, developer);
            assert cisClient.isUserExist(developer);
            cisClient.assignUsersToRole(project, "", developer);
        }


        for (Developer developer : createDevelops()) {
            GitlabCISClient cisClient1 = new GitlabCISClient(getConfiguration());
            cisClient1.removeUser(project, developer);
        }

        cisClient.removeProject(project);
        assert !cisClient.isProjectExist(project);


    }

    private List<Developer> createDevelops() {
        List<Developer> result = new ArrayList<Developer>();
        for (String id : Arrays.asList("id4", "id5", "id6")) {
            result.add(createDevelop(id));
        }

        return result;
    }

    private Project createProject() {
        Project project = new Project();
        project.setArtifactId("projectforte222");
        project.setDescription("This project is for test");
        project.setProjectName("projettt666");
        project.setPhysicalPath(GitlabCISClientIntegrationTest.class.getResource("/ProjectTest/").getFile());
        return project;
    }

    private Developer createDevelop(String id) {
        Developer developer = new Developer();
        developer.setId(id);
        developer.setPassword("12345678");
        developer.setEmail(id + "@123.com");
        developer.setName(developer.getId() + "name");
        return developer;
    }


    private GitlabConfiguration getConfiguration() {
        GitlabConfiguration configuration = new GitlabConfiguration();

        configuration.setToken(getToken());
        configuration.setGitHostURL("http://127.0.0.1");
        configuration.setAdminUsername("root");
        configuration.setAdminEmail("admin@local.com");
        configuration.setAdminPassword("5iveL!fe");
        return configuration;
    }


    public String getToken() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://127.0.0.1/api/v3/session");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", "admin@local.host"));
        params.add(new BasicNameValuePair("password", "5iveL!fe"));


        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);

            String jsonStr = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();

            return gson.fromJson(jsonStr, GUser.class).getPrivate_token();

        } catch (IOException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("gitlab.getCurrentUser.IOException", e);
        }
    }


}
