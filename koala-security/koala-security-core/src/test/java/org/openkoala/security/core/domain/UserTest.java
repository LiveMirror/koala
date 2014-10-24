package org.openkoala.security.core.domain;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.*;

import java.util.List;

import org.junit.Test;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotHasRoleException;

public class UserTest extends AbstractDomainIntegrationTestCase {

    // ~ User Save Test
    // ========================================================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testSaveUserAccountIsNull() throws Exception {
        new User("测试000000000000000002", null);
    }

    @Test(expected = UserAccountIsExistedException.class)
    public void testSaveUserAccountExisted() throws Exception {
        initUser().save();
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

    @Test
    public void testGetUserCount() throws Exception {
        User user = initUser();
        user.save();
        User.hasUserExisted();
    }
}
