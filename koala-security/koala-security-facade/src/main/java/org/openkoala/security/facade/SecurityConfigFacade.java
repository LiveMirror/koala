package org.openkoala.security.facade;

import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;

public interface SecurityConfigFacade {

	/**
	 * 保存用户
	 * 
	 * @param userDTO
	 */
	void saveUserDTO(UserDTO userDTO);

	/**
	 * 撤销用户
	 * 
	 * @param userDTOs
	 */
	void terminateUserDTOs(UserDTO[] userDTOs);

	/**
	 * 重置用户密码
	 * 
	 * @param userDTO
	 */
	void resetPassword(UserDTO userDTO);

	/**
	 * 保存角色
	 * 
	 * @param roleDTO
	 */
	void saveRoleDTO(RoleDTO roleDTO);

	/**
	 * 更新角色
	 * 
	 * @param roleDTO
	 */
	void updateRoleDTO(RoleDTO roleDTO);

	/**
	 * 批量撤销角色
	 * 
	 * @param roleDTOs
	 */
	void terminateRoleDTOs(RoleDTO[] roleDTOs);

	/**
	 * 保存权限
	 * 
	 * @param permissionDTO
	 */
	void savePermissionDTO(PermissionDTO permissionDTO);

	/**
	 * 更新权限
	 * 
	 * @param permissionDTO
	 */
	void updatePermissionDTO(PermissionDTO permissionDTO);

	/**
	 * 批量撤销权限
	 * 
	 * @param permissionDTOs
	 */
	void terminatePermissionDTOs(PermissionDTO[] permissionDTOs);

	/**
	 * 保存菜单资源
	 * 
	 * @param menuResourceDTO
	 */
	void saveMenuResourceDTO(MenuResourceDTO menuResourceDTO);

	/**
	 * 更新菜单资源
	 * 
	 * @param menuResourceDTO
	 */
	void updateMenuResourceDTO(MenuResourceDTO menuResourceDTO);

	/**
	 * 撤销菜单资源
	 * 
	 * @param menuResourceDTOs
	 */
	void terminateMenuResourceDTOs(MenuResourceDTO[] menuResourceDTOs);

}
