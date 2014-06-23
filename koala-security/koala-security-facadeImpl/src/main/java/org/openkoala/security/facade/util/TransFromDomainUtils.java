package org.openkoala.security.facade.util;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.OrganizationScope;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;

/**
 * 转换领域工具类
 * 
 * @author luzhao
 * 
 */
public final class TransFromDomainUtils {

	/**
	 * UserDto转换成User
	 * 
	 * @param userDTO
	 * @return
	 */
	public static User transFromUserBy(UserDTO userDTO) {
		User result = new User(userDTO.getUserAccount(), userDTO.getUserPassword(), userDTO.getEmail(),
				userDTO.getTelePhone());
		result.setId(userDTO.getId());
		result.setName(userDTO.getName());
		result.setDescription(userDTO.getDescription());
		return result;
	}

	public static Role transFromRoleBy(RoleDTO roleDTO) {
		Role result = new Role(roleDTO.getRoleName());
		result.setId(roleDTO.getRoleId());
		result.setDescription(roleDTO.getDescription());
		return result;
	}

	public static Permission transFromPermissionBy(PermissionDTO permissionDTO) {
		Permission result = new Permission(permissionDTO.getPermissionName());
		result.setDescription(permissionDTO.getPermissionName());
		return result;
	}

	public static MenuResource transFromMenuResourceBy(MenuResourceDTO menuResourceDTO) {
		MenuResource results = new MenuResource(menuResourceDTO.getName());
		results.setDescription(menuResourceDTO.getDescription());
		results.setMenuIcon(menuResourceDTO.getIcon());
		results.setUrl(menuResourceDTO.getUrl());
		return results;
	}

	public static List<MenuResource> transFromMenuResourcesBy(List<MenuResourceDTO> menuResourceDTOs) {
		List<MenuResource> results = new ArrayList<MenuResource>();
		for (MenuResourceDTO menuResourceDTO : menuResourceDTOs) {
			results.add(transFromMenuResourceBy(menuResourceDTO));
		}
		return results;
	}

	public static OrganizationScope transFromOrganizationScopeBy(OrganizationScopeDTO organizationScopeDTO) {
		OrganizationScope results = new OrganizationScope(organizationScopeDTO.getName());
		results.setDescription(organizationScopeDTO.getDescription());
		return results;
	}
}
