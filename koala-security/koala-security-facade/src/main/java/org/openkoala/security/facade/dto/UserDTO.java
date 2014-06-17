package org.openkoala.security.facade.dto;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 6559662110574697001L;

	private Long id;

	private String name;

	private String userAccount;

	private String userPassword;

	private Date createDate;

	private String email;

	private String description;

	private String oldUserPassword;

	private String userStatus;

	private Date lastLoginTime;

	private String telePhone;

	public UserDTO() {
	}

	public UserDTO(String username, String userPassword) {
		this.userAccount = username;
		this.userPassword = userPassword;
	}

	public UserDTO(String username, String userPassword, Date createDate, String description, String userStatus) {
		this.userAccount = username;
		this.userPassword = userPassword;
		this.createDate = createDate;
		this.description = description;
		this.userStatus = userStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOldPassword() {
		return oldUserPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldUserPassword = oldPassword;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getOldUserPassword() {
		return oldUserPassword;
	}

	public void setOldUserPassword(String oldUserPassword) {
		this.oldUserPassword = oldUserPassword;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
}
