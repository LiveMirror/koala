package org.openkoala.security.application.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class SecurityConfigApplicationImpl implements SecurityConfigApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfigApplicationImpl.class);

	public void createActor(Actor actor) {
		actor.save();
	}

	public void terminateActor(Actor actor) {
		actor.remove();
	}

	public void suspendUser(User user) {
		user.disable();
	}

	public void activateUser(User user) {
		user.enable();
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
		securityResource.update();
	}

	public void terminateSecurityResource(SecurityResource securityResource) {
		securityResource.remove();
	}

	public void grantAuthoritiesToSecurityResource(List<Authority> authorities, SecurityResource securityResource) {
		for (Authority authority : authorities) {
			grantAuthorityToSecurityResource(authority, securityResource);
		}
	}

	public void grantRoleToPermission(Role role, Permission permission) {
		role.addPermission(permission);
	}

	public void grantRoleToPermissions(Role role, List<Permission> permissions) {
		role.addPermissions(permissions);
	}

	public void grantRolesToPermission(List<Role> roles, Permission permission) {
		for (Role role : roles) {
			role.addPermission(permission);
		}
	}

	public void grantActorToAuthority(Actor actor, Authority authority) {
		actor.grant(authority, null);
	}

	public void grantActorToAuthorities(Actor actor, List<Authority> authorities) {
		for (Authority authority : authorities) {
			this.grantActorToAuthority(actor, authority);
		}
	}

	public void grantActorsToAuthority(List<Actor> actors, Authority authority) {
		for (Actor actor : actors) {
			this.grantActorToAuthority(actor, authority);
		}
	}

	public void terminateSecurityResourceFromAuthority(SecurityResource securityResource, Authority authority) {
		authority.terminateSecurityResource(securityResource);
	}

	public void terminateSecurityResourcesFromAuthority(List<? extends SecurityResource> securityResources, Authority authority) {
		authority.terminateSecurityResources(securityResources);
	}

	public void terminateAuthoritiesFromSecurityResource(List<Authority> authorities, SecurityResource securityResource) {
		for (Authority authority : authorities) {
			terminateAuthorityFromSecurityResource(authority, securityResource);
		}
	}

	public void terminateAuthorityFromSecurityResource(Authority authority, SecurityResource securityResource) {
		authority.terminateSecurityResource(securityResource);
	}

	public void terminatePermissionFromRole(Permission permission, Role role) {
		role.terminatePermission(permission);
	}

	public void terminatePermissionsFromRole(List<Permission> permissions, Role role) {
		role.terminatePermissions(permissions);
	}

	public void terminateRolesFromPermission(List<Role> roles, Permission permission) {
		for (Role role : roles) {
			terminateRoleFromPermission(role, permission);
		}
	}

	private void terminateRoleFromPermission(Role role, Permission permission) {
		role.terminatePermission(permission);
	}

	public void terminateActorFromAuthority(Actor actor, Authority authority) {
		Authorization authorization = Authorization.findByActorInAuthority(actor, authority);
		authorization.remove();
	}

	public void terminateAuthoritiesFromActor(List<Authority> authorities, Actor actor) {
		for (Authority authority : authorities) {
			this.terminateActorFromAuthority(actor, authority);
		}
	}

	// TODO
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

	public void createScope(Scope scope) {
		scope.save();
	}

	public void grantActorToAuthorityInScope(Actor actor, Authority authority, Scope scope) {
		actor.grant(authority, scope);
	}

	@Override
	public void grantAuthorityToSecurityResource(Authority authority, SecurityResource securityResource) {
		authority.addSecurityResource(securityResource);

	}

	@Override
	public void grantAuthorityToSecurityResources(Authority authority, List<? extends SecurityResource> securityResources) {
		authority.addSecurityResources(securityResources);
	}

	@Override
	public void resetPassword(User user) {
		user.resetPassword();
	}

	@Override
	public void createChildToParent(MenuResource child, Long parentId) {
		MenuResource parent = MenuResource.get(MenuResource.class, parentId);
		parent.addChild(child);
	}

	@Override
	public void updateScope(Scope scope) {
		scope.update();
	}

	@Override
	public void terminateScope(Scope scope) {
		scope.remove();
	}

	@Override
	public void createChildToParent(Scope child, Long parentId) {
		Scope parent = Scope.get(Scope.class, parentId);
		parent.addChild(child);
	}

	@Override
	public void grantSecurityResourcesToAuthority(List<? extends SecurityResource> securityResources, Authority authority) {
		authority.addSecurityResources(securityResources);
	}

	@Override
	public void grantPermissionToRole(Permission permission, Role role) {
		role.addPermission(permission);
	}

	@Override
	public void grantSecurityResourceToAuthority(SecurityResource securityResource, Authority authority) {
		authority.addSecurityResource(securityResource);
	}

	@Override
	public void grantPermissionsToRole(List<Permission> permissions, Role role) {
		role.addPermissions(permissions);
	}

	@Override
	public boolean checkAuthoritiHasPageElementResource(Set<Authority> authorities, String identifier) {
		return Authority.checkHasPageElementResource(authorities, identifier);
	}

	@Override
	public void updateActor(Actor actor) {
		actor.update();
	}

	@Override
	public void grantAuthorityToActor(Authority authority, Actor actor) {
		actor.grant(authority, null);
	}

	@Override
	public void updateUserLastLoginTime(User user) {
		user.updateLastLoginTime();
	}

}
