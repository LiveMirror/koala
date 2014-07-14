package org.openkoala.security.core.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.openkoala.security.core.util.EntitiesHelper.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.openkoala.security.core.AuthorizationIsNotExisted;

public class AuthorizationTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testSave() throws Exception {
		Role role = initRole();
		User user = initUser();
		role.save();
		user.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
		assertUser(user, (User) authorization.getActor());
		assertRole(role, (Role) authorization.getAuthority());
	}

	@Test
	public void testFindByActor() throws Exception {
		Role role = initRole();
		User user = initUser();
		role.save();
		user.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		List<Authorization> authorizations = Authorization.findByActor(user);
		assertNotNull(authorizations);
		assertTrue(authorizations.size() == 1);
	}

	@Test
	public void testFindByAuthority() throws Exception {
		Role role = initRole();
		User user = initUser();
		role.save();
		user.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		List<Authorization> authorizations = Authorization.findByAuthority(role);
		assertNotNull(authorizations);
		assertTrue(authorizations.size() == 1);
	}

	@Test
	public void testFindAuthoritiesByActorInScope() throws Exception {
		User user = initUser();
		Role role = initRole();
		Scope scope = new OrganizationScope("testscope0000000000");
		scope.setDescription("testDescription00000");
		user.save();
		role.save();
		scope.save();
		Authorization authorization = initAuthorization(user, role, scope);
		authorization.save();
		Set<Authority> authorities = Authorization.findAuthoritiesByActorInScope(user, scope);
		assertNotNull(authorities);
		assertTrue(authorities.size() == 1);
	}

	@Test
	public void testFindAuthoritiesByActor() throws Exception {
		Role role = initRole();
		User user = initUser();
		role.save();
		user.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		Set<Authority> authorities = Authorization.findAuthoritiesByActor(user);
		assertNotNull(authorities);
		assertTrue(authorities.size() == 1);
	}

	@Test
	public void testFindByActorInAuthority() throws Exception {
		Role role = initRole();
		User user = initUser();
		role.save();
		user.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		authorization = Authorization.findByActorInAuthority(user, role);
		assertNotNull(authorization);
		assertUser(user, (User) authorization.getActor());
		assertRole(role, (Role) authorization.getAuthority());
	}

	@Test
	public void testCheckAuthorizationNoScope() throws Exception {
		Role role = initRole();
		User user = initUser();
		role.save();
		user.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		Authorization.checkAuthorization(user, role);
	}

	@Test(expected = AuthorizationIsNotExisted.class)
	public void testCheckAuthorizationNoScopeNoExisted() throws Exception {
		Role role = initRole();
		User user = initUser();
		User user2 = new User("test000000000010", "000000","test000000000010@foreveross.com","12345654321");
		role.save();
		user.save();
		user2.save();
		Authorization authorization = initAuthorization(user, role);
		authorization.save();
		Authorization.checkAuthorization(user2, role);
	}

	@Test
	public void testCheckAuthorization() throws Exception {
		Role role = initRole();
		User user = initUser();
		Scope scope = new OrganizationScope("testscope0000000000");
		scope.setDescription("testDescription00000");
		role.save();
		user.save();
		scope.save();
		Authorization authorization = initAuthorization(user, role, scope);
		authorization.save();
		Authorization.checkAuthorization(user, role, scope);
	}

	@Test(expected = AuthorizationIsNotExisted.class)
	public void testCheckAuthorizationNoExisted() throws Exception {
		Role role = initRole();
		User user = initUser();
		User user2 = new User("test000000000010", "000000","test000000000010@foreveross.com","12345654321");
		Scope scope = new OrganizationScope("testscope0000000000");
		scope.setDescription("testDescription00000");
		role.save();
		user.save();
		user2.save();
		scope.save();
		Authorization authorization = initAuthorization(user, role, scope);
		authorization.save();
		Authorization.checkAuthorization(user2, role, scope);
	}

}
