package org.openkoala.security.web.tags;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

public interface Authz {

	ApplicationContext getApplicationContext();

	void setApplicationContext(ApplicationContext applicationContext);

	public ServletContext getServletContext();

	public void setServletContext(ServletContext servletContext);
	
	Object getPrincipal();
	
	boolean ifAllRole(Collection<String> roles);

	boolean ifAnyRole(Collection<String> roles);

	boolean ifNotRole(Collection<String> roles);

	boolean ifAllPermission(Collection<String> permissions);

	boolean ifAnyPermission(Collection<String> permissions);

	boolean ifNotPermission(Collection<String> permissions);

	boolean hasSecurityResource(String name);

}
