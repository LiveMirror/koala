package org.openkoala.opencis.jenkins;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.client.JenkinsClient;
import nl.tudelft.jenkins.client.JenkinsClientFactory;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.client.exceptions.NoSuchUserException;
import nl.tudelft.jenkins.jobs.Job;
import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {

    /**
     * 源码版本控制 svn or git
     */
    private KoalaScmConfig koalaScmConfig;

    private JenkinsClient client;

    public JenkinsCISClient(String jenkinsUrl, String username, String passwordOrAPIToken) {
        JenkinsClientFactory factory = new JenkinsClientFactory(convert(jenkinsUrl), username, passwordOrAPIToken);
        client = factory.getJenkinsClient();
    }

    public void setKoalaScmConfig(KoalaScmConfig koalaScmConfig) {
        this.koalaScmConfig = koalaScmConfig;
    }

    @Override
    public void close() {
        client.close();
    }


    @Override
    public void createProject(Project project) {
        project.validate();

        if (existProject(project)) {
            return;
        }

        client.createJob(project.getProjectName(),
                koalaScmConfig.getScmConfig(), new ArrayList<User>());
    }

    private boolean existProject(Project project) {
        try {
            client.retrieveJob(project.getProjectName());
            return true;
        } catch (NoSuchJobException e) {
            return false;
        }
    }

    @Override
    public void removeProject(Project project) {
        try {
            Job job = client.retrieveJob(project.getProjectName());
            if (null != job) {
                client.deleteJob(job);
            }
        } catch (NoSuchJobException e) {
            return;
        }
    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        client.createUser(developer.getId(), developer.getPassword(),
                developer.getEmail(), developer.getName());
        addUserToProjectMatrixAuthorization(developer.getId());
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        try {
            User user =
                    client.retrieveUser(developer.getId());
            if (null != user) client.deleteUser(user);
        } catch (NoSuchUserException e) {
            return;
        }

    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        assert StringUtils.isNotEmpty(project.getProjectLead());

        Job job;
        try {
            job = client.retrieveJob(project.getProjectName());
        } catch (NoSuchJobException e) {
            throw new CISClientBaseRuntimeException("jenkins.NoSuchJobException", e);
        }
        for (User user : createByDeveloper(developers)) {
            job.addPermissionsForUser(user);
            job.addNotificationRecipient(user);
        }

        job.addFullPermissionsForUser(createByDeveloper(project.getProjectLead(),""));

        client.updateJob(job);
    }

    @Override
    public boolean authenticate() {
        return client.validateServerOnEndpoint();
    }

    private URL convert(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new CISClientBaseRuntimeException("jenkins.URL.MalformedURLException", e);
        }

    }

    private List<User> createByDeveloper(Developer... developers) {
        List<User> result = new ArrayList<User>();

        for (Developer each : developers) {
            System.out.println(each.getId() + "++++++++++++++++++");
            result.add(createByDeveloper(each.getId(), each.getEmail()));
        }

        return result;
    }

    public void addUserToProjectMatrixAuthorization(String developerId){
        client.sendScriptText(MessageFormat.format(getUserAuthorizationConfig(), developerId));
    }

    private String getUserAuthorizationConfig() {
        String configFileName = "UserAuthorizationConfig.groovy";
        if ( null == JenkinsCISClient.class.getResource("/" + configFileName)) throw new CISClientBaseRuntimeException("Not found UserAuthorizationConfig.groovy");

        File file = new File(getClass().getResource("/" + configFileName).getFile());
        byte[] fileContent = new byte[Long.valueOf(file.length()).intValue()];
        try {
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(fileContent);
            inputStream.close();
        } catch (FileNotFoundException e) {
            throw new CISClientBaseRuntimeException("Not found UserAuthorizationConfig.groovy",e);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("There is a Exception when readUserAuthorizationConfig.groovy",e);
        }
        return new String(fileContent);
    }


    private User createByDeveloper(String developerId, String developEmail) {
        System.out.println(developerId + "]]]]]]]]]]]]]");
        return new UserImpl(developerId,developEmail);
    }

}
