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
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "transactionManager_security")
public interface SecurityConfigFacade {

	/**
	 * 创建用户
	 * 
	 * @param command
	 */
	InvokeResult createUser(CreateUserCommand command);

	/**
	 * 创建权限
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult createPermission(CreatePermissionCommand command);

	/**
	 * 创建角色
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult createRole(CreateRoleCommand command);

	/**
	 * 创建菜单权限资源
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult createMenuResource(CreateMenuResourceCommand command);

	/**
	 * 创建子菜单权限资源
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult createChildMenuResouceToParent(CreateChildMenuResourceCommand command);

	/**
	 * 创建页面元素
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult createPageElementResource(CreatePageElementResourceCommand command);

	/**
	 * 创建URL访问资源
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult createUrlAccessResource(CreateUrlAccessResourceCommand command);

	/**
	 * 更改用户的一些属性。
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeUserProps(ChangeUserPropsCommand command);

	/**
	 * 更改用户账号
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeUserAccount(ChangeUserAccountCommand command);

	/**
	 * 更改用户邮箱
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeUserEmail(ChangeUserEmailCommand command);

	/**
	 * 更改用户联系电话
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeUserTelePhone(ChangeUserTelePhoneCommand command);

	/**
	 * 更改URL访问资源
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeUrlAccessResourceProps(ChangeUrlAccessResourcePropsCommand command);

	/**
	 * 更改角色的一些属性
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeRoleProps(ChangeRolePropsCommand command);

	/**
	 * 更改权限的一些属性
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changePermissionProps(ChangePermissionPropsCommand command);

	/**
	 * 更改页面元素的一些属性
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changePageElementResourceProps(ChangePageElementResourcePropsCommand command);

	/**
	 * 更改菜单的一些属性。
	 * 
	 * @param command
	 * @return
	 */
	InvokeResult changeMenuResourceProps(ChangeMenuResourcePropsCommand command);

	/**
	 * 更新用户密码
	 * 
	 * @param command
	 *            TODO
	 * 
	 * @return
	 */
	InvokeResult changeUserPassword(ChangeUserPasswordCommand command);

	/**
	 * 重置用户密码
	 * 
	 * @param userId
	 * @return
	 */
	InvokeResult resetPassword(Long userId);

	/**
	 * 撤销用户
	 * 
	 * @param userId
	 * @return
	 */
	InvokeResult terminateUser(Long userId);

	/**
	 * 批量撤销用户
	 * 
	 * @param userIds
	 * @return
	 */
	InvokeResult terminateUsers(Long[] userIds);

	/**
	 * 撤销角色
	 * 
	 * @param roleId
	 * @return
	 */
	InvokeResult terminateRole(Long roleId);

	/**
	 * 批量撤销角色
	 * 
	 * @param roleIds
	 * @return
	 */
	InvokeResult terminateRoles(Long[] roleIds);

	/**
	 * 撤销权限
	 * 
	 * @param permissionId
	 * @return
	 */
	InvokeResult terminatePermission(Long permissionId);

	/**
	 * 批量撤销权限
	 * 
	 * @param permissionIds
	 * @return
	 */
	InvokeResult terminatePermissions(Long[] permissionIds);

	/**
	 * 撤销菜单权限资源
	 * 
	 * @param menuResourceId
	 * @return
	 */
	InvokeResult terminateMenuResource(Long menuResourceId);

	/**
	 * 批量撤销菜单权限资源
	 * 
	 * @param menuResourceIds
	 * @return
	 */
	InvokeResult terminateMenuResources(Long[] menuResourceIds);

	/**
	 * 撤销URL访问权限资源
	 * 
	 * @param urlAccessResourceId
	 * @return
	 */
	InvokeResult terminateUrlAccessResource(Long urlAccessResourceId);

	/**
	 * 批量撤销URL访问权限资源
	 * 
	 * @param urlAccessResourceIds
	 * @return
	 */
	InvokeResult terminateUrlAccessResources(Long[] urlAccessResourceIds);

	/**
	 * 撤销页面元素权限资源。
	 * 
	 * @param pageElementResourceId
	 * @return
	 */
	InvokeResult terminatePageElementResource(Long pageElementResourceId);

	/**
	 * 批量撤销页面元素权限资源。
	 * 
	 * @param pageElementResourceIds
	 * @return
	 */
	InvokeResult terminatePageElementResources(Long[] pageElementResourceIds);

	void grantRoleToUserInScope(Long userId, Long roleId, Long scopeId);

	void grantRolesToUserInScope(Long userId, Long[] roleIds, Long scopeId);

	void grantPermissionToUserInScope(Long userId, Long permissionId, Long scopeId);

	void grantPermissionsToUserInScope(Long userId, Long[] permissionIds, Long scopeId);

	InvokeResult grantRoleToUser(Long userId, Long roleId);

	InvokeResult grantRolesToUser(Long userId, Long[] roleIds);

	InvokeResult grantPermissionToUser(Long userId, Long permissionId);

	InvokeResult grantPermissionsToUser(Long userId, Long[] permissionIds);

	InvokeResult activate(Long userId);

	InvokeResult suspend(Long userId);

	InvokeResult activate(Long[] userIds);

	InvokeResult suspend(Long[] userIds);

	InvokeResult terminateAuthorizationByUserInRole(Long userId, Long roleId);

    InvokeResult terminateAuthorizationByUserInPermission(Long userId, Long permissionId);

	InvokeResult terminateAuthorizationByUserInRoles(Long userId, Long[] roleIds);

    InvokeResult terminateAuthorizationByUserInPermissions(Long userId, Long[] permissionIds);

	InvokeResult grantMenuResourcesToRole(Long roleId, Long[] menuResourceIds);

	InvokeResult grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds);

	InvokeResult grantUrlAccessResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantMethodInvocationResourcesToRole(Long roleId, Long[] menuResourceIds);

	InvokeResult grantPermissionsToRole(Long roleId, Long[] permissionIds);

	InvokeResult terminatePermissionsFromRole(Long roleId, Long[] permssionIds);

	InvokeResult terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds);

	void grantUrlAccessResourceToPermission(Long urlAccessResourceId, Long permissionId);

	void terminateUrlAccessResourceFromPermission(Long urlAccessResourceId, Long permissionId);

	InvokeResult grantPermisssionsToUrlAccessResource(Long permissionId, Long urlAccessResourceId);

	InvokeResult terminatePermissionsFromUrlAccessResource(Long permissionId, Long urlAccessResourceId);

	InvokeResult grantPermisssionsToMenuResource(Long permissionId, Long menuResourceId);

	InvokeResult terminatePermissionsFromMenuResource(Long permissionId, Long menuResourceId);

	InvokeResult terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds);

	InvokeResult grantPermisssionsToPageElementResource(Long permissionId, Long pageElementResourceId);

	InvokeResult terminatePermissionsFromPageElementResource(Long permissionId, Long pageElementResourceId);

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

}