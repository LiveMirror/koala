package org.openkoala.security.web.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.openkoala.security.core.domain.PasswordService;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;

/**
 * 自定义Realm 放入用户以及角色和权限
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
		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo result = new SimpleAuthorizationInfo();
		result.setRoles(getRoleNames(username));
		result.setStringPermissions(getPermissionNames(username));
		return result;
	}

	private Set<String> getPermissionNames(String username) {
		Set<String> results = new HashSet<String>();

		Set<PermissionDTO> permissionDtos = securityAccessFacade.findPermissionDtosBy(username);
		for (PermissionDTO permissionDto : permissionDtos) {
			results.add(permissionDto.getPermissionName());
		}
		return results;
	}

	private Set<String> getRoleNames(String username) {
		Set<String> results = new HashSet<String>();

		List<RoleDTO> roleDtos = securityAccessFacade.findRoleDtosBy(username);

		for (RoleDTO roleDto : roleDtos) {
			results.add(roleDto.getRoleName());
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
		String username = (String) token.getPrincipal();
		UserDTO userDTO = securityAccessFacade.getUserDtoBy(username);
		if (userDTO == null) {
			throw new UnknownAccountException();// 没找到账户
		}
		// TODO and so on.
		SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(//
				userDTO.getUserAccount(), //
				userDTO.getUserPassword(),//
				getName());

		return result;
	}

}
