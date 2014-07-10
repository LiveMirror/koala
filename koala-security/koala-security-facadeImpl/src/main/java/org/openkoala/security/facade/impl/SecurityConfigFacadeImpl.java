package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.util.TransFromDomainUtils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.facade.util.TransFromDomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class SecurityConfigFacadeImpl implements SecurityConfigFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfigFacadeImpl.class);
	
	@Inject
	private SecurityConfigApplication securityConfigApplication;

	@Inject
	private SecurityAccessApplication securityAccessApplication;

	public void saveUserDTO(UserDTO userDTO) {
		User user = transFromUserBy(userDTO);
		LOGGER.info("save user:{}",user);
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
	public void grantRoleToUser(Long userId, Long roleId) {
		securityConfigApplication.grantActorToAuthority(userId, roleId);
	}

	@Override
	public void grantRolesToUser(Long userId, Long[] roleIds) {
		for (Long roleId : roleIds) {
			grantRoleToUser(userId, roleId);
		}
	}

	@Override
	public void grantPermissionToUser(Long userId, Long permissionId) {
		securityConfigApplication.grantActorToAuthority(userId, permissionId);
	}

	@Override
	public void grantPermissionsToUser(Long userId, Long[] permissionIds) {
		for (Long permissionId : permissionIds) {
			grantPermissionToUser(userId, permissionId);
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
		User user = securityAccessApplication.getUserBy(userId);
		LOGGER.info("roleIds:{}",roleIds);
		for (Long roleId : roleIds) {
			Role role = securityAccessApplication.getRoleBy(roleId);
			securityConfigApplication.terminateActorFromAuthority(user,role);
		}
	}

	@Override
	public void terminateAuthorizationsByPermissions(Long userId, Long[] permissionIds) {
		for (Long permissionId : permissionIds) {
			this.terminateAuthorizationByPermission(userId, permissionId);
		}
	}

	// 树 等待验证
	@Override
	public void grantMenuResourcesToRole(Long roleId, List<MenuResourceDTO> menuResourceDTOs) {
		//
		Role role = securityAccessApplication.getRoleBy(roleId);

		// 现在的
		List<MenuResource> targetOwnerMenuResources = transFromMenuResourcesBy(menuResourceDTOs);
		// 原有的
		List<MenuResource> originalOwnerMenuResources = securityAccessApplication.findAllMenuResourcesByRole(role);

		List<MenuResource> tmpList = new ArrayList<MenuResource>(originalOwnerMenuResources);
		// 待添加的
		List<MenuResource> waitingAddList = new ArrayList<MenuResource>();
		// 带删除的
		List<MenuResource> waitingDelList = new ArrayList<MenuResource>();

		targetOwnerMenuResources.retainAll(originalOwnerMenuResources);
		originalOwnerMenuResources.removeAll(targetOwnerMenuResources);

		waitingDelList.addAll(originalOwnerMenuResources);

		tmpList.removeAll(targetOwnerMenuResources);

		waitingAddList.addAll(tmpList);

		securityConfigApplication.terminateSecurityResourcesFromAuthority(waitingDelList, role);
		securityConfigApplication.grantSecurityResourcesToAuthority(waitingAddList, role);

	}

	@Override
	public void grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long pageElementResourceId : pageElementResourceIds) {
			PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(pageElementResourceId);
			securityConfigApplication.grantSecurityResourceToAuthority(pageElementResource, role);
		}
	}

	@Override
	public void grantUrlAccessResourcesToRole(Long roleId, Long[] urlAccessResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long urlAccessResourceId : urlAccessResourceIds) {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			securityConfigApplication.grantSecurityResourceToAuthority(urlAccessResource, role);
		}
	}

	@Override
	public void grantMethodInvocationResourcesToUser(Long roleId, Long[] menuResourceIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grantPermissionsToRole(Long roleId, Long[] permissionIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long permissionId : permissionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantRoleToPermission(role, permission);
			securityConfigApplication.grantPermissionToRole(permission, role);
		}
	}

	@Override
	public void terminatePermissionsFromRole(Long roleId, Long[] permssionIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long permissionId : permssionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminatePermissionFromRole(permission, role);
		}
	}

	@Override
	public void saveUrlAccessResourceDTO(UrlAccessResourceDTO urlAccessResourceDTO) {
		UrlAccessResource urlAccessResource = transFromUrlAccessResourceBy(urlAccessResourceDTO);
		securityConfigApplication.createSecurityResource(urlAccessResource);
	}

	@Override
	public void updateUrlAccessResourceDTO(UrlAccessResourceDTO urlAccessResourceDTO) {
		UrlAccessResource urlAccessResource = transFromUrlAccessResourceBy(urlAccessResourceDTO);
		securityConfigApplication.updateSecurityResource(urlAccessResource);
	}

	@Override
	public void terminateUrlAccessResourceDTOs(UrlAccessResourceDTO[] urlAccessResourceDTOs) {
		for (UrlAccessResourceDTO urlAccessResourceDTO : urlAccessResourceDTOs) {
			UrlAccessResource urlAccessResource = transFromUrlAccessResourceBy(urlAccessResourceDTO);
			securityConfigApplication.terminateSecurityResource(urlAccessResource);
		}
	}

	@Override
	public void terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long urlAccessResourceId : urlAccessResourceIds) {
			UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
			securityConfigApplication.terminateSecurityResourceFromAuthority(urlAccessResource, role);
		}
	}

	@Override
	public void grantUrlAccessResourceToPermission(Long urlAccessResourceId, Long permissionId) {
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		securityConfigApplication.grantSecurityResourceToAuthority(urlAccessResource, permission);
	}

	@Override
	public void terminateUrlAccessResourceFromPermission(Long urlAccessResourceId, Long permissionId) {
		Permission permission = securityAccessApplication.getPermissionBy(permissionId);
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		securityConfigApplication.terminateSecurityResourceFromAuthority(urlAccessResource, permission);
	}

	@Override
	public void grantPermisssionsToUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId) {
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		for (Long permissionId : permissionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToSecurityResource(permission, urlAccessResource);
		}
	}

	@Override
	public void terminatePermissionsFromUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId) {
		UrlAccessResource urlAccessResource = securityAccessApplication.getUrlAccessResourceBy(urlAccessResourceId);
		for (Long permissionId : permissionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthorityFromSecurityResource(permission,urlAccessResource);
		}
	}

	@Override
	public void grantPermisssionsToMenuResource(Long[] permissionIds, Long menuResourceId) {
		MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
		for (Long permissionId : permissionIds) {
			Permission permssion = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToSecurityResource(permssion, menuResource);
		}
	}

	@Override
	public void terminatePermissionsFromMenuResource(Long[] permissionIds, Long menuResourceId) {
		MenuResource menuResource = securityAccessApplication.getMenuResourceBy(menuResourceId);
		for (Long permissionId : permissionIds) {
			Permission permssion = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthorityFromSecurityResource(permssion, menuResource);
		}	
	}

	@Override
	public void savePageElementResourceDTO(PageElementResourceDTO pageElementResourceDTO) {
		PageElementResource pageElementResource = transFromPageElementResourceBy(pageElementResourceDTO);
		securityConfigApplication.createSecurityResource(pageElementResource);
	}

	@Override
	public void updatePageElementResourceDTO(PageElementResourceDTO pageElementResourceDTO) {
		PageElementResource pageElementResource = transFromPageElementResourceBy(pageElementResourceDTO);
		securityConfigApplication.updateSecurityResource(pageElementResource);
	}

	@Override
	public void terminatePageElementResourceDTOs(PageElementResourceDTO[] pageElementResourceDTOs) {
		for (PageElementResourceDTO pageElementResourceDTO : pageElementResourceDTOs) {
			PageElementResource pageElementResource = transFromPageElementResourceBy(pageElementResourceDTO);
			securityConfigApplication.terminateSecurityResource(pageElementResource);
		}
	}

	@Override
	public void terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds) {
		Role role = securityAccessApplication.getRoleBy(roleId);
		for (Long pageElementResourceId : pageElementResourceIds) {
			PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(pageElementResourceId);
			securityConfigApplication.terminateSecurityResourceFromAuthority(pageElementResource, role);
		}
	}

	@Override
	public void grantPermisssionsToPageElementResource(Long[] permissionIds, Long pageElementResourceId) {
		PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(pageElementResourceId);
		for (Long permissionId : permissionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.grantAuthorityToSecurityResource(permission, pageElementResource);
		}
	}

	@Override
	public void terminatePermissionsFromPageElementResource(Long[] permissionIds, Long pageElementResourceId) {
		PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(pageElementResourceId);
		for (Long permissionId : permissionIds) {
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
			securityConfigApplication.terminateAuthorityFromSecurityResource(permission, pageElementResource);
		}
	}

	@Override
	public boolean checkUserHasPageElementResource(String userAccount,String currentRoleName, String securityResourceName) {
		
		Role role = securityAccessApplication.getRoleBy(currentRoleName);
		Set<Permission> rolePermissions = role.getPermissions();
		Set<Permission> userPermissions = User.findAllPermissionsBy(userAccount);
		
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(role);
		authorities.addAll(userPermissions);
		authorities.addAll(rolePermissions);
		
		PageElementResource pageElementResource = securityAccessApplication.getPageElementResourceBy(securityResourceName);
		
		return securityConfigApplication.checkAuthoritiHasPageElementResource(authorities,pageElementResource);
	}

}







