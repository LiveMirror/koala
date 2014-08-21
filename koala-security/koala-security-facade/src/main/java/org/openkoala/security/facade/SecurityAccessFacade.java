package org.openkoala.security.facade;

import java.util.List;
import java.util.Set;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.dto.*;

public interface SecurityAccessFacade {

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	UserDTO getUserById(Long userId);

	/**
	 * 根据用户名查找所有的角色
	 * 
	 * @param userAccount
	 *            用户名
	 * @return
	 */
	List<RoleDTO> findRolesByUserAccount(String userAccount);

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
	 * @param queryUserCondition
	 * @return
	 */
	Page<UserDTO> pagingQueryUsers(int currentPage, int pageSize, UserDTO queryUserCondition);

	/**
	 * 分页查询角色信息
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param queryRoleCondition
	 * @return
	 */
	Page<RoleDTO> pagingQueryRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition);

	/**
	 * 分页查询权限信息
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param permissionDTO
	 * @return
	 */
	Page<PermissionDTO> pagingQueryPermissions(int currentPage, int pageSize, PermissionDTO queryPermissionCondition);

	/***
	 * 查询某个角色下用户的菜单资源。
	 * 
	 * @param username
	 * @param roleDTO
	 * @return
	 */
	List<MenuResourceDTO> findMenuResourceByUserAsRole(String userAccount, Long roleId);

	List<MenuResourceDTO> findAllMenusTree();

	Page<RoleDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition, Long userId);

	Page<PermissionDTO> pagingQueryGrantPermissionByUserId(int currentPage, int pageSize, Long userId);

	Page<RoleDTO> pagingQueryGrantRolesByUserId(int currentPage, int pageSize, Long userId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId);

	Page<PermissionDTO> pagingQueryGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId);

	Page<UrlAccessResourceDTO> pagingQueryUrlAccessResources(int currentPage, int pageSize,
			UrlAccessResourceDTO queryUrlAccessResourceCondition);

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

	List<UrlAuthorityDTO> findAllUrlAccessResources();

    /**
     * 查找所有的菜单资源。
     * @return
     */
    List<MenuResourceDTO> findAllMenuResources();

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
			PageElementResourceDTO queryPageElementResourceCondition);

	Page<PageElementResourceDTO> pagingQueryGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId);

	Page<PageElementResourceDTO> pagingQueryNotGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId);

	Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId);

	Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId);

	UserDTO login(String principal, String password);

	/**
	 * 通过账户查找用户
	 * 
	 * @param userAccount
	 *            账户
	 * @return
	 */
	UserDTO getUserByUserAccount(String userAccount);

	/**
	 * 通过邮箱查询用户
	 * 
	 * @param email
	 *            邮箱
	 * @return
	 */
	UserDTO getUserByEmail(String email);

	/**
	 * 通过联系电话查询用户
	 * 
	 * @param telePhone
	 *            联系电话
	 * @return
	 */
	UserDTO getUserByTelePhone(String telePhone);

	/**
	 * 根据账户和角色名称查找所有权限
	 * 
	 * @param userAccount
	 * @param roleName
	 * @return
	 */
	Set<PermissionDTO> findPermissionsByUserAccountAndRoleName(String userAccount, String roleName);

}
