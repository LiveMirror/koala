package org.openkoala.security.facade.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 6559662110574697001L;

	private Long id;

	private int version;

	private String name;

	private String userAccount;

	private String userPassword;

	private Date createDate;

	private String email;

	private String description;

	private String oldUserPassword;

	private Date lastLoginTime;

	private String telePhone;

	private String createOwner;

	private Date lastModifyTime;

	private String salt;

	/**
	 * TRUE FALSE NULL
	 */
	private Boolean disabled;

	protected UserDTO() {
	}

	public UserDTO(Long id) {
		this.id = id;
	}

	public UserDTO(String userAccount, String userPassword) {
		this.userAccount = userAccount;
		this.userPassword = userPassword;
	}

	public UserDTO(String userAccount, String userPassword, Date createDate, String description) {
		this.userAccount = userAccount;
		this.userPassword = userPassword;
		this.createDate = createDate;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCreateOwner() {
		return createOwner;
	}

	public void setCreateOwner(String createOwner) {
		this.createOwner = createOwner;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()//
				.append(userPassword)//
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof UserDTO)) {
			return false;
		}
		UserDTO that = (UserDTO) other;
		return new EqualsBuilder()//
				.append(this.getUserAccount(), that.getUserAccount())//
				.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getId())//
				.append(getName())//
				.append(getUserAccount())//
				.append(getTelePhone())//
				.append(getEmail())//
				.append(getCreateOwner())//
				.append(getDescription())//
				.append(getDisabled())//
				.append(getDisabled())//
				.append(getLastLoginTime())//
				.append(getLastModifyTime())//
				.build();
	}
}
