package org.openkoala.opencis.git;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.*;
import org.openkoala.opencis.CISClientBaseRuntimeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 连接GitLab服务器并实现创建项目等操作的客户端.
 * zhaizhijun，注：目前使用第三方库来实现，第三方库中有个GitlabHTTPRequestor，如果重复使用它，<p/>
 * 可能会有不可预知的问题，所以，每次使用它都需要重新创建一个。下个版本进行重写!TODO
 *
 * @author xmfang
 */
public class GitClient {

    private String username;
    private String password;
    private String email;
    private String localRepositoryPath;


    public GitClient(String username, String password, String email, String localRepositoryPath) {
        if (StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(localRepositoryPath)) {
            throw new CISClientBaseRuntimeException("username, password, email, localRepositoryPath must no be empty!");
        }

        this.username = username;
        this.password = password;
        this.email = email;
        this.localRepositoryPath = localRepositoryPath;
    }


    public void pushRepositoryToRemote(String remoteRepositoryUrl) {
        if (StringUtils.isBlank(localRepositoryPath)) {
            throw new CISClientBaseRuntimeException("project's physicalPath must bet not null!");
        }


        try {
            PushCommand pushCommand = Git.open(new File(localRepositoryPath)).push();

            pushCommand.setRemote(remoteRepositoryUrl);

            CredentialsProvider credentialsProvider =
                    new UsernamePasswordCredentialsProvider(username, password);

            pushCommand.setCredentialsProvider(credentialsProvider).call();

        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("git.pushRepositoryToRemote.IOException", e);
        } catch (GitAPIException e) {
            throw new CISClientBaseRuntimeException("git.pushRepositoryToRemote.GitAPIException", e);
        }
    }

    public void add(String filePattern) {
        Git git = null;
        try {
            git = Git.open(new File(localRepositoryPath));
            git.add().addFilepattern(filePattern).call();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.add.IOException", e);

        } catch (NoFilepatternException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.add.NoFilepatternException", e);

        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.add.GitAPIException", e);

        }


    }

    public void commit(String msg) {
        msg = StringUtils.isEmpty(msg) ? "default msg" : msg;

        try {

            Git git = Git.open(new File(localRepositoryPath));
            git.commit().setCommitter(username, email).setMessage(msg).call();

        } catch (NoMessageException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.haveToSetMessage", e);
        } catch (WrongRepositoryStateException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.WrongRepositoryStateException", e);

        } catch (IOException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.IOException", e);

        } catch (NoFilepatternException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.NoFilepatternException", e);

        } catch (UnmergedPathsException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.UnmergedPathsException", e);

        } catch (NoHeadException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.NoHeadException", e);

        } catch (ConcurrentRefUpdateException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.ConcurrentRefUpdateException", e);

        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.commit.GitAPIException", e);
        }

    }

    public static void init(String repositoryPhysicalPath, String remoteRepositoryUrl) {
        Repository repository = null;
        InitCommand init = new InitCommand();

        if (StringUtils.isBlank(repositoryPhysicalPath)) {
            throw new CISClientBaseRuntimeException("project's physicalPath must bet not null!");
        }

        init.setDirectory(new File(repositoryPhysicalPath));
        init.setBare(false);
        try {
            Git git = init.call();

            repository = git.getRepository();

            git.add().addFilepattern(".").call();

            StoredConfig config = repository.getConfig();

            RemoteConfig remoteConfig = new RemoteConfig(config, "origin");

            URIish uri = new URIish(remoteRepositoryUrl);

            remoteConfig.addURI(uri);
            remoteConfig.addPushURI(uri);

            remoteConfig.update(config);
            config.save();

        } catch (URISyntaxException e) {
            throw new CISClientBaseRuntimeException("git.init.URISyntaxException", e);
        } catch (IOException e) {
            throw new CISClientBaseRuntimeException("git.init.IOException", e);
        } catch (GitAPIException e) {
            throw new CISClientBaseRuntimeException("git.init.GitAPIException", e);
        }
    }


    /**
     * 判断某文件夹是否已经init
     *
     * @param folderPath
     * @return
     */
    public static boolean isInited(String folderPath) {
        assert StringUtils.isNotEmpty(folderPath);

        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new CISClientBaseRuntimeException("It must be a folder");
        }
        if (!folder.exists()) {
            throw new CISClientBaseRuntimeException("folder not found", new FileNotFoundException());
        }

        String[] names = folder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ".git".equals(name) && dir.isDirectory();
            }
        });

        return names.length != 0;
    }


    public static void clone(String repositoryPath, File downToPathFile) {
        try {
            Git.cloneRepository().setURI(repositoryPath).setDirectory(downToPathFile).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("git.clone.GitAPIException", e);

        }
    }
}
