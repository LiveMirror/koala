package org.openkoala.security.facade.impl.util;


import static org.junit.Assert.*;

import java.util.Date;

import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.dto.UserDTO;

public final class EntitiesHelper {

	public static void assertUserDTO(UserDTO expected, UserDTO actual) {
		assertEquals(expected.getCreateOwner(), actual.getCreateOwner());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getTelePhone(), actual.getTelePhone());
		assertEquals(expected.getUserAccount(), actual.getUserAccount());
		assertEquals(expected.getLastLoginTime(), actual.getLastLoginTime());
		assertEquals(expected.getLastModifyTime(), actual.getLastModifyTime());
	}

	public static void assertRole(Role expected, Role actual) {
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

	public static void assertPermission(Permission expected, Permission actual) {
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

	public static void assertMenuResource(MenuResource expected, MenuResource actual) {
		assertEquals(expected.getLevel(), actual.getLevel());
		assertEquals(expected.getMenuIcon(), actual.getMenuIcon());
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getPosition(), actual.getPosition());
	}

	public static void assertPageElementResource(PageElementResource expected, PageElementResource actual) {
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getPageElementType(), actual.getPageElementType());
		assertEquals(expected.getUrl(), actual.getUrl());
		assertEquals(expected.isDisabled(), actual.isDisabled());
	}
	
	public static void assertUrlAccessResource(UrlAccessResource expected, UrlAccessResource actual){
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getUrl(), actual.getUrl());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

	public static User initUser() {
		User result = new User("test000000000000000001", "aaa", "test01@foreveross.com", "18665588990");
		result.setName("测试01");
		result.setCreateOwner("admin");
		result.setDescription("测试");
		return result;
	}

	public static Role initRole() {
		Role result = new Role("testRole0000000000");
		result.setDescription("用于测试角色");
		return result;
	}

	public static Permission initPermission() {
		Permission result = new Permission("测试权限000001", "testPermission000001");
		result.setDescription("用于测试权限");
		return result;
	}

	public static MenuResource initMenuResource() {
		MenuResource result = new MenuResource("用户管理00000000000");
		result.setMenuIcon("glyphicon  glyphicon-list-alt");
		result.setUrl("/pages/auth/user-list.jsp");
		return result;
	}

	public static PageElementResource initPageElementResource() {
		PageElementResource result = new PageElementResource("用户添加0000000000");
		result.setPageElementType("按钮");
		result.setIdentifier("userAdd");
		return result;
	}

	public static UrlAccessResource initUrlAccessResource() {
		UrlAccessResource result = new UrlAccessResource("测试管理0000000000", "/auth/test/**********");
		return result;
	}

	public static Authorization initAuthorization(Actor actor, Authority authority) {
		Authorization result = new Authorization(actor, authority);
		return result;
	}

	public static Authorization initAuthorization(Actor actor, Authority authority, Scope scope) {
		Authorization result = new Authorization(actor, authority, scope);
		return result;
	}
}
