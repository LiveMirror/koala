package org.openkoala.security.facade;

import java.util.List;

import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.facade.dto.UserDTO;

public interface SecurityConfigFacade {

	/**
	 * 保存用户
	 * 
	 * @param userDTO
	 */
	void saveUser(UserDTO userDTO);

	/**
	 * 撤销用户
	 * 
	 * @param userDTOs
	 */
	void terminateUsers(UserDTO[] userDTOs);

	/**
	 * 更新用户
	 * 
	 * @param userDTO
	 */
	void updateUser(UserDTO userDTO);

	/**
	 * 更新用户密码
	 * 
	 * @param userAccount
	 * @param userPassword
	 * @param oldUserPassword
	 * @return
	 */
	boolean updatePassword(String userAccount, String userPassword, String oldUserPassword);

	/**
	 * 重置用户密码
	 * 
	 * @param userId
	 */
	void resetPassword(Long userId);

	/**
	 * 保存角色
	 * 
	 * @param roleDTO
	 */
	void saveRole(RoleDTO roleDTO);

	/**
	 * 更新角色
	 * 
	 * @param roleDTO
	 */
	void updateRole(RoleDTO roleDTO);

	/**
	 * 批量撤销角色
	 * 
	 * @param roleDTOs
	 */
	void terminateRoles(RoleDTO[] roleDTOs);

	/**
	 * 保存权限
	 * 
	 * @param permissionDTO
	 */
	void savePermission(PermissionDTO permissionDTO);

	/**
	 * 更新权限
	 * 
	 * @param permissionDTO
	 */
	void updatePermission(PermissionDTO permissionDTO);

	/**
	 * 批量撤销权限
	 * 
	 * @param permissionDTOs
	 */
	void terminatePermissions(PermissionDTO[] permissionDTOs);

	/**
	 * 保存菜单资源
	 * 
	 * @param menuResourceDTO
	 */
	void saveMenuResource(MenuResourceDTO menuResourceDTO);

	/**
	 * 更新菜单资源
	 * 
	 * @param menuResourceDTO
	 */
	void updateMenuResource(MenuResourceDTO menuResourceDTO);

	/**
	 * 撤销菜单资源
	 * 
	 * @param menuResourceDTOs
	 */
	void terminateMenuResources(MenuResourceDTO[] menuResourceDTOs);

	/**
	 * 添加一个子菜单到父菜单。
	 * 
	 * @param child
	 * @param parentId
	 */
	void saveChildToParent(MenuResourceDTO child, Long parentId);

	void grantRoleToUserInScope(Long userId, Long roleId, Long scopeId);

	void grantRolesToUserInScope(Long userId, Long[] roleIds, Long scopeId);

	void grantPermissionToUserInScope(Long userId, Long permissionId, Long scopeId);

	void grantPermissionsToUserInScope(Long userId, Long[] permissionIds, Long scopeId);

	void grantRoleToUser(Long userId, Long roleId);

	void grantRolesToUser(Long userId, Long[] roleIds);

	void grantPermissionToUser(Long userId, Long permissionId);

	void grantPermissionsToUser(Long userId, Long[] permissionIds);

	void activate(Long userId);

	void suspend(Long userId);

	void activate(Long[] userIds);

	void suspend(Long[] userIds);

	void terminateAuthorizationByUserInRole(Long userId, Long roleId);

	void terminateAuthorizationByUserInPermission(Long userId, Long permissionId);

	void terminateAuthorizationByUserInRoles(Long userId, Long[] roleIds);

	void terminateAuthorizationByUserInPermissions(Long userId, Long[] permissionIds);

	void grantMenuResourcesToRole(Long roleId, List<MenuResourceDTO> menuResourceDTOs);

	void grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds);

	void grantUrlAccessResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantMethodInvocationResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantPermissionsToRole(Long roleId, Long[] permissionIds);

	void terminatePermissionsFromRole(Long roleId, Long[] permssionIds);

	void saveUrlAccessResource(UrlAccessResourceDTO urlAccessResourceDTO);

	void updateUrlAccessResource(UrlAccessResourceDTO urlAccessResourceDTO);

	void terminateUrlAccessResources(UrlAccessResourceDTO[] urlAccessResourceDTOs);

	void terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds);

	void grantUrlAccessResourceToPermission(Long urlAccessResourceId, Long permissionId);

	void terminateUrlAccessResourceFromPermission(Long urlAccessResourceId, Long permissionId);

	void grantPermisssionsToUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId);

	void terminatePermissionsFromUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId);

	void grantPermisssionsToMenuResource(Long[] permissionIds, Long menuResourceId);

	void terminatePermissionsFromMenuResource(Long[] permissionIds, Long menuResourceId);

	void savePageElementResource(PageElementResourceDTO pageElementResourceDTO);

	void updatePageElementResource(PageElementResourceDTO pageElementResourceDTO);

	void terminatePageElementResources(PageElementResourceDTO[] pageElementResourceDTOs);

	void terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds);

	void grantPermisssionsToPageElementResource(Long[] permissionIds, Long pageElementResourceId);

	void terminatePermissionsFromPageElementResource(Long[] permissionIds, Long pageElementResourceId);

	boolean checkUserHasPageElementResource(String userAccount, String currentRoleName, String securityResourceName);

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
