package org.openkoala.security.core.util;

import static org.junit.Assert.*;

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

public final class EntitiesHelper {

	public static void assertUser(User expected, User actual) {
		assertEquals(expected.getCreateOwner(), actual.getCreateOwner());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getTelePhone(), actual.getTelePhone());
		assertEquals(expected.getUserAccount(), actual.getUserAccount());
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
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getPosition(), actual.getPosition());
	}

	public static void assertPageElementResource(PageElementResource expected, PageElementResource actual) {
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getName(), actual.getName());
	}
	
	public static void assertUrlAccessResource(UrlAccessResource expected, UrlAccessResource actual){
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getUrl(), actual.getUrl());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

	public static User initUser() {
		User result = new User("测试000000000000000001", "test000000000000000001");
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
		return new PageElementResource("用户添加0000000000","userAdd");
	}

	public static UrlAccessResource initUrlAccessResource() {
		return new UrlAccessResource("测试管理0000000000", "/auth/test/**********");
	}

    public static Authorization initAuthorization(Actor actor, Authority authority) {
        return new Authorization(actor, authority);
    }

    public static Authorization initAuthorization(Actor actor, Authority authority, Scope scope) {
        return new Authorization(actor, authority, scope);
    }
}
