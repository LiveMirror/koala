package org.openkoala.security.core.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotHasRoleException;
import org.openkoala.security.core.UserPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 位于系统外部，与系统交互的人，是使用软件的人。
 * 系统的登录，即认证。
 * 可以对其授予角色 {@link Role}、权限 {@link Permission}和用户组 <code> UserGroup </code>
 *
 * @author lucas
 */
@Entity
@DiscriminatorValue("USER")
@NamedQueries({
        @NamedQuery(name = "User.loginByUserAccount", query = "SELECT _user FROM User _user WHERE _user.userAccount = :userAccount AND _user.password = :password"),
        @NamedQuery(name = "User.count", query = "SELECT COUNT(_user.id) FROM User _user")})
public class User extends Actor {

    private static final long serialVersionUID = 7849700468353029794L;

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    private static final String INIT_PASSWORD = "888888";

    @NotNull
    @Column(name = "USER_ACCOUNT")
    private String userAccount;

    @Column(name = "PASSWORD")
    private String password = INIT_PASSWORD;

    //    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DISABLED")
    private boolean disabled = false;

    @Column(name = "TELE_PHONE")
    private String telePhone;

    /**
     * 加密盐值
     */
    @Column(name = "SALT")
    private String salt;

    protected User() {
    }

    /**
     * TODO 验证规则，账号，邮箱，电话。
     */
    public User(String name, String userAccount) {
        super(name);
        checkArgumentIsNull("userAccount", userAccount);
        isExistUserAccount(userAccount);
        this.userAccount = userAccount;
        String userPassword = encryptPassword(this.getPassword());
        this.password = userPassword;
    }

    public void disable() {
        disabled = true;
    }

    public void enable() {
        disabled = false;
    }

    @Override
    public void save() {
        super.save();
    }

    public boolean updatePassword(String userPassword, String oldUserPassword) {
        String encryptOldUserPassword = encryptPassword(oldUserPassword);
        if (this.getPassword().equals(encryptOldUserPassword)) {
            this.password = encryptPassword(userPassword);
            return true;
        }
        return false;
    }

    public void resetPassword() {
        User user = User.get(User.class, this.getId());
        user.password = encryptPassword(INIT_PASSWORD);
    }

    /**
     * 更改账户 TODO 更加严格的验证
     *
     * @param userAccount
     */
    public void changeUserAccount(String userAccount, String userPassword) {

        verifyPassword(userPassword);

        if (!this.getUserAccount().equals(userAccount)) {
            isExistUserAccount(userAccount);
            this.userAccount = userAccount;
            save();
        }
    }

    /**
     * 更改邮箱
     *
     * @param email
     */
    public void changeEmail(String email, String userPassword) {

        verifyPassword(userPassword);

        // TODO 邮箱验证规则。
        if (!email.equals(this.getEmail())) {
            isExistEmail(email);
            this.email = email;
            save();
        }
    }

    /**
     *  TODO 需要排序
     * @return
     */
    public Set<Role> findAllRoles() {
        List<Role> results = getRepository()//
                .createNamedQuery("Authorization.findAuthoritiesByActor")//
                .addParameter("actor", this)//
                .addParameter("authorityType", Role.class)//
                .list();
        return Sets.newHashSet(results);
    }

    /**
     * TODO 需要排序
     * @return
     */
    public Set<Permission> findAllPermissions() {
        List<Permission> results = getRepository()//
                .createNamedQuery("Authorization.findAuthoritiesByActor")//
                .addParameter("actor", this)//
                .addParameter("authorityType", Permission.class)//
                .list();
        return Sets.newHashSet(results);
    }

    /**
     * 更改联系电话
     *
     * @param telePhone
     */
    public void changeTelePhone(String telePhone, String userPassword) {

        verifyPassword(userPassword);

        // TODO 联系电话验证
        if (!telePhone.equals(this.getTelePhone())) {
            isExistTelePhone(telePhone);
            this.telePhone = telePhone;
            save();
        }
    }

    /**
     * 根据账户查找拥有的所有角色Role
     *
     * @param userAccount
     * @return
     */
    public static List<Role> findAllRolesBy(String userAccount) {
        List<Role> results = getRepository()//
                .createNamedQuery("Authority.findAllAuthoritiesByUserAccount")//
                .addParameter("userAccount", userAccount)//
                .addParameter("authorityType", Role.class)//
                .list();
        if (results.isEmpty()) {
            throw new UserNotHasRoleException("user do have not a role");
        }
        return results;
    }

    /**
     * 根据账户查找拥有的所有权限Permission
     *
     * @param userAccount
     * @return
     */
    public static List<Permission> findAllPermissionsBy(String userAccount) {
        return getRepository()//
                .createNamedQuery("Authority.findAllAuthoritiesByUserAccount")//
                .addParameter("userAccount", userAccount)//
                .addParameter("authorityType", Permission.class)//
                .list();
    }

    public static User getById(Long userId) {
        return User.get(User.class, userId);
    }

    /**
     * TODO 校验规则~~正则表达式
     *
     * @param userAccount
     * @return
     */
    public static User getByUserAccount(String userAccount) {
        checkArgumentIsNull("userAccount", userAccount);
        User result = getRepository()//
                .createCriteriaQuery(User.class)//
                .eq("userAccount", userAccount) //
                .singleResult();
        return result;
    }

    /**
     * TODO 校验规则~~正则表达式
     *
     * @param email
     * @return
     */
    public static User getByEmail(String email) {
        checkArgumentIsNull("email", email);
        User result = getRepository()//
                .createCriteriaQuery(User.class)//
                .eq("email", email) //
                .singleResult();
        return result;
    }

    /**
     * TODO 校验规则~~正则表达式
     *
     * @param telePhone
     * @return
     */
    public static User getByTelePhone(String telePhone) {
        checkArgumentIsNull("telePhone", telePhone);
        User result = getRepository()//
                .createCriteriaQuery(User.class)//
                .eq("telePhone", telePhone) //
                .singleResult();
        return result;
    }

    /**
     * 检查仓储中用户是否有数据。
     *
     * @return
     */
    public static boolean hasUserExisted() {
        Long result = getRepository()//
                .createNamedQuery("User.count")//
                .singleResult();
        return result > 0;
    }

    protected static EncryptService passwordEncryptService;

    protected static EncryptService getPasswordEncryptService() {
        if (passwordEncryptService == null) {
            passwordEncryptService = InstanceFactory.getInstance(EncryptService.class, "encryptService");
        }
        return passwordEncryptService;
    }

    protected static void setPasswordEncryptService(EncryptService passwordEncryptService) {
        User.passwordEncryptService = passwordEncryptService;
    }

    protected static String encryptPassword(String password) {
        checkArgumentIsNull("password", password);
        return getPasswordEncryptService().encryptPassword(password, null);
    }

	/*------------- Private helper methods  -----------------*/

    private void isExistTelePhone(String telePhone) {
        User user = getRepository().createCriteriaQuery(User.class)//
                .eq("telePhone", telePhone)//
                .singleResult();
        if (user != null) {
            throw new TelePhoneIsExistedException("user telePhone is existed.");
        }
    }

    private void isExistEmail(String email) {
        User user = getRepository().createCriteriaQuery(User.class)//
                .eq("email", email)//
                .singleResult();
        if (user != null) {
            throw new EmailIsExistedException("user email is existed.");
        }
    }

    private void isExistUserAccount(String userAccount) {
        if (getByUserAccount(userAccount) != null) {
            throw new UserAccountIsExistedException("user userAccount is existed.");
        }
    }

    private void verifyPassword(String userPassword) {
        checkArgumentIsNull("userPassword", userPassword);

        if (!encryptPassword(userPassword).equals(this.getPassword())) {
            throw new UserPasswordException("user password is not right.");
        }
    }

    @Override
    public String[] businessKeys() {
        return new String[]{"userAccount"};
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getId())
                .append(getUserAccount())
                .append(getEmail())
                .append(getTelePhone())
                .append(getName())
                .build();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public String getSalt() {
        return salt;
    }
}