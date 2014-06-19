package org.openkoala.security.application;

import java.util.Set;

import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.User;

public interface SecurityAccessApplication {

	/**
	 * TODO 处理各种情况
	 * 
	 * @param actor
	 */
	void updateActor(Actor actor);

	/**
	 * 
	 * @param user
	 */
	boolean hasPermission(User user);

	/**
	 * 
	 * @param user
	 * @param securityResource
	 */
	boolean canAccessSecurityResource(User user, SecurityResource securityResource);

	/**
	 * 根据账户查找该用户拥有的所有角色
	 * 
	 * @param userAccount
	 * @return
	 */
	Set<Role> findAllRolesByUserAccount(String userAccount);

	/**
	 * 根据账户查找该用户拥有的所有角色
	 * 
	 * @param userAccount
	 * @return
	 */
	Set<Permission> findAllPermissionsByUserAccount(String userAccount);

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	User getUserBy(Long userId);
	
	Role getRoleBy(Long roleId);

	/**
	 * 根据账户获取用户
	 * 
	 * @param userAccount
	 * @return
	 */
	User getUserBy(String userAccount);

	/**
	 * 根据账户查找拥有的菜单资源
	 * 
	 * @param username
	 * @return
	 */
	Set<MenuResource> findMenuResourceByUserAccount(String userAccount);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 * @param oldUserPassword
	 * @return
	 */
	boolean updatePassword(User user, String oldUserPassword);

	/**
	 * 通过角色下得用户检查Authorization是否存在
	 * 
	 * @param userAccount
	 * @param role
	 */
	void checkAuthorization(String userAccount, Role role);

	Permission getPermissionBy(Long permissionId);

	MenuResource getMenuResourceBy(Long menuResourceId);

}