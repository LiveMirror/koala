package org.openkoala.security.facade.util;

import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.core.domain.UserStatus;
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
		result.setUserStatus(UserStatus.valueOf(userDTO.getUserStatus()));
		return result;
	}

	public static Role transFromRoleBy(RoleDTO roleDTO) {
		Role result = new Role(roleDTO.getRoleName(), roleDTO.getDescription(), roleDTO.isMaster());
		return result;
	}

	public static Permission transFromPermissionBy(PermissionDTO permissionDTO) {
		Permission result = new Permission(permissionDTO.getPermissionName(), permissionDTO.getPermissionName());
		return result;
	}
	
	public static MenuResource transFromMenuResourceBy(MenuResourceDTO menuResourceDTO) {
		MenuResource result = new MenuResource(menuResourceDTO.getName(), menuResourceDTO.isValid(), menuResourceDTO.getDescription(), menuResourceDTO.getIcon(), menuResourceDTO.getUrl());
//		result.setChildren(new HashSet<MenuResource>(menuResourceDTO.getChildren()));
		return result;
	}
}
