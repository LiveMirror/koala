package org.openkoala.github;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GitHub;
import org.openkoala.opencis.github.GithubClient;

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

        client.addCollaborators(repository, otherAccount);

        Field githubField = GithubClient.class.getDeclaredField("github");
        githubField.setAccessible(true);

        GitHub github = (GitHub) githubField.get(client);
        assert github.getRepository(client.getRepositoryFullName(repository)).getCollaboratorNames().contains(otherAccount);


        client.removeCollaborators(repository, otherAccount);
        assert !github.getRepository(client.getRepositoryFullName(repository)).getCollaboratorNames().contains(otherAccount);


        client.removeRepository(repository);


        assert !client.isRepositoryExist(repository);


    }
}
