package org.openkoala.security.application;

import java.util.List;
import java.util.Set;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;

public interface SecurityAccessApplication {

	/**
	 * 判断用户是否有权限Permission
	 * 
	 * @param user
	 *            用户
	 */
	boolean hasPermission(User user);

	/**
	 * 判断用户是否拥有权限资源securityResource
	 * 
	 * @param user
	 *            用户
	 * @param securityResource
	 *            权限资源
	 */
	boolean hasOwnSecurityResource(User user, SecurityResource securityResource);

	/**
	 * 根据账户查找该用户拥有的所有角色
	 * 
	 * @param userAccount
	 *            账户
	 * @return
	 */
	List<Role> findAllRolesByUserAccount(String userAccount);

	/**
	 * 根据账户查找该用户拥有的所有角色
	 * 
	 * @param userAccount
	 *            账户
	 * @return
	 */
	List<Permission> findAllPermissionsByUserAccount(String userAccount);

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	User getUserBy(Long userId);

	/**
	 * 根据角色ID获取角色
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	Role getRoleBy(Long roleId);

	/**
	 * 根据账户获取用户
	 * 
	 * @param userAccount
	 *            账户
	 * @return
	 */
	User getUserBy(String userAccount);

	/**
	 * 根据权限ID得到权限
	 * 
	 * @param permissionId
	 *            权限ID
	 * @return
	 */
	Permission getPermissionBy(Long permissionId);

	/**
	 * 根据菜单资源ID得到菜单资源
	 * 
	 * @param menuResourceId
	 *            菜单资源ID
	 * @return
	 */
	MenuResource getMenuResourceBy(Long menuResourceId);

	/**
	 * 根据URL访问资源ID得到URL访问资源
	 * 
	 * @param urlAccessResourceId
	 * @return
	 */
	UrlAccessResource getUrlAccessResourceBy(Long urlAccessResourceId);

	/**
	 * 根据页面元素ID得到页面元素资源
	 * 
	 * @param pageElementResourceId
	 *            页面元素ID
	 * @return
	 */
	PageElementResource getPageElementResourceBy(Long pageElementResourceId);

	/**
	 * 根据页面元素名称得到页面元素资源
	 * 
	 * @param pageElementResourceName
	 *            页面元素资源名称
	 * @return
	 */
	PageElementResource getPageElementResourceBy(String pageElementResourceName);

	/**
	 * 根据角色名称得到角色。
	 * 
	 * @param roleName
	 *            角色名称
	 * @return
	 */
	Role getRoleBy(String roleName);

	Scope getScope(Long scopeId);

	/**
	 * 根据账户查找拥有的菜单资源
	 * 
	 * @param userAccount
	 *            账户
	 * @return
	 */
	Set<MenuResource> findMenuResourceByUserAccount(String userAccount);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 *            用户
	 * @param oldUserPassword
	 *            旧密码
	 * @return
	 */
	boolean updatePassword(User user, String oldUserPassword);

	/**
	 * 通过角色下的用户检查Authorization是否存在
	 * 
	 * @param userAccount
	 *            账户
	 * @param role
	 *            角色
	 */
	void checkAuthorization(String userAccount, Role role);

	/**
	 * 根据角色查找菜单资源
	 * 
	 * @param role
	 *            角色
	 * @return
	 */
	List<MenuResource> findAllMenuResourcesByRole(Role role);

	/**
	 * 查找所有的角色
	 * 
	 * @return
	 */
	List<Role> findAllRoles();

	/**
	 * 查找所有的URL访问资源。
	 * 
	 * @return
	 */
	List<UrlAccessResource> findAllUrlAccessResources();

	/**
	 * 用户登录
	 * 
	 * @param principal
	 *            当事人 可能是用户名、邮箱、电话 目前只实现用户名,
	 * @param password
	 *            密码
	 * @return
	 */
	User login(String principal, String password);

	boolean hasPageElementResource(String identifier);
	
}