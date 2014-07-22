package org.openkoala.security.web.realm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.openkoala.security.core.domain.PasswordService;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.web.util.AuthUserUtil;

/**
 * 这里加入一个角色。 自定义Realm 放入用户以及角色和权限
 * 
 * @author luzhao
 * 
 */
@Named("customAuthoringRealm")
public class CustomAuthoringRealm extends AuthorizingRealm {

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private PasswordService passwordService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo result = new SimpleAuthorizationInfo();
		result.setRoles(getRoles(shiroUser.getRoleName()));
		result.setStringPermissions(getPermissionIdentifiers(shiroUser.getUserAccount()));
		shiroUser.setAuthorizationInfo(result);
		return result;
	}

	private Set<String> getRoles(String roleName) {
		Set<String> roles = new HashSet<String>();
		roles.add(AuthUserUtil.getRoleName());
		return roles;
	}

	private Set<String> getPermissionIdentifiers(String username) {
		Set<String> results = new HashSet<String>();

		Set<PermissionDTO> permissionDtos = securityAccessFacade.findPermissionDTOsBy(username);
		for (PermissionDTO permissionDto : permissionDtos) {
			results.add(permissionDto.getIdentifier());
		}
		return results;
	}

	/**
	 * 初始化密码加密匹配器
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(passwordService.getCredentialsStrategy());
		matcher.setHashIterations(passwordService.getHashIterations());
		setCredentialsMatcher(matcher);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken toToken = null;

		if (token instanceof UsernamePasswordToken) {
			toToken = (UsernamePasswordToken) token;
		}

		String principal = (String) toToken.getPrincipal();
		String password = "";

		if (toToken.getCredentials() != null) {
			password = new String(toToken.getPassword());
		}
		UserDTO userDTO = null;
		
		userDTO = securityAccessFacade.login(principal, password);		
		
		// TODO 以后修改成前后台获取一致。
		List<RoleDTO> roleDTOs = securityAccessFacade.findRoleDTOsBy(userDTO.getUserAccount());
		String roleName = "";
		
		if(roleDTOs.isEmpty()){
			// no deal
		}else{
			roleName = roleDTOs.get(0).getRoleName();
		}
		
		ShiroUser shiroUser = null;
		if (!StringUtils.isBlank(roleName)) {
			shiroUser = new ShiroUser(userDTO.getId(), userDTO.getUserAccount(), userDTO.getName(), roleName);
		} else {
			shiroUser = new ShiroUser(userDTO.getId(), userDTO.getUserAccount(), userDTO.getName());
		}

		SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(//
				shiroUser, //
				userDTO.getUserPassword(),//
				getName());

		return result;
	}

	@Override
	protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	protected void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = 5876323074602221444L;

		private Long id;

		private String userAccount;

		private String name;

		private String roleName;

		private AuthenticationInfo authenticationInfo;

		private AuthorizationInfo authorizationInfo;

		ShiroUser() {
		}

		public ShiroUser(Long id, String userAccount, String name) {
			this.id = id;
			this.userAccount = userAccount;
			this.name = name;
		}

		public ShiroUser(Long id, String userAccount, String name, String roleName) {
			super();
			this.id = id;
			this.userAccount = userAccount;
			this.name = name;
			this.roleName = roleName;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUserAccount() {
			return userAccount;
		}

		public void setUserAccount(String userAccount) {
			this.userAccount = userAccount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

		public AuthenticationInfo getAuthenticationInfo() {
			return authenticationInfo;
		}

		public void setAuthenticationInfo(AuthenticationInfo authenticationInfo) {
			this.authenticationInfo = authenticationInfo;
		}

		public AuthorizationInfo getAuthorizationInfo() {
			return authorizationInfo;
		}

		public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
			this.authorizationInfo = authorizationInfo;
		}

		@Override
		public String toString() {
			return "ShiroUser [id=" + id + ", userAccount=" + userAccount + ", name=" + name + ", roleName=" + roleName
					+ ", authenticationInfo=" + authenticationInfo + ", authorizationInfo=" + authorizationInfo + "]";
		}

	}
}
