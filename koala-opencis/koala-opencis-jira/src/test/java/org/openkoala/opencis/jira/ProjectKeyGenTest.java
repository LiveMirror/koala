package org.openkoala.opencis.jira;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.openkoala.opencis.api.Project;

/**
 * User: zjzhai
 * Date: 2/17/14
 * Time: 9:52 AM
 */
public class ProjectKeyGenTest {

    @Test
    public void testName() throws Exception {


        Project project = new Project();
        project.setProjectName("projName");


        String encode = DigestUtils.sha256Hex("projName");

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : encode.toCharArray()) {
            if (c >= 'a' && c <= 'z' && i < 10) {
                sb.append(c);
                i++;
            }

        }

        assert KoalaJiraService.getProjectKey(project).equals(sb.toString().toUpperCase());
    }


}
