package org.openkoala.security.application.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class SecurityConfigApplicationImpl implements SecurityConfigApplication {

	public void createActor(Actor actor) {
		actor.save();
	}

	public void terminateActor(Actor actor) {
		actor.remove();
	}

	public void resetPassword() {
		// TODO Auto-generated method stub

	}

	public void suspendUser(User user) {
		// TODO Auto-generated method stub

	}

	public void activateUser(User user) {
		// TODO Auto-generated method stub

	}

	public void createAuthority(Authority authority) {
		authority.save();
	}

	public void updateAuthority(Authority authority) {
		authority.update();
	}

	public void terminateAuthority(Authority authority) {
		authority.remove();
	}

	public void createSecurityResource(SecurityResource securityResource) {
		securityResource.save();
	}

	public void updateSecurityResource(SecurityResource securityResource) {
		// TODO Auto-generated method stub

	}

	public void terminateSecurityResource(SecurityResource securityResource) {
		// TODO Auto-generated method stub

	}

	public void grantAuthoritiesToSecurityResource(List<Authority> authorities, SecurityResource securityResource) {
		// TODO Auto-generated method stub
	}

	// TODO 需要改进
	public void grantRoleToPermission(Role role, Permission permission) {
		Set<Permission> permissions = new HashSet<Permission>();
		permissions.add(permission);
		role.setPermissions(permissions);
	}

	public void grantRoleToPermissions(Role role, List<Permission> permissions) {
		role.setPermissions(new HashSet<Permission>(permissions));
	}

	public void grantRolesToPermission(List<Role> roles, Permission permission) {
		for (Role role : roles) {
			grantRoleToPermission(role, permission);
		}
	}

	public void grantActorToAuthority(Actor actor, Authority authority) {
		// TODO Auto-generated method stub

	}

	public void grantActorToAuthorities(Actor actor, List<Authority> authorities) {
		// TODO Auto-generated method stub

	}

	public void grantActorsToAuthority(List<Actor> actors, Authority authority) {
		// TODO Auto-generated method stub

	}

	public void terminateSecurityResourceFromAuthority(SecurityResource securityResource, Authority authority) {
		// TODO Auto-generated method stub

	}

	public void terminateSecurityResourcesFromAuthority(List<SecurityResource> securityResources, Authority authority) {
		// TODO Auto-generated method stub

	}

	public void terminateAuthoritiesFromSecurityResource(List<Authority> authorities, SecurityResource securityResource) {
		// TODO Auto-generated method stub

	}

	public void terminatePermissionFromRole(Permission permission, Role role) {
		// TODO Auto-generated method stub

	}

	public void terminatePermissionsFromRole(List<Permission> permissions, Role role) {
		// TODO Auto-generated method stub

	}

	public void terminateRolesFromPermission(List<Role> roles, Permission permission) {
		// TODO Auto-generated method stub

	}

	public void terminateActorFromAuthority(Actor actor, Authority authority) {
		// TODO Auto-generated method stub

	}

	public void terminateActorsFromAuthority(List<Actor> actors, Authority authority) {
		// TODO Auto-generated method stub

	}

	public void terminateAuthoritiesFromActor(List<Authority> authorities, Actor actor) {
		// TODO Auto-generated method stub

	}

	public void createMenuResourceUnderParent(Set<MenuResource> menuResources, MenuResource toParent) {
		for (MenuResource menuResource : menuResources) {
			menuResource.setParent(toParent);
		}
	}

	public void addMenuResourceUnderParent(MenuResource menuResource, MenuResource toParent) {
		// TODO Auto-generated method stub

	}

	public void updateMenuResources(List<MenuResource> menuResources) {
		// TODO Auto-generated method stub

	}

	public boolean isSecurityResourceEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSecurityResourceNameExist(SecurityResource securityResource) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSecurityResourceIdentifierExist(SecurityResource securityResource) {
		// TODO Auto-generated method stub
		return false;
	}

	public void initSecurityResources() {
		// TODO Auto-generated method stub

	}

	public void createScope(Scope scope) {
		scope.save();
	}

	public void grantActorToAuthorityInScope(Actor actor, Authority authority, Scope scope) {
		new Authorization(actor, authority, scope).save();
	}

	@Override
	public void grantAuthorityToSecurityResource(Authority authority, SecurityResource securityResource) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grantAuthorityToSecurityResources(Authority authority,
			List<? extends SecurityResource> securityResources) {
		authority.setSecurityResources(new HashSet<SecurityResource>(securityResources));
	}

	@Override
	public void resetPassword(User user) {
		user.resetPassword();
	}

}
