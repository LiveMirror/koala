package org.openkoala.security.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class SecurityAccessApplicationImpl implements SecurityAccessApplication {

	private QueryChannelService queryChannelService;

	public QueryChannelService getQueryChannelService() {
		if (queryChannelService == null) {
			queryChannelService = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel");
		}
		return queryChannelService;
	}

	public boolean hasPermission(User user) {
		return false;
	}

	public boolean canAccessSecurityResource(User user, SecurityResource securityResource) {
		return false;
	}

	public Set<Role> findAllRolesByUserAccount(String userAccount) {
		return User.findAllRolesBy(userAccount);
	}

	public Set<Permission> findAllPermissionsByUserAccount(String userAccount) {
		return User.findAllPermissionsBy(userAccount);
	}

	public User getUserBy(Long userId) {
		return Actor.get(User.class, userId);
	}

	public User getUserBy(String userAccount) {
		return User.getBy(userAccount);
	}

	public Set<MenuResource> findMenuResourceByUserAccount(String userAccount) {
		User user = getUserBy(userAccount);
		Set<Authority> authorities = Authorization.findAuthoritiesByActor(user);
		Set<MenuResource> result = Authority.findMenuResourceByAuthorities(authorities);
		return result;
	}

	@Override
	public void updateActor(Actor actor) {
		actor.update();
	}

	@Override
	public boolean updatePassword(User user, String oldUserPassword) {
		return user.updatePassword(oldUserPassword);
	}

	@Override
	public void checkAuthorization(String userAccount, Role role) {
		User user = getUserBy(userAccount);
		Authorization.checkAuthorization(user, role);
	}

	@Override
	public Role getRoleBy(Long roleId) {
		return Role.get(Role.class, roleId);
	}

	@Override
	public Permission getPermissionBy(Long permissionId) {
		return Permission.get(Permission.class, permissionId);
	}

	@Override
	public MenuResource getMenuResourceBy(Long menuResourceId) {
		return MenuResource.get(MenuResource.class, menuResourceId);
	}

	@Override
	public List<MenuResource> findAllMenuResourcesByRole(Role role) {
		return new ArrayList<MenuResource>(Authority.findMenuResourceByAuthority(role));
	}
}
