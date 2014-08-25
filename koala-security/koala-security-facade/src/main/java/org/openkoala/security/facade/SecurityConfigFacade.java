package org.openkoala.security.facade;

import org.openkoala.koala.commons.InvokeResult;
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

public interface SecurityConfigFacade {

	/**
	 * 创建用户
	 * 
	 * @param command
	 */
	JsonResult createUser(CreateUserCommand command);

	/**
	 * 创建权限
	 * 
	 * @param command
	 * @return
	 */
	JsonResult createPermission(CreatePermissionCommand command);

	/**
	 * 创建角色
	 * 
	 * @param command
	 * @return
	 */
	JsonResult createRole(CreateRoleCommand command);

	/**
	 * 创建菜单权限资源
	 * 
	 * @param command
	 * @return
	 */
	JsonResult createMenuResource(CreateMenuResourceCommand command);

	/**
	 * 创建子菜单权限资源
	 * 
	 * @param command
	 * @return
	 */
	JsonResult createChildMenuResouceToParent(CreateChildMenuResourceCommand command);

	/**
	 * 创建页面元素
	 * 
	 * @param command
	 * @return
	 */
	JsonResult createPageElementResource(CreatePageElementResourceCommand command);

	/**
	 * 创建URL访问资源
	 * 
	 * @param command
	 * @return
	 */
	JsonResult createUrlAccessResource(CreateUrlAccessResourceCommand command);

	/**
	 * 更改用户的一些属性。
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeUserProps(ChangeUserPropsCommand command);

	/**
	 * 更改用户账号
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeUserAccount(ChangeUserAccountCommand command);

	/**
	 * 更改用户邮箱
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeUserEmail(ChangeUserEmailCommand command);

	/**
	 * 更改用户联系电话
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeUserTelePhone(ChangeUserTelePhoneCommand command);

	/**
	 * 更改URL访问资源
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeUrlAccessResourceProps(ChangeUrlAccessResourcePropsCommand command);

	/**
	 * 更改角色的一些属性
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeRoleProps(ChangeRolePropsCommand command);

	/**
	 * 更改权限的一些属性
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changePermissionProps(ChangePermissionPropsCommand command);

	/**
	 * 更改页面元素的一些属性
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changePageElementResourceProps(ChangePageElementResourcePropsCommand command);

	/**
	 * 更改菜单的一些属性。
	 * 
	 * @param command
	 * @return
	 */
	JsonResult changeMenuResourceProps(ChangeMenuResourcePropsCommand command);

	/**
	 * 更新用户密码
	 * 
	 * @param command
	 *            TODO
	 * 
	 * @return
	 */
	JsonResult changeUserPassword(ChangeUserPasswordCommand command);

	/**
	 * 重置用户密码
	 * 
	 * @param userId
	 * @return
	 */
	JsonResult resetPassword(Long userId);

	/**
	 * 撤销用户
	 * 
	 * @param userId
	 * @return
	 */
	JsonResult terminateUser(Long userId);

	/**
	 * 批量撤销用户
	 * 
	 * @param userIds
	 * @return
	 */
	JsonResult terminateUsers(Long[] userIds);

	/**
	 * 撤销角色
	 * 
	 * @param roleId
	 * @return
	 */
	JsonResult terminateRole(Long roleId);

	/**
	 * 批量撤销角色
	 * 
	 * @param roleIds
	 * @return
	 */
	JsonResult terminateRoles(Long[] roleIds);

	/**
	 * 撤销权限
	 * 
	 * @param permissionId
	 * @return
	 */
	JsonResult terminatePermission(Long permissionId);

	/**
	 * 批量撤销权限
	 * 
	 * @param permissionIds
	 * @return
	 */
	JsonResult terminatePermissions(Long[] permissionIds);

	/**
	 * 撤销菜单权限资源
	 * 
	 * @param menuResourceId
	 * @return
	 */
	JsonResult terminateMenuResource(Long menuResourceId);

	/**
	 * 批量撤销菜单权限资源
	 * 
	 * @param menuResourceIds
	 * @return
	 */
	JsonResult terminateMenuResources(Long[] menuResourceIds);

	/**
	 * 撤销URL访问权限资源
	 * 
	 * @param urlAccessResourceId
	 * @return
	 */
	JsonResult terminateUrlAccessResource(Long urlAccessResourceId);

	/**
	 * 批量撤销URL访问权限资源
	 * 
	 * @param urlAccessResourceIds
	 * @return
	 */
	JsonResult terminateUrlAccessResources(Long[] urlAccessResourceIds);

	/**
	 * 撤销页面元素权限资源。
	 * 
	 * @param pageElementResourceId
	 * @return
	 */
	JsonResult terminatePageElementResource(Long pageElementResourceId);

	/**
	 * 批量撤销页面元素权限资源。
	 * 
	 * @param pageElementResourceIds
	 * @return
	 */
	JsonResult terminatePageElementResources(Long[] pageElementResourceIds);

	void grantRoleToUserInScope(Long userId, Long roleId, Long scopeId);

	void grantRolesToUserInScope(Long userId, Long[] roleIds, Long scopeId);

	void grantPermissionToUserInScope(Long userId, Long permissionId, Long scopeId);

	void grantPermissionsToUserInScope(Long userId, Long[] permissionIds, Long scopeId);

	void grantRoleToUser(Long userId, Long roleId);

	void grantRolesToUser(Long userId, Long[] roleIds);

	void grantPermissionToUser(Long userId, Long permissionId);

	void grantPermissionsToUser(Long userId, Long[] permissionIds);

	JsonResult activate(Long userId);

	JsonResult suspend(Long userId);

	JsonResult activate(Long[] userIds);

	JsonResult suspend(Long[] userIds);

	void terminateAuthorizationByUserInRole(Long userId, Long roleId);

    InvokeResult terminateAuthorizationByUserInPermission(Long userId, Long permissionId);

	void terminateAuthorizationByUserInRoles(Long userId, Long[] roleIds);

    InvokeResult terminateAuthorizationByUserInPermissions(Long userId, Long[] permissionIds);

	void grantMenuResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds);

	void grantUrlAccessResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantMethodInvocationResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantPermissionsToRole(Long roleId, Long[] permissionIds);

	void terminatePermissionsFromRole(Long roleId, Long[] permssionIds);

	void terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds);

	void grantUrlAccessResourceToPermission(Long urlAccessResourceId, Long permissionId);

	void terminateUrlAccessResourceFromPermission(Long urlAccessResourceId, Long permissionId);

	void grantPermisssionsToUrlAccessResource(Long permissionId, Long urlAccessResourceId);

	void terminatePermissionsFromUrlAccessResource(Long permissionId, Long urlAccessResourceId);

	void grantPermisssionsToMenuResource(Long permissionId, Long menuResourceId);

	void terminatePermissionsFromMenuResource(Long permissionId, Long menuResourceId);

	void terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds);

	void grantPermisssionsToPageElementResource(Long permissionId, Long pageElementResourceId);

	void terminatePermissionsFromPageElementResource(Long permissionId, Long pageElementResourceId);

	boolean checkUserHasPageElementResource(String userAccount, String currentRoleName, String securityResourceIdentifier);

	/**
	 * 初始化系统权限资源。
	 */
	public void initSecuritySystem();

	/**
	 * 更新登陆时间
	 * 
	 * @param userId
	 */
	void updateUserLastLoginTime(Long userId);

    InvokeResult changeRoleOfUser(Long roleId, String userAccount);
}