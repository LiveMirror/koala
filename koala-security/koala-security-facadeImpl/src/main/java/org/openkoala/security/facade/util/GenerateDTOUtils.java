package org.openkoala.security.facade.util;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;

/**
 * 生成DTO工具类
 * 
 * @author luzhao
 * 
 */
public final class GenerateDTOUtils {

	/**
	 * User 生成UserDto
	 * 
	 * @param user
	 * @return
	 */
	public static UserDTO generateUserDtoBy(User user) {
		UserDTO result = new UserDTO();
		result.setId(user.getId());
		result.setCreateDate(user.getCreateDate());
		result.setDescription(user.getDescription());
		result.setUserAccount(user.getUserAccount());
		result.setUserPassword(user.getPassword());
		return result;
	}

	public static List<UserDTO> generateUserDtosBy(List<User> users) {
		List<UserDTO> results = new ArrayList<UserDTO>();
		for (User user : users) {
			results.add(generateUserDtoBy(user));
		}
		return results;
	}

	public static List<RoleDTO> generateRoleDtosBy(List<Role> roles) {
		List<RoleDTO> results = new ArrayList<RoleDTO>();
		for (Role role : roles) {
			results.add(generateRoleDtoBy(role));
		}
		return results;
	}

	public static List<PermissionDTO> generatePermissionDtosBy(List<Permission> permissions) {
		List<PermissionDTO> results = new ArrayList<PermissionDTO>();
		for (Permission permission : permissions) {
			results.add(generatePermissionDtoBy(permission));
		}
		return results;
	}

	/**
	 * 生成RoleDto
	 * 
	 * @param role
	 * @return
	 */
	public static RoleDTO generateRoleDtoBy(Role role) {
		RoleDTO roleDTO = new RoleDTO(role.getId(), role.getName());
		return roleDTO;
	}

	/**
	 * 生成PermissionDto
	 * 
	 * @param permission
	 * @return
	 */
	public static PermissionDTO generatePermissionDtoBy(Permission permission) {
		PermissionDTO result = new PermissionDTO();
		result.setRoleName(null);
		result.setPermissionName(permission.getName());
		return result;
	}

	public static MenuResourceDTO generateMenuResourceDtoBy(MenuResource menuResource) {
		MenuResourceDTO result = new MenuResourceDTO();
		MenuResource parentMenuResource = menuResource.getParent();
		result.setId(menuResource.getId());
		result.setName(menuResource.getName());
		result.setUrl(menuResource.getUrl());
		result.setDescription(menuResource.getDescription());
		return result;
	}
}
