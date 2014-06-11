package org.openkoala.security.application;

import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.openkoala.security.core.domain.Role;

public class SecurityAccessApplicationTest extends AbstractSecurityIntegrationTestCase{
	
	@Inject
	private SecurityAccessApplication securityAccessApplication;
	
	@Test
	public void testFindAllRolesByUserAccount() throws Exception {
		Set<Role> roles = securityAccessApplication.findAllRolesByUserAccount("zhangsan");
		for (Role role : roles) {
			System.out.println(role.isMaster());
		}
	}
}
