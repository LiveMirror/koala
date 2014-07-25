package org.openkoala.security.facade;

import java.util.List;
import java.util.Set;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.facade.dto.UserDTO;

public interface SecurityAccessFacade {

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	UserDTO getUserBy(Long userId);

	/**
	 * 根据用户名获取用户
	 * 
	 * @param userAccount
	 * @return
	 */
	UserDTO getUserBy(String userAccount);

	/**
	 * 根据用户名查找所有的角色
	 * 
	 * @param userAccount
	 *            用户名
	 * @return
	 */
	List<RoleDTO> findRolesBy(String userAccount);

	/**
	 * 根据用户名查找其拥有的所有权限
	 * 
	 * @param userAccount
	 *            用户名
	 * @return
	 */
	Set<PermissionDTO> findPermissionsBy(String userAccount);

	/**
	 * 根据用户名查找该
	 * 
	 * @param userAccount
	 * @return
	 */
	List<MenuResourceDTO> findMenuResourceByUserAccount(String userAccount);

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
	List<MenuResourceDTO> findMenuResourceByUserAsRole(String userAccount, Long roleId);

	List<MenuResourceDTO> findAllMenusTree();

	List<OrganizationScopeDTO> findAllOrganizationScopesTree();

	Page<RoleDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition, Long userId);

	Page<PermissionDTO> pagingQueryGrantPermissionByUserId(int currentPage, int pageSize, Long userId);

	Page<RoleDTO> pagingQueryGrantRolesByUserId(int currentPage, int pageSize, Long userId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId);

	Page<PermissionDTO> pagingQueryGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId);

	Page<UrlAccessResourceDTO> pagingQueryUrlAccessResources(int currentPage, int pageSize,
			UrlAccessResourceDTO urlAccessResourceDTO);

	/**
	 * 根据URL或者菜单类型查找所有的权限
	 * 
	 * @return
	 */
	Set<PermissionDTO> findPermissionsByMenuOrUrl();

	/**
	 * 查找所有的角色
	 * 
	 * @return
	 */
	Set<RoleDTO> findRolesByMenuOrUrl();

	List<UrlAccessResourceDTO> findAllUrlAccessResources();

	/**
	 * 根据角色ID查找所有的URL访问资源。
	 * 
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	Page<UrlAccessResourceDTO> pagingQueryGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId);

	/**
	 * 根据角色ID查找所有没有授权的URL访问资源。
	 * 
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	Page<UrlAccessResourceDTO> pagingQueryNotGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId);

	/**
	 * 根据URL访问资源分页查询已经授权的权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param urlAccessResourceId
	 * @return
	 */
	Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId);

	/**
	 * 根据URL访问资源分页查询没有授权的权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param urlAccessResourceId
	 * @return
	 */
	Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId);

	/**
	 * 根据角色ID查询菜单树（已经选择的有选择标识）。
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<MenuResourceDTO> findMenuResourceTreeSelectItemByRoleId(Long roleId);

	Page<PermissionDTO> pagingQueryGrantPermissionsByMenuResourceId(int page, int pagesize, Long menuResourceId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByMenuResourceId(int page, int pagesize, Long menuResourceId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByUserId(int page, int pagesize,
			PermissionDTO queryPermissionCondition, Long userId);

	Page<PageElementResourceDTO> pagingQueryPageElementResources(int page, int pagesize,
			PageElementResourceDTO pageElementResourceDTO);

	Page<PageElementResourceDTO> pagingQueryGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId);

	Page<PageElementResourceDTO> pagingQueryNotGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId);

	Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId);

	UserDTO login(String principal, String password);

}
