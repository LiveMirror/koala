package org.openkoala.security.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotExistedException;
import org.openkoala.security.core.UserNotHasRoleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * "ASC" 和 "DESC" 分别为升序和降序， JPQL 中默认为 ASC 升序
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("USER")
@NamedQueries({
		@NamedQuery(name = "User.loginByUserAccount", query = "SELECT _user FROM User _user WHERE _user.userAccount = :userAccount AND _user.password = :password"),
		@NamedQuery(name = "User.count", query = "SELECT COUNT(_user.id) FROM User _user") })
public class User extends Actor {

	private static final long serialVersionUID = 7849700468353029794L;

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

	private static final String INIT_PASSWORD = "888888";

	@Column(name = "USER_ACCOUNT")
	private String userAccount;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "DISABLED")
	private boolean disabled = false;

	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "TELE_PHONE")
	private String telePhone;

	/**
	 * 加密盐值
	 */
	@Column(name = "SALT")
	private String salt;

	protected User() {}

	public User(String userAccount, String password) {
		this.userAccount = userAccount;
		this.password = password;
	}

	/**
	 * XXX 不能在构造方法中检查。因为删除的会报错。
	 * 
	 * @param userAccount
	 * @param password
	 * @param email
	 * @param telePhone
	 */
	public User(String userAccount, String password, String email, String telePhone) {
		// isBlanked(userAccount, password, email, telePhone);
		this.userAccount = userAccount;
		this.password = password;
		this.email = email;
		this.telePhone = telePhone;
		// this.salt = generateSalt();
	}

	// ~ Methods
	// ========================================================================================================

	public void disable() {
		disabled = true;
	}

	public void enable() {
		disabled = false;
	}

	/**
	 * TODO 邮箱、电话可能为空。 保存用户 TODO 验证规则，账号，邮箱，电话。
	 */
	@Override
	public void save() {
		isExisted();
		String password = encryptPassword(this);
		this.setPassword(password);
		this.setLastModifyTime(new Date());
		super.save();
	}

	/**
	 * 更新用户
	 */
	@Override
	public void update() {
		User user = getBy(this.getId());
		if (user == null) {
			throw new NullArgumentException("user");
		}

		if (!StringUtils.isBlank(this.getEmail()) && !this.getEmail().equals(user.getEmail())) {
			isExistEmail(this.getEmail());
			user.setEmail(this.getEmail());
		}

		if (!StringUtils.isBlank(this.getTelePhone()) && !this.getTelePhone().equals(user.getTelePhone())) {
			isExistTelePhone(this.getTelePhone());
			user.setTelePhone(this.getTelePhone());
		}

		// 每次修改自动插入修改时间。
		user.setLastModifyTime(new Date());

		if (!StringUtils.isBlank(this.getCreateOwner())) {
			user.setCreateOwner(this.getCreateOwner());
		}

		user.setName(this.getName());
		user.setDescription(this.getDescription());
	}

	public static User getBy(Long userId) {
		if (StringUtils.isBlank(userId + "")) {
			throw new NullArgumentException("user.id");
		}
		return User.get(User.class, userId);
	}

	public static User getBy(String userAccount) {
		if (StringUtils.isBlank(userAccount)) {
			throw new NullArgumentException("user.userAccount");
		}
		User user = getRepository().createCriteriaQuery(User.class)//
				.eq("userAccount", userAccount) //
				.singleResult();
		return user == null ? null : user;
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

	public boolean updatePassword(String oldUserPassword) {
		User user = getBy(this.getUserAccount());
		String encryptOldUserPassword = encryptPassword(new User(user.getUserAccount(), oldUserPassword));
		if (user.getPassword().equals(encryptOldUserPassword)) {
			String password = passwordService.encryptPassword(this);
			user.setPassword(password);
			return true;
		}
		return false;
	}

	public void resetPassword() {
		User user = User.get(User.class, this.getId());
		String password = encryptPassword(new User(user.getUserAccount(), INIT_PASSWORD));
		user.setPassword(password);
	}

	public static User login(String principal, String password) {
		if (StringUtils.isBlank(principal) || StringUtils.isBlank(password)) {
			throw new NullArgumentException("userAccount or password is empty ");
		}
		User user = getRepository()//
				.createNamedQuery("User.loginByUserAccount")//
				.addParameter("userAccount", principal)//
				.addParameter("password", encryptPassword(new User(principal, password)))//
				.singleResult();

		if (user == null) {
			throw new UserNotExistedException("userAccount or password is error");
		}
		return user;
	}

	public static long getCount() {
		return getRepository().createNamedQuery("User.count").singleResult();
	}

	/**
	 * 修改最后登陆时间。
	 */
	public void updateLastLoginTime() {
		User user = getBy(this.getId());
		user.lastLoginTime = new Date();
	}

	protected static PasswordService passwordService;

	protected static void setPasswordService(PasswordService passwordService) {
		User.passwordService = passwordService;
	}

	protected static PasswordService getPasswordService() {
		if (passwordService == null) {
			passwordService = InstanceFactory.getInstance(PasswordService.class, "passwordService");
		}
		return passwordService;
	}

	protected static String encryptPassword(User user) {
		return getPasswordService().encryptPassword(user);
	}

	/*------------- Private helper methods  -----------------*/

	private boolean isExistTelePhone(String telePhone) {
		if (StringUtils.isBlank(telePhone)) {
			throw new NullArgumentException("user.telePhone");
		}
		User user = getRepository().createCriteriaQuery(User.class)//
				.eq("telePhone", telePhone)//
				.singleResult();
		return user != null;
	}

	private boolean isExistEmail(String email) {
		if (StringUtils.isBlank(email)) {
			throw new NullArgumentException("user.email");
		}
		User user = getRepository().createCriteriaQuery(User.class)//
				.eq("email", email)//
				.singleResult();
		return user != null;
	}

	private boolean isExistUserAccount(String userAccount) {
		return getBy(userAccount) != null;
	}

	/**
	 * 生成盐值
	 * 
	 * @return
	 */
	/*
	 * private String generateSalt() { SecureRandom random = new SecureRandom(); byte[] bytes = new byte[8];
	 * random.nextBytes(bytes); try { return new String(bytes, "UTF-8"); } catch (UnsupportedEncodingException e) {
	 * throw new RuntimeException(e); } }
	 */

	private void isExisted() {
		if (isExistUserAccount(this.getUserAccount())) {
			throw new UserAccountIsExistedException("user.userAccount.existed");
		}

		if (isExistEmail(this.getEmail())) {
			throw new EmailIsExistedException("user.email.existed");
		}

		if (isExistTelePhone(this.getTelePhone())) {
			throw new TelePhoneIsExistedException("user.telePhone.existed");
		}
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "userAccount" };
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	protected void setEmail(String email) {
		this.email = email;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public String getTelePhone() {
		return telePhone;
	}

	protected void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getSalt() {
		return salt;
	}

}