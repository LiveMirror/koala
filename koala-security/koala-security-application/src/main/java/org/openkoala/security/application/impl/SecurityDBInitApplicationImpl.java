package org.openkoala.security.application.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.openkoala.security.application.SecurityDBInitApplication;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Named
@Transactional
public class SecurityDBInitApplicationImpl implements SecurityDBInitApplication {

	@Override
	public void initSecuritySystem() {
		if (User.getCount() == 0) {
			User user = initUser();
			User user1 = new User("李四", "lisi");
			user1.save();
			
			Role role1 = new Role("test");
			role1.save();
			user1.grant(role1);
			List<MenuResource> menuResources = initMenuResources();
			role1.addSecurityResources(menuResources);
			
			user.changeEmail("zhangsan@koala.com", "888888");
			user.changeTelePhone("18665589188", "888888");
			Role role = initRole();
			user.grant(role);
			

			role.addSecurityResources(menuResources);
			role.addSecurityResources(initUrlAccessResources());
			role.addSecurityResources(initPageElementResources());
		}
	}

	public User initUser() {
		User result = createUser();
		result.save();
		return result;
	}

	public Role initRole() {
		Role result = createRole();
		result.save();
		return result;
	}

	// TODO 初始化permission
	public void initPermissions() {

	}

	public List<MenuResource> initMenuResources() {
		List<MenuResource> results = createMenuResource();
		return results;
	}

	public List<UrlAccessResource> initUrlAccessResources() {
		List<UrlAccessResource> results = createUserUrlAccessResources();
		return results;
	}

	public List<PageElementResource> initPageElementResources() {
		List<PageElementResource> userResults = createPageElementResourcesOfUser();
		List<PageElementResource> roleResults = createPageElementResourcesOfRole();
		List<PageElementResource> permissonResults = createPageElementResourcesOfPermission();
		List<PageElementResource> menuResourceResults = createPageElementResourcesOfMenuResource();
		List<PageElementResource> urlAccessResourceResults = createPageElementResourcesOfUrlAccessResource();
		List<PageElementResource> pageElementResourceResults = createPageElementResourcesOfPageElementResource();

		List<PageElementResource> results = new ArrayList<PageElementResource>();
		results.addAll(userResults);
		results.addAll(roleResults);
		results.addAll(permissonResults);
		results.addAll(menuResourceResults);
		results.addAll(urlAccessResourceResults);
		results.addAll(pageElementResourceResults);
		return results;
	}

	private List<PageElementResource> createPageElementResourcesOfPageElementResource() {
		PageElementResource pageElementResourceManagerAdd = new PageElementResource("页面元素资源管理-添加",
				"pageElementResourceManagerAdd");
		pageElementResourceManagerAdd.save();

		PageElementResource pageElementResourceManagerUpdate = new PageElementResource("页面元素资源管理-修改",
				"pageElementResourceManagerUpdate");
		pageElementResourceManagerUpdate.save();

		PageElementResource pageElementResourceManagerTerminate = new PageElementResource("页面元素资源管理-撤销",
				"pageElementResourceManagerTerminate");
		pageElementResourceManagerTerminate.save();

		PageElementResource pageElementResourceManagerGrantPermission = new PageElementResource("页面元素资源管理-授权权限",
				"pageElementResourceManagerGrantPermission");
		pageElementResourceManagerGrantPermission.save();

		return Lists.newArrayList(//
				pageElementResourceManagerAdd,//
				pageElementResourceManagerUpdate,//
				pageElementResourceManagerTerminate,//
				pageElementResourceManagerGrantPermission);
	}

	private List<PageElementResource> createPageElementResourcesOfUrlAccessResource() {
		PageElementResource urlAccessResourceManagerAdd = new PageElementResource("路径访问资源管理-添加",
				"urlAccessResourceManagerAdd");
		urlAccessResourceManagerAdd.save();

		PageElementResource urlAccessResourceManagerUpdate = new PageElementResource("路径访问资源管理-修改",
				"urlAccessResourceManagerUpdate");
		urlAccessResourceManagerUpdate.save();

		PageElementResource urlAccessResourceManagerTerminate = new PageElementResource("路径访问资源管理-撤销",
				"urlAccessResourceManagerTerminate");
		urlAccessResourceManagerTerminate.save();

		PageElementResource urlAccessResourceManagerGrantPermission = new PageElementResource("路径访问资源管理-授权权限",
				"urlAccessResourceManagerGrantPermission");
		urlAccessResourceManagerGrantPermission.save();

		return Lists.newArrayList(//
				urlAccessResourceManagerAdd,//
				urlAccessResourceManagerUpdate,//
				urlAccessResourceManagerTerminate,//
				urlAccessResourceManagerGrantPermission);
	}

	private List<PageElementResource> createPageElementResourcesOfMenuResource() {
		PageElementResource menuResourceManagerAdd = new PageElementResource("菜单资源管理-添加", "menuResourceManagerAdd");
		menuResourceManagerAdd.save();

		PageElementResource menuResourceManagerUpdate = new PageElementResource("菜单资源管理-修改",
				"menuResourceManagerUpdate");
		menuResourceManagerUpdate.save();

		PageElementResource menuResourceManagerTerminate = new PageElementResource("菜单资源管理-撤销",
				"menuResourceManagerTerminate");
		menuResourceManagerTerminate.save();

		PageElementResource menuResourceManagerGrantPermission = new PageElementResource("菜单资源管理-授权权限",
				"menuResourceManagerGrantPermission");
		menuResourceManagerGrantPermission.save();

		return Lists.newArrayList(//
				menuResourceManagerAdd,//
				menuResourceManagerUpdate,//
				menuResourceManagerTerminate,//
				menuResourceManagerGrantPermission);
	}

	private List<PageElementResource> createPageElementResourcesOfPermission() {
		PageElementResource permissionManagerAdd = new PageElementResource("权限管理-添加", "permissionManagerAdd");
		permissionManagerAdd.save();

		PageElementResource permissionManagerUpdate = new PageElementResource("权限管理-修改", "permissionManagerUpdate");
		permissionManagerUpdate.save();

		PageElementResource permissionManagerTerminate = new PageElementResource("权限管理-撤销",
				"permissionManagerTerminate");
		permissionManagerTerminate.save();

		return Lists.newArrayList(//
				permissionManagerAdd,//
				permissionManagerUpdate,//
				permissionManagerTerminate);
	}

	private List<PageElementResource> createPageElementResourcesOfRole() {
		PageElementResource roleManagerAdd = new PageElementResource("角色管理-添加", "roleManagerAdd");
		roleManagerAdd.save();

		PageElementResource roleManagerUpdate = new PageElementResource("角色管理-修改", "roleManagerUpdate");
		roleManagerUpdate.save();

		PageElementResource roleManagerTerminate = new PageElementResource("角色管理-撤销", "roleManagerTerminate");
		roleManagerTerminate.save();

		PageElementResource roleManagerGrantUrlAccessResource = new PageElementResource("角色管理-分配路径访问权限资源",
				"roleManagerGrantUrlAccessResource");
		roleManagerGrantUrlAccessResource.save();

		PageElementResource roleManagerGrantMenuResource = new PageElementResource("角色管理-分配菜单权限资源",
				"roleManagerGrantMenuResource");
		roleManagerGrantMenuResource.save();

		PageElementResource roleManagerPageElementResource = new PageElementResource("角色管理-分配页面元素权限资源",
				"roleManagerGrantPageElementResource");
		roleManagerPageElementResource.save();

		PageElementResource roleManagerPagePermission = new PageElementResource("角色管理-分配权限",
				"roleManagerGrantPermission");
		roleManagerPagePermission.save();

		return Lists.newArrayList(//
				roleManagerAdd,//
				roleManagerUpdate,//
				roleManagerTerminate,//
				roleManagerGrantUrlAccessResource,//
				roleManagerGrantMenuResource,//
				roleManagerPageElementResource,//
				roleManagerPagePermission);
	}

	private List<PageElementResource> createPageElementResourcesOfUser() {
		PageElementResource userManagerAdd = new PageElementResource("用户管理-添加", "userManagerAdd");
		userManagerAdd.save();

		PageElementResource userManagerUpdate = new PageElementResource("用户管理-修改", "userManagerUpdate");
		userManagerUpdate.save();

		PageElementResource userManagerTerminate = new PageElementResource("用户管理-撤销", "userManagerTerminate");
		userManagerTerminate.save();

		PageElementResource userManagerGrantRole = new PageElementResource("用户管理-分配角色", "userManagerGrantRole");
		userManagerGrantRole.save();

		PageElementResource userManagerGrantPermission = new PageElementResource("用户管理-分配权限",
				"userManagerGrantPermission");
		userManagerGrantPermission.save();

		PageElementResource userManagerResetPassword = new PageElementResource("用户管理-重置密码", "userManagerResetPassword");
		userManagerResetPassword.save();

		PageElementResource userManagerSuspend = new PageElementResource("用户管理-挂起", "userManagerSuspend");
		userManagerSuspend.save();

		PageElementResource userManagerActivate = new PageElementResource("用户管理-激活", "userManagerActivate");
		userManagerActivate.save();

		return Lists.newArrayList(//
				userManagerAdd,//
				userManagerUpdate,//
				userManagerTerminate,//
				userManagerGrantRole,//
				userManagerGrantPermission,//
				userManagerResetPassword,//
				userManagerSuspend,//
				userManagerActivate//
				);
	}

	private User createUser() {
		User user = new User("张三", "zhangsan");
		user.setCreateOwner("admin");
		user.setDescription("普通用户");
		return user;
	}

	private Role createRole() {
		Role role = new Role("superAdmin");
		role.setDescription("超级管理员");
		return role;
	}

	private List<MenuResource> createMenuResource() {
		String menuIcon = "glyphicon  glyphicon-list-alt";

		MenuResource actorSecurityMenuResource = new MenuResource("参与者管理");
		actorSecurityMenuResource.setDescription("用户、用户组等页面管理。");
		actorSecurityMenuResource.setMenuIcon(menuIcon);
		actorSecurityMenuResource.save();

		MenuResource userMenuResource = new MenuResource("用户管理");
		userMenuResource.setMenuIcon(menuIcon);
		userMenuResource.setUrl("/pages/auth/user-list.jsp");
		actorSecurityMenuResource.addChild(userMenuResource);

		MenuResource userDisabledMenuResource = new MenuResource("用户挂起管理");
		userDisabledMenuResource.setMenuIcon(menuIcon);
		userDisabledMenuResource.setUrl("/pages/auth/forbidden-list.jsp");
		actorSecurityMenuResource.addChild(userDisabledMenuResource);

		MenuResource authoritySecurityMenuResource = new MenuResource("授权体管理");
		authoritySecurityMenuResource.setDescription("角色、权限等页面管理。");
		authoritySecurityMenuResource.setMenuIcon(menuIcon);
		authoritySecurityMenuResource.save();

		MenuResource roleMenuResource = new MenuResource("角色管理");
		roleMenuResource.setMenuIcon(menuIcon);
		roleMenuResource.setUrl("/pages/auth/role-list.jsp");
		authoritySecurityMenuResource.addChild(roleMenuResource);

		MenuResource permisisonMenuResource = new MenuResource("权限管理");
		permisisonMenuResource.setMenuIcon(menuIcon);
		permisisonMenuResource.setUrl("/pages/auth/permission-list.jsp");
		authoritySecurityMenuResource.addChild(permisisonMenuResource);

		MenuResource securityMenuResource = new MenuResource("权限资源管理");
		securityMenuResource.setDescription("角色、权限等页面管理。");
		securityMenuResource.setMenuIcon(menuIcon);
		securityMenuResource.save();

		MenuResource menuResource = new MenuResource("菜单管理");
		menuResource.setMenuIcon(menuIcon);
		menuResource.setUrl("/pages/auth/menu-list.jsp");
		securityMenuResource.addChild(menuResource);

		MenuResource urlAccessResource = new MenuResource("URL访问管理");
		urlAccessResource.setMenuIcon(menuIcon);
		urlAccessResource.setUrl("/pages/auth/url-list.jsp");
		securityMenuResource.addChild(urlAccessResource);

		MenuResource pageElementResource = new MenuResource("页面元素管理");
		pageElementResource.setMenuIcon(menuIcon);
		pageElementResource.setUrl("/pages/auth/page-list.jsp");
		securityMenuResource.addChild(pageElementResource);

		return Lists.newArrayList(actorSecurityMenuResource, //
				authoritySecurityMenuResource,//
				securityMenuResource,//
				userMenuResource, //
				roleMenuResource, //
				permisisonMenuResource,//
				menuResource, //
				urlAccessResource, //
				pageElementResource);
	}

	private List<UrlAccessResource> createUserUrlAccessResources() {
		UrlAccessResource usersUrl = new UrlAccessResource("用户管理", "/auth/user/**");
		UrlAccessResource usersUrlList = new UrlAccessResource("用户管理", "/pages/auth/user-list.jsp");
		UrlAccessResource userAddUrl = new UrlAccessResource("用户管理-添加", "/auth/user/add**");
		UrlAccessResource userUpdateUrl = new UrlAccessResource("用户管理-更新", "/auth/user/update");
		UrlAccessResource userTerminateUrl = new UrlAccessResource("用户管理-撤销", "/auth/user/terminate");
		UrlAccessResource userPagingqueryUrl = new UrlAccessResource("用户管理-分页查询", "/auth/user/pagingQuery**");
		UrlAccessResource userUpdatePasswordUrl = new UrlAccessResource("用户管理-更新密码", "/auth/user/updatePassword");
		UrlAccessResource userResetPasswordUrl = new UrlAccessResource("用户管理-重置密码", "/auth/user/resetPassword");
		UrlAccessResource userActivateUrl = new UrlAccessResource("用户管理-激活", "/auth/user/activate");
		UrlAccessResource userActivatesUrl = new UrlAccessResource("用户管理-激动所有", "/auth/user/activates");
		UrlAccessResource userSuspendsUrl = new UrlAccessResource("用户管理-挂起所有", "/auth/user/suspends");
		UrlAccessResource userGrantRoleUrl = new UrlAccessResource("用户管理-授权一个角色", "/auth/user/grantRole");
		UrlAccessResource userGrantRolesUrl = new UrlAccessResource("用户管理-授权多个角色", "/auth/user/grantRoles");
		UrlAccessResource userGrantPermissionUrl = new UrlAccessResource("用户管理-授权一个权限", "/auth/user/grantPermission");
		UrlAccessResource userGrantPermissionsUrl = new UrlAccessResource("用户管理-授权多个权限", "/auth/user/grantPermissions");
		UrlAccessResource userTerminateRoleByUserUrl = new UrlAccessResource("用户管理-撤销一个角色",
				"/auth/user/terminateRoleByUser");
		UrlAccessResource userTerminatePermissionByUserUrl = new UrlAccessResource("用户管理-撤销一个权限",
				"/auth/user/terminatePermissionByUser");
		UrlAccessResource userTerminateRolesByUserUrl = new UrlAccessResource("用户管理-撤销多个角色",
				"/auth/user/suspend/terminateRolesByUser");
		UrlAccessResource userTerminatePermissionsByUser = new UrlAccessResource("用户管理-撤销多个权限",
				"/auth/user/terminatePermissionsByUser");
		UrlAccessResource userPagingQueryGrantRoleByUserIdUrl = new UrlAccessResource("用户管理-查找授权的角色",
				"/auth/user/pagingQueryGrantRoleByUserId");
		UrlAccessResource userPagingQueryGrantPermissionByUserIdUrl = new UrlAccessResource("用户管理-查找授权的权限",
				"/auth/user/pagingQueryGrantPermissionByUserId");
		UrlAccessResource userPagingQueryNotGrantRolesUrl = new UrlAccessResource("用户管理-查找没有授权的角色",
				"/auth/user/pagingQueryNotGrantRoles");
		UrlAccessResource userPagingQueryNotGrantPermissionsUrl = new UrlAccessResource("用户管理-查找没有授权的权限",
				"/auth/user/pagingQueryNotGrantPermissions");
		usersUrl.save();
		usersUrlList.save();
		userAddUrl.save();
		userUpdateUrl.save();
		userTerminateUrl.save();
		userPagingqueryUrl.save();
		userUpdatePasswordUrl.save();
		userResetPasswordUrl.save();
		userActivateUrl.save();
		userActivatesUrl.save();
		userSuspendsUrl.save();
		userGrantRoleUrl.save();
		userGrantRolesUrl.save();
		userGrantPermissionUrl.save();
		userGrantPermissionsUrl.save();
		userTerminateRoleByUserUrl.save();
		userTerminatePermissionByUserUrl.save();
		userTerminateRolesByUserUrl.save();
		userTerminatePermissionsByUser.save();
		userPagingQueryGrantRoleByUserIdUrl.save();
		userPagingQueryGrantPermissionByUserIdUrl.save();
		userPagingQueryNotGrantRolesUrl.save();
		userPagingQueryNotGrantPermissionsUrl.save();

		return Lists.newArrayList(usersUrl,//
				userAddUrl,//
				usersUrlList,//
				userUpdateUrl,//
				userTerminateUrl,//
				userPagingqueryUrl,//
				userUpdatePasswordUrl,//
				userTerminateUrl,//
				userPagingqueryUrl,//
				userResetPasswordUrl,//
				userGrantRolesUrl,//
				userActivateUrl,//
				userActivatesUrl,//
				userSuspendsUrl,//
				userGrantRoleUrl,//
				userGrantPermissionUrl,//
				userTerminatePermissionByUserUrl,//
				userPagingQueryGrantRoleByUserIdUrl,//
				userPagingQueryGrantPermissionByUserIdUrl,//
				userPagingQueryNotGrantRolesUrl,//
				userPagingQueryNotGrantPermissionsUrl);
	}

}
