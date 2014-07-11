package org.openkoala.security.web.tags;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.util.StringUtils;

public abstract class AbstractAuthorizationTag extends TagSupport {

	private static final long serialVersionUID = 4069161514316265327L;

	private static final String DEFAULT_NAMES_DELIMETER = ",";

	/**
	 * 验证属性。
	 */
	protected void verifyAttributes() throws JspException {
	}

	/**
	 * 对标签开始时做处理。
	 * 
	 * @return
	 * @throws JspException
	 */
	public abstract int onDoStartTag() throws JspException;

	@Override
	public int doStartTag() throws JspException {

		verifyAttributes();

		return onDoStartTag();
	}

	protected Set<String> splitAuthorities(String authorities) {
		String[] authorityArray = StringUtils.tokenizeToStringArray(authorities, DEFAULT_NAMES_DELIMETER);
		Set<String> results = new HashSet<String>(authorityArray.length);
		for (String authority : authorityArray) {
			results.add(authority);
		}
		return results;
	}

}
