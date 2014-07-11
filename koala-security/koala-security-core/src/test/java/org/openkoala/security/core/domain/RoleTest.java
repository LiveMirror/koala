package org.openkoala.security.core.domain;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RoleTest extends AbstractDomainIntegrationTestCase {
	@Test
	public void testFindMenuResourceByRole() throws Exception {
		System.out.println("find Role");
		Authority authority = Authority.get(Authority.class, 6l);
		Set<MenuResource> menuResources = Authority.findTopMenuResourceByAuthority(authority);
		System.out.println(menuResources);
	}
}
