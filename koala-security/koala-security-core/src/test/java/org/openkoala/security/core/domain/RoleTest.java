package org.openkoala.security.core.domain;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.openkoala.security.core.NameIsExistedException;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.*;

public class RoleTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testSave() throws Exception {
		Role role = initRole();
		role.save();
		assertNotNull(role.getId());
		Role loadRole = Role.getBy(role.getName());
		assertNotNull(loadRole);
		assertRole(role, loadRole);
	}

	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		testSave();
		Role role = new Role("testRole0000000000");
		role.save();
	}

	@Test
	public void testFindAllAndSecurityResource() throws Exception {
		init();
		List<Role> roles = Role.findAll();
		assertFalse(roles.isEmpty());
		Set<SecurityResource> securityResources = roles.get(0).getSecurityResources();
		assertFalse(securityResources.isEmpty());
		for (SecurityResource securityResource : securityResources) {
			assertEquals("测试管理0000000000", securityResource.getName());
		}
	}

	private void init() {
		Role role = initRole();
		role.save();
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		role.addSecurityResource(urlAccessResource);
	}

	@Test
	public void testUpdate() throws Exception {
		String name = "name update";
		String description = "description update";
		Role role = initRole();
		role.save();
		Role updateRole = new Role(name);
		updateRole.setDescription(description);
		updateRole.setId(role.getId());
		updateRole.save();
		Role loadRole = Role.getBy(updateRole.getId());
		assertNotNull(loadRole);
		System.out.println(loadRole);
		assertRole(updateRole, loadRole);
	}

	@Test(expected = NameIsExistedException.class)
	public void testUpdateNameIsExisted() throws Exception {
		String name = "testRole00000000001";
		String description = "description update";
		Role role = initRole();
		Role role2 = new Role(name);
		role2.setDescription(description);
		role2.save();
		role.save();
		Role updateRole = new Role(name);
		updateRole.setDescription(description);
		updateRole.setId(role.getId());
		updateRole.save();
		Role loadRole = Role.getBy(updateRole.getId());
		assertNotNull(loadRole);
		assertRole(updateRole, loadRole);
	}

	@Test
	public void testFindRoleByUser() throws Exception {
		User user = initUser();
		Role role = initRole();
		user.save();
		role.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();

		Set<Role> roles = Role.findByUser(user);

		assertNotNull(roles);
		assertEquals(1, roles.size());
	}

	@Test
	public void testFindAuthoritiesByRole() throws Exception {
		Role role = initRole();
		Permission permission = initPermission();
		Permission permission2 = new Permission("测试权限000002", "testPermission000002");
		role.save();
		permission.save();
		permission2.save();
		role.addPermission(permission);
		role.addPermission(permission2);

		Set<Authority> authorities = role.findAuthoritiesBy();

		assertNotNull(authorities);
		assertEquals(3, authorities.size());
	}

	@Test
	public void testAddPermission() throws Exception {
		Role role = initRole();
		Permission permission = initPermission();
		role.save();
		permission.save();
		role.addPermission(permission);
		Set<Permission> permissions = role.getPermissions();
		assertNotNull(permissions);
		assertEquals(1, permissions.size());
	}

	@Test
	public void testAddPermissions() throws Exception {
		Role role = initRole();
		Permission permission = initPermission();
		Permission permission2 = new Permission("测试权限000002", "testPermission000002");
		role.save();
		permission.save();
		permission2.save();

		role.addPermissions(Lists.newArrayList(permission, permission2));
		Set<Permission> permissions = role.getPermissions();
		assertNotNull(permissions);
		assertEquals(2, permissions.size());
	}

	@Test
	public void testTerminatePermission() throws Exception {
		Role role = initRole();
		Permission permission = initPermission();
		Permission permission2 = new Permission("测试权限000002", "testPermission000002");
		role.save();
		permission.save();
		permission2.save();

		role.addPermissions(Lists.newArrayList(permission, permission2));
		Set<Permission> permissions = role.getPermissions();
		assertNotNull(permissions);
		assertEquals(2, permissions.size());

		role.terminatePermission(permission);

		permissions = role.getPermissions();
		assertNotNull(permissions);
		assertEquals(1, permissions.size());
	}

	@Test
	public void testTerminatePermissions() throws Exception {
		Role role = initRole();
		Permission permission = initPermission();
		Permission permission2 = new Permission("测试权限000002", "testPermission000002");
		role.save();
		permission.save();
		permission2.save();

		role.addPermissions(Lists.newArrayList(permission, permission2));
		Set<Permission> permissions = role.getPermissions();
		assertNotNull(permissions);
		assertEquals(2, permissions.size());

		role.terminatePermissions(Lists.newArrayList(permission, permission2));

		permissions = role.getPermissions();
		assertNotNull(permissions);
		assertEquals(0, permissions.size());
	}

	@Test
	public void testGetAuthorityBy() throws Exception {
		Role role = initRole();
		role.save();
		Authority authority = role.getBy(role.getName());
		assertNotNull(authority);
	}

	@Test
	public void testGetBy() throws Exception {
		Role role = initRole();
		role.save();

		Role loadRole = Role.getBy(role.getId());
		assertNotNull(loadRole);

	}
}
