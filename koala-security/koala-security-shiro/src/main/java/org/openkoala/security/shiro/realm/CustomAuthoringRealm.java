package org.openkoala.security.shiro.realm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.collect.Sets;
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
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.core.domain.EncryptService;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.openkoala.security.shiro.extend.ShiroFilterChainManager;
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

    @Inject
    private ShiroFilterChainManager shiroFilterChainManager;

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

    // TODO 角色切换适配器 切换角色之后下一次登录的角色？目前好像不控制也没有问题。
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
		String roleName = getRoleName(user);
		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUserAccount(), user.getName(), roleName);
        shiroUser.setEmail(user.getEmail());
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

	/**TODO 需要重构。 可能不存在角色。。。
	 * 查找出用户的第一个角色,因为需要角色切换
	 * @param user
	 *            用户
	 * @return
	 */
	private String getRoleName(UserDTO user) {
		String defaultRoleName = "";
        InvokeResult result =  securityAccessFacade.findRolesByUserAccount(user.getUserAccount());
        if(result.isSuccess()){
          List<RoleDTO> roles = (List<RoleDTO>) result.getData();
            if (!roles.isEmpty()) {
                defaultRoleName =  roles.get(0).getName();
            }
            if(StringUtils.isBlank(defaultRoleName)){
                defaultRoleName = "暂未分配角色";
            }
        }else{
            defaultRoleName = "暂未分配角色";
        }
		return defaultRoleName;
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
        return Sets.newHashSet(roleName);
	}

	public Set<String> getPermissions(String username, String roleName) {
		Set<String> results = new HashSet<String>();
		Set<PermissionDTO> permissions = securityAccessFacade.findPermissionsByUserAccountAndRoleName(username,
				roleName);
		for (PermissionDTO permission : permissions) {
			results.add(permission.getIdentifier());
		}
		return results;
	}

    /**
     * 切换角色
     * @param roleName 角色名称
     * @return
     */
    public InvokeResult switchOverRoleOfUser(String roleName) {

        if (StringUtils.isBlank(roleName)) {
            return InvokeResult.failure("角色名为空。");
        }
        // 角色名相同 不做任何处理
        if (CurrentUser.getRoleName().equals(roleName)) {
            return InvokeResult.success();
        }
        if (!securityAccessFacade.checkRoleByName(roleName)) {
            return InvokeResult.failure("角色名不存在。");
        }
        PrincipalCollection principalCollection = CurrentUser.getPrincipals();
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        shiroUser.setRoleName(roleName);
        this.doGetAuthorizationInfo(principalCollection);
        return InvokeResult.success();
    }

    // TODO
    public void resetRoleNameOfCurrentUser(String name) {
        switchOverRoleOfUser(name);
        shiroFilterChainManager.initFilterChain();
    }


    public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = 5876323074602221444L;

		private Long id;

		private String userAccount;

		private String name;

		private String roleName;

        private String email;

        private String telePhone;

		ShiroUser() {}

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

        public String getUserAccount() {
            return userAccount;
        }

        public String getName() {
            return name;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelePhone() {
            return telePhone;
        }

        public void setTelePhone(String telePhone) {
            this.telePhone = telePhone;
        }


        @Override
		public String toString() {
			return "ShiroUser [id=" + id + ", userAccount=" + userAccount + ", name=" + name + ", roleName=" + roleName + "]";
		}

	}
}
