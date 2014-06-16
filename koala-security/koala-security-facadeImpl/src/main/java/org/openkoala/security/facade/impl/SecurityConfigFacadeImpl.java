package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.util.TransFromDomainUtils.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.facade.util.TransFromDomainUtils;

@Named
public class SecurityConfigFacadeImpl implements SecurityConfigFacade {

	@Inject
	private SecurityConfigApplication securityConfigApplication;

	public void saveUserDTO(UserDTO userDTO) {
		User user = transFromUserBy(userDTO);
		securityConfigApplication.createActor(user);
	}

	@Override
	public void terminateUserDTOs(UserDTO[] userDTOs) {
		for (UserDTO userDTO : userDTOs) {
			User user = transFromUserBy(userDTO);
			securityConfigApplication.terminateActor(user);
		}
	}

	@Override
	public void resetPassword(UserDTO userDTO) {
		User user = transFromUserBy(userDTO);
		securityConfigApplication.resetPassword(user);
	}

	@Override
	public void saveRoleDTO(RoleDTO roleDTO) {
		Role role = transFromRoleBy(roleDTO);
		securityConfigApplication.createAuthority(role);
	}

	@Override
	public void updateRoleDTO(RoleDTO roleDTO) {
		Role role = transFromRoleBy(roleDTO);
		securityConfigApplication.updateAuthority(role);
	}

	@Override
	public void terminateRoleDTOs(RoleDTO[] roleDTOs) {
		for (RoleDTO roleDTO : roleDTOs) {
			Role role = transFromRoleBy(roleDTO);
			securityConfigApplication.terminateAuthority(role);
		}
	}

	@Override
	public void savePermissionDTO(PermissionDTO permissionDTO) {
		Permission permission = transFromPermissionBy(permissionDTO);
		securityConfigApplication.createAuthority(permission);
	}

	@Override
	public void updatePermissionDTO(PermissionDTO permissionDTO) {
		Permission permission = TransFromDomainUtils.transFromPermissionBy(permissionDTO);
		securityConfigApplication.updateAuthority(permission);
	}

	@Override
	public void terminatePermissionDTOs(PermissionDTO[] permissionDTOs) {
		for (PermissionDTO permissionDTO : permissionDTOs) {
			Permission permission = transFromPermissionBy(permissionDTO);
			securityConfigApplication.terminateAuthority(permission);
		}
	}

	@Override
	public void saveMenuResourceDTO(MenuResourceDTO menuResourceDTO) {
		securityConfigApplication.createSecurityResource(transFromMenuResourceBy(menuResourceDTO));
	}

	@Override
	public void updateMenuResourceDTO(MenuResourceDTO menuResourceDTO) {
		securityConfigApplication.updateSecurityResource(transFromMenuResourceBy(menuResourceDTO));
	}

	@Override
	public void terminateMenuResourceDTOs(MenuResourceDTO[] menuResourceDTOs) {
		for (MenuResourceDTO menuResourceDTO : menuResourceDTOs) {
			securityConfigApplication.terminateSecurityResource(transFromMenuResourceBy(menuResourceDTO));
		}
	}

	@Override
	public void saveChildToParent(MenuResourceDTO child, MenuResourceDTO parent) {
		securityConfigApplication.createChildToParent(transFromMenuResourceBy(child),transFromMenuResourceBy(parent));
	}

}
