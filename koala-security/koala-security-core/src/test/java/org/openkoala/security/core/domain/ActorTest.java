package org.openkoala.security.core.domain;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.initPermission;
import static org.openkoala.security.core.util.EntitiesHelper.initRole;
import static org.openkoala.security.core.util.EntitiesHelper.initUser;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ActorTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testFindActor() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		user.grant(role);
	}
	
	@Test
	public void testRemove() throws Exception {
		User user = initUser();
		user.save();
		assertNotNull(user);
		assertNotNull(user.getId());
		Role role = initRole();
		role.save();
		user.grant(role);
		user.remove();
		User loadUser = User.getById(user.getId());
		assertNull(loadUser);
	}
	

	@Test
	public void testGrantNoScope() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		user.grant(role);
		Authorization authorization = Authorization.findByActorInAuthority(user, role);
		assertNotNull(authorization);
	}

	@Test
	public void testGrant() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		user.grant(role);
		Set<Authority> authorities = Authorization.findAuthoritiesByActor(user);
		assertNotNull(authorities);
		assertTrue(authorities.size() == 1);
	}
	
	@Test
	public void testGrantIsExisted() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		user.grant(role);
		user.grant(role);
	}

	@Test
	public void testGetPermissions() throws Exception {
		User user = initUser();
		user.save();
		Permission permission = initPermission();
		permission.save();
		user.grant(permission);
		List<Permission> permissions = User.findAllPermissionsBy(user.getUserAccount());
		assertNotNull(permissions);
		assertEquals(1, permissions.size());
	}
}
