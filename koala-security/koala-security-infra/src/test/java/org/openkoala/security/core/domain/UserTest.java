package org.openkoala.security.core.domain;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.AbstractInfrantegrationTestCase;

import static org.junit.Assert.*;

@Ignore
public class UserTest extends AbstractInfrantegrationTestCase {

    @Test
    public void testSave() throws Exception {
        User user = initUser();
        assertFalse(user.existed());
        user.save();
        User loadUser = User.getById(user.getId());
        assertTrue(user.existed());
        assertNotNull(user.getId());
        assertUser(user, loadUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeUserAccountAndPasswordIsNull() throws Exception {
        String userAccount = "test000000000000000002";
        User user = initUser();
        user.save();
        assertNotNull(user.getId());
        user.changeUserAccount(userAccount, null);
    }

    @Test
    public void testChangeUserAccount() throws Exception {
        String userAccount = "test000000000000000002";
        String password = "888888";
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
        User user = initUser();
        user.save();

        new User("测试000000000000000002", userAccount).save();
        assertNotNull(user.getId());
        user.changeUserAccount(userAccount, password);
        User loadUser = User.getById(user.getId());
        assertNotNull(loadUser);
        assertEquals(userAccount, loadUser.getUserAccount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeEmailAndPasswordIsNull() throws Exception {
        String email = "test000000000000000002@koala.com";
        User user = initUser();
        user.save();
        assertNotNull(user.getId());
        user.changeEmail(email, null);
    }

    @Test
    public void testChangeEmail() throws Exception {
        String email = "test000000000000000002@koala.com";
        String password = "888888";
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

    @Test(expected = IllegalArgumentException.class)
    public void testChangeTelePhoneAndPasswordIsNull() throws Exception {
        String telePhone = "17654321987";
        String password = "888888";
        User user = initUser();
        user.save();
        assertNotNull(user.getId());
        user.changeTelePhone(telePhone, null);
    }

    @Test
    public void testChangeTelePhone() throws Exception {
        String telePhone = "17654321987";
        String password = "888888";
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
    public void testGetByEmail() throws Exception {
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

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePasswordAndOldPasswordIsNull() throws Exception {
        String userPassword = "999999";
        User user = initUser();
        user.save();
        User loadUser = User.getById(user.getId());
        loadUser.updatePassword(userPassword, null);
    }

    @Test
    public void testUpdatePassword() throws Exception {
        String oldUserPassword = "888888";
        String userPassword = "999999";
        User user = initUser();
        user.save();
        User loadUser = User.getById(user.getId());
        boolean result = loadUser.updatePassword(userPassword, oldUserPassword);
        assertTrue(result);
    }

    /*------------- Private helper methods  -----------------*/
    public User initUser() {
        User result = new User("测试000000000000000001", "test000000000000000001");
        result.setCreateOwner("admin");
        result.setDescription("测试");
        return result;
    }

    public void assertUser(User expected, User actual) {
        assertEquals(expected.getCreateOwner(), actual.getCreateOwner());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getTelePhone(), actual.getTelePhone());
        assertEquals(expected.getUserAccount(), actual.getUserAccount());
        assertEquals(expected.getLastModifyTime(), actual.getLastModifyTime());
    }
}
