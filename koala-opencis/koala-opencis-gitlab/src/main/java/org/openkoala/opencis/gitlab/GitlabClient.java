package org.openkoala.opencis.gitlab;

import org.apache.commons.lang3.StringUtils;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.git.GitClient;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 连接GitLab服务器并实现创建项目等操作的客户端.
 * zhaizhijun，注：目前使用第三方库来实现，第三方库中有个GitlabHTTPRequestor，如果重复使用它，<p/>
 *
 * @author xmfang
 */
public class GitlabClient implements CISClient {

    public final static int DEVELOPER_MODE = 30;

    /*
     * gitlab配置
     */
    private GitlabConfiguration config;

    private GitlabAPI gitlab;

    public GitlabClient(GitlabConfiguration config) {
        this.config = config;
    }


    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void createProject(Project project) {
        if (isProjectExist(project)) return;

        createProjectInGitLab(project);

        try {
            Thread.sleep(5000);
            pushProjectToGitLab(project, config);
        } catch (InterruptedException e) {
            throw new CISClientBaseRuntimeException(e);
        }
    }


    public boolean isProjectExist(Project project) {
        try {
            return gitlab.getProject(getProjectFullName(project)) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void removeProject(Project project) {
        try {
            gitlab.deleteProject(getProjectFullName(project));
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab removeProject failure", e);
        }
    }

    /**
     * 在gitlab中创建项目.
     *
     * @param project
     */
    private GitlabProject createProjectInGitLab(Project project) {
        GitlabProject gitlabProject = null;
        try {

            gitlabProject = new GitlabProject();
            gitlabProject.setName(project.getProjectName());
            gitlabProject.setDescription(project.getDescription());
            gitlabProject = gitlab.createProject(gitlabProject);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.createProject.IOException", e);
        }
        return gitlabProject;
    }


    /**
     * 根据用户帐号获取Gitlab用户的id.
     *
     * @param developerId
     * @return
     */
    public Integer getUserIdByDeveloper(String developerId) {

        try {
            GitlabUser user = gitlab.getUser(developerId);
            if (null == user) return null;
            return user.getId();
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * 推送项目到Gitlab中.
     *
     * @param project
     */
    private void pushProjectToGitLab(Project project, GitlabConfiguration config) {
        String projectPath = project.getPhysicalPath();
        if (StringUtils.isBlank(projectPath)) {
            throw new CISClientBaseRuntimeException("project's physicalPath must bet not null!");
        }

        GitClient.init(project.getPhysicalPath(), getHttpTransportUrl(project.getProjectName(), config));

        GitClient gitClient = new GitClient(config.getUsername(), config.getPassword(), config.getEmail(), project.getPhysicalPath());

        gitClient.add(".");

        gitClient.commit("init project");

        gitClient.pushRepositoryToRemote(getHttpTransportUrl(project.getProjectName(), config));

    }

    private String getHttpTransportUrl(String projectName, GitlabConfiguration gitlabConfiguration) {
        assert StringUtils.isNotEmpty(projectName);
        return gitlabConfiguration.getGitlabUserUrl() + projectName.toLowerCase() + ".git";
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        if (isUserExist(developer)) {
            return;
        }
        try {
            GitlabUser user = new GitlabUser();
            user.setUsername(developer.getId());
            user.setEmail(developer.getEmail());
            user.setName(developer.getName());

            gitlab.createUser(user, developer.getPassword());
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.createUser.IOException", e);
        }
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        try {
            gitlab.deleteUser(developer.getId());
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab removeUser failure!", e);
        }
    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer developer : developers) {
            Integer userId = getUserIdByDeveloper(developer.getId());
            if (userId == null) throw new CISClientBaseRuntimeException("gitlab.assignUsersToRole.notFoundTheUser");
            try {
                gitlab.assignUserAsDeveloperOfProject(developer.getId(), getProjectFullName(project));
            } catch (IOException e) {
                throw new CISClientBaseRuntimeException("gitlab.assignUsersToRole.IOException", e);
            }
        }

    }


    @Override
    public boolean authenticate() {
        gitlab = GitlabAPI.connect(config.getGitlabHostURL(), config.getToken());
        return gitlab != null;
    }

    /**
     * 根据用户帐号检查该帐号是否已经存在
     *
     * @param developer
     * @return
     */
    public boolean isUserExist(Developer developer) {
        try {
            return gitlab.getUser(developer.getId()) != null;
        } catch (IOException e) {
            return false;
        }
    }

    public String getProjectFullName(Project project) {
        return config.getUsername() + "/" + project.getProjectName();
    }


}
