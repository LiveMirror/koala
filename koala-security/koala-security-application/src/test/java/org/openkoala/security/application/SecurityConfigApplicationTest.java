package org.openkoala.security.application;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;

@Ignore
public class SecurityConfigApplicationTest extends AbstractApplicationIntegrationTestCase {

	private static final String MENUICON = "glyphicon  glyphicon-list-alt";
	
	@Inject
	private SecurityConfigApplication securityConfigApplication;

	@Inject
	private SecurityAccessApplication securityAccessApplication;
	
	@Test
	public void testPageElementResource() throws Exception {
		PageElementResource addPpageElementResource = new PageElementResource("用户添加","userAdd");
		
		PageElementResource terminatePageElementResource = new PageElementResource("用户删除","userTerminate");
		
		PageElementResource updatePageElementResource = new PageElementResource("用户修改","userUpdate");
		
		PageElementResource listPageElementResource = new PageElementResource("用户列表","userList");
		
		securityConfigApplication.createSecurityResource(addPpageElementResource);
		securityConfigApplication.createSecurityResource(terminatePageElementResource);
		securityConfigApplication.createSecurityResource(updatePageElementResource);
		securityConfigApplication.createSecurityResource(listPageElementResource);
	}
	
	@Test
	public void testAddRole() throws Exception {
		System.out.println("Add Role");
		
		List<String> aa = new ArrayList<String>();
		System.out.println(aa.toString());
		// Role role = new Role("开发经理");
		// securityConfigApplication.createAuthority(role);
		// User user = Actor.get(User.class, 1l);
		// Scope scope = Scope.get(OrganizationScope.class, 1l);
		// securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
	}
	
	@Test
	public void testOneUrlAccessResource() throws Exception {
		UrlAccessResource userUrlAccessResource = new UrlAccessResource("测试管理","/auth/test/**");
		securityConfigApplication.createSecurityResource(userUrlAccessResource);
	}

	@Test
	public void testInitUrlAccessResources() throws Exception {
		UrlAccessResource userUrlAccessResource = new UrlAccessResource("用户管理","/auth/user/**");
		UrlAccessResource permissionUrlAccessResource = new UrlAccessResource("权限管理","/auth/permission/**");
		securityConfigApplication.createSecurityResource(userUrlAccessResource);
		securityConfigApplication.createSecurityResource(permissionUrlAccessResource);
		
		Role adminRole = securityAccessApplication.getRoleBy(2l);
		Role testRole = securityAccessApplication.getRoleBy(1l);
		
		securityConfigApplication.grantSecurityResourceToAuthority(userUrlAccessResource, adminRole);
		securityConfigApplication.grantSecurityResourceToAuthority(permissionUrlAccessResource, adminRole);
		
		securityConfigApplication.grantSecurityResourceToAuthority(userUrlAccessResource, testRole);
		securityConfigApplication.grantSecurityResourceToAuthority(permissionUrlAccessResource, testRole);
		
	}
	
	@Test
	public void testInit() throws Exception {
		initPrivilege();
	}

	private void initPrivilege() {

		// 用户
		User user = initUser();
		User adminUser = adminUser();// 超级管理员
		securityConfigApplication.createActor(user);
		securityConfigApplication.createActor(adminUser);

		// 角色
		Role role = initRole();
		securityConfigApplication.createAuthority(role);

		Role adminRole = adminRole();
		securityConfigApplication.createAuthority(adminRole);

		// 权限
		Permission addPermission = new Permission("菜单添加", "menu:create");
		Permission updatePermission = new Permission("菜单修改", "menu:update");
		Permission deletPermission = new Permission("菜单撤销", "menu:terminate");
		Permission listPermission = new Permission("菜单列表", "menu:list");

		securityConfigApplication.createAuthority(addPermission);
		securityConfigApplication.createAuthority(updatePermission);
		securityConfigApplication.createAuthority(deletPermission);
		securityConfigApplication.createAuthority(listPermission);

		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(addPermission);
		permissions.add(listPermission);

		// 范围
		// OrganizationScope scope = new OrganizationScope("总公司");
		// securityConfigApplication.createScope(scope);
		//
		// OrganizationScope child1=new OrganizationScope("广州分公司");
		// securityConfigApplication.createChildToParent(child1, scope.getId());
		//
		// OrganizationScope child2=new OrganizationScope("深圳分公司");
		// securityConfigApplication.createChildToParent(child2, scope.getId());
		//
		// OrganizationScope child11=new OrganizationScope("工程一部");
		// securityConfigApplication.createChildToParent(child11, child1.getId());
		//
		// OrganizationScope child12=new OrganizationScope("工程二部");
		// securityConfigApplication.createChildToParent(child12, child1.getId());
		//
		// OrganizationScope child13=new OrganizationScope("工程三部");
		// securityConfigApplication.createChildToParent(child13, child1.getId());

		// 授权
		securityConfigApplication.grantRoleToPermissions(role, permissions);

		securityConfigApplication.grantPermissionsToRole(permissions, adminRole);
		securityConfigApplication.grantPermissionToRole(updatePermission, adminRole);
		securityConfigApplication.grantPermissionToRole(deletPermission, adminRole);

//		securityConfigApplication.grantActorToAuthority(adminUser.getId(), adminRole.getId());
//		securityConfigApplication.grantActorToAuthority(user.getId(), role.getId());
//		securityConfigApplication.grantActorToAuthority(user.getId(), deletPermission.getId());

		// securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
		// securityConfigApplication.grantActorToAuthorityInScope(user, deletPermission, scope);

		// 用户管理 user-list
		// 角色管理 role-list
		// 权限管理 permission-list
		// 菜单管理 menu-list
		// 请求管理 url-list
		// 页面元素管理 pageElement-list
		// 方法调用管理 methodInvocation-list
		//
		MenuResource securityMenuResource = new MenuResource("权限管理");
		securityMenuResource.setDescription("所有的权限页面");
		securityMenuResource.setMenuIcon(MENUICON);
		MenuResource userMenuResource = new MenuResource("用户管理");
		userMenuResource.setMenuIcon(MENUICON);
		userMenuResource.setUrl("/pages/auth/user-list.jsp");

		MenuResource roleMenuResource = new MenuResource("角色管理");
		roleMenuResource.setMenuIcon(MENUICON);
		roleMenuResource.setUrl("/pages/auth/role-list.jsp");

		MenuResource permisisonMenuResource = new MenuResource("权限管理");
		permisisonMenuResource.setMenuIcon(MENUICON);
		permisisonMenuResource.setUrl("/pages/auth/permission-list.jsp");

		MenuResource menuResource = new MenuResource("菜单管理");
		menuResource.setMenuIcon(MENUICON);
		menuResource.setUrl("/pages/auth/menu-list.jsp");

		MenuResource urlAccessResource = new MenuResource("URL管理");
		urlAccessResource.setMenuIcon(MENUICON);
		urlAccessResource.setUrl("/pages/auth/url-list.jsp");

		securityConfigApplication.createSecurityResource(securityMenuResource);
		securityConfigApplication.createChildToParent(userMenuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(roleMenuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(permisisonMenuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(menuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(urlAccessResource, securityMenuResource.getId());

		List<MenuResource> resources = new ArrayList<MenuResource>();
		resources.add(securityMenuResource);
		resources.add(userMenuResource);
		resources.add(roleMenuResource);

		securityConfigApplication.grantAuthorityToSecurityResources(role, resources);

		List<MenuResource> resources2 = new ArrayList<MenuResource>();
		resources2.add(menuResource);
		securityConfigApplication.grantAuthorityToSecurityResources(listPermission, resources2);
	}

	private Role initRole() {
		return new Role("行政经理");
	}

	private User initUser() {
		User user = new User("张三", "zhangsan");
		user.setCreateOwner("admin");
		user.setDescription("普通用户");
		return user;
	}

	private User adminUser() {
		User user = new User("李四", "lisi");
		user.setCreateOwner("admin");
		user.setDescription("lisi");
		return user;
	}

	private Role adminRole() {
		Role role = new Role("admin");
		role.setDescription("超级管理员");
		return role;
	}
}
