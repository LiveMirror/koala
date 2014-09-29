package org.openkoala.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.openkoala.security.shiro.realm.ShiroUser;

public final class CurrentUser {

	public static ShiroUser getPrincipal() {
		return (ShiroUser) getSubject().getPrincipal();
	}

	public static String getUserAccount() {
		return getPrincipal().getUserAccount();
	}

	public static String getRoleName() {
		return getPrincipal().getRoleName();
	}

    public static String getTelePhone(){
        return getPrincipal().getTelePhone();
    }

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

    public static PrincipalCollection getPrincipals(){
        return getSubject().getPrincipals();
    }

	public static org.apache.shiro.mgt.SecurityManager getSecurityManager() {
       return SecurityUtils.getSecurityManager();
	}
	
}
