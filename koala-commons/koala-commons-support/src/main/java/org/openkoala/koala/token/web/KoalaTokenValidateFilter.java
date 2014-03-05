package org.openkoala.koala.token.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.openkoala.koala.token.TokenHelper;

public class KoalaTokenValidateFilter implements Filter {

	private static final List<String> DEFAULT_IGNORE_SUFFIX = Arrays.asList("css;js;jpg;ico;jpeg;bmp;gif;png;css;swf".split(";"));

	private static String VALIDATE = "validate";

	private static String EXCLUDE = "exclude";

	private static List<String> validateUrls = new ArrayList<String>();
	private static List<String> excludeUrls = new ArrayList<String>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String validate = filterConfig.getInitParameter(VALIDATE);
		String exclude = filterConfig.getInitParameter(EXCLUDE);
		if (validate != null) {
			validateUrls.addAll(Arrays.asList(validate.split(" *; *")));
		}
		if (exclude != null) {
			excludeUrls.addAll(Arrays.asList(exclude.split(" *; *")));
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!ignore(request, response, chain)) {
			HttpServletRequest _request = (HttpServletRequest) request;
			boolean flag = TokenHelper.validToken(_request);
			if (!flag) {
				throw new RuntimeException("Invalid token");
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * 判断是否忽略该条请求
	 * 
	 * @param request
	 * @param response
	 * @param filterChain
	 * @return
	 */
	public boolean ignore(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		try {
			HttpServletRequest httprequest = (HttpServletRequest) request;
			String path = httprequest.getRequestURI().trim();

			if (path.indexOf(".") > 0) {
				String suffix = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
				if (DEFAULT_IGNORE_SUFFIX.contains(suffix))
					return true;
			}
			// 如果包含网址不为空，则只处理包含在内的网址
			if (!validateUrls.isEmpty()) {
				for (String url : validateUrls) {
					if (url.indexOf("*") >= 0) {
						if (path.matches(url.replaceAll("\\*", ".*"))) {
							return false;
						}
					} else {
						if (path.indexOf(url) >= 0) {
							return false;
						}
					}
				}
				return true;
			}
			// 排除网址
			for (String excludeUrl : excludeUrls) {
				if (excludeUrl.indexOf("*") >= 0) {
					if (path.matches(excludeUrl.replaceAll("\\*", ".*"))) {
						return true;
					}
				} else {
					if (path.indexOf(excludeUrl) >= 0) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public void destroy() {

	}
}
