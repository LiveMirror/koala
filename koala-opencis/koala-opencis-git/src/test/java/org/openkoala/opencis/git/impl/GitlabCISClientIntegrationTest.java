package org.openkoala.opencis.git.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
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

    @Test
    public void test1() throws Exception {
        Project project = new Project();
        project.setArtifactId("projectfortes111");
        project.setDescription("This project is for test");
        project.setProjectName("proje33333");
        project.setPhysicalPath(GitlabCISClientIntegrationTest.class.getResource("/ProjectTest/").getFile());
        cisClient.createProject(project);
        assert cisClient.projectExist(project);




        cisClient.removeProject(project);
        assert !cisClient.projectExist(project);


    }

    private GitlabConfiguration getConfiguration() {
        GitlabConfiguration configuration = new GitlabConfiguration();
        configuration.setToken(token);
        configuration.setGitHostURL("http://127.0.0.1/");
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
