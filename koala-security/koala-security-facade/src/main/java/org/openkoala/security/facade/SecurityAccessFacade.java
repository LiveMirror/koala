package org.openkoala.security.facade;

import java.util.List;
import java.util.Set;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.facade.dto.UserDTO;

public interface SecurityAccessFacade {

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 * @return
	 */
	UserDTO getUserDtoBy(Long userId);

	/**
	 * 根据用户名获取用户
	 * 
	 * @param username
	 * @return
	 */
	UserDTO getUserDtoBy(String username);

	/**
	 * 根据用户名查找所有的角色
	 * 
	 * @param username
	 *            用户名
	 * @return
	 */
	List<RoleDTO> findRoleDtosBy(String username);

	/**
	 * 根据用户名查找其拥有的所有权限
	 * 
	 * @param username
	 *            用户名
	 * @return
	 */
	Set<PermissionDTO> findPermissionDtosBy(String username);

	/**
	 * 根据用户名查找该
	 * 
	 * @param username
	 * @return
	 */
	List<MenuResourceDTO> findMenuResourceDtoByUsername(String username);

	/**
	 * 更新密码
	 * 
	 * @param userDto
	 * @param oldUserPassword
	 * @return
	 */
	boolean updatePassword(UserDTO userDto, String oldUserPassword);

	/**
	 * 更新用户
	 * 
	 * @param userDTO
	 */
	void updateUserDTO(UserDTO userDTO);

	/**
	 * 分页查询用户信息
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param userDTO
	 * @return
	 */
	Page<UserDTO> pagingQueryUsers(int currentPage, int pageSize, UserDTO userDTO);

	/**
	 * 分页查询角色信息
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param roleDTO
	 * @return
	 */
	Page<RoleDTO> pagingQueryRoles(int currentPage, int pageSize, RoleDTO roleDTO);

	/**
	 * 分页查询权限信息
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param permissionDTO
	 * @return
	 */
	Page<PermissionDTO> pagingQueryPermissions(int currentPage, int pageSize, PermissionDTO permissionDTO);

	/***
	 * 查询某个角色下用户的菜单资源。
	 * 
	 * @param username
	 * @param roleDTO
	 * @return
	 */
	List<MenuResourceDTO> findMenuResourceDTOByUserAccountAsRole(String userAccount, Long roleId);

	List<MenuResourceDTO> findAllMenusTree();

	List<OrganizationScopeDTO> findAllOrganizationScopesTree();

	Page<RoleDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition, Long userId);

	Page<PermissionDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize, PermissionDTO queryPermissionCondition,
			Long userId);

	Page<PermissionDTO> pagingQueryPermissionsByUserAccount(int currentPage, int pageSize, Long userId);

	Page<RoleDTO> pagingQueryRolesByUserAccount(int currentPage, int pageSize, Long userId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByRole(int currentPage, int pageSize, Long roleId);

	Page<PermissionDTO> pagingQueryPermissionsByRole(int currentPage, int pageSize, Long roleId);

	Page<UrlAccessResourceDTO> pagingQueryUrlAccessResources(int currentPage, int pageSize,
			UrlAccessResourceDTO urlAccessResourceDTO);

	/**
	 * 查找所有的权限
	 * 
	 * @return
	 */
	Set<PermissionDTO> findPermissions();

	/**
	 * 查找所有的角色
	 * 
	 * @return
	 */
	Set<RoleDTO> findRoles();

	MenuResourceDTO findMenuResourceBy(PermissionDTO permissionDTO);

	UrlAccessResourceDTO findUrlAccessResourceBy(PermissionDTO permissionDTO);

}
