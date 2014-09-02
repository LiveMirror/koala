package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.impl.util.EntitiesHelper.initRole;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initUser;

import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.*;
import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;

import com.google.common.collect.Lists;

public class MenuResourceTest extends AbstractFacadeIntegrationTestCase{

	@Inject
	private SecurityAccessFacade securityAccessFacade;
	
	@Test
	public void testFindMenuResourceByUserAsRoleHasTop() throws Exception {
		User user = initUser();
		user.save();
		Role role = initRole();
		role.save();
		user.grant(role);
		List<MenuResource> menuResources = initTopMenuResource();
		role.addSecurityResources(menuResources);
		List<MenuResourceDTO> results = (List<MenuResourceDTO>) securityAccessFacade.findMenuResourceByUserAsRole(user.getUserAccount(), role.getName()).getData();
		assertFalse(results.isEmpty());
		assertTrue(results.size() == 3);
	}
	
	private List<MenuResource> initTopMenuResource() {
		String menuIcon = "glyphicon  glyphicon-list-alt";

		MenuResource actorSecurityMenuResource = new MenuResource("参与者管理");
		actorSecurityMenuResource.setDescription("用户、用户组等页面管理。");
		actorSecurityMenuResource.setMenuIcon(menuIcon);
		actorSecurityMenuResource.save();
		
		MenuResource userMenuResource = new MenuResource("用户管理");
		userMenuResource.setMenuIcon(menuIcon);
		userMenuResource.setUrl("/pages/auth/user-list.jsp");
		actorSecurityMenuResource.addChild(userMenuResource);
		
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
		securityMenuResource.setDescription("权限资源等页面管理。");
		securityMenuResource.setMenuIcon(menuIcon);
		securityMenuResource.save();
		
		MenuResource menuResource = new MenuResource("菜单资源管理");
		menuResource.setMenuIcon(menuIcon);
		menuResource.setUrl("/pages/auth/menu-list.jsp");

		MenuResource urlAccessResource = new MenuResource("URL访问管理");
		urlAccessResource.setMenuIcon(menuIcon);
		urlAccessResource.setUrl("/pages/auth/url-list.jsp");

		MenuResource pageElementResource = new MenuResource("页面元素管理");
		pageElementResource.setMenuIcon(menuIcon);
		pageElementResource.setUrl("/pages/auth/page-list.jsp");
		
		securityMenuResource.addChild(menuResource);
		securityMenuResource.addChild(urlAccessResource);
		securityMenuResource.addChild(pageElementResource);

		return Lists.newArrayList(actorSecurityMenuResource, //
				authoritySecurityMenuResource,
				securityMenuResource,
				userMenuResource, //
				roleMenuResource, //
				permisisonMenuResource,//
				menuResource, //
				urlAccessResource, //
				pageElementResource);
	}
}
