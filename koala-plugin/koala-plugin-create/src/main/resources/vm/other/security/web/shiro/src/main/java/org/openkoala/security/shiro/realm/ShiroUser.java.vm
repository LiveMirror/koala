package org.openkoala.security.shiro.realm;

import java.io.Serializable;

public class ShiroUser implements Serializable {

	private static final long serialVersionUID = 573154901435223916L;

	private String userAccount;

	private String name;

	private String roleName;

    private String email;

    private String telePhone;

	public ShiroUser(String userAccount, String name, String roleName) {
		super();
		this.userAccount = userAccount;
		this.name = name;
		this.roleName = roleName;
	}

    public String getUserAccount() {
        return userAccount;
    }

    public String getName() {
        return name;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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


    @Override
	public String toString() {
		return "ShiroUser [userAccount=" + userAccount + ", name=" + name + ", roleName=" + roleName + "]";
	}

	
}
