package org.openkoala.security.application;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.openkoala.security.application.impl.SecurityAccessApplicationImpl;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.SecurityResource;

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
	@Test
	public void testFindMenuResourceDTOByUserAccountInRoleDTO() throws Exception {
		securityAccessApplication = new SecurityAccessApplicationImpl();
		String userAccount = "zhangsan";
//		Long roleId = 1l;
//		Role role = securityAccessApplication.getRoleBy(roleId);
//		List<SecurityResource> menuResources = securityAccessApplication.findMenuResourceDTOByUserAccountInRoleDTO(userAccount, role);
//		System.out.println(menuResources);
	}
}
