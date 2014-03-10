package org.openkoala.organisation.web.filter;

import org.openkoala.businesslog.utils.BusinessLogServletFilter;

import javax.servlet.*;

/**
 * User: zjzhai
 * Date: 3/7/14
 * Time: 2:58 PM
 */
public class LogFilter extends BusinessLogServletFilter {
    @Override
    public void beforeFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {
        addIpContext(getIp(req));
        String user = req.getParameter("user") == null ? "未知" : req.getParameter("user");
        addUserContext(user);
    }


}
