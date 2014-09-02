package org.openkoala.security.core.domain;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.*;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotHasRoleException;

public class UserTest extends AbstractDomainIntegrationTestCase {

	// ~ User Save Test
	// ========================================================================================================

	@Test
	public void testSave() throws Exception {
		encryptPassword("000000", "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		assertFalse(user.existed());
		user.save();
		User loadUser = User.getById(user.getId());
		assertTrue(user.existed());
		assertNotNull(user.getId());
		assertUser(user, loadUser);
	}

	@Test(expected = NullArgumentException.class)
	public void testSaveUserAccountIsNull() throws Exception {
		new User("测试000000000000000002", null);
	}

	@Test(expected = UserAccountIsExistedException.class)
	public void testSaveUserAccountExisted() throws Exception {
		testSave();
		User user = new User("测试000000000000000003", "test000000000000000001");
		user.save();
	}

	// ~ User Change Properties Test
	// ========================================================================================================

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

	@Test(expected = NullArgumentException.class)
	public void testChangeUserAccountAndPasswordIsNull() throws Exception {
		String userAccount = "test000000000000000002";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeUserAccount(userAccount, null);
	}

	@Test
	public void testChangeUserAccount() throws Exception {
		String userAccount = "test000000000000000002";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeUserAccount(userAccount, password);
		User loadUser = User.getById(user.getId());
		assertNotNull(loadUser);
		assertEquals(userAccount, loadUser.getUserAccount());
	}

	@Test(expected = UserAccountIsExistedException.class)
	public void testChangeUserAccountIsExisted() throws Exception {
		String userAccount = "test000000000000000002";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();

		new User("测试000000000000000002", userAccount).save();
		assertNotNull(user.getId());
		user.changeUserAccount(userAccount, password);
		User loadUser = User.getById(user.getId());
		assertNotNull(loadUser);
		assertEquals(userAccount, loadUser.getUserAccount());
	}

	@Test(expected = NullArgumentException.class)
	public void testChangeEmailAndPasswordIsNull() throws Exception {
		String email = "test000000000000000002@koala.com";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeEmail(email, null);
	}

	@Test
	public void testChangeEmail() throws Exception {
		String email = "test000000000000000002@koala.com";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeEmail(email, password);
		User loadUser = User.getById(user.getId());
		assertNotNull(loadUser);
		assertEquals(email, loadUser.getEmail());
	}

	@Test(expected = EmailIsExistedException.class)
	public void testChangeEmailIsExisted() throws Exception {
		String email = "test000000000000000001@koala.com";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeEmail(email, password);
		User loadUser = User.getById(user.getId());
		assertNotNull(loadUser);
		assertEquals(email, loadUser.getEmail());

		User user2 = new User("测试000000000000000002", "test000000000000000002");
		user2.save();
		assertNotNull(user2.getId());
		user2.changeEmail(email, password);
	}

	@Test(expected = NullArgumentException.class)
	public void testChangeTelePhoneAndPasswordIsNull() throws Exception {
		String telePhone = "17654321987";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeTelePhone(telePhone, null);
	}
	
	@Test
	public void testChangeTelePhone() throws Exception {
		String telePhone = "17654321987";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeTelePhone(telePhone, password);
		User loadUser = User.getById(user.getId());
		assertNotNull(loadUser);
		assertEquals(telePhone, loadUser.getTelePhone());
	}

	@Test(expected = TelePhoneIsExistedException.class)
	public void testChangeTelePhoneisExisted() throws Exception {
		String telePhone = "17654321987";
		String password = "888888";
		encryptPassword(password, "670b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		assertNotNull(user.getId());
		user.changeTelePhone(telePhone, password);
		User loadUser = User.getById(user.getId());
		assertNotNull(loadUser);
		assertEquals(telePhone, loadUser.getTelePhone());

		User user2 = new User("测试000000000000000002", "test000000000000000002");
		user2.save();
		assertNotNull(user2.getId());
		user2.changeTelePhone(telePhone, password);
	}

	@Test
	public void testGetUserById() throws Exception {
		User user = initUser();
		user.save();
		User getUser = User.getById(user.getId());
		assertNotNull(getUser);
	}

	@Test
	public void testGetByUserAccount() throws Exception {
		User user = initUser();
		user.save();
		User getUser = User.getByUserAccount(user.getUserAccount());
		assertNotNull(getUser);
	}

	@Test
	public void testGetByEmail() throws Exception {
		encryptPassword("888888", "21218cca77804d2ba1922c33e0151105");
		String email = "test000000000000000001@koala.com";
		User user = initUser();
		user.save();
		assertNotNull(user.getPassword());
		user.changeEmail(email, "888888");
		User result = User.getByEmail(email);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertUser(user, result);
	}
	
	@Test
	public void testGetByTelePhone() throws Exception {
		encryptPassword("888888", "21218cca77804d2ba1922c33e0151105");
		String telePhone = "18665589100";
		User user = initUser();
		user.save();
		assertNotNull(user.getPassword());
		user.changeTelePhone(telePhone, "888888");
		User result = User.getByTelePhone(telePhone);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertUser(user, result);
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

	@Test(expected = NullArgumentException.class)
	public void testUpdatePasswordAndOldPasswordIsNull() throws Exception {
		String oldUserPassword = "888888";
		String userPassword = "999999";
		encryptPassword(oldUserPassword, "670b14728ad9902aecba32e22fa4f6bd");
		encryptPassword(userPassword, "170b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		User loadUser = User.getById(user.getId());
		loadUser.updatePassword(userPassword, null);
	}
	
	@Test
	public void testUpdatePassword() throws Exception {
		String oldUserPassword = "888888";
		String userPassword = "999999";
		encryptPassword(oldUserPassword, "670b14728ad9902aecba32e22fa4f6bd");
		encryptPassword(userPassword, "170b14728ad9902aecba32e22fa4f6bd");
		User user = initUser();
		user.save();
		User loadUser = User.getById(user.getId());
		boolean result = loadUser.updatePassword(userPassword, oldUserPassword);
		assertTrue(result);
	}
	
	

	/**
	 * TODO 放在infor层测试。
	 * 
	 * @throws Exception
	 */
	@Test
	public void testResetPassword() throws Exception {
		User user = initUser();
		encryptPassword("aaaaaa", "670b14728ad9902aecba32e22fa4f6bd");
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

	/*------------- Private helper methods  -----------------*/

	private void encryptPassword(String password, String returnPassword) {
		Mockito.when(User.encryptPassword(password)).thenReturn(returnPassword);
	}
}
