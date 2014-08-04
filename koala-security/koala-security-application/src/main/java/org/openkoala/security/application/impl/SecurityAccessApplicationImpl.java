package org.openkoala.security.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.Scope;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class SecurityAccessApplicationImpl implements SecurityAccessApplication {

	@Override
	public boolean hasPermission(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasOwnSecurityResource(User user, SecurityResource securityResource) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Role> findAllRolesByUserAccount(String userAccount) {
		return User.findAllRolesBy(userAccount);
	}

	public List<Permission> findAllPermissionsByUserAccount(String userAccount) {
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

	@Override
	public UrlAccessResource getUrlAccessResourceBy(Long urlAccessResourceId) {
		return UrlAccessResource.get(UrlAccessResource.class, urlAccessResourceId);
	}

	@Override
	public List<Role> findAllRoles() {
		return Role.findAll(Role.class);
	}

	@Override
	public List<UrlAccessResource> findAllUrlAccessResources() {
		return UrlAccessResource.findAllUrlAccessResources();
	}

	@Override
	public PageElementResource getPageElementResourceBy(Long pageElementResourceId) {
		return PageElementResource.get(PageElementResource.class, pageElementResourceId);
	}

	@Override
	public PageElementResource getPageElementResourceBy(String securityResourceName) {
		return PageElementResource.getBy(securityResourceName);
	}

	@Override
	public Role getRoleBy(String roleName) {
		return Role.getBy(roleName);
	}

	@Override
	public Scope getScope(Long scopeId) {
		return Scope.getBy(scopeId);
	}

	@Override
	public User login(String principal, String password) {
		return User.login(principal,password);
	}

	@Override
	public boolean hasPageElementResource(String identifier) {
		return PageElementResource.hasIdentifier(identifier);
	}
}
