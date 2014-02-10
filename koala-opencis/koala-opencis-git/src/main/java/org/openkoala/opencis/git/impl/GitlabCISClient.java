package org.openkoala.opencis.git.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.CISClientBaseRuntimeException;

/**
 * 连接GitLab服务器并实现创建项目等操作的客户端.
 * zhaizhijun，注：目前使用第三方库来实现，第三方库中有个GitlabHTTPRequestor，如果重复使用它，<p/>
 * 可能会有不可预知的问题，所以，每次使用它都需要重新创建一个。下个版本进行重写!TODO
 *
 * @author xmfang
 */
public class GitlabCISClient implements CISClient {

    public final static int DEVELOPER = 30;

    /*
     * gitlab配置
     */
    private GitlabConfiguration gitLabConfiguration;

    private GitlabAPI gitlabAPI;

    public GitlabCISClient(GitlabConfiguration gitLabConfiguration) {
        this.gitLabConfiguration = gitLabConfiguration;
    }


    private Set<GitlabUser> getCurrentGitlabUsers() {
        Set<GitlabUser> currentGitlabUsers = new HashSet<GitlabUser>();
        List<LinkedHashMap<String, Object>> usersMap = null;
        try {
            usersMap = createGitlabHTTPRequestor().method("GET").to(GitlabUser.URL, List.class);
        } catch (IOException e) {
            e.printStackTrace();
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
        pushProjectToGitLab(project, gitLabConfiguration);
    }

    public GitlabProject getGitlabProjectBy(Project project) {
        try {
            for (GitlabProject gitlabProject : gitlabAPI.getProjects()) {
                if (gitlabProject.getName().equals(project.getProjectName())) {
                    return gitlabProject;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
    private boolean pushProjectToGitLab(Project project, GitlabConfiguration gitlabConfiguration) {
        Repository repository = null;
        InitCommand init = new InitCommand();

        String projectPath = project.getPhysicalPath();
        if (StringUtils.isBlank(projectPath)) {
            throw new CISClientBaseRuntimeException("project's physicalPath must bet not null!");
        }

        init.setDirectory(new File(projectPath));
        try {
            Git git = init.call();
            repository = git.getRepository();
            StoredConfig config = repository.getConfig();
            RemoteConfig remoteConfig = new RemoteConfig(config, "origin");

            URIish uri = new URIish(gitLabConfiguration.getGitHostURL()
                    + "/" + getCurrentUser().getUsername()
                    + "/" + project.getProjectName().toLowerCase() + ".git");

            RefSpec refSpec = new RefSpec("+refs/heads/*:*");
            remoteConfig.addFetchRefSpec(refSpec);
            remoteConfig.addPushRefSpec(refSpec);
            remoteConfig.addURI(uri);
            remoteConfig.addPushURI(uri);

            remoteConfig.update(config);
            config.save();
            repository.close();

            git.add().addFilepattern(".").call();
            git.commit().setCommitter(gitLabConfiguration.getAdminUsername(),
                    gitLabConfiguration.getAdminEmail()).setMessage("init project").call();

            CredentialsProvider credentialsProvider =
                    new UsernamePasswordCredentialsProvider(gitLabConfiguration.getAdminUsername(),
                            gitLabConfiguration.getAdminPassword());
            git.push().setCredentialsProvider(credentialsProvider).call();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("gitlab.pushProjectToGitLab.URISyntaxException", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("gitlab.pushProjectToGitLab.IOException", e);
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("gitlab.pushProjectToGitLab.GitAPIException", e);
        }
        return true;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private GitlabUser getCurrentUser() {
        GitlabUser result = null;
        try {
            result = createGitlabHTTPRequestor().method("GET").to("/user", GitlabUser.class);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("gitlab.getCurrentUser.IOException", e);
        }
        return result;
    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        if (isUserExist(developer)) {
            return;
        }
        try {
            createGitlabHTTPRequestor().method("POST")
                    .with("email", developer.getEmail()).with("username",
                    developer.getId()).with("name", developer.getName()).with("password", developer.getPassword())
                    .to(GitlabUser.URL, GitlabUser.class);
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("gitlab removeUser failure!", e);
        }
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        // do nothing
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
                        .with("user_id", userId).with("access_level", DEVELOPER)
                        .to(wsUrl, GitlabUser.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CISClientBaseRuntimeException("gitlab.addProjectTeamMember.IOException", e);
            }
        }

    }

    @Override
    public boolean authenticate() {
        gitlabAPI = GitlabAPI.connect(gitLabConfiguration.getGitHostURL(), gitLabConfiguration.getToken());
        return gitlabAPI != null;
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
        GitlabAPI gitlabAPI = GitlabAPI.connect(gitLabConfiguration.getGitHostURL(), gitLabConfiguration.getToken());
        GitlabHTTPRequestor gitlabHTTPRequestor = new GitlabHTTPRequestor(gitlabAPI);
        return gitlabHTTPRequestor;
    }


}
