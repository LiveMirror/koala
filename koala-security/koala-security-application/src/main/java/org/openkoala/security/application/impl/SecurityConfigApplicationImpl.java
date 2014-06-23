package org.openkoala.security.application.impl;

import java.util.List;

import javax.inject.Named;

import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.OrganizationScope;
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
		securityResource.addAuthorities(authorities);
	}

	public void grantRoleToPermission(Role role, Permission permission) {
		permission.addRole(role);
	}

	public void grantRoleToPermissions(Role role, List<Permission> permissions) {
		for (Permission permission : permissions) {
			permission.addRole(role);
		}
	}

	public void grantRolesToPermission(List<Role> roles, Permission permission) {
		permission.addRoles(roles);
	}

	public void grantActorToAuthority(Actor actor, Authority authority) {
		new Authorization(actor, authority, null).save();
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
		securityResource.terminateAuthorities(authorities);
	}

	public void terminatePermissionFromRole(Permission permission, Role role) {
		role.terminatePermission(permission);
	}

	public void terminatePermissionsFromRole(List<Permission> permissions, Role role) {
		role.terminatePermissions(permissions);
	}

	public void terminateRolesFromPermission(List<Role> roles, Permission permission) {
		permission.removeRoles(roles);
	}

	public void terminateActorFromAuthority(Actor actor, Authority authority) {
		Authorization.findByActorInAuthority(actor,authority).remove();
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
	public void grantActorToAuthorityInScope(Long actorId, Long authorityId, Long scopeId) {
		Actor actor = Actor.get(Actor.class, actorId);
		Authority authority = Authority.get(Authority.class, authorityId);
		Scope scope = Scope.get(Scope.class, scopeId);
		grantActorToAuthorityInScope(actor, authority, scope);
	}

	@Override
	public void grantAuthorityToSecurityResource(Authority authority, SecurityResource securityResource) {
		securityResource.addAuthority(authority);
	}

	@Override
	public void grantAuthorityToSecurityResources(Authority authority,
			List<? extends SecurityResource> securityResources) {
		for (SecurityResource securityResource : securityResources) {
			this.grantAuthorityToSecurityResource(authority, securityResource);
		}
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
	public void createChildToParent(OrganizationScope child, Long parentId) {
		OrganizationScope parent = OrganizationScope.get(OrganizationScope.class, parentId);
		parent.addChild(child);
	}

	@Override
	public void grantActorsToAuthority(Long[] userIds, Long roleId) {
		for (Long userId : userIds) {
			grantActorToAuthority(userId, roleId);
		}
	}

	@Override
	public void grantActorToAuthority(Long actorId, Long authorityId) {
		Actor actor = Actor.get(Actor.class, actorId);
		Authority authority = Authority.get(Authority.class, authorityId);
		grantActorToAuthority(actor, authority);
	}

	@Override
	public void grantSecurityResourcesToAuthority(List<? extends SecurityResource> securityResources,
			Authority authority) {
		authority.addSecurityResources(securityResources);
	}

	@Override
	public void grantPermissionToRole(Permission permission, Role role) {
		role.addPermission(permission);
	}
	
}
