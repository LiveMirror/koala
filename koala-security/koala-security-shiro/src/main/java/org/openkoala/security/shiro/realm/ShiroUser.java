package org.openkoala.security.shiro.realm;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Shiro用户，主要是让Shiro能够支持多字段的方式登录系统。
 * 目前实现账号、联系电话、和邮箱三种登录方式。
 * 如果还不能满足需求可以扩展该类。
 *
 * @author lucas
 */
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
        return new ToStringBuilder(this)
                .append(getUserAccount())
                .append(getName())
                .append(getRoleName())
                .append(getTelePhone())
                .append(getEmail())
                .build();
    }
}
