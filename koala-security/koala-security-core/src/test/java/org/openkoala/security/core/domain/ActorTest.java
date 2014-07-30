package org.openkoala.security.core.domain;


import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import static org.openkoala.security.core.util.EntitiesHelper.*;

public class ActorTest extends AbstractDomainIntegrationTestCase{
	
	@Test
	public void testRemove() throws Exception {
		User user = initUser();
		user.save();
		assertNotNull(user);
		assertNotNull(user.getId());
		user.remove();
		User loadUser = User.getBy(user.getId());
		assertNull(loadUser);
	}
	
	@Test
	public void testGrantNoScope() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		user.grant(role, null);
		Authorization authorization = Authorization.findByActorInAuthority(user, role);
		assertNotNull(authorization);
	}
	
//	@Test
//	public void testGrant() throws Exception {
//		User user = initUser();
//		user.save();
//		Role role = initRole();
//		role.save();
//		Scope scope = new OrganizationScope("testscope0000000000");
//		scope.setDescription("testDescription00000");
//		scope.save();
//		user.grant(role, scope);
//		Set<Authority> authorities = Authorization.findAuthoritiesByActorInScope(user, scope);
//		assertNotNull(authorities);
//		assertTrue(authorities.size() == 1);
//	}
	
//	@Test
//	public void testGetPermissions() throws Exception {
//		
//		User user = initUser();
//		user.save();
//		Permission permission = initPermission();
//		permission.save();
//		Scope scope = new OrganizationScope("testscope0000000000");
//		scope.setDescription("testDescription00000");
//		scope.save();
//		user.grant(permission, scope);
//		
//		Set<Permission> permissions = user.getPermissions(scope);
//		assertNotNull(permissions);
//		assertEquals(1, permissions.size());
//	}
	
}
