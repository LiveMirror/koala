package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.util.TransFromDomainUtils.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.facade.util.TransFromDomainUtils;
import org.springframework.transaction.annotation.Transactional;

@Named
public class SecurityConfigFacadeImpl implements SecurityConfigFacade {

	@Inject
	private SecurityConfigApplication securityConfigApplication;

	@Inject
	private SecurityAccessApplication securityAccessApplication;

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
	public void saveChildToParent(MenuResourceDTO child, Long parentId) {
		securityConfigApplication.createChildToParent(transFromMenuResourceBy(child), parentId);
	}

	@Override
	public void saveOrganizationDTO(OrganizationScopeDTO organizationScopeDTO) {
		securityConfigApplication.createScope(transFromOrganizationScopeBy(organizationScopeDTO));
	}

	@Override
	public void updateOrganizationDTO(OrganizationScopeDTO organizationScopeDTO) {
		securityConfigApplication.updateScope(transFromOrganizationScopeBy(organizationScopeDTO));
	}

	@Override
	public void terminateOrganizationDTOs(OrganizationScopeDTO[] organizationDTOs) {
		for (OrganizationScopeDTO organizationScopeDTO : organizationDTOs) {
			securityConfigApplication.terminateScope(transFromOrganizationScopeBy(organizationScopeDTO));
		}
	}

	@Override
	public void saveChildToParent(OrganizationScopeDTO child, Long parentId) {
		securityConfigApplication.createChildToParent(transFromOrganizationScopeBy(child), parentId);
	}

	@Override
	public void grantRoleInScope(Long userId, Long roleId, Long scopeId) {
		securityConfigApplication.grantActorToAuthorityInScope(userId, roleId, scopeId);
	}

	@Override
	public void grantRolesInScope(Long userId, Long[] roleIds, Long scopeId) {
		for (Long roleId : roleIds) {
			grantRoleInScope(userId, roleId, scopeId);
		}
	}

	@Override
	public void grantPermissionInScope(Long userId, Long permissionId, Long scopeId) {
		securityConfigApplication.grantActorToAuthorityInScope(userId, permissionId, scopeId);
	}

	@Override
	public void grantPermissionsInScope(Long userId, Long[] permissionIds, Long scopeId) {
		for (Long permissionId : permissionIds) {
			grantPermissionInScope(userId, permissionId, scopeId);
		}
	}

	@Override
	public void grantRole(Long userId, Long roleId) {
		securityConfigApplication.grantActorToAuthority(userId, roleId);
	}

	@Override
	public void grantRoles(Long userId, Long[] roleIds) {
		for (Long roleId : roleIds) {
			grantRole(userId, roleId);
		}
	}

	@Override
	public void grantPermission(Long userId, Long permissionId) {
		securityConfigApplication.grantActorToAuthority(userId, permissionId);
	}

	@Override
	public void grantPermissions(Long userId, Long[] permissionIds) {
		for (Long permissionId : permissionIds) {
			grantPermission(userId, permissionId);
		}
	}

	@Override
	public void activate(Long userId) {
		User user = securityAccessApplication.getUserBy(userId);
		securityConfigApplication.activateUser(user);
	}

	@Override
	public void suspend(Long userId) {
		User user = securityAccessApplication.getUserBy(userId);
		securityConfigApplication.suspendUser(user);
	}

	@Override
	public void activate(Long[] userIds) {
		for (Long userId : userIds) {
			this.activate(userId);
		}
	}

	@Override
	public void suspend(Long[] userIds) {
		for (Long userId : userIds) {
			this.suspend(userId);
		}
	}

	@Override
	public void terminateAuthorizationByRole(Long userId, Long roleId) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		User user = securityAccessApplication.getUserBy(userId);
		securityConfigApplication.terminateActorFromAuthority(user, role);
	}

	@Override
	public void terminateAuthorizationByPermission(Long userId, Long permissionId) {
		User user = securityAccessApplication.getUserBy(userId);
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		securityConfigApplication.terminateActorFromAuthority(user, permission);
	}

	// TODO 待优化。。。
	@Override
	public void terminateAuthorizationsByRoles(Long userId, Long[] roleIds) {
		for (Long roleId : roleIds) {
			this.terminateAuthorizationByRole(userId, roleId);
		}
	}

	@Override
	public void terminateAuthorizationsByPermissions(Long userId, Long[] permissionIds) {
		for (Long permissionId : permissionIds) {
			this.terminateAuthorizationByPermission(userId, permissionId);
		}
	}

	// 树
	@Override
	public void grantMenuResources(Long roleId,Long[] menuResourceIds) {
		//
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long menuResourceId : menuResourceIds) {
			this.grantMenuResource(role,menuResourceId);
		}
	}

	private void grantMenuResource(Role role,Long menuResourceId) {
		MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
		securityConfigApplication.grantAuthorityToSecurityResource(role, menuResource);
	}

	// 列表
	@Override
	public void grantPageElementResources(Long roleId,Long[] menuResourceIds) {
		
	}

	@Override
	public void grantUrlAccessResources(Long roleId,Long[] menuResourceIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void grantMethodInvocationResources(Long roleId,Long[] menuResourceIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void grantPermissions(Long[] menuResourceIds) {
		
	}

}
