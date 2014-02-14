package org.openkoala.koala.auth.ss3adapter;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dayatang.domain.InstanceFactory;

public class AuthUserUtil {
	
	private static RoleApplication roleApplication;
	
	private static  RoleApplication getRoleApplication(){
		if(roleApplication==null){
			roleApplication = InstanceFactory.getInstance(RoleApplication.class);
		}
		return roleApplication;
	}
	/**
	 * 获取当前登录用户名
	 * 
	 * @return
	 */
	public static String getLoginUserName() {
		if (getAuthentication() == null) {
			return null;
		}
		if (getPrincipal() != null && getPrincipal() instanceof CustomUserDetails) {
			return ((CustomUserDetails) getPrincipal()).getUsername();
		}
		return null;
	}

	private static Object getPrincipal() {
		return getAuthentication().getPrincipal();
	}

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 返回当前登录用户
	 * 
	 * @return
	 */
	public static CustomUserDetails getLoginUser() {
		if (getPrincipal() != null && getPrincipal() instanceof CustomUserDetails) {
			return (CustomUserDetails) getPrincipal();
		}
		return null;
	}

	public static List<String> getRolesByCurrentUser() {
		String user = getLoginUserName();
		return getRolesByUser(user);
	}

	public static List<String> getRolesByUser(String user) {
		List<RoleVO> roleVos = getRoleApplication().findRoleByUserAccount(user);
		List<String> roles = new ArrayList<String>();
		for(RoleVO roleVo:roleVos){
			roles.add(roleVo.getName());
		}
		return roles;
	}

	public static List<String> getResByRole(String roleName) {
		List<ResourceVO> resourceVOs = getRoleApplication().findResourceByRoleName(roleName);
		List<String> resources = new ArrayList<String>();
		for(ResourceVO resourceVO:resourceVOs){
			resources.add(resourceVO.getName());
		}
		return resources;
	}

	public static List<String> getResByRoles(String[] roles) {
		List<String> resources = new ArrayList<String>();
		for(String role:roles){
			resources.addAll(getResByRole(role));
		}
		return resources;
	}
}
