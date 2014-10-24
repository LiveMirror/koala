package org.openkoala.security.core.domain;

import org.junit.Test;
import org.openkoala.security.core.NameIsExistedException;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.assertMenuResource;
import static org.openkoala.security.core.util.EntitiesHelper.initMenuResource;

public class MenuResourceTest extends AbstractDomainIntegrationTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNameIsNull() throws Exception {
        new MenuResource(null);
    }

    @Test(expected = NameIsExistedException.class)
    public void testSaveNameExisted() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        securityMenuResource.save();
        MenuResource menuResource = new MenuResource("用户管理00000000000");
        menuResource.save();
    }

    @Test
    public void testSave() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        securityMenuResource.save();
        assertNotNull(securityMenuResource.getId());
        MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
        assertNotNull(loadMenuResource);
        assertMenuResource(securityMenuResource, loadMenuResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeNameIsNull() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        securityMenuResource.save();
        securityMenuResource.changeName(null);
    }

    @Test(expected = NameIsExistedException.class)
    public void testChangeNameIsExisted() throws Exception {
        String name = "update0000000000";
        MenuResource securityMenuResource = initMenuResource();
        securityMenuResource.save();
        MenuResource menuResource2 = new MenuResource(name);
        menuResource2.save();
        securityMenuResource.changeName(name);
    }

    @Test
    public void testChangeName() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        securityMenuResource.save();
        securityMenuResource.changeName("update0000000000");
        MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
        assertNotNull(loadMenuResource);
        assertMenuResource(securityMenuResource, loadMenuResource);
    }

    @Test
    public void testAddChild() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        MenuResource childMenuResource = new MenuResource("update0000000000");
        securityMenuResource.save();
        securityMenuResource.addChild(childMenuResource);
        assertNotNull(childMenuResource.getId());
        Set<MenuResource> childrenMenuResource = securityMenuResource.getChildren();
        assertNotNull(childrenMenuResource);
        assertTrue(childrenMenuResource.size() == 1);
    }

    @Test
    public void testRemoveChild() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        MenuResource childMenuResource = new MenuResource("update0000000000");
        securityMenuResource.save();
        securityMenuResource.addChild(childMenuResource);
        Set<MenuResource> childrenMenuResource = securityMenuResource.getChildren();
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
        Set<MenuResource> childrenMenuResource = securityMenuResource.getChildren();
        assertNotNull(childrenMenuResource);
        assertTrue(childrenMenuResource.size() == 2);
        securityMenuResource.remove();
        MenuResource loadMenuResource = MenuResource.getBy(securityMenuResource.getId());
        assertNull(loadMenuResource);
        MenuResource loadMenuResource1 = MenuResource.getBy(childMenuResource1.getId());
        assertNull(loadMenuResource1);
        MenuResource loadMenuResource2 = MenuResource.getBy(childMenuResource2.getId());
        assertNull(loadMenuResource2);
        List<MenuResource> allMenuResources = MenuResource.findAll(MenuResource.class);
        assertTrue(allMenuResources.isEmpty());
    }

    @Test
    public void testRemove() throws Exception {
        MenuResource securityMenuResource = initMenuResource();
        MenuResource childMenuResource1 = new MenuResource("child0000000001");
        MenuResource childMenuResource2 = new MenuResource("child0000000002");
        securityMenuResource.save();
        securityMenuResource.addChild(childMenuResource1);
        childMenuResource1.addChild(childMenuResource2);
        securityMenuResource.remove();
        List<MenuResource> allMenuResources = MenuResource.findAll(MenuResource.class);
        assertTrue(allMenuResources.isEmpty());
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

    @Test
    public void testFindAllByIds() throws Exception {
        MenuResource actorSecurityMenuResource = new MenuResource("参与者管理");
        actorSecurityMenuResource.setDescription("用户、用户组等页面管理。");
        actorSecurityMenuResource.save();

        MenuResource userMenuResource = new MenuResource("用户管理");
        userMenuResource.setUrl("/pages/auth/user-list.jsp");
        actorSecurityMenuResource.addChild(userMenuResource);

        Long[] menuResourceIds = new Long[]{actorSecurityMenuResource.getId(), userMenuResource.getId()};
        List<MenuResource> menuResources = MenuResource.findAllByIds(menuResourceIds);
        assertFalse(menuResources.isEmpty());
        assertTrue(menuResources.size() == 2);
    }

}






