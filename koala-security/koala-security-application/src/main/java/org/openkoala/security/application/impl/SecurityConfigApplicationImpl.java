package org.openkoala.security.application.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Named
@Transactional
public class SecurityConfigApplicationImpl implements SecurityConfigApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfigApplicationImpl.class);

	public void createActor(Actor actor) {
		actor.save();
	}

	public void terminateActor(Actor actor) {
		actor.remove();
	}

	public void suspendUser(User user) {
		user.disable();
	}

	public void activateUser(User user) {
		user.enable();
	}

	public void createAuthority(Authority authority) {
		authority.save();
	}

	public void updateAuthority(Authority authority) {
		authority.update();
	}

	public void terminateAuthority(Authority authority) {
		authority.remove();
	}

	public void createSecurityResource(SecurityResource securityResource) {
		securityResource.save();
	}

	public void updateSecurityResource(SecurityResource securityResource) {
		securityResource.update();
	}

	public void terminateSecurityResource(SecurityResource securityResource) {
		securityResource.remove();
	}

	public void grantAuthoritiesToSecurityResource(List<Authority> authorities, SecurityResource securityResource) {
		for (Authority authority : authorities) {
			grantAuthorityToSecurityResource(authority, securityResource);
		}
	}

	public void grantRoleToPermission(Role role, Permission permission) {
		role.addPermission(permission);
	}

	public void grantRoleToPermissions(Role role, List<Permission> permissions) {
		role.addPermissions(permissions);
	}

	public void grantRolesToPermission(List<Role> roles, Permission permission) {
		for (Role role : roles) {
			role.addPermission(permission);
		}
	}

	public void grantActorToAuthority(Actor actor, Authority authority) {
		actor.grant(authority, null);
	}

	public void grantActorToAuthorities(Actor actor, List<Authority> authorities) {
		for (Authority authority : authorities) {
			this.grantActorToAuthority(actor, authority);
		}
	}

	public void grantActorsToAuthority(List<Actor> actors, Authority authority) {
		for (Actor actor : actors) {
			this.grantActorToAuthority(actor, authority);
		}
	}

	public void terminateSecurityResourceFromAuthority(SecurityResource securityResource, Authority authority) {
		authority.terminateSecurityResource(securityResource);
	}

	public void terminateSecurityResourcesFromAuthority(List<? extends SecurityResource> securityResources, Authority authority) {
		authority.terminateSecurityResources(securityResources);
	}

	public void terminateAuthoritiesFromSecurityResource(List<Authority> authorities, SecurityResource securityResource) {
		for (Authority authority : authorities) {
			terminateAuthorityFromSecurityResource(authority, securityResource);
		}
	}

	public void terminateAuthorityFromSecurityResource(Authority authority, SecurityResource securityResource) {
		authority.terminateSecurityResource(securityResource);
	}

	public void terminatePermissionFromRole(Permission permission, Role role) {
		role.terminatePermission(permission);
	}

	public void terminatePermissionsFromRole(List<Permission> permissions, Role role) {
		role.terminatePermissions(permissions);
	}

	public void terminateRolesFromPermission(List<Role> roles, Permission permission) {
		for (Role role : roles) {
			terminateRoleFromPermission(role, permission);
		}
	}

	private void terminateRoleFromPermission(Role role, Permission permission) {
		role.terminatePermission(permission);
	}

	public void terminateActorFromAuthority(Actor actor, Authority authority) {
		Authorization authorization = Authorization.findByActorInAuthority(actor, authority);
		authorization.remove();
	}

	public void terminateAuthoritiesFromActor(List<Authority> authorities, Actor actor) {
		for (Authority authority : authorities) {
			this.terminateActorFromAuthority(actor, authority);
		}
	}

	// TODO
	public void updateMenuResources(List<MenuResource> menuResources) {
		// TODO Auto-generated method stub

	}

	public boolean isSecurityResourceEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSecurityResourceNameExist(SecurityResource securityResource) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSecurityResourceIdentifierExist(SecurityResource securityResource) {
		// TODO Auto-generated method stub
		return false;
	}

	public void createScope(Scope scope) {
		scope.save();
	}

	public void grantActorToAuthorityInScope(Actor actor, Authority authority, Scope scope) {
		actor.grant(authority, scope);
	}

	@Override
	public void grantAuthorityToSecurityResource(Authority authority, SecurityResource securityResource) {
		authority.addSecurityResource(securityResource);

	}

	@Override
	public void grantAuthorityToSecurityResources(Authority authority, List<? extends SecurityResource> securityResources) {
		authority.addSecurityResources(securityResources);
	}

	@Override
	public void resetPassword(User user) {
		user.resetPassword();
	}

	@Override
	public void createChildToParent(MenuResource child, Long parentId) {
		MenuResource parent = MenuResource.get(MenuResource.class, parentId);
		parent.addChild(child);
	}

	@Override
	public void updateScope(Scope scope) {
		scope.update();
	}

	@Override
	public void terminateScope(Scope scope) {
		scope.remove();
	}

	@Override
	public void createChildToParent(Scope child, Long parentId) {
		Scope parent = Scope.get(Scope.class, parentId);
		parent.addChild(child);
	}

	@Override
	public void grantSecurityResourcesToAuthority(List<? extends SecurityResource> securityResources, Authority authority) {
		authority.addSecurityResources(securityResources);
	}

	@Override
	public void grantPermissionToRole(Permission permission, Role role) {
		role.addPermission(permission);
	}

	@Override
	public void grantSecurityResourceToAuthority(SecurityResource securityResource, Authority authority) {
		authority.addSecurityResource(securityResource);
	}

	@Override
	public void grantPermissionsToRole(List<Permission> permissions, Role role) {
		role.addPermissions(permissions);
	}

	@Override
	public boolean checkAuthoritiHasPageElementResource(Set<Authority> authorities,
			PageElementResource pageElementResource) {
		return Authority.checkHasPageElementResource(authorities, pageElementResource);
	}

	@Override
	public void updateActor(Actor actor) {
		actor.update();
	}

	@Override
	public void grantAuthorityToActor(Authority authority, Actor actor) {
		actor.grant(authority, null);
	}

	// TODO 其他的URL 其他的页面元素资源
	@Override
	public void initSecuritySystem() {
		if (User.getCount() == 0) {
			User user = initUser();
			Role role = initRole();
			grantAuthorityToActor(role, user);
			List<MenuResource> menuResources = initMenuResource();
			grantSecurityResourcesToAuthority(menuResources, role);
			initUserUrlAccessResource();
		}
	}

	private void initUserUrlAccessResource() {

		UrlAccessResource usersUrlAccessResource = new UrlAccessResource("用户管理", "/auth/user/**");
		UrlAccessResource userLoginUrlAccessResource = new UrlAccessResource("用户管理-登陆", "/auth/user/login");
		UrlAccessResource userLogoutUrlAccessResource = new UrlAccessResource("用户管理-退出", "/auth/user/logout");
		UrlAccessResource userAddUrlAccessResource = new UrlAccessResource("用户管理-添加", "/auth/user/add");
		UrlAccessResource userUpdateUrlAccessResource = new UrlAccessResource("用户管理-更新", "/auth/user/update");
		UrlAccessResource userTerminateUrlAccessResource = new UrlAccessResource("用户管理-撤销", "/auth/user/terminate");
		UrlAccessResource userPagingqueryUrlAccessResource = new UrlAccessResource("用户管理-分页查询", "/auth/user/pagingquery");
		UrlAccessResource userUpdatePasswordUrlAccessResource = new UrlAccessResource("用户管理-更新密码", "/auth/user/updatePassword");
		UrlAccessResource userResetPasswordUrlAccessResource = new UrlAccessResource("用户管理-重置密码", "/auth/user/resetPassword");
		UrlAccessResource userActivateUrlAccessResource = new UrlAccessResource("用户管理-激活", "/auth/user/activate");
		UrlAccessResource userActivatesUrlAccessResource = new UrlAccessResource("用户管理-激动所有", "/auth/user/activates");
		UrlAccessResource userSuspendsUrlAccessResource = new UrlAccessResource("用户管理-挂起所有", "/auth/user/suspends");
		UrlAccessResource userGrantRoleUrlAccessResource = new UrlAccessResource("用户管理-授权一个角色", "/auth/user/grantRole");
		UrlAccessResource userGrantRolesUrlAccessResource = new UrlAccessResource("用户管理-授权多个角色", "/auth/user/grantRoles");
		UrlAccessResource userGrantPermissionUrlAccessResource = new UrlAccessResource("用户管理-授权一个权限",
				"/auth/user/grantPermission");
		UrlAccessResource userGrantPermissionsUrlAccessResource = new UrlAccessResource("用户管理-授权多个权限",
				"/auth/user/grantPermissions");
		UrlAccessResource userTerminateRoleByUserUrlAccessResource = new UrlAccessResource("用户管理-撤销一个角色",
				"/auth/user/terminateRoleByUser");
		UrlAccessResource userTerminatePermissionByUserUrlAccessResource = new UrlAccessResource("用户管理-撤销一个权限",
				"/auth/user/terminatePermissionByUser");
		UrlAccessResource userTerminateRolesByUserUrlAccessResource = new UrlAccessResource("用户管理-撤销多个角色",
				"/auth/user/suspend/terminateRolesByUser");
		UrlAccessResource userTerminatePermissionsByUserUrlAccessResource = new UrlAccessResource("用户管理-撤销多个权限",
				"/auth/user/terminatePermissionsByUser");
		UrlAccessResource userPagingQueryGrantRoleByUserIdUrlAccessResource = new UrlAccessResource("用户管理-查找授权的角色",
				"/auth/user/pagingQueryGrantRoleByUserId");
		UrlAccessResource userPagingQueryGrantPermissionByUserIdUrlAccessResource = new UrlAccessResource(
				"用户管理-查找授权的权限", "/auth/user/pagingQueryGrantPermissionByUserId");
		UrlAccessResource userPagingQueryNotGrantRolesUrlAccessResource = new UrlAccessResource("用户管理-查找没有授权的角色",
				"/auth/user/pagingQueryNotGrantRoles");
		UrlAccessResource userPagingQueryNotGrantPermissionsUrlAccessResource = new UrlAccessResource("用户管理-查找没有授权的权限",
				"/auth/user/pagingQueryNotGrantPermissions");

		createSecurityResource(usersUrlAccessResource);
		createSecurityResource(userLoginUrlAccessResource);
		createSecurityResource(userLogoutUrlAccessResource);
		createSecurityResource(userAddUrlAccessResource);
		createSecurityResource(userUpdateUrlAccessResource);
		createSecurityResource(userTerminateUrlAccessResource);
		createSecurityResource(userPagingqueryUrlAccessResource);
		createSecurityResource(userUpdatePasswordUrlAccessResource);
		createSecurityResource(userResetPasswordUrlAccessResource);
		createSecurityResource(userActivateUrlAccessResource);
		createSecurityResource(userActivatesUrlAccessResource);
		createSecurityResource(userSuspendsUrlAccessResource);
		createSecurityResource(userGrantRoleUrlAccessResource);
		createSecurityResource(userGrantRolesUrlAccessResource);
		createSecurityResource(userGrantPermissionUrlAccessResource);
		createSecurityResource(userGrantPermissionsUrlAccessResource);
		createSecurityResource(userTerminateRoleByUserUrlAccessResource);
		createSecurityResource(userTerminatePermissionByUserUrlAccessResource);
		createSecurityResource(userTerminateRolesByUserUrlAccessResource);
		createSecurityResource(userTerminatePermissionsByUserUrlAccessResource);
		createSecurityResource(userPagingQueryGrantRoleByUserIdUrlAccessResource);
		createSecurityResource(userPagingQueryGrantPermissionByUserIdUrlAccessResource);
		createSecurityResource(userPagingQueryNotGrantRolesUrlAccessResource);
		createSecurityResource(userPagingQueryNotGrantPermissionsUrlAccessResource);
	}

	private List<MenuResource> initMenuResource() {
		String menuIcon = "glyphicon  glyphicon-list-alt";

		MenuResource actorSecurityMenuResource = new MenuResource("参与者管理");
		actorSecurityMenuResource.setDescription("用户、用户组等页面管理。");
		actorSecurityMenuResource.setMenuIcon(menuIcon);
		createSecurityResource(actorSecurityMenuResource);
		
		MenuResource userMenuResource = new MenuResource("用户管理");
		userMenuResource.setMenuIcon(menuIcon);
		userMenuResource.setUrl("/pages/auth/user-list.jsp");
		createChildToParent(userMenuResource, actorSecurityMenuResource.getId());
		
		MenuResource authoritySecurityMenuResource = new MenuResource("授权体管理");
		authoritySecurityMenuResource.setDescription("角色、权限等页面管理。");
		authoritySecurityMenuResource.setMenuIcon(menuIcon);
		createSecurityResource(authoritySecurityMenuResource);
		
		MenuResource roleMenuResource = new MenuResource("角色管理");
		roleMenuResource.setMenuIcon(menuIcon);
		roleMenuResource.setUrl("/pages/auth/role-list.jsp");

		MenuResource permisisonMenuResource = new MenuResource("权限管理");
		permisisonMenuResource.setMenuIcon(menuIcon);
		permisisonMenuResource.setUrl("/pages/auth/permission-list.jsp");
		
		createChildToParent(roleMenuResource, authoritySecurityMenuResource.getId());
		createChildToParent(permisisonMenuResource, authoritySecurityMenuResource.getId());

		MenuResource securityMenuResource = new MenuResource("权限资源管理");
		securityMenuResource.setDescription("角色、权限等页面管理。");
		securityMenuResource.setMenuIcon(menuIcon);
		createSecurityResource(securityMenuResource);
		
		MenuResource menuResource = new MenuResource("菜单资源管理");
		menuResource.setMenuIcon(menuIcon);
		menuResource.setUrl("/pages/auth/menu-list.jsp");

		MenuResource urlAccessResource = new MenuResource("URL访问管理");
		urlAccessResource.setMenuIcon(menuIcon);
		urlAccessResource.setUrl("/pages/auth/url-list.jsp");

		MenuResource pageElementResource = new MenuResource("页面元素管理");
		pageElementResource.setMenuIcon(menuIcon);
		pageElementResource.setUrl("/pages/auth/page-list.jsp");

		createChildToParent(menuResource, securityMenuResource.getId());
		createChildToParent(urlAccessResource, securityMenuResource.getId());
		createChildToParent(pageElementResource, securityMenuResource.getId());

		return Lists.newArrayList(actorSecurityMenuResource, //
				userMenuResource, //
				roleMenuResource, //
				permisisonMenuResource,//
				menuResource, //
				urlAccessResource, //
				pageElementResource);
	}

	private User initUser() {
		User user = new User("zhangsan", "000000", "zhangsan@koala.com", "139*********");
		user.setCreateOwner("admin");
		user.setDescription("普通用户");
		user.setName("张三");
		createActor(user);
		return user;
	}

	private Role initRole() {
		Role role = new Role("superAdmin");
		role.setDescription("超级管理员");
		createAuthority(role);
		return role;
	}

	@Override
	public void updateUserLastLoginTime(User user) {
		user.updateLastLoginTime();
	}

}
