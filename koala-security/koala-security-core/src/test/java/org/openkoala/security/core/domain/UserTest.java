package org.openkoala.security.core.domain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotHasRoleException;

import static org.openkoala.security.core.util.EntitiesHelper.*;

public class UserTest extends AbstractDomainIntegrationTestCase {

	// ~ User Save Test
	// ========================================================================================================

	@Test
	public void testSave() throws Exception {
		User user = initUser();
		encryptPassword("aaa", "670b14728ad9902aecba32e22fa4f6bd");
		assertFalse(user.existed());
		user.save();
		User loadUser = User.getBy(user.getId());
		assertTrue(user.existed());
		assertNotNull(user.getId());
		assertUser(user, loadUser);
	}

	@Test(expected = UserAccountIsExistedException.class)
	public void testSaveUserAccountExisted() throws Exception {
		testSave();
		User user = new User("test000000000000000001", "aaa");
		user.save();
	}

	@Test(expected = NullArgumentException.class)
	public void testSaveUserEmailNull() throws Exception {
		testSave();
		User user = new User("test02", "aaa",null,"123");
		user.save();
	}

	@Test(expected = EmailIsExistedException.class)
	public void testSaveUserEmailExisted() throws Exception {
		testSave();
		User user = new User("test02", "aaa","test01@foreveross.com","1223424242");
		user.save();
	}

	@Test(expected = NullArgumentException.class)
	public void testSaveUserTelePhoneNull() throws Exception {
		testSave();
		User user = new User("test03", "aaa", "test03@foreveross.com", null);
		user.save();
	}

	@Test(expected = TelePhoneIsExistedException.class)
	public void testSaveUserTelePhoneExisted() throws Exception {
		testSave();
		User user = new User("test03", "aaa","test03@foreveross.com","18665588990");
		user.save();
	}

	// ~ User Update Test
	// ========================================================================================================

	@Test
	public void testUpdate() throws Exception {
		User user = initUser();
		user.save();
		User updateUser = User.getBy(user.getId());
//		updateUser.setEmail("test01update@foreveross.com");
//		updateUser.setTelePhone("18665588990");
		updateUser.setCreateOwner("test01");
		updateUser.setDescription("ceshi");
		updateUser.setName("更新test01");
		updateUser.save();
		User actual = User.getBy(updateUser.getId());
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertUser(updateUser, actual);
	}

	@Test
	public void testDisable() throws Exception {
		User user = initUser();
		user.save();
		assertFalse(user.isDisabled());
		user.disable();
		assertTrue(user.isDisabled());
	}

	@Test
	public void testEnable() throws Exception {
		User user = initUser();
		user.save();
		assertFalse(user.isDisabled());
		user.disable();
		assertTrue(user.isDisabled());
		user.enable();
		assertFalse(user.isDisabled());
	}

	@Test
	public void testGetUserById() throws Exception {
		User user = initUser();
		user.save();
		User getUser = User.getBy(user.getId());
		assertNotNull(getUser);
	}

	@Test
	public void testGetByUserAccount() throws Exception {
		User user = initUser();
		user.save();
		User getUser = User.getBy(user.getUserAccount());
		assertNotNull(getUser);
	}

	@Test(expected = UserNotHasRoleException.class)
	public void testFindAllRolesByUserAccountNoHasRole() throws Exception {
		User user = initUser();
		user.save();
		User.findAllRolesBy(user.getUserAccount());
	}
	
	@Test
	public void testFindAllRolesByUserAccount() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		new Authorization(user, role).save();
		List<Role> roles = User.findAllRolesBy(user.getUserAccount());
		assertEquals(1, roles.size());
	}

	@Test
	public void testfindAllPermissionsByUserAccount() throws Exception {
		User user = initUser();
		user.save();
		List<Permission> permissions = User.findAllPermissionsBy(user.getUserAccount());
		assertEquals(0, permissions.size());
		Permission permission = initPermission();
		permission.save();
		new Authorization(user, permission).save();
		permissions = User.findAllPermissionsBy(user.getUserAccount());
		assertEquals(1, permissions.size());
	}

	/**
	 * XXX 同一个Session中。
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePassword() throws Exception {
		User user = initUser();
		encryptPassword("aaa", "670b14728ad9902aecba32e22fa4f6bd");
		user.save();
		User loadUser = User.getBy(user.getId());
//		loadUser.setPassword("170b25431ad9902aecba32e22fa4f6bd");
//		loadUser.updatePassword(user.getPassword());
	}

	@Test
	public void testResetPassword() throws Exception {
		User user = initUser();
		encryptPassword("aaaaaa","670b14728ad9902aecba32e22fa4f6bd");
		user.save();
		user.resetPassword();
	}

	@Test
	public void testGetUserCount() throws Exception {
		User user = initUser();
		user.save();
		long size = User.getCount();
		assertTrue(size > 0);
	}

	// TODO 登陆
	@Ignore
	@Test
	public void testLogin() throws Exception {
		User user = initUser();
		encryptPassword(user.getPassword(),"aaaaaaaaa");
		user.save();
		assertNotNull(user.getPassword());
		User loginUser = User.login(user.getUserAccount(), user.getPassword());
		assertNotNull(loginUser);
	}

	@Test(expected = NullArgumentException.class)
	public void testLoginUserAccountOrPasswordIsNull() throws Exception {
		User user = initUser();
		encryptPassword("aaaaaa","aaaaaaaaa");
		user.save();
		User.login("", user.getPassword());
	}
	
	/*------------- Private helper methods  -----------------*/

	private void encryptPassword(String password, String returnPassword) {
		Mockito.when(User.encryptPassword(password)).thenReturn(returnPassword);
	}
}
