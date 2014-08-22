package org.openkoala.security.core.domain;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import static org.openkoala.security.core.util.EntitiesHelper.*;


public class AuthorityTest extends AbstractDomainIntegrationTestCase{
	
	@Test
	public void testFindMenuResourceByAuthority() throws Exception {
		Role role = initRole();
		Permission permission = initPermission();
		role.addPermission(permission);
		Permission permission2 = new Permission("permission0000007", "permission0000007:create");
		permission2.save();
		MenuResource menuResource1 = new MenuResource("menu1");
		MenuResource menuResource2 = new MenuResource("menu2");
		MenuResource menuResource3 = new MenuResource("menu3");
		MenuResource menuResource4 = new MenuResource("menu4");
		MenuResource menuResource5 = new MenuResource("menu5");
		menuResource1.save();
		menuResource2.save();
		menuResource3.save();
		menuResource4.save();
		menuResource5.save();
		
		role.addSecurityResource(menuResource1);
		role.addSecurityResource(menuResource2);
		
		permission.addSecurityResource(menuResource4);
		permission.addSecurityResource(menuResource5);
		permission2.addSecurityResource(menuResource3);
		
		List<MenuResource> permission1MenuResources = permission.findMenuResourceByAuthority();
		assertFalse(permission1MenuResources.isEmpty());
		assertTrue(permission1MenuResources.size() == 2);
		
		List<MenuResource> permission2MenuResources = permission2.findMenuResourceByAuthority();
		assertFalse(permission2MenuResources.isEmpty());
		assertTrue(permission2MenuResources.size() == 1);
		
		List<MenuResource> roleMenuResources = role.findMenuResourceByAuthority();
		assertFalse(roleMenuResources.isEmpty());
		assertTrue(roleMenuResources.size() == 4);
	}

    @Test
    public void testAddSecurityResources(){
        Role role = initRole();
        role.save();
        UrlAccessResource urlAccessResource = initUrlAccessResource();
        urlAccessResource.save();
        MenuResource menuResource = initMenuResource();
        menuResource.save();
        role.addSecurityResources(Lists.newArrayList(urlAccessResource,menuResource));

        List<UrlAccessResource> actualUrlAccessResources = role.findUrlAccessResourceByAuthority();
        assertFalse(actualUrlAccessResources.isEmpty());
        assertTrue(actualUrlAccessResources.size() == 1);
        UrlAccessResource actualUrlAccessResource = actualUrlAccessResources.get(0);
        assertNotNull(actualUrlAccessResource);
        assertNotNull(actualUrlAccessResource.getId());
        assertUrlAccessResource(urlAccessResource,actualUrlAccessResource);

        List<MenuResource> actualMenuResources = role.findMenuResourceByAuthority();
        assertFalse(actualMenuResources.isEmpty());
        assertTrue(actualMenuResources.size() == 1);
        MenuResource actualMenuResource = actualMenuResources.get(0);
        assertNotNull(actualMenuResource);
        assertNotNull(actualMenuResource.getId());
        assertMenuResource(menuResource,actualMenuResource);
    }

    @Test
    public void testTerminateSecurityResources(){
        Role role = initRole();
        role.save();
        UrlAccessResource urlAccessResource = initUrlAccessResource();
        urlAccessResource.save();
        MenuResource menuResource = initMenuResource();
        menuResource.save();
        role.addSecurityResources(Lists.newArrayList(urlAccessResource,menuResource));
        role.terminateSecurityResources(Sets.newHashSet(urlAccessResource, menuResource));

        List<MenuResource> actualMenuResources = role.findMenuResourceByAuthority();
        assertTrue(actualMenuResources.isEmpty());

        List<UrlAccessResource> actualUrlAccessResources = role.findUrlAccessResourceByAuthority();
        assertTrue(actualUrlAccessResources.isEmpty());
    }

}
