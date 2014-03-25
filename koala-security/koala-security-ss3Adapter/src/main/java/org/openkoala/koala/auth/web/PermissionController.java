package org.openkoala.koala.auth.web;

import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.koala.auth.ss3adapter.AuthUserUtil;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.SecurityMetadataSource;

import java.util.List;

@Named("owner")
public class PermissionController {

	public PermissionController() {

	}

	public String getMessage(String name) {
		return "";
	}

	/**
	 * 获取SecurityMetadataSource实例 
	 * @return
	 */
	public SecurityMetadataSource getSecuritySource() {
		return InstanceFactory.getInstance(SecurityMetadataSource.class, "securityMetadataSource");
	}

	/**
	 * 根据一个资源标识判断用户是否有权限
	 * @param identifier
	 * @return
	 */
	public boolean hasPrivilege(String identifier) {
		if (AuthUserUtil.getLoginUser().isSuper()) {
			return true;
		}
		return getSecuritySource().getResAuthByUseraccount(AuthUserUtil.getLoginUserName(), identifier);
	}

    public boolean hasRole(String role){
        if (AuthUserUtil.getLoginUser().isSuper()) {
            return true;
        }
        List<String> roles =  AuthUserUtil.getRolesByCurrentUser();
        if(roles.contains(role)){
            return true;
        }
        return false;
    }

	/**
	 * 获取登录用户
	 * @return
	 */
	public CustomUserDetails getLoginUser() {
		return AuthUserUtil.getLoginUser();
	}

	/**
	 * 获取登录用户名
	 * @return
	 */
	public String getLoginUsername() {
		return AuthUserUtil.getLoginUserName();
	}

}
