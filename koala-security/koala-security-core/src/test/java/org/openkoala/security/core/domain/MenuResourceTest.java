package org.openkoala.security.core.domain;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.openkoala.security.core.NameIsExistedException;

import static org.openkoala.security.core.util.EntitiesHelper.*;

public class MenuResourceTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testSave() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		securityMenuResource.save();
		assertNotNull(securityMenuResource.getId());
		MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
		assertNotNull(loadMenuResource);
		assertMenuResource(securityMenuResource, loadMenuResource);
	}

	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		securityMenuResource.save();
		MenuResource menuResource = new MenuResource("用户管理00000000000");
		menuResource.save();
	}

	@Test
	public void testUpdate() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		securityMenuResource.save();
		MenuResource updateSecurityMenuResource = new MenuResource("update0000000000");
		updateSecurityMenuResource.setId(securityMenuResource.getId());
		updateSecurityMenuResource.update();
		MenuResource loadMenuResource = MenuResource.getBy(updateSecurityMenuResource.getId());
		assertNotNull(loadMenuResource);
		assertMenuResource(updateSecurityMenuResource, loadMenuResource);
	}

	@Test(expected = NameIsExistedException.class)
	public void testUpdateNameExisted() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		MenuResource menuResource2 = new MenuResource("update0000000000");
		menuResource2.save();
		securityMenuResource.save();
		MenuResource updateSecurityMenuResource = new MenuResource("update0000000000");
		updateSecurityMenuResource.setId(securityMenuResource.getId());
		updateSecurityMenuResource.update();
	}
	
	@Test
	public void testAddChild() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		MenuResource childMenuResource = new MenuResource("update0000000000");
		securityMenuResource.save();
		securityMenuResource.addChild(childMenuResource);
		Set<MenuResource> childrenMenuResource  = securityMenuResource.getChildren();
		assertNotNull(childrenMenuResource);
		assertTrue(childrenMenuResource.size() == 1);
	}
	
	@Test
	public void testRemoveChild() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		MenuResource childMenuResource = new MenuResource("update0000000000");
		securityMenuResource.save();
		securityMenuResource.addChild(childMenuResource);
		Set<MenuResource> childrenMenuResource  = securityMenuResource.getChildren();
		assertNotNull(childrenMenuResource);
		assertTrue(childrenMenuResource.size() == 1);
		securityMenuResource.removeChild(childMenuResource);
		childrenMenuResource = securityMenuResource.getChildren();
		assertTrue(childrenMenuResource.isEmpty());
	}
	
	@Test
	public void testRemoveNoChildren() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		securityMenuResource.save();
		assertNotNull(securityMenuResource);
		securityMenuResource.remove();
		MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
		assertNull(loadMenuResource);
	}
	
	@Test
	public void testRemoveHasChildren() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		MenuResource childMenuResource1 = new MenuResource("child0000000001");
		MenuResource childMenuResource2 = new MenuResource("child0000000002");
		securityMenuResource.save();
		securityMenuResource.addChild(childMenuResource1);
		securityMenuResource.addChild(childMenuResource2);
		Set<MenuResource> childrenMenuResource  = securityMenuResource.getChildren();
		assertNotNull(childrenMenuResource);
		assertTrue(childrenMenuResource.size() == 2);
		securityMenuResource.remove();
		MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
		assertNull(loadMenuResource);
		MenuResource loadMenuResource1 = MenuResource.getBy(childMenuResource1.getId());
		assertNull(loadMenuResource1);
		MenuResource loadMenuResource2 = MenuResource.getBy(childMenuResource2.getId());
		assertNull(loadMenuResource2);
	}
	
	@Test
	public void testGetBy() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		securityMenuResource.save();
		MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
		assertNotNull(loadMenuResource);
	}
	@Test
	public void testFindByName() throws Exception {
		MenuResource securityMenuResource = initMenuResource();
		securityMenuResource.save();
		MenuResource loadMenuResource = (MenuResource) securityMenuResource.findByName(securityMenuResource.getName());
		assertNotNull(loadMenuResource);
		assertMenuResource(securityMenuResource, loadMenuResource);
	}
	
	
	
	
	
}
