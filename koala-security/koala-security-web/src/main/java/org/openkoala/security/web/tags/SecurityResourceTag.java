package org.openkoala.security.web.tags;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;

import org.springframework.util.StringUtils;

public class SecurityResourceTag extends AbstractAuthorizationTag {

	private static final long serialVersionUID = 8833958170778849746L;

	protected Authz authz =  null;
	
	/**
	 * 资源名称
	 */
	protected String name;

	private boolean hasTextName = false;
	
	@Override
	protected void verifyAttributes() throws JspException {
		hasTextName = StringUtils.hasText(name);
		if(!hasTextName){
			throw new JspException("The 'name' must be set!");
		}
	}

	@Override
	public int onDoStartTag() throws JspException {
		if(hasTextName){
			initAuthz();
			if(authz.hasSecurityResource(name)){
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHasTextName() {
		return hasTextName;
	}

	public void setHasTextName(boolean hasTextName) {
		this.hasTextName = hasTextName;
	}

}
