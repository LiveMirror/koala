package org.openkoala.auth.application.vo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openkoala.koala.auth.core.domain.Role;

import com.dayatang.utils.DateUtils;

public class RoleVO extends IdentityVO implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 7192376219578931945L;
	private String roleDesc;

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

}
