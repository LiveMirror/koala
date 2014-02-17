package org.openkoala.koala.auth.ss3adapter.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Session超时过滤器
 * 
 * @author zhuyuanbiao
 * @date 2014年2月17日 下午3:57:17
 * 
 */
public class SessionTimeoutFilter extends GenericFilterBean {

	private InvalidSessionStrategy invalidSessionStrategy;

	public void setInvalidSessionStrategy(InvalidSessionStrategy invalidSessionStrategy) {
		this.invalidSessionStrategy = invalidSessionStrategy;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
			if (invalidSessionStrategy != null) {
				invalidSessionStrategy.onInvalidSessionDetected(request, response);
				return;
			}
		}

		chain.doFilter(request, response);
	}

}
