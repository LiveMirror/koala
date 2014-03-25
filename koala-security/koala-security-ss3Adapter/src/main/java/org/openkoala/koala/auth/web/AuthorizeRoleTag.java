package org.openkoala.koala.auth.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by lingen on 14-3-25.
 */
public class AuthorizeRoleTag extends TagSupport {

    private String ifAllRoles;

    private String ifAnyRoles;

    private String ifNotRoles;

    private PermissionController permissionController;

    @Override
    public int doStartTag() throws JspException {
        if (ifAllRoles == null && ifAnyRoles == null && ifNotRoles == null) {
            return Tag.SKIP_BODY;
        }

        if (permissionController == null) {
            permissionController = new PermissionController();
        }

        if (ifAllRoles != null) {
            return ifAllRoles();
        }

        if (ifAnyRoles != null) {
            return ifAnyRoles();
        }

        if (ifNotRoles != null) {
            return ifNotRoles();
        }

        return Tag.EVAL_BODY_INCLUDE;
    }

    private int ifAnyRoles() {
        for(String role:ifAllRoles.split(",")){
            if(permissionController.hasRole(role)){
               return Tag.EVAL_BODY_INCLUDE;
            }
        }
        return Tag.SKIP_BODY;
    }

    private int ifNotRoles() {
        for(String role:ifNotRoles.split(",")){
            if(permissionController.hasRole(role)){
                return Tag.SKIP_BODY;
            }
        }
        return Tag.EVAL_BODY_INCLUDE;
    }

    private int ifAllRoles() {
        for(String role:ifAllRoles.split(",")){
            if(permissionController.hasRole(role)==false){
                return Tag.SKIP_BODY;
            }
        }
        return Tag.EVAL_BODY_INCLUDE;
    }

    public String getIfAllRoles() {
        return ifAllRoles;
    }

    public void setIfAllRoles(String ifAllRoles) {
        this.ifAllRoles = ifAllRoles;
    }

    public String getIfAnyRoles() {
        return ifAnyRoles;
    }

    public void setIfAnyRoles(String ifAnyRoles) {
        this.ifAnyRoles = ifAnyRoles;
    }

    public String getIfNotRoles() {
        return ifNotRoles;
    }

    public void setIfNotRoles(String ifNotRoles) {
        this.ifNotRoles = ifNotRoles;
    }
}
