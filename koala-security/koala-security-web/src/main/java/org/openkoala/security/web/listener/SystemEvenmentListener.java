package org.openkoala.security.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openkoala.security.web.filter.ShiroFilerChainManager;
import org.openkoala.security.web.ini.CustomDefaultFilterChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SystemEvenmentListener implements ServletContextListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemEvenmentListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		CustomDefaultFilterChainManager filterChainManager = (CustomDefaultFilterChainManager) applicationContext.getBean("filterChainManager");
		ShiroFilerChainManager shiroFilerChainManager = (ShiroFilerChainManager) applicationContext.getBean("shiroFilerChainManager");
		shiroFilerChainManager.init();
		filterChainManager.init();
		shiroFilerChainManager.initFilterChain();
		LOGGER.info("init System Evenment.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
