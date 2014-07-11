package org.openkoala.security.core.domain;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AuthorizationTest extends AbstractDomainIntegrationTestCase{
	
	@Test
	public void testFind() throws Exception {
		Actor actor = Actor.get(User.class, 2l);
		Authority authority = Authority.get(Role.class, 7l);
		Authorization authorization = Authorization.findByActorInAuthority(actor, authority);
		assertNotNull(authorization);
		authorization.remove();
		assertNull(authorization);
	}
}	
