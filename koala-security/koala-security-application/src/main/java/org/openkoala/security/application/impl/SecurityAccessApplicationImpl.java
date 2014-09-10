package org.openkoala.security.application.impl;

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

import com.google.common.collect.Lists;

@Named
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

    public User getUserById(Long userId) {
        return Actor.get(User.class, userId);
    }

    @Override
    public <T extends Actor> T getActorById(Long actorId) {
        return (T) Actor.get(Actor.class,actorId);
    }

    public User getUserByUserAccount(String userAccount) {
        return User.getByUserAccount(userAccount);
    }

    public List<MenuResource> findMenuResourceByUserAccount(String userAccount) {
        User user = getUserByUserAccount(userAccount);
        Set<Authority> authorities = Authorization.findAuthoritiesByActor(user);
        List<MenuResource> result = Authority.findMenuResourceByAuthorities(authorities);
        return result;
    }

    @Override
    public boolean updatePassword(User user, String userPassword, String oldUserPassword) {
        return user.updatePassword(userPassword, oldUserPassword);
    }

    @Override
    public void checkAuthorization(String userAccount, Role role) {
        User user = getUserByUserAccount(userAccount);
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
        return role.findMenuResourceByAuthority();
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
    public List<MenuResource> findAllMenuResorces() {
        return MenuResource.findAllMenuResources();
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
        return Role.getRoleBy(roleName);
    }

    @Override
    public <T extends Scope> T getScope(Long scopeId) {
        return (T)Scope.getBy(scopeId);
    }

    @Override
    public boolean hasPageElementResource(String identifier) {
        return PageElementResource.hasIdentifier(identifier);
    }

    @Override
    public User getUserByEmail(String email) {
        return User.getByEmail(email);
    }

    @Override
    public User getUserByTelePhone(String telePhone) {
        return User.getByTelePhone(telePhone);
    }

    @Override
    public List<MenuResource> findAllMenuResourcesByIds(Long[] menuResourceIds) {
        return MenuResource.findAllByIds(menuResourceIds);
    }

	@Override
	public boolean checkRoleByName(String roleName) {
		return Role.checkName(roleName);
	}

    @Override
    public <T extends Authority> T getAuthority(Long authorityId) {
        return Authority.getBy(authorityId);
    }
}
