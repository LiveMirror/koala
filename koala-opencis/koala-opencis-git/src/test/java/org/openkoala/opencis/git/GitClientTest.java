package org.openkoala.opencis.git;

import org.eclipse.jgit.api.Git;
import org.junit.*;

import java.io.File;

/**
 * User: zjzhai
 * Date: 2/19/14
 * Time: 9:21 AM
 */
@Ignore
public class GitClientTest {

    private static final String TEST_DIR_NAME = "ProjectTest/";

    private static final String REPOSITORY_PATH = GitClientTest.class.getResource("/" + TEST_DIR_NAME).getFile();


    private String REPOSITORY_NAME = "xdffdasdfasd";


    private String REMOTE_REPOSITORY_URL = "http://git.oschina.net/xxx/xdffdasdfasd.git";

    private GitClient client;


    @Before
    public void setUp() throws Exception {

        client = new GitClient("xxxx", "xxxx", "xxxx", REPOSITORY_PATH);

    }

    @Test
    public void test1() throws Exception {


        assert !GitClient.isInited(REPOSITORY_PATH);


        if (!GitClient.isInited(REPOSITORY_PATH)) {
            GitClient.init(REPOSITORY_PATH, REMOTE_REPOSITORY_URL);
            assert GitClient.isInited(REPOSITORY_PATH);
        }

        Git git = Git.open(new File(REPOSITORY_PATH));

        assert !git.getRepository().isBare();

        client.add(".");

        client.commit("init");

        client.pushRepositoryToRemote(REMOTE_REPOSITORY_URL);




    }

    @Test(expected = AssertionError.class)
    public void testIsInitedException() {
        GitClient.isInited(null);
    }


}
