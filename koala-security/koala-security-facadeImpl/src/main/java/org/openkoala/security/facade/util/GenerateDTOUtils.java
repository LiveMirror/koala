package org.openkoala.security.facade.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
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
	public static UserDTO generateUserDTOBy(User user) {
		UserDTO result = new UserDTO();
		result.setId(user.getId());
		result.setCreateDate(user.getCreateDate());
		result.setDescription(user.getDescription());
		result.setUserAccount(user.getUserAccount());
		result.setName(user.getName());
		result.setEmail(user.getEmail());
		result.setUserPassword(user.getPassword());
		result.setTelePhone(user.getTelePhone());
		result.setLastLoginTime(user.getLastLoginTime());
		return result;
	}

	public static List<UserDTO> generateUserDTOsBy(List<User> users) {
		List<UserDTO> results = new ArrayList<UserDTO>();
		for (User user : users) {
			results.add(generateUserDTOBy(user));
		}
		return results;
	}

	public static List<RoleDTO> generateRoleDTOsBy(List<Role> roles) {
		List<RoleDTO> results = new ArrayList<RoleDTO>();
		for (Role role : roles) {
			results.add(generateRoleDTOBy(role));
		}
		return results;
	}

	public static List<PermissionDTO> generatePermissionDTOsBy(Collection<Permission> permissions) {
		List<PermissionDTO> results = new ArrayList<PermissionDTO>();
		for (Permission permission : permissions) {
			results.add(generatePermissionDTOBy(permission));
		}
		return results;
	}

	/**
	 * 生成RoleDto
	 * 
	 * @param role
	 * @return
	 */
	public static RoleDTO generateRoleDTOBy(Role role) {
		RoleDTO roleDTO = new RoleDTO(role.getId(), role.getName());
		roleDTO.setDescription(role.getDescription());
		return roleDTO;
	}

	/**
	 * 生成PermissionDto
	 * 
	 * @param permission
	 * @return
	 */
	public static PermissionDTO generatePermissionDTOBy(Permission permission) {
		PermissionDTO result = new PermissionDTO();
		result.setRoleName(null);
		result.setPermissionName(permission.getName());
		result.setIdentifier(permission.getIdentifier());
		result.setDescription(permission.getDescription());
		return result;
	}

	public static MenuResourceDTO generateMenuResourceDTOBy(MenuResource menuResource) {
		MenuResourceDTO result = new MenuResourceDTO();
		result.setId(menuResource.getId());
		result.setName(menuResource.getName());
		result.setUrl(menuResource.getUrl());
		result.setDescription(menuResource.getDescription());
		return result;
	}

	public static UrlAccessResourceDTO generateUrlAccessResourceDTOBy(UrlAccessResource urlAccessResource) {
		UrlAccessResourceDTO result = new UrlAccessResourceDTO(urlAccessResource.getId(), urlAccessResource.getName(), urlAccessResource.isDisabled(), urlAccessResource.getUrl(), urlAccessResource.getIdentifier());
		result.setDescription(urlAccessResource.getDescription());
		return result;
	}
}
