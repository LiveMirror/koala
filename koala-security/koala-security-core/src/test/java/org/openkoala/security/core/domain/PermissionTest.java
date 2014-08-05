package org.openkoala.security.core.domain;

import org.junit.Test;
import org.openkoala.security.core.IdentifierIsExistedException;
import org.openkoala.security.core.NameIsExistedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.openkoala.security.core.util.EntitiesHelper.assertPermission;
import static org.openkoala.security.core.util.EntitiesHelper.initPermission;

public class PermissionTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testSave() throws Exception {
		Permission permission = initPermission();
		permission.save();
		assertNotNull(permission.getId());
		Permission loadPermission = Permission.getBy(permission.getId());
		assertNotNull(loadPermission);
		assertPermission(permission, loadPermission);
	}

	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		testSave();
		Permission permission = new Permission("测试权限000001", "testPermission000002");
		permission.save();
	}

	@Test(expected = IdentifierIsExistedException.class)
	public void testSaveIdentifierExisted() throws Exception {
		testSave();
		Permission permission = new Permission("测试权限000002", "testPermission000001");
		permission.save();
	}

	@Test
	public void testUpdate() throws Exception {
		Permission permission = initPermission();
		permission.save();
		Permission updatePermission = new Permission("测试权限000003", "testPermission000003");
		updatePermission.setId(permission.getId());
		updatePermission.save();

		Permission loadPermission = Permission.getBy(updatePermission.getId());
		assertNotNull(loadPermission);
		assertPermission(updatePermission, loadPermission);
	}

	@Test(expected = NameIsExistedException.class)
	public void testUpdateNameExisted() throws Exception {
		String name = "测试权限000003";
		String identifier = "testPermission000003";
		Permission permission = initPermission();
		permission.save();
		Permission permission2 = new Permission(name, identifier);
		permission2.save();
		Permission updatePermission = new Permission(name, "testPermission000004");
		updatePermission.setId(permission.getId());
		updatePermission.save();
	}

	@Test(expected = IdentifierIsExistedException.class)
	public void testUpdateIdentifierExisted() throws Exception {
		String name = "测试权限000003";
		String identifier = "testPermission000003";
		Permission permission = initPermission();
		permission.save();
		Permission permission2 = new Permission(name, identifier);
		permission2.save();
		Permission updatePermission = new Permission("测试权限000004", identifier);
		updatePermission.setId(permission.getId());
		updatePermission.save();
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
		Authority authority = permission.getAuthorityBy(permission.getName());
		assertNotNull(authority);
	}
	
}
