package org.openkoala.security.web.Jcaptcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

/**
 * 验证码过滤器
 * 
 * @author luzhao
 * 
 */
public class JCaptchaValidateFilter extends AccessControlFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JCaptchaValidateFilter.class);
	
	/**
	 * 默认情况下验证码可用
	 */
	private boolean jCaptchaDisabled = false;

	/**
	 * 前台输入的验证码
	 */
	private String jCaptchaCode = "jCaptchaCode";

	/**
	 * 验证失败
	 */
	private String failureKeyAttribute = "shiroLoginFailure";

	/**
	 * 请求方式
	 */
	private static final String REQUEST_METHOD = "POST";

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		LOGGER.info("into JCaptchaValidateFilter isAccessAllowed");
		request.setAttribute("jCaptchaDisabled", jCaptchaDisabled);
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		if (jCaptchaDisabled || !REQUEST_METHOD.equalsIgnoreCase(httpServletRequest.getMethod())) {
			return true;
		}
		String jCaptchaCodeParamter = httpServletRequest.getParameter(jCaptchaCode);
		
		if(httpServletRequest.getSession(false) == null){
			return false;
		}
//		boolean validated = false;
//		String sessionId = httpServletRequest.getSession().getId();
//		
		return SimpleImageCaptchaServlet.validateResponse(httpServletRequest,
				jCaptchaCodeParamter);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		request.setAttribute(failureKeyAttribute, "jCaptchaCode.error");
		return true;
	}

	public boolean isjCaptchaDisabled() {
		return jCaptchaDisabled;
	}

	public void setjCaptchaDisabled(boolean jCaptchaDisabled) {
		this.jCaptchaDisabled = jCaptchaDisabled;
	}

	public String getjCaptchaCode() {
		return jCaptchaCode;
	}

	public void setjCaptchaCode(String jCaptchaCode) {
		this.jCaptchaCode = jCaptchaCode;
	}

	public String getFailureKeyAttribute() {
		return failureKeyAttribute;
	}

	public void setFailureKeyAttribute(String jCaptchaFailure) {
		this.failureKeyAttribute = jCaptchaFailure;
	}

}
