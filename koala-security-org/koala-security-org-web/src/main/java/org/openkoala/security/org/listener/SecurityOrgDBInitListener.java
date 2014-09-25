package org.openkoala.security.org.listener;

import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.listener.SecurityDBInitListener;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 权限组织初始化必要数据。
 * Created by luzhao on 14-9-25.
 */
public class SecurityOrgDBInitListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityDBInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        SecurityOrgConfigFacade SecurityOrgConfigFacade = applicationContext.getBean(SecurityOrgConfigFacade.class);
        SecurityOrgConfigFacade.initSecurityOrgSystem();
        LOGGER.info("init Security db.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
