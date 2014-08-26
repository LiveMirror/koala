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
	public JsonResult createUser(CreateUserCommand command) {
		JsonResult result = new JsonResult();
		try {
			User user = UserAssembler.toUser(command);
			securityConfigApplication.createActor(user);
			result.setSuccess(true);
			result.setMessage("添加用户成功。");
		} catch (UserAccountIsExistedException e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("用户账号:" + command.getUserAccount() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			result.setSuccess(false);
			result.setMessage("添加用户失败。");
		}
		return result;
	}

	@Override
	public JsonResult terminateUsers(Long[] userIds) {
		JsonResult result = null;
		for (Long userId : userIds) {
			result = terminateUser(userId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public JsonResult terminateUser(Long userId) {
		JsonResult result = new JsonResult();
		User user = null;
		try {
			user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.terminateActor(user);
			result.setSuccess(true);
			result.setMessage("撤销用户：" + user.getUserAccount() + "成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("撤销用户：" + user.getUserAccount() + "失败。");
		}
		return result;
	}

	@Override
	public JsonResult resetPassword(Long userId) {
		JsonResult result = new JsonResult();
		try {
			User user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.resetPassword(user);
			result.setSuccess(true);
			result.setMessage("重置用户密码成功，密码：888888");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("重置用户密码失败。");
		}
		return result;
	}

	@Override
	public JsonResult changeUserPassword(ChangeUserPasswordCommand command) {
		JsonResult result = new JsonResult();
		User user = securityAccessApplication.getUserByUserAccount(command.getUserAccount());
		boolean message = securityAccessApplication.updatePassword(user, command.getUserPassword(),
				command.getOldUserPassword());
		if (message) {
			result.setSuccess(true);
			result.setMessage("更新用户：" + user.getName() + "密码成功。");
		} else {
			result.setSuccess(false);
			result.setMessage("更新用户：" + user.getName() + "密码失败。");
		}
		return result;
	}

	@Override
	public JsonResult terminateRoles(Long[] roleIds) {
		JsonResult result = null;
		for (Long roleId : roleIds) {
			result = terminateRole(roleId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public JsonResult terminateRole(Long roleId) {
		JsonResult result = new JsonResult();
		Role role = null;
		try {
			role = securityAccessApplication.getRoleBy(roleId);
			securityConfigApplication.terminateAuthority(role);
			result.setSuccess(true);
			result.setMessage("撤销角色成功");
		} catch (CorrelationException e) {
			result.setSuccess(false);
			result.setMessage("撤销角色：" + role.getName() + "失败。");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("撤销角色失败。");
		}
		return result;
	}

	@Override
	public JsonResult terminateMenuResources(Long[] menuResourceIds) {
		JsonResult result = null;
		for (Long menuResourceId : menuResourceIds) {
			result = terminateMenuResource(menuResourceId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public JsonResult terminateMenuResource(Long menuResourceId) {
		JsonResult result = new JsonResult();
		try {
			MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
			securityConfigApplication.terminateSecurityResource(menuResource);
			result.setSuccess(true);
			result.setMessage("撤销菜单全线资源成功。");
		} catch (CorrelationException e) {
			result.setSuccess(false);
			result.setMessage("不能撤销，因为有角色或者权限关联。");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("撤销菜单全线资源失败。");
		}
		return result;

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
	public void grantRoleToUser(Long userId, Long roleId) {
		User user = securityAccessApplication.getUserById(userId);
		Role role = securityAccessApplication.getRoleBy(roleId);
		securityConfigApplication.grantAuthorityToActor(role, user);
	}

	@Override
	public void grantRolesToUser(Long userId, Long[] roleIds) {
		for (Long roleId : roleIds) {
			this.grantRoleToUser(userId, roleId);
		}
	}

	@Override
	public void grantPermissionToUser(Long userId, Long permissionId) {
		User user = securityAccessApplication.getUserById(userId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.grantAuthorityToActor(permission, user);
	}

	@Override
	public void grantPermissionsToUser(Long userId, Long[] permissionIds) {
		for (Long permissionId : permissionIds) {
			grantPermissionToUser(userId, permissionId);
		}
	}

	@Override
	public JsonResult activate(Long userId) {
		JsonResult result = new JsonResult();
		User user = null;
		try {
			user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.activateUser(user);
			result.setSuccess(true);
			result.setMessage("激活用户：" + user.getUserAccount() + "成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("激活用户：" + user.getUserAccount() + "失败。");
		}
		return result;
	}

	@Override
	public JsonResult suspend(Long userId) {
		JsonResult result = new JsonResult();
		User user = null;
		try {
			user = securityAccessApplication.getUserById(userId);
			securityConfigApplication.suspendUser(user);
			result.setSuccess(true);
			result.setMessage("挂起用户：" + user.getUserAccount() + "成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("挂起用户：" + user.getUserAccount() + "失败。");
		}
		return result;
	}

	@Override
	public JsonResult activate(Long[] userIds) {
		JsonResult result = null;
		for (Long userId : userIds) {
			result = this.activate(userId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public JsonResult suspend(Long[] userIds) {
		JsonResult result = null;
		for (Long userId : userIds) {
			result = this.suspend(userId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	@Override
	public void terminateAuthorizationByUserInRole(Long userId, Long roleId) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		User user = securityAccessApplication.getUserById(userId);
		securityConfigApplication.terminateActorFromAuthority(user, role);
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
	public void terminateAuthorizationByUserInRoles(Long userId, Long[] roleIds) {
		User user = securityAccessApplication.getUserById(userId);
		for (Long roleId : roleIds) {
			Role role = securityAccessApplication.getRoleBy(roleId);
			securityConfigApplication.terminateActorFromAuthority(user, role);
		}
	}

	@Override
	public InvokeResult terminateAuthorizationByUserInPermissions(Long userId, Long[] permissionIds) {
        for (Long permissionId : permissionIds) {
			InvokeResult invokeResult = this.terminateAuthorizationByUserInPermission(userId, permissionId);
            if(!invokeResult.isSuccess()){
                break;
            }
		}
        return InvokeResult.success();
	}

	/**
	 * 为角色授权菜单资源。
	 */
	@Override
	public void grantMenuResourcesToRole(Long roleId, Long[] menuResourceIds) {

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
	}

	@Override
	public void grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long pageElementResourceId : pageElementResourceIds) {
			PageElementResource pageElementResource = securityAccessApplication
					.getPageElementResourceBy(pageElementResourceId);
			securityConfigApplication.grantSecurityResourceToAuthority(pageElementResource, role);
		}
	}

	@Override
	public void grantUrlAccessResourcesToRole(Long roleId, Long[] urlAccessResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long urlAccessResourceId : urlAccessResourceIds) {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			securityConfigApplication.grantSecurityResourceToAuthority(urlAccessResource, role);
		}
	}

	@Override
	public void grantMethodInvocationResourcesToRole(Long roleId, Long[] menuResourceIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grantPermissionsToRole(Long roleId, Long[] permissionIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long permissionId : permissionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantRoleToPermission(role, permission);
			securityConfigApplication.grantPermissionToRole(permission, role);
		}
	}

	@Override
	public void terminatePermissionsFromRole(Long roleId, Long[] permssionIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long permissionId : permssionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminatePermissionFromRole(permission, role);
		}
	}

	@Override
	public JsonResult terminateUrlAccessResources(Long[] urlAccessResourceIds) {
		JsonResult result = null;
		for (Long urlAccessResourceId : urlAccessResourceIds) {
			result = this.terminateUrlAccessResource(urlAccessResourceId);
			if (!result.isSuccess()) {
				break;
			}
		}
		result.setMessage("批量撤销URL访问权限资源成功");
		return result;
	}

	public JsonResult terminateUrlAccessResource(Long urlAccessResourceId) {
		JsonResult result = new JsonResult();
		try {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			securityConfigApplication.terminateSecurityResource(urlAccessResource);
			result.setSuccess(true);
			result.setMessage("撤销URL访问权限资源成功");
		} catch (CorrelationException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("撤销URL访问权限资源失败，有角色或者权限关联。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("撤销URL访问权限资源失败");
		}
		return result;
	}

	@Override
	public void terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long urlAccessResourceId : urlAccessResourceIds) {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			securityConfigApplication.terminateSecurityResourceFromAuthority(urlAccessResource, role);
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
	public void grantPermisssionsToUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.grantAuthorityToSecurityResource(permission, urlAccessResource);
	}

	@Override
	public void terminatePermissionsFromUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.terminateAuthorityFromSecurityResource(permission, urlAccessResource);
	}

	@Override
	public void grantPermisssionsToMenuResource(Long permissionId, Long menuResourceId) {
		MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
		Permission permssion = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.grantAuthorityToSecurityResource(permssion, menuResource);
	}

	@Override
	public void terminatePermissionsFromMenuResource(Long permissionId, Long menuResourceId) {
		MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
		Permission permssion = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.terminateAuthorityFromSecurityResource(permssion, menuResource);
	}

	@Override
	public JsonResult terminatePageElementResources(Long[] pageElementResourceIds) {
		JsonResult result = null;
		for (Long pageElementResourceId : pageElementResourceIds) {
			result = this.terminatePageElementResource(pageElementResourceId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	public JsonResult terminatePageElementResource(Long pageElementResourceId) {
		JsonResult result = new JsonResult();
		try {
			PageElementResource pageElementResource = securityAccessApplication
					.getPageElementResourceBy(pageElementResourceId);
			securityConfigApplication.terminateSecurityResource(pageElementResource);
			result.setSuccess(true);
			result.setMessage("撤销页面元素权限资源成功。");
		} catch (CorrelationException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(true);
			result.setMessage("因为有角色或者权限，不能撤销。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(true);
			result.setMessage("撤销页面元素权限资源失败。");
		}
		return result;
	}

	@Override
	public void terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long pageElementResourceId : pageElementResourceIds) {
			PageElementResource pageElementResource = securityAccessApplication
					.getPageElementResourceBy(pageElementResourceId);
			securityConfigApplication.terminateSecurityResourceFromAuthority(pageElementResource, role);
		}
	}

	@Override
	public void grantPermisssionsToPageElementResource(Long permissionId, Long pageElementResourceId) {
		PageElementResource pageElementResource = securityAccessApplication
				.getPageElementResourceBy(pageElementResourceId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.grantAuthorityToSecurityResource(permission, pageElementResource);
	}

	@Override
	public void terminatePermissionsFromPageElementResource(Long permissionId, Long pageElementResourceId) {
		PageElementResource pageElementResource = securityAccessApplication
				.getPageElementResourceBy(pageElementResourceId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.terminateAuthorityFromSecurityResource(permission, pageElementResource);
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
	public JsonResult changeUserProps(ChangeUserPropsCommand command) {
		JsonResult result = new JsonResult();
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			user.setName(command.getName());
			user.setDescription(command.getDescription());
			securityConfigApplication.createActor(user);// 显示调用。
			result.setSuccess(true);
			result.setMessage("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("修改用户失败。");
		}
		return result;
	}

	@Override
	public JsonResult changeUserAccount(ChangeUserAccountCommand command) {
		JsonResult result = new JsonResult();
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			securityConfigApplication.changeUserAccount(user, command.getUserAccount(), command.getUserPassword());// 显示调用。
			result.setSuccess(true);
			result.setMessage("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("修改用户失败。");
		}
		return result;
	}

	@Override
	public JsonResult changeUserEmail(ChangeUserEmailCommand command) {
		JsonResult result = new JsonResult();
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			securityConfigApplication.changeUserEmail(user, command.getEmail(), command.getUserPassword());// 显示调用。
			result.setSuccess(true);
			result.setMessage("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("修改用户失败。");
		}
		return result;
	}

	@Override
	public JsonResult changeUserTelePhone(ChangeUserTelePhoneCommand command) {
		JsonResult result = new JsonResult();
		try {
			User user = securityAccessApplication.getUserById(command.getId());
			securityConfigApplication.changeUserTelePhone(user, command.getTelePhone(), command.getUserPassword());// 显示调用。
			result.setSuccess(true);
			result.setMessage("修改用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("修改用户失败。");
		}
		return result;
	}

	@Override
	public JsonResult createUrlAccessResource(CreateUrlAccessResourceCommand command) {
		JsonResult result = new JsonResult();
		UrlAccessResource urlAccessResource = null;
		try {
			urlAccessResource = UrlAccessResourceAssembler.toUrlAccessResource(command);
			securityConfigApplication.createSecurityResource(urlAccessResource);
			result.setSuccess(true);
			result.setMessage("添加URL访问权限资源成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("URL访问权限资源名称：" + urlAccessResource.getName() + "已经存在。");
		} catch (UrlIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("URL访问权限资源名称：" + urlAccessResource.getUrl() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加URL访问权限资源失败");
		}
		return result;
	}

	@Override
	public JsonResult changeUrlAccessResourceProps(ChangeUrlAccessResourcePropsCommand command) {
		JsonResult result = new JsonResult();
		UrlAccessResource urlAccessResource = null;
		try {
			urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(command.getId());
			securityConfigApplication.changeNameOfUrlAccessResource(urlAccessResource, command.getName());
			securityConfigApplication.changeUrlOfUrlAccessResource(urlAccessResource, command.getUrl());
			urlAccessResource.setDescription(command.getDescription());
			securityConfigApplication.createSecurityResource(urlAccessResource);
			result.setSuccess(true);
			result.setMessage("更新URL访问权限资源成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("名称或者URL为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更新URL访问权限资源名称：" + command.getName() + "已经存在。");
		} catch (UrlIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更新URL访问权限资源名称：" + command.getUrl() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更新URL访问权限资源失败。");
		}
		return result;
	}

	@Override
	public JsonResult createRole(CreateRoleCommand command) {
		JsonResult result = new JsonResult();
		try {
			Role role = RoleAssembler.toRole(command);
			securityConfigApplication.createAuthority(role);
			result.setSuccess(true);
			result.setMessage("添加角色成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("添加角色名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("添加角色名称：" + command.getName() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("添加角色失败。");
		}
		return result;
	}

	@Override
	public JsonResult changeRoleProps(ChangeRolePropsCommand command) {
		JsonResult result = new JsonResult();
		try {
			Role role = securityAccessApplication.getRoleBy(command.getId());
			securityConfigApplication.changeNameOfRole(role, command.getName());
			role.setDescription(command.getDescription());
			securityConfigApplication.createAuthority(role);
			result.setSuccess(true);
			result.setMessage("更新角色成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("添加角色名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("更新角色名称：" + command.getName() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setSuccess(false);
			result.setMessage("更新角色失败。");
		}
		return result;
	}

	@Override
	public JsonResult createPermission(CreatePermissionCommand command) {
		JsonResult result = new JsonResult();
		try {
			Permission permission = PermissionAssembler.toPermission(command);
			securityConfigApplication.createAuthority(permission);
			result.setSuccess(true);
			result.setMessage("添加权限成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("权限名称或者标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加权限失败，权限名称：" + command.getName() + " 已经存在。");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加权限失败，权限标识：" + command.getIdentifier() + " 已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改权限失败。");
		}
		return result;
	}

	@Override
	public JsonResult changePermissionProps(ChangePermissionPropsCommand command) {
		JsonResult result = new JsonResult();
		try {
			Permission permission = securityAccessApplication.getPermissionBy(command.getId());
			securityConfigApplication.changeNameOfPermission(permission, command.getName());
			securityConfigApplication.changeIdentifierOfPermission(permission, command.getIdentifier());
			permission.setDescription(command.getDescription());
			result.setSuccess(true);
			result.setMessage("添加权限成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("权限名称或者标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改权限失败，权限名称：" + command.getName() + " 已经存在。");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改权限失败，权限标识：" + command.getIdentifier() + " 已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改权限失败。");
		}
		return result;
	}

	@Override
	public JsonResult terminatePermissions(Long[] permissionIds) {
		JsonResult result = null;
		for (Long permissionId : permissionIds) {
			result = this.terminatePermission(permissionId);
			if (!result.isSuccess()) {
				break;
			}
		}
		return result;
	}

	public JsonResult terminatePermission(Long permissionId) {
		JsonResult result = new JsonResult();
		Permission permission = null;
		try {
			permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthority(permission);
			result.setSuccess(true);
			result.setMessage("撤销权限成功。");
		} catch (CorrelationException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("权限有用户或者角色关联，不能撤销。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("撤销权限失败。");
		}
		return result;
	}

	@Override
	public JsonResult createPageElementResource(CreatePageElementResourceCommand command) {
		JsonResult result = new JsonResult();
		try {
			PageElementResource pageElementResource = PageElementResourceAssembler.toPageElementResource(command);
			securityConfigApplication.createSecurityResource(pageElementResource);
			result.setSuccess(true);
			result.setMessage("添加页面元素权限资源成功");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("名称和标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("页面元素权限资源名称" + command.getName() + "已经存在");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("页面元素权限资源标识" + command.getIdentifier() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加页面元素权限资源失败。");
		}
		return result;
	}

	@Override
	public JsonResult changePageElementResourceProps(ChangePageElementResourcePropsCommand command) {
		JsonResult result = new JsonResult();
		try {
			PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(command
					.getId());
			securityConfigApplication.changeNameOfPageElementResouce(pageElementResource, command.getName());
			securityConfigApplication
					.changeIdentifierOfPageElementResouce(pageElementResource, command.getIdentifier());
			pageElementResource.setDescription(command.getDescription());
			securityConfigApplication.createSecurityResource(pageElementResource);
			result.setSuccess(true);
			result.setMessage("更改页面元素权限资源成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("名称和标识不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("页面元素权限资源名称" + command.getName() + "已经存在");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("页面元素权限资源标识" + command.getIdentifier() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改页面元素权限资源失败。");
		}
		return result;
	}

	@Override
	public JsonResult createMenuResource(CreateMenuResourceCommand command) {
		JsonResult result = new JsonResult();
		try {
			MenuResource menuResource = MenuResourceAssembler.toMenuResource(command);
			securityConfigApplication.createSecurityResource(menuResource);
			result.setSuccess(true);
			result.setMessage("添加菜单权限资源名称成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加菜单权限资源名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加菜单权限资源名称" + command.getName() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加菜单权限资源失败");
		}
		return result;
	}

	@Override
	public JsonResult createChildMenuResouceToParent(CreateChildMenuResourceCommand command) {
		JsonResult result = new JsonResult();
		try {
			MenuResource menuResource = MenuResourceAssembler.toMenuResource(command);
			securityConfigApplication.createChildToParent(menuResource, command.getParentId());
			result.setSuccess(true);
			result.setMessage("添加子菜单权限资源名称成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("菜单权限资源名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加子菜单权限资源名称" + command.getName() + "已经存在");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("添加子菜单权限资源失败");
		}
		return result;
	}

	@Override
	public JsonResult changeMenuResourceProps(ChangeMenuResourcePropsCommand command) {
		JsonResult result = new JsonResult();
		try {
			MenuResource menuResource = securityAccessApplication.getMenuResourceBy(command.getId());
			securityConfigApplication.changeNameOfMenuResource(menuResource, command.getName());
			menuResource.setUrl(command.getUrl());
			menuResource.setMenuIcon(command.getMenuIcon());
			menuResource.setDescription(command.getDescription());
			result.setSuccess(true);
			result.setMessage("更改菜单权限资源成功。");
		} catch (NullArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("菜单权限资源名称不能为空。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改菜单权限资源名称" + command.getName() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("更改菜单权限资源失败。");
		}
		return result;
	}
}
