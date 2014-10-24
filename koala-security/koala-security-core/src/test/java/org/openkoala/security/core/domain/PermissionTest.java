package org.openkoala.security.core.domain;

import org.junit.Test;
import org.openkoala.security.core.CorrelationException;
import org.openkoala.security.core.IdentifierIsExistedException;
import org.openkoala.security.core.NameIsExistedException;

import java.util.List;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.*;

public class PermissionTest extends AbstractDomainIntegrationTestCase {

	@Test(expected = IllegalArgumentException.class)
	public void testSaveNameIsNull() throws Exception {
		new Permission(null, "testPermission000002");
	}
	
	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		testSave();
		Permission permission = new Permission("测试权限000001", "testPermission000002");
		permission.save();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSaveIdentifierIsNull() throws Exception {
		new Permission("测试权限000002", null);
	}
	
	@Test(expected = IdentifierIsExistedException.class)
	public void testSaveIdentifierExisted() throws Exception {
		testSave();
		Permission permission = new Permission("测试权限000002", "testPermission000001");
		permission.save();
	}
	
	@Test
	public void testSave() throws Exception {
		Permission permission = initPermission();
		permission.save();
		assertNotNull(permission.getId());
		Permission loadPermission = Permission.getBy(permission.getId());
		assertNotNull(loadPermission);
		assertPermission(permission, loadPermission);
	}

	@Test
	public void testChangeName() throws Exception {
		Permission permission = initPermission();
		permission.save();
		permission.changeName("测试权限000004");
		Permission loadPermission = Permission.getBy(permission.getId());
		assertNotNull(loadPermission);
		assertPermission(permission, loadPermission);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangeNameIsNull() throws Exception {
		Permission permission = initPermission();
		permission.save();
		permission.changeName(null);
	}
	
	@Test(expected = NameIsExistedException.class)
	public void testChangeNameExisted() throws Exception {
		String name = "测试权限000003";
		String identifier = "testPermission000003";
		Permission permission = initPermission();
		permission.save();
		Permission permission2 = new Permission(name, identifier);
		permission2.save();
		permission.changeName(name);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangeIdentifierIsNull() throws Exception {
		Permission permission = initPermission();
		permission.save();
		permission.changeIdentifier(null);
	}
	
	@Test(expected = IdentifierIsExistedException.class)
	public void testChangeIdentifierExisted() throws Exception {
		String name = "测试权限000003";
		String identifier = "testPermission000003";
		Permission permission = initPermission();
		permission.save();
		Permission permission2 = new Permission(name, identifier);
		permission2.save();
		permission.changeIdentifier(identifier);
	}
	
	@Test(expected = CorrelationException.class)
	public void testRemoveHasRole() throws Exception {
		Role role = initRole();
		role.save();
		Permission permission = initPermission();
		permission.save();
		role.addPermission(permission);
		Permission loadPermission = Permission.getBy(permission.getId());
		loadPermission.remove();
	}
	
	@Test(expected = CorrelationException.class)
	public void testRemoveHasActor() throws Exception {
		User user = initUser();
		user.save();
		Permission permission = initPermission();
		permission.save();
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		new Authorization(user, permission).save();
		permission.remove();
	}
	
	@Test
	public void testRemove() throws Exception {
		Permission permission = initPermission();
		permission.save();
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		permission.addSecurityResource(urlAccessResource);
		permission.remove();
	}

	@Test
	public void testGetPermissionBy() throws Exception {
		Permission permission = initPermission();
		permission.save();
		Permission loadPermission = Permission.getBy(permission.getId());
		assertNotNull(loadPermission);
	}
	
	@Test
	public void testGetPermissionByIdIsNull() throws Exception {
		Permission loadPermission = Permission.getBy(-1l);
		assertNull(loadPermission);
	}
	
	@Test
	public void testGetAuthorityBy() throws Exception {
		Permission permission = initPermission();
		permission.save();
		Authority authority = permission.getBy(permission.getName());
		assertNotNull(authority);
	}

    @Test
    public void testFindResources() throws Exception{
        Permission permission = initPermission();
        permission.save();
        MenuResource menuResource = initMenuResource();
        menuResource.save();
        permission.addSecurityResource(menuResource);
        List<SecurityResource> securityResources =  permission.findResources();
        assertFalse(securityResources.isEmpty());
        assertTrue(securityResources.size() == 1);
    }
}
