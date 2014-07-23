package org.openkoala.security.facade;

import java.util.List;

import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
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
	void saveUserDTO(UserDTO userDTO);

	/**
	 * 撤销用户
	 * 
	 * @param userDTOs
	 */
	void terminateUserDTOs(UserDTO[] userDTOs);

	/**
	 * 更新用户
	 * 
	 * @param userDTO
	 */
	void updateUserDTO(UserDTO userDTO);
	
	/**
	 * 更新密码
	 * 
	 * @param userDto
	 * @param oldUserPassword
	 * @return
	 */
	boolean updatePassword(UserDTO userDto, String oldUserPassword);

	/**
	 * 重置用户密码
	 * 
	 * @param userDTO
	 */
	void resetPassword(UserDTO userDTO);

	/**
	 * 保存角色
	 * 
	 * @param roleDTO
	 */
	void saveRoleDTO(RoleDTO roleDTO);

	/**
	 * 更新角色
	 * 
	 * @param roleDTO
	 */
	void updateRoleDTO(RoleDTO roleDTO);

	/**
	 * 批量撤销角色
	 * 
	 * @param roleDTOs
	 */
	void terminateRoleDTOs(RoleDTO[] roleDTOs);

	/**
	 * 保存权限
	 * 
	 * @param permissionDTO
	 */
	void savePermissionDTO(PermissionDTO permissionDTO);

	/**
	 * 更新权限
	 * 
	 * @param permissionDTO
	 */
	void updatePermissionDTO(PermissionDTO permissionDTO);

	/**
	 * 批量撤销权限
	 * 
	 * @param permissionDTOs
	 */
	void terminatePermissionDTOs(PermissionDTO[] permissionDTOs);

	/**
	 * 保存菜单资源
	 * 
	 * @param menuResourceDTO
	 */
	void saveMenuResourceDTO(MenuResourceDTO menuResourceDTO);

	/**
	 * 更新菜单资源
	 * 
	 * @param menuResourceDTO
	 */
	void updateMenuResourceDTO(MenuResourceDTO menuResourceDTO);

	/**
	 * 撤销菜单资源
	 * 
	 * @param menuResourceDTOs
	 */
	void terminateMenuResourceDTOs(MenuResourceDTO[] menuResourceDTOs);

	/**
	 * 添加一个子菜单到父菜单。
	 * 
	 * @param child
	 * @param parentId
	 */
	void saveChildToParent(MenuResourceDTO child, Long parentId);

	/**
	 * 保存组织范围
	 * 
	 * @param organizationDTO
	 */
	void saveOrganizationDTO(OrganizationScopeDTO organizationScopeDTO);

	/**
	 * 更新组织范围
	 * 
	 * @param organizationDTO
	 */
	void updateOrganizationDTO(OrganizationScopeDTO organizationScopeDTO);

	/**
	 * 批量撤销组织范围
	 * 
	 * @param organizationDTOs
	 */
	void terminateOrganizationDTOs(OrganizationScopeDTO[] organizationScopeDTOs);

	void saveChildToParent(OrganizationScopeDTO child, Long parentId);

	void grantRoleInScope(Long userId, Long roleId, Long scopeId);

	void grantRolesInScope(Long userId, Long[] roleIds, Long scopeId);

	void grantPermissionInScope(Long userId, Long permissionId, Long scopeId);

	void grantPermissionsInScope(Long userId, Long[] permissionIds, Long scopeId);

	void grantRoleToUser(Long userId, Long roleId);

	void grantRolesToUser(Long userId, Long[] roleIds);

	void grantPermissionToUser(Long userId, Long permissionId);

	void grantPermissionsToUser(Long userId, Long[] permissionIds);

	void activate(Long userId);

	void suspend(Long userId);

	void activate(Long[] userIds);

	void suspend(Long[] userIds);

	void terminateAuthorizationByRole(Long userId, Long roleId);

	void terminateAuthorizationByPermission(Long userId, Long permissionId);

	void terminateAuthorizationsByRoles(Long userId, Long[] roleIds);

	void terminateAuthorizationsByPermissions(Long userId, Long[] permissionIds);

	void grantMenuResourcesToRole(Long roleId, List<MenuResourceDTO> menuResourceDTOs);

	void grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds);

	void grantUrlAccessResourcesToRole(Long roleId, Long[] menuResourceIds);

	void grantMethodInvocationResourcesToUser(Long roleId, Long[] menuResourceIds);

	void grantPermissionsToRole(Long roleId, Long[] permissionIds);

	void terminatePermissionsFromRole(Long roleId, Long[] permssionIds);

	void saveUrlAccessResourceDTO(UrlAccessResourceDTO urlAccessResourceDTO);

	void updateUrlAccessResourceDTO(UrlAccessResourceDTO urlAccessResourceDTO);

	void terminateUrlAccessResourceDTOs(UrlAccessResourceDTO[] urlAccessResourceDTOs);

	void terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds);

	void grantUrlAccessResourceToPermission(Long urlAccessResourceId, Long permissionId);

	void terminateUrlAccessResourceFromPermission(Long urlAccessResourceId, Long permissionId);

	void grantPermisssionsToUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId);

	void terminatePermissionsFromUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId);

	void grantPermisssionsToMenuResource(Long[] permissionIds, Long menuResourceId);

	void terminatePermissionsFromMenuResource(Long[] permissionIds, Long menuResourceId);

	void savePageElementResourceDTO(PageElementResourceDTO pageElementResourceDTO);

	void updatePageElementResourceDTO(PageElementResourceDTO pageElementResourceDTO);

	void terminatePageElementResourceDTOs(PageElementResourceDTO[] pageElementResourceDTOs);

	void terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds);

	void grantPermisssionsToPageElementResource(Long[] permissionIds, Long pageElementResourceId);

	void terminatePermissionsFromPageElementResource(Long[] permissionIds, Long pageElementResourceId);

	boolean checkUserHasPageElementResource(String userAccount, String currentRoleName, String securityResourceName);

	/**
	 * 初始化系统权限资源。
	 */
	public void initSecuritySystem();

}
