package org.openkoala.security.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openkoala.security.web.filter.ShiroFilerChainManager;
import org.openkoala.security.web.ini.CustomDefaultFilterChainManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SystemEvenmentListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		CustomDefaultFilterChainManager customDefaultFilterChainManager = (CustomDefaultFilterChainManager) applicationContext.getBean("customDefaultFilterChainManager");
		ShiroFilerChainManager ShiroFilerChainManager = (org.openkoala.security.web.filter.ShiroFilerChainManager) applicationContext.getBean("shiroFilerChainManager");
		ShiroFilerChainManager.init();
		ShiroFilerChainManager.initFilterChain();
		customDefaultFilterChainManager.init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
