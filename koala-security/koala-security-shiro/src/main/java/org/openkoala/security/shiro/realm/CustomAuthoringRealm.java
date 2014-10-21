package org.openkoala.security.shiro.realm;

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
import org.apache.shiro.util.ByteSource;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.core.domain.EncryptService;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.openkoala.security.shiro.RoleHandle;
import org.openkoala.security.shiro.extend.ShiroFilterChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * 这里加入一个角色。 自定义Realm 放入用户以及角色和权限
 * 
 * @author luzhao
 * 
 */
@Named("customAuthoringRealm")
public class CustomAuthoringRealm extends AuthorizingRealm implements RoleHandle {

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
//		String userPassword = getUserPassword(toToken);
		UserDTO user = findUser(username);
		checkUserStatus(user);
//		checkUserPassword(userPassword, user);
		String roleName = getRoleName(user);
		ShiroUser shiroUser = new ShiroUser(user.getUserAccount(), user.getName(), roleName);


        if(StringUtils.isBlank(user.getEmail())){
            shiroUser.setEmail("您还没有邮箱，请添加邮箱！");
        }else{
            shiroUser.setEmail(user.getEmail());
        }

        if(StringUtils.isBlank(user.getTelePhone())){
            shiroUser.setTelePhone("您还没有联系电话，请添加电话！");
        }else{
            shiroUser.setTelePhone(user.getTelePhone());
        }

        SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(shiroUser, user.getUserPassword(),getName());
        if (!passwordEncryptService.saltDisabled()){
            result.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()+shiroUser.getUserAccount()));
        }
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
	@SuppressWarnings("unchecked")
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
    public void switchOverRoleOfUser(String roleName) {
        PrincipalCollection principalCollection = CurrentUser.getPrincipals();
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        shiroUser.setRoleName(roleName);
        this.doGetAuthorizationInfo(principalCollection);
    }

	@Override
	public void resetRoleName(String name) {
		switchOverRoleOfUser(name);
        shiroFilterChainManager.initFilterChain();
	}

}
