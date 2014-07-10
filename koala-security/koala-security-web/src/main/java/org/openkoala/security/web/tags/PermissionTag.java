package org.openkoala.security.web.tags;

import java.util.Collection;
import java.util.Set;

import javax.servlet.jsp.JspException;

import org.springframework.util.StringUtils;

public class PermissionTag extends AbstractAuthorizationTag {

	private static final long serialVersionUID = 7275289804294997087L;

	protected Authz authz = null;
	
	protected String ifAllPermissions = null;

	protected String ifAnyPermissions = null;

	protected String ifNotPermissions = null;

	private boolean hasTextAllPermission = false;

	private boolean hasTextAnyPermission = false;

	private boolean hasTextNotPermission = false;

	@Override
	protected void verifyAttributes() throws JspException {
		hasTextAllPermission = StringUtils.hasText(getIfAllPermissions());
		hasTextAnyPermission = StringUtils.hasText(getIfAnyPermissions());
		hasTextNotPermission = StringUtils.hasText(getIfNotPermissions());

		if ((!hasTextAllPermission) && (!hasTextAnyPermission) && (!hasTextNotPermission)) {
			String msg = "The 'ifAllPermissions' or 'ifAnyPermissions' or 'ifNotPermissions' must be set the another!";
			throw new JspException(msg);
		}
	}

	@Override
	public int onDoStartTag() throws JspException {
		if (authz == null) {
			authz = new AuthzImpl();
		}
		
		if (hasTextAllPermission) {
			final Collection<String> requiredPermissions = splitAuthorities(getIfAllPermissions());
			if(authz.ifAllPermission(requiredPermissions)){
				return EVAL_BODY_INCLUDE;
			}
		}	

		if (hasTextAnyPermission) {
			final Set<String> expectOneOfPermissions = splitAuthorities(getIfAnyPermissions());
			if(authz.ifAnyPermission(expectOneOfPermissions)){
				return EVAL_BODY_INCLUDE;
			}
		}

		if (hasTextNotPermission) {
			final Set<String> expectNoneOfPermissions = splitAuthorities(getIfNotPermissions());
			if(authz.ifNotPermission(expectNoneOfPermissions)){
				return EVAL_BODY_INCLUDE;
			}
		}
		
		return SKIP_BODY;
	}

	public String getIfAllPermissions() {
		return ifAllPermissions;
	}

	public void setIfAllPermissions(String ifAllPermission) {
		this.ifAllPermissions = ifAllPermission;
	}

	public String getIfAnyPermissions() {
		return ifAnyPermissions;
	}

	public void setIfAnyPermissions(String ifAnyPermission) {
		this.ifAnyPermissions = ifAnyPermission;
	}

	public String getIfNotPermissions() {
		return ifNotPermissions;
	}

	public void setIfNotPermissions(String ifNotPermission) {
		this.ifNotPermissions = ifNotPermission;
	}

	public Authz getAuthz() {
		return authz;
	}

	public void setAuthz(Authz authz) {
		this.authz = authz;
	}

}
