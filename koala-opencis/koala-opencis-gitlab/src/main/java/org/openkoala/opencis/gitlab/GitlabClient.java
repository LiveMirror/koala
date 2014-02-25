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
import java.net.URLEncoder;
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


    private Set<GitlabUser> getCurrentGitlabUsers() {
        Set<GitlabUser> currentGitlabUsers = new HashSet<GitlabUser>();
        List<LinkedHashMap<String, Object>> usersMap = null;
        try {
            usersMap = createGitlabHTTPRequestor().method("GET").to(GitlabUser.URL, List.class);
        } catch (IOException e) {

            throw new CISClientBaseRuntimeException("gitlab.getUsers.IOException", e);
        }
        for (LinkedHashMap<String, Object> userMap : usersMap) {
            GitlabUser user = new GitlabUser();
            user.setId((Integer) userMap.get("id"));
            user.setEmail(String.valueOf(userMap.get("email")));
            user.setUsername(String.valueOf(userMap.get("username")));
            user.setName(String.valueOf(userMap.get("name")));
            user.setState(String.valueOf(userMap.get("state")));
            currentGitlabUsers.add(user);
        }
        return currentGitlabUsers;
    }


    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void createProject(Project project) {
        if (isProjectExist(project)) {
            return;
        }
        createProjectInGitLab(project);
        pushProjectToGitLab(project, config);
    }

    public GitlabProject getGitlabProjectBy(Project project) {
        try {
            for (GitlabProject gitlabProject : gitlab.getProjects()) {
                if (gitlabProject.getName().equals(project.getProjectName())) {
                    return gitlabProject;
                }
            }
        } catch (IOException e) {

            throw new CISClientBaseRuntimeException("gitClient'method:projectExist IOEXception", e);
        }
        return null;
    }

    public boolean isProjectExist(Project project) {
        return getGitlabProjectBy(project) != null;
    }

    @Override
    public void removeProject(Project project) {
        if (!isProjectExist(project)) {
            return;
        }
        try {
            createGitlabHTTPRequestor().method("DELETE").with("id", getGitlabProjectIdBy(project))
                    .to("/projects/" + getGitlabProjectIdBy(project), GitlabProject.class);
        } catch (IOException e) {

            throw new CISClientBaseRuntimeException("gitlab removeProject failure", e);
        }
    }

    public Integer getGitlabProjectIdBy(Project project) {
        if (!isProjectExist(project)) {
            throw new CISClientBaseRuntimeException("project not found");
        }
        return getGitlabProjectBy(project).getId();
    }

    /**
     * 在gitlab中创建项目.
     *
     * @param project
     */
    private GitlabProject createProjectInGitLab(Project project) {
        GitlabProject gitlabProject = null;
        try {
            gitlabProject = createGitlabHTTPRequestor().method("POST")
                    .with("name", project.getProjectName())
                    .with("description", project.getDescription())
                    .with("public", true)
                    .to(GitlabProject.URL, GitlabProject.class);

        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.createProject.IOException", e);
        }
        return gitlabProject;
    }


    /**
     * 根据用户帐号获取Gitlab用户的id.
     *
     * @param developer
     * @return
     */
    public Integer getUserIdByDeveloper(Developer developer) {
        for (GitlabUser user : getCurrentGitlabUsers()) {
            if (user.getUsername().equals(developer.getId())) {
                return user.getId();
            }
        }
        return null;
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

        GitClient.init(project.getPhysicalPath(), getHttpTransportUrl(project.getPhysicalPath(), config));

        GitClient gitClient = new GitClient(config.getUsername(), config.getPassword(), config.getEmail(), project.getPhysicalPath());

        gitClient.add(".");

        gitClient.commit("init project");

        gitClient.pushRepositoryToRemote(getHttpTransportUrl(project.getProjectName(), config));

    }

    private String getHttpTransportUrl(String projectName, GitlabConfiguration gitlabConfiguration) {
        assert StringUtils.isNotEmpty(projectName);
        return gitlabConfiguration.getGitlabUserUrl()
                + "/" + projectName.toLowerCase() + ".git";
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        if (isUserExist(developer)) {
            return;
        }
        try {
            createGitlabHTTPRequestor().method("POST")
                    .with("email", developer.getEmail()).with("username",
                    developer.getId()).with("name", URLEncoder.encode(developer.getName(), "UTF-8")).with("password", developer.getPassword())
                    .to(GitlabUser.URL, GitlabUser.class);
        } catch (IOException e) {

            throw new CISClientBaseRuntimeException("gitlab.createUser.IOException", e);
        }
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        if (!isUserExist(developer)) {
            return;
        }
        try {
            createGitlabHTTPRequestor().method("DELETE").with("id", getUserIdByDeveloper(developer))
                    .to(GitlabUser.URL + "/" + getUserIdByDeveloper(developer), GitlabUser.class);
        } catch (IOException e) {

            throw new CISClientBaseRuntimeException("gitlab removeUser failure!", e);
        }
    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer developer : developers) {
            Integer userId = getUserIdByDeveloper(developer);
            if (userId == null) {
                continue;
            }
            String wsUrl = "/projects/" + getGitlabProjectIdBy(project) + "/members";
            try {
                createGitlabHTTPRequestor().method("POST")
                        .with("id", getGitlabProjectIdBy(project))
                        .with("user_id", userId).with("access_level", DEVELOPER_MODE)
                        .to(wsUrl, GitlabUser.class);
            } catch (IOException e) {
                throw new CISClientBaseRuntimeException("gitlab.addProjectTeamMember.IOException", e);
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
        for (GitlabUser user : getCurrentGitlabUsers()) {
            if (user.getUsername().equals(developer.getId())) {
                return true;
            }
        }
        return false;
    }


    private GitlabHTTPRequestor createGitlabHTTPRequestor() {
        GitlabAPI gitlabAPI = GitlabAPI.connect(config.getGitlabHostURL(), config.getToken());
        GitlabHTTPRequestor gitlabHTTPRequestor = new GitlabHTTPRequestor(gitlabAPI);
        return gitlabHTTPRequestor;
    }


}
