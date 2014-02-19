package org.openkoala.opencis.git;

import org.eclipse.jgit.api.Git;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openkoala.opencis.github.GithubClient;

import java.io.File;

/**
 * User: zjzhai
 * Date: 2/19/14
 * Time: 9:21 AM
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GitCISClientTest {

    private static final String TEST_DIR_NAME = "ProjectTest/";

    private static final String REPOSITORY_PATH = GitCISClientTest.class.getResource("/" + TEST_DIR_NAME).getFile();

    private GithubClient githubClient;


    private String REPOSITORY_NAME = "gittest";


    private String REMOTE_REPOSITORY_URL = null;

    private GitCISClient client;


    @Before
    public void setUp() throws Exception {
        githubClient = new GithubClient("foreverosstest", "f12345678");
        githubClient.authenticate();


        githubClient.createRepository(REPOSITORY_NAME, "");


        REMOTE_REPOSITORY_URL = githubClient.getRemoteRepositoryUrl(REPOSITORY_NAME) + ".git";


        client = new GitCISClient("foreverosstest", "f12345678", "foreverosstest@163.com", REPOSITORY_PATH);

    }

    @After
    public void tearDown() throws Exception {
        githubClient.removeRepository(REPOSITORY_NAME);
    }

    @Test
    public void test1() throws Exception {


        assert !GitCISClient.isInited(REPOSITORY_PATH);


        if (!GitCISClient.isInited(REPOSITORY_PATH)) {
            GitCISClient.init(REPOSITORY_PATH, REMOTE_REPOSITORY_URL);
            assert GitCISClient.isInited(REPOSITORY_PATH);
        }

        Git git = Git.open(new File(REPOSITORY_PATH));

        assert !git.getRepository().isBare();

        client.add(".");

        client.commit("init");

        client.pushRepositoryToRemote(REMOTE_REPOSITORY_URL);

        String downToPath = GitCISClient.class.getResource("/").getFile() + File.separator + REPOSITORY_NAME;

        File downToPathFile = new File(downToPath);

        assert !downToPathFile.exists();

        client.clone(REPOSITORY_PATH, downToPathFile);

        assert downToPathFile.exists();


    }

    @Test(expected = AssertionError.class)
    public void test3() {
        GitCISClient.isInited(null);
    }


}
