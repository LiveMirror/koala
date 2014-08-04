package org.openkoala.security.taglibs;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;

import org.springframework.util.StringUtils;

public class SecurityResourceTag extends AbstractAuthorizationTag {

	private static final long serialVersionUID = 8833958170778849746L;

	protected Authz authz =  null;
	
	/**
	 * 资源名称
	 */
	protected String identifier;

	private boolean hasTextIdentifier = false;
	
	@Override
	protected void verifyAttributes() throws JspException {
		hasTextIdentifier = StringUtils.hasText(identifier);
		if(!hasTextIdentifier){
			throw new JspException("The 'identifier' must be set!");
		}
	}

	@Override
	public int onDoStartTag() throws JspException {
		if(hasTextIdentifier){
			initAuthz();
			if(authz.hasSecurityResource(identifier)){
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}

	private void initAuthz() {
		if(authz == null){
			authz = new AuthzImpl();
			ServletContext servletContext = this.pageContext.getServletContext();
			authz.setServletContext(servletContext);
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String name) {
		this.identifier = name;
	}

	public boolean isHasTextIdentifier() {
		return hasTextIdentifier;
	}

	public void setHasTextIdentifier(boolean hasTextName) {
		this.hasTextIdentifier = hasTextName;
	}

}
