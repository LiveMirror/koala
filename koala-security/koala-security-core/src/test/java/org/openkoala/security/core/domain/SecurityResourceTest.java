package org.openkoala.security.core.domain;

import static org.junit.Assert.*;
import org.junit.Test;

import static org.openkoala.security.core.util.EntitiesHelper.*;

public class SecurityResourceTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testDisable() throws Exception{
		MenuResource menuResource = initMenuResource();
		menuResource.save();
		assertNotNull(menuResource.getId());
		MenuResource loadMenuResource = MenuResource.getBy(menuResource.getId());
		assertFalse(loadMenuResource.isDisabled());
		loadMenuResource.disable();
		assertTrue(loadMenuResource.isDisabled());
	}

	@Test
	public void testEnable() throws Exception{
		MenuResource menuResource = initMenuResource();
		menuResource.save();
		assertNotNull(menuResource.getId());
		MenuResource loadMenuResource = MenuResource.getBy(menuResource.getId());
		assertFalse(loadMenuResource.isDisabled());
		loadMenuResource.disable();
		assertTrue(loadMenuResource.isDisabled());
		loadMenuResource.enable();
		assertFalse(loadMenuResource.isDisabled());
	}

}
