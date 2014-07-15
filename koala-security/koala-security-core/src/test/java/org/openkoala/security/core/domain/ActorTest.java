package org.openkoala.security.core.domain;


import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.dayatang.dbunit.DbUnitUtils;
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
	
	@Test
	public void testGrant() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		Scope scope = new OrganizationScope("testscope0000000000");
		scope.setDescription("testDescription00000");
		scope.save();
		user.grant(role, scope);
		Set<Authority> authorities = Authorization.findAuthoritiesByActorInScope(user, scope);
		assertNotNull(authorities);
		assertTrue(authorities.size() == 1);
	}
}
