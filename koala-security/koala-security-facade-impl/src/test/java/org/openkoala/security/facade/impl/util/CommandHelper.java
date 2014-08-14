package org.openkoala.security.facade.impl.util;

import static org.junit.Assert.assertEquals;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.command.CreateMenuResourceCommand;
import org.openkoala.security.facade.command.CreatePageElementResourceCommand;
import org.openkoala.security.facade.command.CreatePermissionCommand;
import org.openkoala.security.facade.command.CreateRoleCommand;
import org.openkoala.security.facade.command.CreateUrlAccessResourceCommand;
import org.openkoala.security.facade.command.CreateUserCommand;

public class CommandHelper {

	public static void assertEqualsMenuResourceAndCommand(CreateMenuResourceCommand command, MenuResource menuResource) {
		assertEquals(command.getName(), menuResource.getName());
		assertEquals(command.getUrl(), menuResource.getUrl());
		assertEquals(command.getMenuIcon(), menuResource.getMenuIcon());
		assertEquals(command.getDescription(), menuResource.getDescription());
	}
	
	public static void assertEqualsPageElementResourceAndCommand(CreatePageElementResourceCommand command, PageElementResource pageElementResource) {
		assertEquals(command.getName(), pageElementResource.getName());
		assertEquals(command.getIdentifier(), pageElementResource.getIdentifier());
		assertEquals(command.getDescription(), pageElementResource.getDescription());
	}
	
	public static void assertEqualsUrlAccessResourceAndCommand(CreateUrlAccessResourceCommand command, UrlAccessResource urlAccessResource) {
		assertEquals(command.getName(), urlAccessResource.getName());
		assertEquals(command.getUrl(), urlAccessResource.getUrl());
		assertEquals(command.getDescription(), urlAccessResource.getDescription());
	}
	
	public static void assertEqualsPermissionAndCommand(CreatePermissionCommand command, Permission permission) {
		assertEquals(command.getName(), permission.getName());
		assertEquals(command.getIdentifier(), permission.getIdentifier());
		assertEquals(command.getDescription(), permission.getDescription());
	}
	
	public static void assertEqualsRoleAndCommand(CreateRoleCommand command, Role role) {
		assertEquals(command.getName(), role.getName());
		assertEquals(command.getDescription(), role.getDescription());
	}
	
	public static void assertEqualsUserAndCommand(CreateUserCommand command, User user) {
		assertEquals(command.getName(), user.getName());
		assertEquals(command.getUserAccount(), user.getUserAccount());
		assertEquals(command.getCreateOwner(), user.getCreateOwner());
		assertEquals(command.getDescription(), user.getDescription());
	}
	

	public static CreateMenuResourceCommand initCreateMenuResourceCommand() {
		CreateMenuResourceCommand result = new CreateMenuResourceCommand();
		result.setName("command name");
		result.setMenuIcon("command menu icon");
		result.setUrl("command url");
		result.setDescription("command description");
		return result;
	}
	
	public static CreateUrlAccessResourceCommand initCreateUrlAccessResourceCommand() {
		CreateUrlAccessResourceCommand result = new CreateUrlAccessResourceCommand();
		result.setName("command name");
		result.setUrl("command url");
		result.setDescription("command description");
		return result;
	}
	
	public static CreatePageElementResourceCommand initCreatePageElementResourceCommand() {
		CreatePageElementResourceCommand result = new CreatePageElementResourceCommand();
		result.setName("command name");
		result.setIdentifier("command identifier");
		result.setDescription("command description");
		return result;
	}
	
	public static CreatePermissionCommand initCreatePermissionCommand() {
		CreatePermissionCommand result = new CreatePermissionCommand();
		result.setName("command name");
		result.setIdentifier("command identifier");
		result.setDescription("command description");
		return result;
	}
	
	public static CreateRoleCommand initCreateRoleCommand() {
		CreateRoleCommand result = new CreateRoleCommand();
		result.setName("command name");
		result.setDescription("command description");
		return result;
	}
	
	public static CreateUserCommand initCreateUserCommand() {
		CreateUserCommand result = new CreateUserCommand();
		result.setUserAccount("test01");
		result.setName("测试01");
		result.setDescription("集成测试01");
		result.setCreateOwner("superAdmin");
		return result;
	}
}
