package org.openkoala.security.core.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;

@Entity
@DiscriminatorValue("USER")
public class User extends Actor {

	private static final long serialVersionUID = 7849700468353029794L;

	@Column(name = "USER_ACCOUNT")
	private String userAccount;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "USER_STATUS")
	private UserStatus userStatus;
	
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "IS_SUPER")
	private boolean isSuper;

	@Column(name = "TELE_PHONE")
	private String telePhone;

	User() {
	}

	public User(String userAccount, String password, String email, String telePhone) {
		isBlanked(userAccount, password, email, telePhone);
		this.userAccount = userAccount;
		this.password = password;
		this.email = email;
		this.telePhone = telePhone;
	}

	private void isBlanked(String userAccount, String password, String email, String telePhone) {
		// isBlank(password, "密码不能为空");
		// isBlank(email, "邮箱不能为空");
		// isBlank(telePhone, "联系电话不能为空");
		// isBlank(userAccount, "账户不能为空");
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

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "userAccount" };
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	/**
	 * TODO 密码加密
	 */
	@Override
	public void save() {
		isExisted();
		super.save();
	}

	private void isExisted() {
		if (isExistUserAccount(this.getUserAccount())) {
			throw new UserAccountIsExistedException("user.userAccount.exist");
		}

		if (isExistEmail(this.getEmail())) {
			throw new EmailIsExistedException();
		}

		if (isExistTelePhone(this.getTelePhone())) {
			throw new TelePhoneIsExistedException();
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

	public static Set<Role> findAllRolesBy(String userAccount) {
		return Role.findByUser(getBy(userAccount));
	}

	@Override
	public void update() {
		isExisted();
		isBlanked(this.getUserAccount(), this.getName(), this.getEmail(), this.getTelePhone());

		User user = User.get(User.class, this.getId());
	
		user.setName(this.getName());
		user.setDescription(this.getDescription());
		user.setUserAccount(this.getUserAccount());
		user.setEmail(this.getEmail());
		user.setTelePhone(this.getTelePhone());
		user.setUserStatus(this.userStatus);
	}

	public boolean updatePassword(String oldUserPassword) {
		User result = getBy(this.getUserAccount());
		if (result.getPassword().equals(oldUserPassword)) {
			result.setPassword(this.getPassword());
			return true;
		}
		return false;
	}

	public void resetPassword() {
		User user = User.get(User.class, this.getId());
		user.setPassword(this.getPassword());
	}

}