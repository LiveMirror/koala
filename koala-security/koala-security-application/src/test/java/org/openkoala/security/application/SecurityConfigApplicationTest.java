package org.openkoala.security.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.OrganizationScope;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.core.domain.UserStatus;

public class SecurityConfigApplicationTest extends AbstractSecurityIntegrationTestCase {

	private static final String MENUICON = "glyphicon  glyphicon-list-alt";
	@Inject
	private SecurityConfigApplication securityConfigApplication;

	@Test
	public void testAddRole() throws Exception {
		Role role = new Role("开发经理", "主导项目开发",Boolean.FALSE);
		securityConfigApplication.createAuthority(role);
		User user = Actor.get(User.class, 1l);
		Scope scope = Scope.get(OrganizationScope.class, 1l);
		securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
	}
	
//	@Test
	public void testInit() throws Exception {
		// 用户
		User user = initUser();
		securityConfigApplication.createActor(user);

		// 角色
		Role role = initRole();
		role.setMaster(Boolean.TRUE);
		securityConfigApplication.createAuthority(role);

		// 权限
		Permission addPermission = new Permission("菜单添加", "菜单添加权限");
		Permission updatePermission = new Permission("菜单修改", "菜单修改权限");
		Permission deletPermission = new Permission("菜单删除", "菜单删除权限");
		Permission listPermission = new Permission("菜单列表", "菜单列表权限");

		securityConfigApplication.createAuthority(addPermission);
		securityConfigApplication.createAuthority(updatePermission);
		securityConfigApplication.createAuthority(deletPermission);
		securityConfigApplication.createAuthority(listPermission);

		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(addPermission);
		permissions.add(listPermission);

		// 范围
		OrganizationScope scope = new OrganizationScope();
		scope.setName("总公司");
		scope.save();

		// 授权
		securityConfigApplication.grantRoleToPermissions(role, permissions);
		securityConfigApplication.grantActorToAuthorityInScope(user, role, scope);
		securityConfigApplication.grantActorToAuthorityInScope(user, deletPermission, scope);

		// 用户管理 user-list
		// 角色管理 role-list
		// 权限管理 permission-list
		// 菜单管理 menu-list
		
		MenuResource securityMenuResource = new MenuResource("权限管理",Boolean.TRUE, "所有的权限页面", MENUICON,
				"");
		MenuResource userMenuResource = new MenuResource("用户管理", Boolean.TRUE, "用户管理", MENUICON,
				"/pages/auth/user-list.jsp");
		MenuResource roleMenuResource = new MenuResource("角色管理",Boolean.TRUE, "角色管理", MENUICON,
				"/pages/auth/role-list.jsp");
		MenuResource permisisonMenuResource = new MenuResource("权限管理",  Boolean.TRUE, "权限管理",
				MENUICON, "/pages/auth/permission-list.jsp");
		MenuResource menuResource = new MenuResource("菜单管理", Boolean.TRUE, "菜单管理", MENUICON,
				"/pages/auth/menu-list.jsp");
		
		securityConfigApplication.createSecurityResource(securityMenuResource);
		securityConfigApplication.createSecurityResource(userMenuResource);
		securityConfigApplication.createSecurityResource(roleMenuResource);
		securityConfigApplication.createSecurityResource(permisisonMenuResource);
		securityConfigApplication.createSecurityResource(menuResource);

		Set<MenuResource> childrenResources = new HashSet<MenuResource>();
		childrenResources.add(userMenuResource);
		childrenResources.add(roleMenuResource);
		childrenResources.add(permisisonMenuResource);
		childrenResources.add(menuResource);
		
		securityConfigApplication.createMenuResourceUnderParent(childrenResources, securityMenuResource);
		
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
		return new Role("行政经理", "管理总公司行政部门的经理",Boolean.TRUE);
	}

	private User initUser() {
		User user = new User("zhangsan","zhangsan","zhangsan@koala.com","139*********");
		user.setCreateDate(new Date());
		user.setCreateOwner("admin");
		user.setDescription("普通用户");
		user.setName("张三");
		user.setSuper(Boolean.TRUE);
		user.setUserStatus(UserStatus.ACTIVATE);
		return user;
	}
}
