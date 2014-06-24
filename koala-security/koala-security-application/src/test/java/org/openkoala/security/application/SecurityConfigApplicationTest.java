package org.openkoala.security.application;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.OrganizationScope;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;

public class SecurityConfigApplicationTest extends AbstractSecurityIntegrationTestCase {

	private static final String MENUICON = "glyphicon  glyphicon-list-alt";
	@Inject
	private SecurityConfigApplication securityConfigApplication;

	@Test
	public void testAddRole() throws Exception {
		System.out.println("Add Role");
		// Role role = new Role("开发经理");
		// securityConfigApplication.createAuthority(role);
		// User user = Actor.get(User.class, 1l);
		// Scope scope = Scope.get(OrganizationScope.class, 1l);
		// securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
	}

	@Test
	public void testInit() throws Exception {
		initPrivilege();
	}

	private void initPrivilege() {
		
		// 用户
		User user = initUser();
		User adminUser = adminUser();//超级管理员
		securityConfigApplication.createActor(user);
		securityConfigApplication.createActor(adminUser);
		
		// 角色
		Role role = initRole();
		securityConfigApplication.createAuthority(role);

		// 权限
		Permission addPermission = new Permission("菜单添加","menu:create");
		Permission updatePermission = new Permission("菜单修改","menu:update");
		Permission deletPermission = new Permission("菜单撤销","menu:terminate");
		Permission listPermission = new Permission("菜单列表","menu:list");

		securityConfigApplication.createAuthority(addPermission);
		securityConfigApplication.createAuthority(updatePermission);
		securityConfigApplication.createAuthority(deletPermission);
		securityConfigApplication.createAuthority(listPermission);

		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(addPermission);
		permissions.add(listPermission);

		// 范围
		OrganizationScope scope = new OrganizationScope("总公司");
		securityConfigApplication.createScope(scope);
		
		OrganizationScope child1=new OrganizationScope("广州分公司");
		securityConfigApplication.createChildToParent(child1, scope.getId());
		
		OrganizationScope child2=new OrganizationScope("深圳分公司");
		securityConfigApplication.createChildToParent(child2, scope.getId());
		
		OrganizationScope child11=new OrganizationScope("工程一部");
		securityConfigApplication.createChildToParent(child11, child1.getId());
		
		OrganizationScope child12=new OrganizationScope("工程二部");
		securityConfigApplication.createChildToParent(child12, child1.getId());
		
		OrganizationScope child13=new OrganizationScope("工程三部");
		securityConfigApplication.createChildToParent(child13, child1.getId());

		// 授权
		securityConfigApplication.grantRoleToPermissions(role, permissions);
		securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
		securityConfigApplication.grantActorToAuthorityInScope(user, deletPermission, scope);

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
		permisisonMenuResource.setUrl("/pages/auth/menu-list.jsp");
		
		securityConfigApplication.createSecurityResource(securityMenuResource);
		securityConfigApplication.createChildToParent(userMenuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(roleMenuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(permisisonMenuResource, securityMenuResource.getId());
		securityConfigApplication.createChildToParent(menuResource, securityMenuResource.getId());

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
		User user = new User("zhangsan", "000000", "zhangsan@koala.com", "139*********");
		user.setCreateOwner("admin");
		user.setDescription("普通用户");
		user.setName("张三");
		user.setSuper(Boolean.FALSE);
		return user;
	}

	private User adminUser() {
		User user = new User("admin", "000000", "admin@koala.com", "193*********");
		user.setCreateOwner("admin");
		user.setDescription("超级管理员,拥有所有的权限");
		user.setName("超级管理员");
		user.setSuper(Boolean.TRUE);
		return user;
	}
}
