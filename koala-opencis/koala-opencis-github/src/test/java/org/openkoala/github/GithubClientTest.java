package org.openkoala.github;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GitHub;
import org.openkoala.opencis.git.GitClient;
import org.openkoala.opencis.github.GithubClient;

import java.io.File;
import java.lang.reflect.Field;

/**
 * User: zjzhai
 * Date: 2/18/14
 * Time: 9:36 AM
 */
@Ignore
public class GithubClientTest {

    private String account = "foreverosstest";

    private String pwd = "f12345678";

    private String repository = "new-repository11";

    private String otherAccount = "foreverosstest1";

    @Test
    public void testAll() throws Exception {

        GithubClient client = new GithubClient(account, pwd);

        assert client.authenticate();

        client.createRepository(repository, "description");

        assert client.isRepositoryExist(repository);

        assert client.isAccountExist(otherAccount);


        assert ("https://github.com/foreverosstest/" + repository + ".git").equals(client.getHttpTransportUrl(repository));

        client.addCollaborators(repository, otherAccount);

        Field githubField = GithubClient.class.getDeclaredField("github");
        githubField.setAccessible(true);

        GitHub github = (GitHub) githubField.get(client);
        assert github.getRepository(client.getRepositoryFullName(repository)).getCollaboratorNames().contains(otherAccount);


        client.removeCollaborators(repository, otherAccount);
        assert !github.getRepository(client.getRepositoryFullName(repository)).getCollaboratorNames().contains(otherAccount);


        GitClient gitClient = new GitClient(account, pwd, "foreverosstest@163.com", GithubClientTest.class.getResource("/ProjectTest").getFile());


        gitClient.pushRepositoryToRemote(client.getHttpTransportUrl(repository));

        String downToPath = GithubClientTest.class.getResource("/").getFile() ;

        File downToPathFile = new File(downToPath);

        assert !new File(downToPathFile,"/" + repository).exists();

        gitClient.clone(client.getHttpTransportUrl(repository), downToPathFile);

        assert new File(downToPathFile,"/" + repository).exists();

        client.removeRepository(repository);
        assert !client.isRepositoryExist(repository);


    }
}
