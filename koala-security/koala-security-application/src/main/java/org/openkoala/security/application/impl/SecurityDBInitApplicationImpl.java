package org.openkoala.security.application.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.application.SecurityDBInitApplication;
import org.openkoala.security.application.systeminit.SystemInit;
import org.openkoala.security.application.systeminit.SystemInitFactory;
import org.openkoala.security.core.domain.Actor;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;

@Named
public class SecurityDBInitApplicationImpl implements SecurityDBInitApplication {

    @Inject
    private SecurityConfigApplication securityConfigApplication;

    private static SystemInit systemInit = SystemInitFactory.INSTANCE.getSystemInit();

    @Override
    public User initUser() {
        User user = createUser();
        securityConfigApplication.createActor(user);
        return user;
    }

    @Override
    public Role initRole() {
        Role role = createRole();
        securityConfigApplication.createAuthority(role);
        return role;
    }

    @Override
    public List<MenuResource> initMenuResources() {
        List<MenuResource> menuResources = new ArrayList<MenuResource>();
        for (SystemInit.MenuResource each : getParentMenuResources()) {
            MenuResource menuResource = transformMenuResourceEntity(each);
            securityConfigApplication.createSecurityResource(menuResource);
            if(!each.isNotGrant()){
                menuResources.add(menuResource);
            }
            createChildrenMenuResource(menuResource, each, menuResources);
        }
        return menuResources;
    }

    @Override
    public List<UrlAccessResource> initUrlAccessResources() {
        List<UrlAccessResource> results = new ArrayList<UrlAccessResource>();
        for (SystemInit.UrlAccessResource each : systemInit.getUrlAccessResource()) {
            UrlAccessResource resource =  new UrlAccessResource(each.getName(), each.getUrl());
            resource.save();
            if(!each.isNotGrant()){
               results.add(resource);
           }
        }
        return results;
    }

    @Override
    public List<PageElementResource> initPageElementResources() {
        List<PageElementResource> results = new ArrayList<PageElementResource>();
        for (SystemInit.PageElementResource each : systemInit.getPageElementResource()) {
            PageElementResource resource = new PageElementResource(each.getName(), each.getUrl());
            resource.save();
            if(!each.isNotGrant()){
                results.add(resource);
            }
        }
        return results;
    }

    private User createUser() {
        SystemInit.User initUser = systemInit.getUser();
        User user = new User(initUser.getName(), initUser.getUsername());
        user.setCreateOwner(initUser.getCreateOwner());
        user.setDescription(initUser.getDescription());
        return user;
    }

    private Role createRole() {
        SystemInit.Role initRole = systemInit.getRole();
        Role role = new Role(initRole.getName());
        role.setDescription(initRole.getDescription());
        return role;
    }

    private MenuResource transformMenuResourceEntity(SystemInit.MenuResource initMenuResource) {
        MenuResource menuResource = new MenuResource(initMenuResource.getName());
        menuResource.setDescription(initMenuResource.getDescription());
        menuResource.setMenuIcon(initMenuResource.getMenuIcon());
        menuResource.setUrl(initMenuResource.getUrl());
        return menuResource;
    }

    private void createChildrenMenuResource(MenuResource menuResource, SystemInit.MenuResource parentMenuResource, List<MenuResource> menuResources) {
        for (SystemInit.MenuResource each : systemInit.getMenuResource()) {
            if (Integer.valueOf(parentMenuResource.getId()).equals(each.getPid())) {
                MenuResource children = transformMenuResourceEntity(each);
                securityConfigApplication.createChildToParent(children, menuResource.getId());
                if(!each.isNotGrant()){
                    menuResources.add(children);
                }
                createChildrenMenuResource(children, each, menuResources);
            }
        }
    }

    private List<SystemInit.MenuResource> getParentMenuResources() {
        List<SystemInit.MenuResource> parentMenuResources = new ArrayList<SystemInit.MenuResource>();
        for (SystemInit.MenuResource each : systemInit.getMenuResource()) {
            if (each.getPid() == null) {
                parentMenuResources.add(each);
            }
        }
        return parentMenuResources;
    }
}
