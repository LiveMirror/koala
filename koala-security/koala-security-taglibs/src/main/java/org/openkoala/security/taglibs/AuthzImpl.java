package org.openkoala.security.taglibs;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.shiro.realm.CustomAuthoringRealm.ShiroUser;
import org.openkoala.security.shiro.util.AuthUserUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AuthzImpl implements Authz {

	private ApplicationContext applicationContext;

	private SecurityConfigFacade securityConfigFacade;

	private ServletContext servletContext;

	public ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		}
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object getPrincipal() {
		return AuthUserUtil.getCurrentUser();
	}

	@Override
	public boolean ifAllRole(Collection<String> roles) {
		return AuthUserUtil.getSubject().hasAllRoles(roles);
	}

	@Override
	public boolean ifAnyRole(Collection<String> roles) {
		for (String role : roles) {
			if (AuthUserUtil.getSubject().hasRole(role)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean ifNotRole(Collection<String> roles) {
		return !ifAnyRole(roles);
	}

	@Override
	public boolean ifAllPermission(Collection<String> permissions) {
		return AuthUserUtil.getSubject().isPermittedAll(permissions.toString());
	}

	@Override
	public boolean ifAnyPermission(Collection<String> permissions) {
		for (String permission : permissions) {
			if (AuthUserUtil.getSubject().isPermitted(permission)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean ifNotPermission(Collection<String> permissions) {
		return !ifAnyPermission(permissions);
	}

	@Override
	public boolean hasSecurityResource(String securityResourceIdentifier) {
		ShiroUser shiroUser = (ShiroUser) getPrincipal();
		if (shiroUser == null) {
			return false;
		}
		
		String userAccount = shiroUser.getUserAccount();
		String currentRoleName = shiroUser.getRoleName();
		
		if(securityConfigFacade == null){
			securityConfigFacade = applicationContext.getBean(SecurityConfigFacade.class);
		}
		
		boolean hasResource = securityConfigFacade.checkUserHasPageElementResource(userAccount, currentRoleName,securityResourceIdentifier);
		
		return hasResource ? true : false;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
