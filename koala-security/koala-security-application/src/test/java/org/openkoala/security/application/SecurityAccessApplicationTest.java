package org.openkoala.security.application;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.openkoala.security.application.impl.SecurityAccessApplicationImpl;
import org.openkoala.security.core.domain.Role;

public class SecurityAccessApplicationTest extends AbstractSecurityIntegrationTestCase{
	
	@Inject
	private SecurityAccessApplication securityAccessApplication;
	
	@Inject
	private SecurityConfigApplication securityConfigApplication;
	
	@Test
	public void testFindAllRolesByUserAccount() throws Exception {
		System.out.println("aaa");
		//		Set<Role> roles = securityAccessApplication.findAllRolesByUserAccount("zhangsan");
	}

}
