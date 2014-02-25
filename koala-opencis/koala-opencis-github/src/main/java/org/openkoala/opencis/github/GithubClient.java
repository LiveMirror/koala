package org.openkoala.opencis.github;

import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.git.GitClient;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * github客户端
 */
public class GithubClient {

    private GitHub github = null;

    private String username;

    public GithubClient(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new CISClientBaseRuntimeException("username or password must not bet empty");
        }

        try {
            this.username = username;
            github = GitHub.connectUsingPassword(username, password);
        } catch (IOException e) {
            // 因为目前只是设置参数，github未实际连接，所以，不需要处理
            throw new CISClientBaseRuntimeException("Unknown exception", e);
        }
    }

    /**
     * 创建项目仓库
     *
     * @param repositoryName
     * @param description
     */
    public void createRepository(String repositoryName, String description) {
        if (isRepositoryExist(repositoryName)) {
            return;
        }

        String emptyHomePage = "";
        Boolean PUBLIC = true;
        try {
            github.createRepository(repositoryName, description, emptyHomePage, PUBLIC);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.createProject.failure", e);
        }

    }

    /**
     * 移除项目仓库
     *
     * @param repositoryName
     */
    public void removeRepository(String repositoryName) {
        if (!isRepositoryExist(repositoryName)) {
            return;
        }
        try {
            GHRepository repository = github.getRepository(getRepositoryFullName(repositoryName));
            repository.delete();
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.removeProject.failure", e);
        }
    }

    /**
     * 移除协作者
     *
     * @param repositoryName
     * @param developsGithubAccounts
     */
    public void removeCollaborators(String repositoryName, String... developsGithubAccounts) {
        if (!isRepositoryExist(repositoryName)) {
            return;
        }
        try {
            GHRepository repository = github.getRepository(getRepositoryFullName(repositoryName));
            for (String login : developsGithubAccounts) {
                if (isAccountExist(login)) {
                    repository.removeCollaborators(github.getUser(login));
                }
            }
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.assignUsersToRole.failure", e);
        }
    }

    /**
     * 添加协作者
     *
     * @param repositoryName
     * @param developsGithubAccounts
     */
    public void addCollaborators(String repositoryName, String... developsGithubAccounts) {
        if (!isRepositoryExist(repositoryName)) {
            return;
        }
        try {
            GHRepository repository = github.getRepository(getRepositoryFullName(repositoryName));
            for (String login : developsGithubAccounts) {
                if (isAccountExist(login)) {
                    repository.addCollaborators(github.getUser(login));

                }
            }
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.assignUsersToRole.failure", e);
        }
    }

    /**
     * 认证
     *
     * @return
     */
    public boolean authenticate() {
        try {
            return github.isCredentialValid();
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 仓库是否存在
     *
     * @param repositoryName
     * @return
     */
    public boolean isRepositoryExist(String repositoryName) {
        if (repositoryName == null || "".equals(repositoryName.trim())) {
            throw new CISClientBaseRuntimeException("project'name must not be empty");
        }
        try {
            return github.getRepository(getRepositoryFullName(repositoryName)) != null;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.isProjectExist.failure", e);
        }
    }


    /**
     * 得到仓库全名。
     *
     * @param repository
     * @return 帐号名/仓库名
     */
    public String getRepositoryFullName(String repository) {
        return username + "/" + repository;
    }

    public String getHttpTransportUrl(String repository) {
        try {
            return github.getRepository(getRepositoryFullName(repository)).gitHttpTransportUrl();
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("github.getRepositoryFullName", e);
        }
    }

    /**
     * github帐号是否存在
     *
     * @param account
     * @return
     */
    public boolean isAccountExist(String account) {
        try {
            return github.getUser(account) != null;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("find github account failure", e);
        }
    }
}
