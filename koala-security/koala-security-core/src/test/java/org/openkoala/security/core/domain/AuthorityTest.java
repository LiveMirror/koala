package org.openkoala.security.core.domain;

import java.util.Set;

import static org.junit.Assert.*;
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
		
		Set<MenuResource> permission1MenuResources = Authority.findMenuResourceByAuthority(permission);
		assertFalse(permission1MenuResources.isEmpty());
		assertTrue(permission1MenuResources.size() == 2);
		
		Set<MenuResource> permission2MenuResources = Authority.findMenuResourceByAuthority(permission2);
		assertFalse(permission2MenuResources.isEmpty());
		assertTrue(permission2MenuResources.size() == 1);
		
		Set<MenuResource> roleMenuResources = Authority.findMenuResourceByAuthority(role);
		assertFalse(roleMenuResources.isEmpty());
		assertTrue(roleMenuResources.size() == 4);
		
	}
}
