package org.openkoala;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * User: zjzhai
 * Date: 2/17/14
 * Time: 2:13 PM
 */
@Ignore
public class MainTest {


    private String username = "foreverosstest";

    private String pwd = "f12345678";

    private String repository = "new-repository11";

    private String longRepositoryName = username + "/" + repository;

    @Test
    public void testName() throws IOException {

        GitHub github = null;

        github = GitHub.connectUsingPassword(username, pwd);

        assert github.isCredentialValid();

        GHRepository repo = github.createRepository(
                repository, "this is my new repository",
                "http://www.kohsuke.org/", true/*public*/);

        assert github.getRepository(longRepositoryName) != null;

        repo.addCollaborators(github.getUser("foreverosstest1"));

        repo.removeCollaborators(github.getUser("foreverosstest1"));

        repo.delete();


    }

    @Test(expected = FileNotFoundException.class)
    public void testNotFoundRepository() throws IOException {
        GitHub github = null;
        github = GitHub.connectUsingPassword(username, pwd);
        assert github.isCredentialValid();

        github.getRepository(username + "/asdfasfsad");
    }


    @Test(expected = FileNotFoundException.class)
    public void testNotFoundUser() throws Exception {
        GitHub github = null;
        github = GitHub.connectUsingPassword(username, pwd);
        assert github.isCredentialValid();
        github.getUser("xxxsdfwefwef");

    }
}
