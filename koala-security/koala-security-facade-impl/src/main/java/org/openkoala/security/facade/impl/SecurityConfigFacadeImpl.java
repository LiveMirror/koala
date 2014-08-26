package org.openkoala.security.facade.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.application.SecurityDBInitApplication;
import org.openkoala.security.core.CorrelationException;
import org.openkoala.security.core.IdentifierIsExistedException;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.UrlIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeMenuResourcePropsCommand;
import org.openkoala.security.facade.command.ChangePageElementResourcePropsCommand;
import org.openkoala.security.facade.command.ChangePermissionPropsCommand;
import org.openkoala.security.facade.command.ChangeRolePropsCommand;
import org.openkoala.security.facade.command.ChangeUrlAccessResourcePropsCommand;
import org.openkoala.security.facade.command.ChangeUserAccountCommand;
import org.openkoala.security.facade.command.ChangeUserEmailCommand;
import org.openkoala.security.facade.command.ChangeUserPasswordCommand;
import org.openkoala.security.facade.command.ChangeUserPropsCommand;
import org.openkoala.security.facade.command.ChangeUserTelePhoneCommand;
import org.openkoala.security.facade.command.CreateChildMenuResourceCommand;
import org.openkoala.security.facade.command.CreateMenuResourceCommand;
import org.openkoala.security.facade.command.CreatePageElementResourceCommand;
import org.openkoala.security.facade.command.CreatePermissionCommand;
import org.openkoala.security.facade.command.CreateRoleCommand;
import org.openkoala.security.facade.command.CreateUrlAccessResourceCommand;
import org.openkoala.security.facade.command.CreateUserCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.impl.assembler.MenuResourceAssembler;
import org.openkoala.security.facade.impl.assembler.PageElementResourceAssembler;
import org.openkoala.security.facade.impl.assembler.PermissionAssembler;
import org.openkoala.security.facade.impl.assembler.RoleAssembler;
import org.openkoala.security.facade.impl.assembler.UrlAccessResourceAssembler;
import org.openkoala.security.facade.impl.assembler.UserAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Named
@Transactional
public class SecurityConfigFacadeImpl implements SecurityConfigFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfigFacadeImpl.class);

	@Inject
	private SecurityConfigApplication securityConfigApplication;

	@Inject
	private SecurityAccessApplication securityAccessApplication;

	@Inject
	private SecurityDBInitApplication securityDBInitApplication;
	
	@Override
	public InvokeResult createUser(CreateUserCommand command) {
		try {
			User user = UserAssembler.toUser(command);
			securityConfigApplication.createActor(user);
			return InvokeResult.success("添加用户成功。");
		} catch (UserAccountIsExistedException e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("用户账号:" + command.getUserAccount() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return InvokeResult.failure("添加用户失败。");
		}
	}

	@Override
	public InvokeResult terminateUsers(Long[] userIds) {
		InvokeResult result = null;
		for (Long userId : userIds) {
			result = terminateUser(userId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return InvokeResult.success(result);
	}

	@Override
	public InvokeResult terminateUser(Long userId) {
		User user = null;
		try {
			user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.terminateActor(user);
			return InvokeResult.success("撤销用户：" + user.getUserAccount() + "成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("撤销用户：" + user.getUserAccount() + "失败。");
		}
	}

	@Override
	public InvokeResult resetPassword(Long userId) {
		try {
			User user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.resetPassword(user);
			return InvokeResult.success("重置用户密码成功，密码：888888");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("重置用户密码失败。");
		}
	}

	@Override
	public InvokeResult changeUserPassword(ChangeUserPasswordCommand command) {
		User user = securityAccessApplication.getUserByUserAccount(command.getUserAccount());
		boolean message = securityAccessApplication.updatePassword(user, command.getUserPassword(),
				command.getOldUserPassword());
		if (message) {
			return InvokeResult.success("更新用户：" + user.getName() + "密码成功。");
		} else {
			return InvokeResult.failure("更新用户：" + user.getName() + "密码失败。");
		}
	}

	@Override
	public InvokeResult terminateRoles(Long[] roleIds) {
		InvokeResult result = null;
		for (Long roleId : roleIds) {
			result = terminateRole(roleId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public InvokeResult terminateRole(Long roleId) {
		Role role = null;
		try {
			role = securityAccessApplication.getRoleBy(roleId);
			securityConfigApplication.terminateAuthority(role);
			return InvokeResult.success("撤销角色成功");
		} catch (CorrelationException e) {
			return InvokeResult.failure("撤销角色：" + role.getName() + "失败。");
		} catch (Exception e) {
			return InvokeResult.failure("撤销角色失败");
		}
	}

	@Override
	public InvokeResult terminateMenuResources(Long[] menuResourceIds) {
		InvokeResult result = null;
		for (Long menuResourceId : menuResourceIds) {
			result = terminateMenuResource(menuResourceId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public InvokeResult terminateMenuResource(Long menuResourceId) {
		try {
			MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
			securityConfigApplication.terminateSecurityResource(menuResource);
			return InvokeResult.success("撤销菜单全线资源成功。");
		} catch (CorrelationException e) {
			return InvokeResult.failure("不能撤销，因为有角色或者权限关联。");
		} catch (Exception e) {
			return InvokeResult.failure("撤销菜单全线资源失败。");
		}
	}

	@Override
	public void grantRoleToUserInScope(Long userId, Long roleId, Long scopeId) {
		User user = securityAccessApplication.getUserById(userId);
		Role role = securityAccessApplication.getRoleBy(roleId);
		Scope scope = securityAccessApplication.getScope(scopeId);
		securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
	}

	@Override
	public void grantRolesToUserInScope(Long userId, Long[] roleIds, Long scopeId) {
		for (Long roleId : roleIds) {
			grantRoleToUserInScope(userId, roleId, scopeId);
		}
	}

	@Override
	public void grantPermissionToUserInScope(Long userId, Long permissionId, Long scopeId) {
		User user = securityAccessApplication.getUserById(userId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		Scope scope = securityAccessApplication.getScope(scopeId);
		securityConfigApplication.grantActorToAuthorityInScope(user, permission, scope);
	}

	@Override
	public void grantPermissionsToUserInScope(Long userId, Long[] permissionIds, Long scopeId) {
		for (Long permissionId : permissionIds) {
			grantPermissionToUserInScope(userId, permissionId, scopeId);
		}
	}

	@Override
	public InvokeResult grantRoleToUser(Long userId, Long roleId) {
		try {
			User user = securityAccessApplication.getUserById(userId);
			Role role = securityAccessApplication.getRoleBy(roleId);
			securityConfigApplication.grantAuthorityToActor(role, user);
			return InvokeResult.success("为用户授权一个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为用户授权一个角色失败。");
		}
	}

	@Override
	public InvokeResult grantRolesToUser(Long userId, Long[] roleIds) {
		try {
			for (Long roleId : roleIds) {
				this.grantRoleToUser(userId, roleId);
			}
			return InvokeResult.success("为用户授权多个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return  InvokeResult.failure("为用户授权多个角色失败。");
		}
	}

	@Override
	public InvokeResult grantPermissionToUser(Long userId, Long permissionId) {
		try {
			User user = securityAccessApplication.getUserById(userId);
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToActor(permission, user);			
			return InvokeResult.success("为用户授权一个权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为用户授权一个权限失败。");
		}
	}

	@Override
	public InvokeResult grantPermissionsToUser(Long userId, Long[] permissionIds) {
		try {
			for (Long permissionId : permissionIds) {
				grantPermissionToUser(userId, permissionId);
			}
			return InvokeResult.success("为用户授权多个权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为用户授权多个权限失败。");
		}
	}

	@Override
	public InvokeResult activate(Long userId) {
		User user = null;
		try {
			user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.activateUser(user);
			return InvokeResult.success("激活用户：" + user.getUserAccount() + "成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("激活用户：" + user.getUserAccount() + "失败。");
		}
	}

	@Override
	public InvokeResult suspend(Long userId) {
		User user = null;
		try {
			user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.suspendUser(user);
			return InvokeResult.success("挂起用户：" + user.getUserAccount() + "成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("挂起用户：" + user.getUserAccount() + "失败。");
		}

	}

	@Override
	public InvokeResult activate(Long[] userIds) {
		InvokeResult result = null;
		for (Long userId : userIds) {
			result = this.activate(userId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public InvokeResult suspend(Long[] userIds) {
		InvokeResult result = null;
		for (Long userId : userIds) {
			result = this.suspend(userId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public InvokeResult terminateAuthorizationByUserInRole(Long userId, Long roleId) {
		try {
			Role role = securityAccessApplication.getRoleBy(roleId);
			User user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.terminateActorFromAuthority(user, role);
			
			return InvokeResult.success("撤销用户的一个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("撤销用户的一个角色失败。");
		}
	}

	@Override
	public InvokeResult terminateAuthorizationByUserInPermission(Long userId, Long permissionId) {
        try {
            User user = securityAccessApplication.getUserById(userId);
		    Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		    securityConfigApplication.terminateActorFromAuthority(user, permission);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return InvokeResult.failure("撤销用户的多个权限失败。");
        }
        return InvokeResult.success();
	}

	// TODO 待优化。。。
	@Override
	public InvokeResult terminateAuthorizationByUserInRoles(Long userId, Long[] roleIds) {
		try {
			User user = securityAccessApplication.getUserById(userId);
			for (Long roleId : roleIds) {
				Role role = securityAccessApplication.getRoleBy(roleId);
				securityConfigApplication.terminateActorFromAuthority(user, role);
			}
			return InvokeResult.success("撤销用户的多个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("撤销用户的多个角色失败。");
		}
	}

	@Override
	public InvokeResult terminateAuthorizationByUserInPermissions(Long userId, Long[] permissionIds) {
        try {
        	 for (Long permissionId : permissionIds) {
     			InvokeResult invokeResult = this.terminateAuthorizationByUserInPermission(userId, permissionId);
                 if(!invokeResult.isSuccess()){
                     break;
                 }
     		}
        	return InvokeResult.success("撤销用户的多个权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("撤销用户的多个权限失败。");
		}

}

	/**
	 * 为角色授权菜单资源。
	 */
	@Override
	public InvokeResult grantMenuResourcesToRole(Long roleId, Long[] menuResourceIds) {
		try {
		Role role = securityAccessApplication.getRoleBy(roleId);

		// 现在的
		
		List<MenuResource> targetOwnerMenuResources = securityAccessApplication.findAllMenuResourcesByIds(menuResourceIds);
		
//		List<MenuResource> targetOwnerMenuResources = transFromMenuResourcesBy(menuResourceIds);
		

		// 原有的 TODO 可以门面层的查询选中项的方法变成一个。
		List<MenuResource> originalOwnerMenuResources = securityAccessApplication.findAllMenuResourcesByRole(role);

		List<MenuResource> tmpList = Lists.newArrayList(targetOwnerMenuResources);

		// 待添加的
		List<MenuResource> waitingAddList = new ArrayList<MenuResource>();

		// 带删除的
		List<MenuResource> waitingDelList = new ArrayList<MenuResource>();

		// 得到相同的菜单
		targetOwnerMenuResources.retainAll(originalOwnerMenuResources);

		// 原有菜单删除相同菜单
		originalOwnerMenuResources.removeAll(targetOwnerMenuResources);

		// 得到待删除的菜单
		waitingDelList.addAll(originalOwnerMenuResources);

		// 现有菜单删除相同菜单
		tmpList.removeAll(targetOwnerMenuResources);

		// 得到带添加的菜单
		waitingAddList.addAll(tmpList);

		securityConfigApplication.terminateSecurityResourcesFromAuthority(waitingDelList, role);
		securityConfigApplication.grantSecurityResourcesToAuthority(waitingAddList, role);

		LOGGER.info("----> waiting delete menuResource list :{}", waitingDelList);
		LOGGER.info("----> waiting add menuResource list :{}", waitingAddList);
		return InvokeResult.success("为角色授权菜单资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		return	InvokeResult.failure("为角色授权菜单资源失败");
		}
	}

	@Override
	public InvokeResult grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		try {
			for (Long pageElementResourceId : pageElementResourceIds) {
				PageElementResource pageElementResource = securityAccessApplication
						.getPageElementResourceBy(pageElementResourceId);
				securityConfigApplication.grantSecurityResourceToAuthority(pageElementResource, role);
			}
			return InvokeResult.success("为角色授权页面元素权限资源成功");
		} catch (Exception e) {
			return InvokeResult.failure("为角色授权页面元素权限资源失败");
		}
	}

	@Override
	public InvokeResult grantUrlAccessResourcesToRole(Long roleId, Long[] urlAccessResourceIds) {
		try {
			Role role = securityAccessApplication.getRoleBy(roleId);
			for (Long urlAccessResourceId : urlAccessResourceIds) {
				UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
				securityConfigApplication.grantSecurityResourceToAuthority(urlAccessResource, role);
			}
			return InvokeResult.success("为角色授权URL访问权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为角色授权URL访问权限资源失败");
		}
	}

	@Override
	public void grantMethodInvocationResourcesToRole(Long roleId, Long[] menuResourceIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public InvokeResult grantPermissionsToRole(Long roleId, Long[] permissionIds) {
		try {
			Role role = securityAccessApplication.getRoleBy(roleId);
			for (Long permissionId : permissionIds) {
				Permission permission = securityAccessApplication.getPermissionBy(permissionId);
				securityConfigApplication.grantRoleToPermission(role, permission);
				securityConfigApplication.grantPermissionToRole(permission, role);
			}
		return	InvokeResult.success("为角色授权权限成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return	InvokeResult.failure("为角色授权权限失败");
		}
	}

	@Override
	public InvokeResult terminatePermissionsFromRole(Long roleId, Long[] permssionIds) {
		try {
			Role role = securityAccessApplication.getRoleBy(roleId);
			for (Long permissionId : permssionIds) {
				Permission permission = securityAccessApplication.getPermissionBy(permissionId);
				securityConfigApplication.terminatePermissionFromRole(permission, role);						
			}
			return InvokeResult.success("从角色中撤销权限成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("从角色中撤销权限失败");
		}
	}

	@Override
	public InvokeResult terminateUrlAccessResources(Long[] urlAccessResourceIds) {
		InvokeResult result = null;
		for (Long urlAccessResourceId : urlAccessResourceIds) {
			result = this.terminateUrlAccessResource(urlAccessResourceId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return InvokeResult.success("批量撤销URL访问权限资源成功");
	}

	public InvokeResult terminateUrlAccessResource(Long urlAccessResourceId) {
		try {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			securityConfigApplication.terminateSecurityResource(urlAccessResource);
			return InvokeResult.success("撤销URL访问权限资源成功");
		} catch (CorrelationException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("撤销URL访问权限资源失败，有角色或者权限关联。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("撤销URL访问权限资源失败");
		}
	}

	@Override
	public InvokeResult terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds) {
		try {
			Role role = securityAccessApplication.getRoleBy(roleId);
			for (Long urlAccessResourceId : urlAccessResourceIds) {
				UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
				securityConfigApplication.terminateSecurityResourceFromAuthority(urlAccessResource, role);
			}
			return InvokeResult.success("从角色中撤销URL访问权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		return InvokeResult.failure("从角色中撤销URL访问权限资源失败");
		}
	}

	@Override
	public void grantUrlAccessResourceToPermission(Long urlAccessResourceId, Long permissionId) {
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		securityConfigApplication.grantSecurityResourceToAuthority(urlAccessResource, permission);
	}

	@Override
	public void terminateUrlAccessResourceFromPermission(Long urlAccessResourceId, Long permissionId) {
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		securityConfigApplication.terminateSecurityResourceFromAuthority(urlAccessResource, permission);
	}

	@Override
	public InvokeResult grantPermisssionsToUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		try {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToSecurityResource(permission, urlAccessResource);
			return 	InvokeResult.success("为URL访问权限资源授权权限失败。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		return 	InvokeResult.failure("为URL访问权限资源授权权限失败。");
		}
	}

	@Override
	public InvokeResult terminatePermissionsFromUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		try {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthorityFromSecurityResource(permission, urlAccessResource);
			return InvokeResult.success("为URL访问权限资源授权权限失败。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为URL访问权限资源授权权限失败。");
		}
	}

	@Override
	public InvokeResult grantPermisssionsToMenuResource(Long permissionId, Long menuResourceId) {
		try {
			MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
			Permission permssion = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToSecurityResource(permssion, menuResource);
			return InvokeResult.success("为菜单权限资源授予权限Permission成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为菜单权限资源授予权限Permission失败");
		}
		
	}

	@Override
	public InvokeResult terminatePermissionsFromMenuResource(Long permissionId, Long menuResourceId) {
		try {
			MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
			Permission permssion = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthorityFromSecurityResource(permssion, menuResource);
			return 	InvokeResult.success("从菜单权限资源中撤销权限Permission成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return	InvokeResult.failure("从菜单权限资源中撤销权限Permission失败");
		}
		
	}

	@Override
	public InvokeResult terminatePageElementResources(Long[] pageElementResourceIds) {
		InvokeResult result = null;
		for (Long pageElementResourceId : pageElementResourceIds) {
			result = this.terminatePageElementResource(pageElementResourceId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	public InvokeResult terminatePageElementResource(Long pageElementResourceId) {
		try {
			PageElementResource pageElementResource = securityAccessApplication
					.getPageElementResourceBy(pageElementResourceId);
			securityConfigApplication.terminateSecurityResource(pageElementResource);
			return InvokeResult.success("撤销页面元素权限资源成功。");
		} catch (CorrelationException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("因为有角色或者权限，不能撤销。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("撤销页面元素权限资源失败。");
		}
	}

	@Override
	public InvokeResult terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		
		try {
			for (Long pageElementResourceId : pageElementResourceIds) {
				PageElementResource pageElementResource = securityAccessApplication
						.getPageElementResourceBy(pageElementResourceId);
				securityConfigApplication.terminateSecurityResourceFromAuthority(pageElementResource, role);
			}
			return InvokeResult.success("从角色中撤销页面元素权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("从角色中撤销页面元素权限资源失败");
		}
	}

	@Override
	public InvokeResult grantPermisssionsToPageElementResource(Long permissionId, Long pageElementResourceId) {
		try {
			PageElementResource pageElementResource = securityAccessApplication
					.getPageElementResourceBy(pageElementResourceId);
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToSecurityResource(permission, pageElementResource);
			return InvokeResult.success("为页面元素资源授予权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("为页面元素资源授予权限失败。");
		}
	}

	@Override
	public InvokeResult terminatePermissionsFromPageElementResource(Long permissionId, Long pageElementResourceId) {
		try {
			PageElementResource pageElementResource = securityAccessApplication
					.getPageElementResourceBy(pageElementResourceId);
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthorityFromSecurityResource(permission, pageElementResource);
			return InvokeResult.success("从页面元素资源中撤销权限。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("从页面元素资源中撤销权限。");
		}
	}

	@Override
	public boolean checkUserHasPageElementResource(String userAccount, String currentRoleName,
			String securityResourceIdentifier) {

		if (!securityAccessApplication.hasPageElementResource(securityResourceIdentifier)) {
			return true;
		}

		Role role = securityAccessApplication.getRoleBy(currentRoleName);
		Set<Permission> rolePermissions = role.getPermissions();
		List<Permission> userPermissions = User.findAllPermissionsBy(userAccount);

		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(role);
		authorities.addAll(userPermissions);
		authorities.addAll(rolePermissions);

		return securityConfigApplication.checkAuthoritiHasPageElementResource(authorities, securityResourceIdentifier);
	}

	@Override
	public void initSecuritySystem() {
		securityDBInitApplication.initSecuritySystem();
	}

	@Override
	public void updateUserLastLoginTime(Long userId) {
		User user = securityAccessApplication.getUserById(userId);
		securityConfigApplication.updateUserLastLoginTime(user);
	}

    @Override
	public InvokeResult changeUserProps(ChangeUserPropsCommand command) {
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			user.setName(command.getName());
			user.setDescription(command.getDescription());
			securityConfigApplication.createActor(user);// 显示调用。
			return InvokeResult.success("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("修改用户失败。");
		}
	}

	@Override
	public InvokeResult changeUserAccount(ChangeUserAccountCommand command) {
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			securityConfigApplication.changeUserAccount(user, command.getUserAccount(), command.getUserPassword());// 显示调用。
			return InvokeResult.success("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return	InvokeResult.failure("修改用户失败。");
		}
	}

	@Override
	public InvokeResult changeUserEmail(ChangeUserEmailCommand command) {
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			securityConfigApplication.changeUserEmail(user, command.getEmail(), command.getUserPassword());// 显示调用。
			return InvokeResult.success("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return	InvokeResult.failure("修改用户失败。");
		}
	}

	@Override
	public InvokeResult changeUserTelePhone(ChangeUserTelePhoneCommand command) {
	try {
			User user = securityAccessApplication.getUserById(command.getId());
			securityConfigApplication.changeUserTelePhone(user, command.getTelePhone(), command.getUserPassword());// 显示调用。
			return InvokeResult.success("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return	InvokeResult.failure("修改用户失败。");
		}
	}

	@Override
	public InvokeResult createUrlAccessResource(CreateUrlAccessResourceCommand command) {
		UrlAccessResource urlAccessResource = null;
		try {
			urlAccessResource = UrlAccessResourceAssembler.toUrlAccessResource(command);
			securityConfigApplication.createSecurityResource(urlAccessResource);
			return InvokeResult.success("添加URL访问权限资源成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("URL访问权限资源名称：" + urlAccessResource.getName() + "已经存在。");
		} catch (UrlIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("URL访问权限资源名称：" + urlAccessResource.getUrl() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加URL访问权限资源失败");
		}
	}

	@Override
	public InvokeResult changeUrlAccessResourceProps(ChangeUrlAccessResourcePropsCommand command) {
		UrlAccessResource urlAccessResource = null;
		try {
			urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(command.getId());
			securityConfigApplication.changeNameOfUrlAccessResource(urlAccessResource, command.getName());
			securityConfigApplication.changeUrlOfUrlAccessResource(urlAccessResource, command.getUrl());
			urlAccessResource.setDescription(command.getDescription());
			securityConfigApplication.createSecurityResource(urlAccessResource);
			return InvokeResult.success("更新URL访问权限资源成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("名称或者URL为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更新URL访问权限资源名称：" + command.getName() + "已经存在。");
		} catch (UrlIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更新URL访问权限资源名称：" + command.getUrl() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更新URL访问权限资源失败。");
		}
	}

	@Override
	public InvokeResult createRole(CreateRoleCommand command) {
		JsonResult result = new JsonResult();
		try {
			Role role = RoleAssembler.toRole(command);
			securityConfigApplication.createAuthority(role);
			return	InvokeResult.success("添加角色成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("添加角色名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("添加角色名称：" + command.getName() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("添加角色失败。");
		}
	}

	@Override
	public InvokeResult changeRoleProps(ChangeRolePropsCommand command) {
		try {
			Role role = securityAccessApplication.getRoleBy(command.getId());
			securityConfigApplication.changeNameOfRole(role, command.getName());
			role.setDescription(command.getDescription());
			securityConfigApplication.createAuthority(role);
			return	InvokeResult.success("更改角色成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("更改角色名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("更改角色名称：" + command.getName() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return InvokeResult.failure("更改角色失败。");
		}
	}

	@Override
	public InvokeResult createPermission(CreatePermissionCommand command) {
			try {
			Permission permission = PermissionAssembler.toPermission(command);
			securityConfigApplication.createAuthority(permission);
			return InvokeResult.success("添加权限成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("权限名称或者标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加权限失败，权限名称：" + command.getName() + " 已经存在。");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加权限失败，权限标识：" + command.getIdentifier() + " 已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更改权限失败。");
		}
	
	}

	@Override
	public InvokeResult changePermissionProps(ChangePermissionPropsCommand command) {
		try {
			Permission permission = securityAccessApplication.getPermissionBy(command.getId());
			securityConfigApplication.changeNameOfPermission(permission, command.getName());
			securityConfigApplication.changeIdentifierOfPermission(permission, command.getIdentifier());
			permission.setDescription(command.getDescription());
			return InvokeResult.success("添加权限成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("权限名称或者标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加权限失败，权限名称：" + command.getName() + " 已经存在。");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加权限失败，权限标识：" + command.getIdentifier() + " 已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更改权限失败。");
		}
	}

	@Override
	public InvokeResult terminatePermissions(Long[] permissionIds) {
		InvokeResult result = null;
		for (Long permissionId : permissionIds) {
			result = this.terminatePermission(permissionId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	public InvokeResult terminatePermission(Long permissionId) {
		Permission permission = null;
		try {
			permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthority(permission);
			return InvokeResult.success("撤销权限成功。");
		} catch (CorrelationException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("权限有用户或者角色关联，不能撤销。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("撤销权限失败。");
		}
	}

	@Override
	public InvokeResult createPageElementResource(CreatePageElementResourceCommand command) {
		try {
			PageElementResource pageElementResource = PageElementResourceAssembler.toPageElementResource(command);
			securityConfigApplication.createSecurityResource(pageElementResource);
			return InvokeResult.success("添加页面元素权限资源成功");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("名称和标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("页面元素权限资源名称" + command.getName() + "已经存在");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("页面元素权限资源标识" + command.getIdentifier() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加页面元素权限资源失败。");
		}
	}

	@Override
	public InvokeResult changePageElementResourceProps(ChangePageElementResourcePropsCommand command) {
		try {
			PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(command.getId());
			securityConfigApplication.changeNameOfPageElementResouce(pageElementResource, command.getName());
			securityConfigApplication.changeIdentifierOfPageElementResouce(pageElementResource, command.getIdentifier());
			pageElementResource.setDescription(command.getDescription());
			securityConfigApplication.createSecurityResource(pageElementResource);
			return InvokeResult.success("更改页面元素权限资源成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("名称和标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("页面元素权限资源名称" + command.getName() + "已经存在");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("页面元素权限资源标识" + command.getIdentifier() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更改页面元素权限资源失败。");
		}
	}

	@Override
	public InvokeResult createMenuResource(CreateMenuResourceCommand command) {
		try {
			MenuResource menuResource = MenuResourceAssembler.toMenuResource(command);
			securityConfigApplication.createSecurityResource(menuResource);
			return InvokeResult.success("添加菜单权限资源名称成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加菜单权限资源名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加菜单权限资源名称" + command.getName() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加菜单权限资源名称" + command.getName() + "已经存在");
		}
	}

	@Override
	public InvokeResult createChildMenuResouceToParent(CreateChildMenuResourceCommand command) {
		try {
			MenuResource menuResource = MenuResourceAssembler.toMenuResource(command);
			securityConfigApplication.createChildToParent(menuResource, command.getParentId());
			return InvokeResult.success("添加菜单权限资源名称成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加菜单权限资源名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加菜单权限资源名称" + command.getName() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("添加子菜单权限资源失败");
		}
	}

	@Override
	public InvokeResult changeMenuResourceProps(ChangeMenuResourcePropsCommand command) {
		try {
			MenuResource menuResource = securityAccessApplication.getMenuResourceBy(command.getId());
			securityConfigApplication.changeNameOfMenuResource(menuResource, command.getName());
			menuResource.setUrl(command.getUrl());
			menuResource.setMenuIcon(command.getMenuIcon());
			menuResource.setDescription(command.getDescription());
			return InvokeResult.success("更改菜单权限资源成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("菜单权限资源名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更改菜单权限资源名称" + command.getName() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return InvokeResult.failure("更改菜单权限资源失败。");
		}
	}
}
