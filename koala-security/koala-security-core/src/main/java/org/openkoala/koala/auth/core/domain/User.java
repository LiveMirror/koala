package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.dayatang.utils.DateUtils;

/**
 * 用户实体类
 * 
 * @author lingen
 * 
 */

@Entity
public class User extends Identity {

	private static final long serialVersionUID = 1828900234948658820L;

	private Date lastLoginTime;

	private String userAccount;

	private String userPassword;

	private String userDesc;

	private String email;

	private Date lastModifyTime;

	private boolean isSuper;

	public User() {

	}

	public User(String name, String account, String password, String desc) {
		this.setCreateDate(new Date());
		this.setAbolishDate(DateUtils.MAX_DATE);
		this.setName(name);
		this.userAccount = account;
		this.userPassword = password;
		this.userDesc = desc;
	}

	/**
	 * 获取用户所拥有的角色
	 * 
	 * @return
	 */
	public List<RoleUserAuthorization> findRoles() {
		return RoleUserAuthorization.findAuthorizationByUser(this);
	}

	@Column(name = "LAST_LOGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "USER_ACCOUNT")
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	@Column(name = "USER_PASSWORD")
	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Column(name = "USER_DESC")
	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	@Column(name = "LAST_MODIFY_TIME")
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "IS_SUPER")
	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public static User findByUserAccount(String userAccount) {
		try {
			return getRepository()
					.createJpqlQuery(
							"select m from User m where m.userAccount=:userAccount and m.abolishDate>:abolishDate")
					.addParameter("userAccount", userAccount)
					.addParameter("abolishDate", new Date()).singleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public static User findByEmail(String email) {
		try {

			return getRepository()
					.createJpqlQuery(
							"select user from User user where user.email=?")
					.addParameter("email", email).singleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 为用户分配单个角色
	 * 
	 * @param role
	 */
	public void assignRole(Role role) {
		saveRoleUser(role);
	}

	/**
	 * 为用户分配多个角色
	 * 
	 * @param roles
	 */
	public void assignRole(List<Role> roles) {
		for (Role role : roles) {
			assignRole(role);
		}
	}

	/**
	 * 用户账号是否存在
	 * 
	 * @return
	 */
	@Transient
	public boolean isAccountExisted() {
		return !getRepository().createNamedQuery("isAccountExisted")
				.addParameter("userAccount", userAccount)
				.addParameter("abolishDate", new Date()).list().isEmpty();

	}

	@Transient
	public boolean isEmailExisted() {
		return !getRepository().createNamedQuery("isEmailExisted")
				.addParameter("email", email)
				.addParameter("abolishDate", new Date()).list().isEmpty();
	}

	/**
	 * 废除用户所拥有的角色
	 * 
	 * @param role
	 */
	public void abolishRole(Role role) {
		String jpql = "select m from RoleUserAuthorization m where m.user.id=:userId and " //
				+ " m.role.id=:roleId and m.abolishDate>:abolishDate";

		List<RoleUserAuthorization> authorizations = getRepository()
				.createJpqlQuery(jpql).addParameter("userId", getId())
				.addParameter("roleId", role.getId())
				.addParameter("abolishDate", new Date()).list();
		for (RoleUserAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}

	}

	/**
	 * 重置密码
	 */
	public void resetPassword() {
		this.setUserPassword("abcd");
		this.save();
	}

	/**
	 * 保存角色与用户的关系
	 * 
	 * @param role
	 */
	private void saveRoleUser(Role role) {
		RoleUserAuthorization roleUserAssignment = new RoleUserAuthorization();
		roleUserAssignment.setAbolishDate(DateUtils.MAX_DATE);
		roleUserAssignment.setCreateDate(new Date());
		roleUserAssignment.setScheduledAbolishDate(new Date());
		roleUserAssignment.setRole(role);
		roleUserAssignment.setUser(this);
		roleUserAssignment.save();
	}

	@Override
	public String[] businessKeys() {
		return new String[] { String.valueOf(getId()), this.getName(),
				this.getAbolishDate().toString() };
	}

	@Override
	public String toString() {
		return "User [lastLoginTime=" + lastLoginTime + ", userAccount="
				+ userAccount + ", userPassword=" + userPassword
				+ ", userDesc=" + userDesc + ", email=" + email
				+ ", lastModifyTime=" + lastModifyTime + ", isSuper=" + isSuper
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime
				* result
				+ ((getAbolishDate() == null) ? 0 : getAbolishDate().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		
		if (getAbolishDate() == null) {
			if (other.getAbolishDate() != null)
				return false;
		} else if (!getAbolishDate().equals(other.getAbolishDate()))
			return false;

		return true;
	}

}
