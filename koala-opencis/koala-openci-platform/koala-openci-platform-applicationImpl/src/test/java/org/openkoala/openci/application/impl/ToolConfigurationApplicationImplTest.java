package org.openkoala.openci.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.openci.AbstractIntegrationTest;
import org.openkoala.openci.application.ToolConfigurationApplication;
import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.core.ToolConfiguration;

public class ToolConfigurationApplicationImplTest extends AbstractIntegrationTest {

	@Inject
	private ToolConfigurationApplication toolConfigurationApplication;

	private ToolConfiguration toolConfiguration;

	@Test
	public void testCreateConfiguration() {
		toolConfigurationApplication.createConfiguration(toolConfiguration);
		assertEquals(toolConfiguration, ToolConfiguration.get(ToolConfiguration.class, toolConfiguration.getId()));
		toolConfiguration.remove();
	}

	@Test
	public void testUpdateConfiguration() {
		toolConfigurationApplication.createConfiguration(toolConfiguration);
		toolConfiguration.setName("abc");
		toolConfigurationApplication.updateConfiguration(toolConfiguration);
		assertEquals("abc", ToolConfiguration.get(ToolConfiguration.class, toolConfiguration.getId()).getName());
		toolConfiguration.remove();
	}

	@Test
	public void testSetToolUsabled() {
		toolConfigurationApplication.createConfiguration(toolConfiguration);
		assertFalse(toolConfiguration.isUsable());
		toolConfigurationApplication.setToolUsabled(toolConfiguration);
		assertTrue(toolConfiguration.isUsable());
		toolConfiguration.remove();
	}

	@Test
	public void testSetToolUnUsabled() {
		toolConfigurationApplication.createConfiguration(toolConfiguration);
		assertFalse(toolConfiguration.isUsable());
		toolConfigurationApplication.setToolUsabled(toolConfiguration);
		assertTrue(toolConfiguration.isUsable());
		toolConfigurationApplication.setToolUnusabled(toolConfiguration);
		assertFalse(toolConfiguration.isUsable());
		toolConfiguration.remove();
	}

	@Test
	public void testCanConnect() {

	}

	@Test
	public void testGetAllUsable() {
		toolConfigurationApplication.createConfiguration(toolConfiguration);
		toolConfigurationApplication.setToolUsabled(toolConfiguration);
		assertTrue(toolConfiguration.isUsable());
		toolConfiguration.remove();
	}

	@Test
	public void testPagingQeuryToolConfigurations() {
		toolConfiguration.save();
		ToolConfiguration toolConfiguration2 = new JenkinsConfiguration("test2", null, null, null);
		toolConfiguration2.save();

		List<JenkinsConfiguration> toolConfigurations = toolConfigurationApplication.pagingQeuryJenkinsConfigurations(0, 1).getData();
		assertEquals(1, toolConfigurations.size());

		toolConfiguration.remove();
		toolConfiguration2.remove();
	}

	@Before
	public void init() {
		toolConfiguration = new GitConfiguration("test", null, null, null, "token", null);
	}

	@After
	public void destory() {
		toolConfiguration = null;
	}
}
