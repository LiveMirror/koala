package org.openkoala.opencis.github;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * github客户端
 */
public class GithubClient implements CISClient {

    private GitHub github = null;

    private String username;


    public GithubClient(String username, String password) {
        try {
            this.username = username;
            github = GitHub.connectUsingPassword(username, password);
        } catch (IOException e) {
            // 因为目前只是设置参数，github未实际连接，所以，不需要处理
        }
    }

    @Override
    public void createProject(Project project) {
        if (isProjectExist(project)) {
            return;
        }

        String emptyHomePage = "";
        try {
            github.createRepository(project.getProjectName(), project.getDescription(), emptyHomePage, true/*public*/);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.createProject.failure", e);
        }

    }

    @Override
    public void removeProject(Project project) {
        if (!isProjectExist(project)) {
            return;
        }
        try {
            GHRepository repository = github.getRepository(getRepositoryName(project));
            repository.delete();
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.removeProject.failure", e);
        }
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        // do nothing
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        // do nothing
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        // do nothing
    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        if (!isProjectExist(project)) {
            return;
        }
        try {
            GHRepository repository = github.getRepository(getRepositoryName(project));
            repository.addCollaborators();
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.assignUsersToRole.failure", e);
        }

    }

    @Override
    public boolean authenticate() {
        try {
            return github.isCredentialValid();
        } catch (IOException e) {
            return false;
        }
    }


    public boolean isProjectExist(Project project) {
        try {
            return github.getRepository(getRepositoryName(project)) != null;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.isProjectExist.failure", e);
        }
    }


    public String getRepositoryName(Project project) {
        return username + "/" + project.getProjectName();
    }


    @Override
    public void close() {

    }
}
