package org.openkoala.security.facade.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.dto.MenuResourceDTO;
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
		List<MenuResourceDTO> childrenDTO = menuResourceDTO.getChildren();
		Set<MenuResource> children = new HashSet<MenuResource>();
		if (childrenDTO.size() > 0) {
			for (MenuResourceDTO childDTO : childrenDTO) {
				children.add(transFromMenuResourceBy(childDTO));
			}
		}
		return results;
	}
}
