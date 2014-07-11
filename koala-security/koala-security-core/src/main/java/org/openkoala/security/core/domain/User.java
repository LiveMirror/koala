package org.openkoala.security.core.domain;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.dayatang.domain.InstanceFactory;
import static org.dayatang.utils.Assert.*;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
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
public class User extends Actor {

	private static final long serialVersionUID = 7849700468353029794L;

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

	@Column(name = "USER_ACCOUNT")
	private String userAccount;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ENABLED")
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

	User() {
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

	private void isBlanked(String userAccount, String password, String email, String telePhone) {
		isBlank(password, "密码不能为空");
		isBlank(email, "邮箱不能为空");
		isBlank(telePhone, "联系电话不能为空");
		isBlank(userAccount, "账户不能为空");
	}

	public void disable() {
		disabled = true;
	}

	public void enable() {
		disabled = false;
	}

	@Override
	public void save() {
		isExisted();
		String password = encryptPassword(this);
		this.setPassword(password);
		LOGGER.info("user save:{}", this);
		super.save();
	}
	
	protected String encryptPassword(User user){
		return getPasswordService().encryptPassword(user);
	}

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

	public static User getBy(String userAccount) {
		User user = getRepository().createCriteriaQuery(User.class)//
				.eq("userAccount", userAccount) //
				.singleResult();
		return user == null ? null : user;
	}

	private boolean isExistTelePhone(String telePhone) {
		User user = getRepository().createCriteriaQuery(User.class)//
				.eq("telePhone", telePhone)//
				.singleResult();
		return user != null;
	}

	private boolean isExistEmail(String email) {
		User user = getRepository().createCriteriaQuery(User.class)//
				.eq("email", email)//
				.singleResult();
		return user != null;
	}

	public boolean isExistUserAccount(String userAccount) {
		return getBy(userAccount) != null;
	}

	/**
	 * @param userAccount
	 * @return
	 */
	public static List<Role> findAllRolesBy(String userAccount) {
		return getRepository()//
				.createNamedQuery("Authority.findAllAuthoritiesByUserAccount")//
				.addParameter("userAccount", userAccount)//
				.addParameter("authorityType", Role.class)//
				.list();
	}

	/**
	 * TODO 使用命名查询
	 * 
	 * @param userAccount
	 * @return
	 */
	public static Set<Permission> findAllPermissionsBy(String userAccount) {
		return Permission.findByUser(getBy(userAccount));
	}

	@Override
	public void update() {
//		isExisted();
//		isBlanked(this.getUserAccount(), this.getName(), this.getEmail(), this.getTelePhone());

		User user = User.get(User.class, this.getId());
		user.setName(this.getName());
		user.setDescription(this.getDescription());
		user.setUserAccount(this.getUserAccount());
		user.setEmail(this.getEmail());
		user.setTelePhone(this.getTelePhone());
	}

	public boolean updatePassword(String oldUserPassword) {
		User result = getBy(this.getUserAccount());
		if (result.getPassword().equals(oldUserPassword)) {
			String password = passwordService.encryptPassword(this);
			result.setPassword(password);
			return true;
		}
		return false;
	}

	public void resetPassword() {
		User user = User.get(User.class, this.getId());
		user.setPassword(this.getPassword());
		String password = getPasswordService().encryptPassword(user);
		user.setPassword(password);
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

	/**
	 * 生成盐值
	 * 
	 * @return
	 */
	private String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[8];
		random.nextBytes(bytes);
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "userAccount" };
	}

	
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getSalt() {
		return salt;
	}

}