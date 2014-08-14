package org.openkoala.security.shiro.realm;

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
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.openkoala.security.core.domain.EncryptService;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这里加入一个角色。 自定义Realm 放入用户以及角色和权限
 * 
 * @author luzhao
 * 
 */
@Named("customAuthoringRealm")
public class CustomAuthoringRealm extends AuthorizingRealm {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthoringRealm.class);

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	protected EncryptService passwordEncryptService;
	
	@Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo result = new SimpleAuthorizationInfo();
		Set<String> roles = getRoles(shiroUser.getRoleName());
		Set<String> permissions = getPermissions(shiroUser.getUserAccount(), shiroUser.getRoleName());
		result.setRoles(roles);
		result.setStringPermissions(permissions);
		LOGGER.info("---->role:{},permission:{}", roles, permissions);
		return result;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken toToken = null;
		if (token instanceof UsernamePasswordToken) {
			toToken = (UsernamePasswordToken) token;
		}
		String username = (String) toToken.getPrincipal();
		String userPassword = getUserPassword(toToken);
		UserDTO user = findUser(username);
		checkUserStatus(user);
		checkUserPassword(userPassword, user);
		// TODO 角色切换适配器
		// TODO 以后修改成前后台获取一致。
		String roleName = getRoleName(user);
		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUserAccount(), user.getName(), roleName);

		// permission 还需要role 的permission

		SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(//
				shiroUser, //
				user.getUserPassword(),//
				getName());

		return result;
	}

	/**
	 * 初始化密码加密匹配器
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(passwordEncryptService.getCredentialsStrategy());
		matcher.setHashIterations(passwordEncryptService.getHashIterations());
		setCredentialsMatcher(matcher);
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

	/*------------- Private helper methods  -----------------*/

	/**
	 * 查找出用户 // TODO 正则匹配
	 * 
	 * @param username
	 * @return
	 */
	private UserDTO findUser(String username) {
		UserDTO result = null;
		result = securityAccessFacade.getUserByUserAccount(username);
		if (result == null) {
			result = securityAccessFacade.getUserByEmail(username);
		}
		if (result == null) {
			result = securityAccessFacade.getUserByTelePhone(username);
		}
		if (result == null) {
			throw new UnknownAccountException("current principal is not existed.");
		}
		return result;
	}

	/**
	 * 检查用户密码是否正确
	 * 
	 * @param password
	 *            密码
	 * @param user
	 *            用户
	 */
	private void checkUserPassword(String password, UserDTO user) {
		if (StringUtils.isBlank(password)) {
			throw new UnknownAccountException("current password is null.");
		}
		String userPassword = passwordEncryptService.encryptPassword(password, null);
		if (!userPassword.equals(user.getUserPassword())) {
			throw new AuthenticationException("principal or password has error.");
		}
	}

	/**
	 * 检查用户状态
	 * 
	 * @param user
	 */
	private void checkUserStatus(UserDTO user) {
		if (user.getDisabled()) {
			throw new LockedAccountException("current user is suspend.");
		}
	}

	/**
	 * 查找出用户的第一个角色,因为需要角色切换
	 * 
	 * @param user
	 *            用户
	 * @return
	 */
	private String getRoleName(UserDTO user) {
		List<RoleDTO> roles = securityAccessFacade.findRolesByUserAccount(user.getUserAccount());
		if (!roles.isEmpty()) {
			return roles.get(0).getName();
		}
		return null;
	}

	/**
	 * 获取用户登陆时的密码。
	 * 
	 * @param toToken
	 * @return
	 */
	private String getUserPassword(UsernamePasswordToken toToken) {
		if (toToken.getCredentials() != null) {
			return new String(toToken.getPassword());
		}
		return null;
	}

	public Set<String> getRoles(String roleName) {
		Set<String> results = new HashSet<String>();
		results.add(roleName);
		return results;
	}

	private Set<String> getPermissions(String username, String roleName) {
		Set<String> results = new HashSet<String>();
		Set<PermissionDTO> permissions = securityAccessFacade.findPermissionsByUserAccountAndRoleName(username,
				roleName);
		for (PermissionDTO permission : permissions) {
			results.add(permission.getIdentifier());
		}
		return results;
	}

	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = 5876323074602221444L;

		private Long id;

		private String userAccount;

		private String name;

		private String roleName;

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

		@Override
		public String toString() {
			return "ShiroUser [id=" + id + ", userAccount=" + userAccount + ", name=" + name + ", roleName=" + roleName
					+ "]";
		}

	}
}
