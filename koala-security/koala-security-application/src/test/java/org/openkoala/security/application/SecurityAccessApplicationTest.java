package org.openkoala.security.application;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openkoala.security.core.domain.User;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({User.class})
public class SecurityAccessApplicationTest{
	
	@Mock
	private SecurityAccessApplication securityAccessApplication;
	
	@Test
	public void testHasPermission() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasOwnSecurityResource() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllRolesByUserAccount() {
//		String userAccount = "userAccount";
////		mock(User.class);
//		Role role1 = new Role("测试角色0000000000");
//		Role role2 = new Role("测试角色0000000001");
//		List<Role> roles = new ArrayList<Role>();
//		roles.add(role1);
//		roles.add(role2);
//		PowerMockito.mockStatic(User.class);
//		PowerMockito.when(User.findAllRolesBy(userAccount)).thenReturn(roles);
//		assertEquals(roles, securityAccessApplication.findAllRolesByUserAccount(userAccount));
	}

	@Test
	public void testFindAllPermissionsByUserAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserByLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoleByLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserByString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindMenuResourceByUserAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdatePassword() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckAuthorization() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPermissionBy() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMenuResourceBy() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllMenuResourcesByRole() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUrlAccessResourceBy() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllUrlAccessResources() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPageElementResourceByLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPageElementResourceByString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoleByString() {
		fail("Not yet implemented");
	}

	public SecurityAccessApplication getSecurityAccessApplication() {
		return securityAccessApplication;
	}

	public void setSecurityAccessApplication(SecurityAccessApplication securityAccessApplication) {
		this.securityAccessApplication = securityAccessApplication;
	}
}
