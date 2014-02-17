package org.openkoala.koala.auth.ss3adapter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.InvalidSessionStrategy;

/**
 * Session过期处理策略
 * @author zhuyuanbiao
 * @date 2014年2月17日 下午7:30:30
 *
 */
public class SessionTimeoutStrategy implements InvalidSessionStrategy {
	
	private String invlidSessionUrl;

	public void setInvlidSessionUrl(String invlidSessionUrl) {
		this.invlidSessionUrl = invlidSessionUrl;
	}

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		if (!"".equals(request.getHeader("X-Requested-With")) && 
				"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setStatus(499);
		} else {
			response.sendRedirect(invlidSessionUrl);
		}
	}

}
