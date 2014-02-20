package org.openkoala.auth.application.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dayatang.utils.DateUtils;
import org.openkoala.koala.auth.core.domain.User;

public class UserVO extends IdentityVO implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -6619649965246109915L;
	
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private String lastLoginTime;
	
	private String userAccount;
	
	private String userPassword;
	
	private String userDesc;
	
	private String lastModifyTime;
	
	private boolean valid;
	
	private String email;
	
	private String oldPassword;
	
	private boolean isSuper;

	public UserVO() {

	}
	
	public UserVO(Long id, String name, int sortOrder, String userAccount, 
			String userDesc, boolean valid, String email, Date lastLoginTime) {
		this.setId(id);
		this.setName(name);
		this.setSortOrder(sortOrder);
		this.setUserAccount(userAccount);
		this.setUserDesc(userDesc);
		this.setValid(valid);
		this.email = email;
		this.lastLoginTime = lastLoginTime != null ? dateFormat(lastLoginTime) : "";
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public void domain2Vo(User user) {
        this.setId(user.getId());
        this.setLastLoginTime(user.getLastLoginTime() != null ? dateFormat(user.getLastLoginTime()) : "");
        this.setUserAccount(user.getUserAccount());
        this.setUserPassword(user.getUserPassword());
        this.setUserDesc(user.getUserDesc());
        this.setAbolishDate(user.getAbolishDate() !=  null ? dateFormat(user.getAbolishDate()) : "");
        this.setCreateDate(user.getCreateDate() != null ? dateFormat(user.getCreateDate()) : "");
        this.setCreateOwner(user.getCreateOwner());
        this.setName(user.getName());
        this.setSerialNumber(user.getSerialNumber());
        this.setSortOrder(user.getSortOrder());
        this.setValid(user.isValid());
        this.setSuper(user.isSuper());
        this.setEmail(user.getEmail());
	}
	
	public void vo2Domain(User user) {
		user.setAbolishDate(DateUtils.MAX_DATE);
        user.setCreateDate(new Date());
        user.setUserAccount(this.getUserAccount().trim());
        user.setUserPassword(this.getUserPassword());
        user.setUserDesc(this.getUserDesc());
        user.setCreateOwner(this.getCreateOwner());
        user.setName(this.getName());
        user.setSerialNumber(this.getSerialNumber());
        user.setSortOrder(this.getSortOrder());
        user.setValid(this.isValid());
        user.setEmail(this.getEmail());
        user.setSuper(this.isSuper());
	}
	
	private String dateFormat(Date date) {
		return new SimpleDateFormat(DATE_PATTERN).format(date);
	}
	
}
