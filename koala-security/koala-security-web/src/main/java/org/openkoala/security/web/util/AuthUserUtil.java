package org.openkoala.security.web.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.openkoala.security.web.realm.CustomAuthoringRealm.ShiroUser;

public class AuthUserUtil {

	public static ShiroUser getCurrentUser() {
		return (ShiroUser) getSubject().getPrincipal();
	}

	public static String getUserAccount() {
		return getCurrentUser().getUserAccount();
	}

	public static String getRoleName() {
		return getCurrentUser().getRoleName();
	}

	public static void setRoleName(String roleName) {
		getCurrentUser().setRoleName(roleName);
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static org.apache.shiro.mgt.SecurityManager getSecurityManager() {
		return SecurityUtils.getSecurityManager();
	}
}
