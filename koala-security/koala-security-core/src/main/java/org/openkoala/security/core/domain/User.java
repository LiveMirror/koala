package org.openkoala.security.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.InstanceFactory;
import org.hibernate.validator.constraints.Email;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotExistedException;
import org.openkoala.security.core.UserNotHasRoleException;
import org.openkoala.security.core.UserPasswordException;
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

	@NotNull
	@Column(name = "USER_ACCOUNT")
	private String userAccount;

	@Column(name = "PASSWORD")
	private String password = INIT_PASSWORD;

	@Email
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

	protected User() {
	}

	public User(String name, String userAccount) {
		super(name);
		checkArgumentIsNull("userAccount", userAccount);
		isExistUserAccount(userAccount);
		this.userAccount = userAccount;
		String password = encryptPassword(this.getPassword());
		this.password = password;
	}

	// public User(String userAccount, String password) {
	// if (!isNew()) {
	// User user = getBy(this.getId());
	// if (!user.getUserAccount().equals(userAccount)) {
	// isExistUserAccount(userAccount);
	// }
	// } else {
	// isExistUserAccount(userAccount);
	// }
	// this.userAccount = userAccount;
	// this.password = password;
	// }

	/**
	 * XXX 不能在构造方法中检查。因为删除的会报错。
	 * 
	 * @param userAccount
	 * @param password
	 * @param email
	 * @param telePhone
	 */
	public User(String userAccount, String password, String email, String telePhone) {
		this(userAccount, password);
		checkArgumentIsNull("email", email);
		checkArgumentIsNull("telePhone", telePhone);
		if (!isNew()) {
			User user = getBy(this.getId());
			if (!user.getEmail().equals(email)) {
				isExistEmail(email);
			}
			if (!user.getTelePhone().equals(telePhone)) {
				isExistTelePhone(telePhone);
			}
			this.setLastModifyTime(new Date());
		} else {
			isExistEmail(email);
			isExistTelePhone(telePhone);
		}
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
		super.save();
	}

	/**
	 * 更新用户
	 */
	// @Override
	// public void update() {
	// User user = getBy(this.getId());
	// if (user == null) {
	// throw new NullArgumentException("user");
	// }
	//
	// if (!StringUtils.isBlank(this.getEmail()) && !this.getEmail().equals(user.getEmail())) {
	// user.setEmail(this.getEmail());
	// }
	//
	// if (!StringUtils.isBlank(this.getTelePhone()) && !this.getTelePhone().equals(user.getTelePhone())) {
	// isExistTelePhone(this.getTelePhone());
	// user.setTelePhone(this.getTelePhone());
	// }
	//
	// // 每次修改自动插入修改时间。
	// user.setLastModifyTime(new Date());
	//
	// if (!StringUtils.isBlank(this.getCreateOwner())) {
	// user.setCreateOwner(this.getCreateOwner());
	// }
	//
	// user.setName(this.getName());
	// user.setDescription(this.getDescription());
	// }

	public static User getBy(Long userId) {
		return User.get(User.class, userId);
	}

	public static User getBy(String userAccount) {
		User result = getRepository().createCriteriaQuery(User.class)//
				.eq("userAccount", userAccount) //
				.singleResult();
		return result;
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
		String password = encryptPassword(INIT_PASSWORD);
		user.password = password;
	}

	public static User login(String principal, String password) {
		checkArgumentIsNull("principal", principal);
		checkArgumentIsNull("password", password);
		String loginPassword = encryptPassword(password);
		User user = getRepository()//
				.createNamedQuery("User.loginByUserAccount")//
				.addParameter("userAccount", principal)//
				.addParameter("password", loginPassword)//
				.singleResult();

		if (user == null) {
			throw new UserNotExistedException("userAccount or password is error");
		} else {
			changeLastLoginTime(user);
		}

		return user;
	}

	private static void changeLastLoginTime(User user) {
		user.lastLoginTime = new Date();
	}

	public static long getCount() {
		return getRepository().createNamedQuery("User.count").singleResult();
	}

	/**
	 * 修改最后登陆时间。
	 */
	public void updateLastLoginTime() {
		User user = getBy(this.getId());
		changeLastLoginTime(user);
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

	protected static String encryptPassword(String password) {
		return getPasswordService().encryptPassword(password, null);
	}

	/**
	 * 更改账户
	 * TODO 更加严格的验证
	 * @param userAccount
	 */
	public void changeUserAccount(String userAccount, String userPassword) {

		verifyPassword(userPassword);

		if (!this.getUserAccount().equals(userAccount)) {
			isExistUserAccount(userAccount);
		}
		this.userAccount = userAccount;
		save();
	}

	private void verifyPassword(String userPassword) {
		checkArgumentIsNull("userPassword", userPassword);

		if (!this.getPassword().equals(encryptPassword(userPassword))) {
			throw new UserPasswordException("user password is not right.");
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
		}
		this.email = email;
		save();
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
		}
		this.telePhone = telePhone;
		save();
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
		if (getBy(userAccount) != null) {
			throw new UserAccountIsExistedException("user userAccount is existed.");
		}
	}

	private static void checkArgumentIsNull(String nullMessage, String argument) {
		if (StringUtils.isBlank(argument)) {
			throw new NullArgumentException(nullMessage);
		}
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

	@Override
	public String[] businessKeys() {
		return new String[] { "userAccount" };
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getId())//
				.append(userAccount)//
				.append(email)//
				.append(telePhone)//
				.append(getName())//
				.build();
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