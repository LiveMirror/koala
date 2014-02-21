package org.openkoala.openci.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.openci.AbstractIntegrationTest;
import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.core.SvnConfiguration;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.core.TracConfiguration;

public class ToolConfigurationTest extends AbstractIntegrationTest {

	private static final String TEST_STR = "test";
	private static final String TEST_STR2 = "test2";
	private static final String TEST_STR3 = "test3";
	
	@Test
	@Ignore
	public void testSave() {
		ToolConfiguration gitConfiguration = createGitConfiguration();
		ToolConfiguration svnConfiguration = createSvnConfiguration();
		ToolConfiguration tracConfiguration = createTracConfiguration();
		ToolConfiguration jenkinsConfiguration = createJenkinsConfiguration();
		gitConfiguration.save();
		svnConfiguration.save();
		tracConfiguration.save();
		jenkinsConfiguration.save();
		
		assertEquals(gitConfiguration, GitConfiguration.get(ToolConfiguration.class, gitConfiguration.getId()));
		
		gitConfiguration.remove();
		svnConfiguration.remove();
		tracConfiguration.remove();
		jenkinsConfiguration.remove();
	}
	
	private ToolConfiguration createJenkinsConfiguration() {
		JenkinsConfiguration jenkinsConfiguration = new JenkinsConfiguration(TEST_STR, TEST_STR, TEST_STR, TEST_STR);
		return jenkinsConfiguration;
	}

	private GitConfiguration createGitConfiguration() {
		GitConfiguration gitConfiguration = new GitConfiguration(TEST_STR, TEST_STR, TEST_STR, TEST_STR, TEST_STR, TEST_STR);
		return gitConfiguration;
	}
	
	private SvnConfiguration createSvnConfiguration() {
		SvnConfiguration svnConfiguration = new SvnConfiguration(TEST_STR2, TEST_STR, TEST_STR, TEST_STR, TEST_STR, TEST_STR);
		return svnConfiguration;
	}
	
	private TracConfiguration createTracConfiguration() {
		TracConfiguration tracConfiguration = new TracConfiguration(TEST_STR3, TEST_STR, TEST_STR, TEST_STR, TEST_STR);
		return tracConfiguration;
	}
}
