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
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;

import com.google.common.collect.Lists;

@Named
public class SecurityDBInitApplicationImpl implements SecurityDBInitApplication {

    public static final String MENU_ICON = "glyphicon  glyphicon-list-alt";

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
    public List<Permission> initPermissions() {
        return Collections.emptyList();
    }

    @Override
    public List<MenuResource> initMenuResources() {
        List<MenuResource> results = createMenuResource();
        return results;
    }

    @Override
    public List<UrlAccessResource> initUrlAccessResources() {
        List<UrlAccessResource> results = new ArrayList<UrlAccessResource>();
        List<UrlAccessResource> pageUrls = createPageElementResourceUrls();
        List<UrlAccessResource> permissionUrls = createPermissionUrls();
        List<UrlAccessResource> roleUrls = createRoleUrls();
        List<UrlAccessResource> userUrls = createUserUrls();
        List<UrlAccessResource> urls = createUrlAccessResourceUrls();
        List<UrlAccessResource> menuUrls = createMenuResourceUrls();
        results.addAll(pageUrls);
        results.addAll(permissionUrls);
        results.addAll(roleUrls);
        results.addAll(userUrls);
        results.addAll(urls);
        results.addAll(menuUrls);
        SecurityResource.batchSave(results);
        return results;
    }

    @Override
    public void initActor(Actor actor) {
        actor.save();
    }

    @Override
    public List<PageElementResource> initPageElementResources() {
        List<PageElementResource> userResults = createPageElementResourcesOfUser();
        List<PageElementResource> roleResults = createPageElementResourcesOfRole();
        List<PageElementResource> permissonResults = createPageElementResourcesOfPermission();
        List<PageElementResource> menuResourceResults = createPageElementResourcesOfMenuResource();
        List<PageElementResource> urlAccessResourceResults = createPageElementResourcesOfUrlAccessResource();
        List<PageElementResource> pageElementResourceResults = createPageElementResourcesOfPageElementResource();

        List<PageElementResource> results = new ArrayList<PageElementResource>();
        results.addAll(userResults);
        results.addAll(roleResults);
        results.addAll(permissonResults);
        results.addAll(menuResourceResults);
        results.addAll(urlAccessResourceResults);
        results.addAll(pageElementResourceResults);
        SecurityResource.batchSave(results);
        return results;
    }

    private List<PageElementResource> createPageElementResourcesOfPageElementResource() {
        PageElementResource pageElementResourceManagerAdd = new PageElementResource("页面元素资源管理-添加",
                "pageElementResourceManagerAdd");
        PageElementResource pageElementResourceManagerUpdate = new PageElementResource("页面元素资源管理-修改",
                "pageElementResourceManagerUpdate");
        PageElementResource pageElementResourceManagerTerminate = new PageElementResource("页面元素资源管理-撤销",
                "pageElementResourceManagerTerminate");
        PageElementResource pageElementResourceManagerGrantPermission = new PageElementResource("页面元素资源管理-授权权限",
                "pageElementResourceManagerGrantPermission");
        PageElementResource pageElementResourceManagerQuery = new PageElementResource("页面元素资源管理-查询",
                "pageElementResourceManagerQuery");
        return Lists.newArrayList(//
                pageElementResourceManagerAdd,//
                pageElementResourceManagerUpdate,//
                pageElementResourceManagerTerminate,//
                pageElementResourceManagerGrantPermission,//
                pageElementResourceManagerQuery
        );
    }

    private List<PageElementResource> createPageElementResourcesOfUrlAccessResource() {
        PageElementResource urlAccessResourceManagerAdd = new PageElementResource("路径访问资源管理-添加",
                "urlAccessResourceManagerAdd");
        PageElementResource urlAccessResourceManagerUpdate = new PageElementResource("路径访问资源管理-修改",
                "urlAccessResourceManagerUpdate");
        PageElementResource urlAccessResourceManagerTerminate = new PageElementResource("路径访问资源管理-撤销",
                "urlAccessResourceManagerTerminate");
        PageElementResource urlAccessResourceManagerGrantPermission = new PageElementResource("路径访问资源管理-授权权限",
                "urlAccessResourceManagerGrantPermission");
        PageElementResource urlAccessResourceManagerQuery = new PageElementResource("路径访问资源管理-查询","urlAccessResourceManagerQuery");
        return Lists.newArrayList(//
                urlAccessResourceManagerAdd,//
                urlAccessResourceManagerUpdate,//
                urlAccessResourceManagerTerminate,//
                urlAccessResourceManagerGrantPermission,//
                urlAccessResourceManagerQuery
        );
    }

    private List<PageElementResource> createPageElementResourcesOfMenuResource() {
        PageElementResource menuResourceManagerAdd = new PageElementResource("菜单资源管理-添加", "menuResourceManagerAdd");
        PageElementResource menuResourceManagerUpdate = new PageElementResource("菜单资源管理-修改","menuResourceManagerUpdate");
        PageElementResource menuResourceManagerTerminate = new PageElementResource("菜单资源管理-撤销","menuResourceManagerTerminate");
        PageElementResource menuResourceManagerGrantPermission = new PageElementResource("菜单资源管理-授权权限","menuResourceManagerGrantPermission");

        return Lists.newArrayList(//
                menuResourceManagerAdd,//
                menuResourceManagerUpdate,//
                menuResourceManagerTerminate,//
                menuResourceManagerGrantPermission);
    }

    private List<PageElementResource> createPageElementResourcesOfPermission() {
        PageElementResource permissionManagerAdd = new PageElementResource("权限管理-添加", "permissionManagerAdd");
        PageElementResource permissionManagerUpdate = new PageElementResource("权限管理-修改", "permissionManagerUpdate");
        PageElementResource permissionManagerTerminate = new PageElementResource("权限管理-撤销","permissionManagerTerminate");
        PageElementResource permissionManagerQuery = new PageElementResource("权限管理-查询","permissionManagerQuery");
        return Lists.newArrayList(//
                permissionManagerAdd,//
                permissionManagerUpdate,//
                permissionManagerTerminate,//
                permissionManagerQuery
        );
    }

    private List<PageElementResource> createPageElementResourcesOfRole() {
        PageElementResource roleManagerAdd = new PageElementResource("角色管理-添加", "roleManagerAdd");
        PageElementResource roleManagerUpdate = new PageElementResource("角色管理-修改", "roleManagerUpdate");
        PageElementResource roleManagerTerminate = new PageElementResource("角色管理-撤销", "roleManagerTerminate");
        PageElementResource roleManagerGrantUrlAccessResource = new PageElementResource("角色管理-分配路径访问权限资源","roleManagerGrantUrlAccessResource");
        PageElementResource roleManagerGrantMenuResource = new PageElementResource("角色管理-分配菜单权限资源","roleManagerGrantMenuResource");
        PageElementResource roleManagerPageElementResource = new PageElementResource("角色管理-分配页面元素权限资源", "roleManagerGrantPageElementResource");
        PageElementResource roleManagerPagePermission = new PageElementResource("角色管理-分配权限", "roleManagerGrantPermission");
        PageElementResource roleManagerQuery = new PageElementResource("角色管理-查询", "roleManagerQuery");
        return Lists.newArrayList(//
                roleManagerAdd,//
                roleManagerUpdate,//
                roleManagerTerminate,//
                roleManagerGrantUrlAccessResource,//
                roleManagerGrantMenuResource,//
                roleManagerPageElementResource,//
                roleManagerPagePermission,//
                roleManagerQuery
        );
    }

    private List<PageElementResource> createPageElementResourcesOfUser() {
        PageElementResource userManagerAdd = new PageElementResource("用户管理-添加", "userManagerAdd");
        PageElementResource userManagerUpdate = new PageElementResource("用户管理-修改", "userManagerUpdate");
        PageElementResource userManagerTerminate = new PageElementResource("用户管理-撤销", "userManagerTerminate");
        PageElementResource userManagerGrantRole = new PageElementResource("用户管理-分配角色", "userManagerGrantRole");
        PageElementResource userManagerGrantPermission = new PageElementResource("用户管理-分配权限", "userManagerGrantPermission");
        PageElementResource userManagerResetPassword = new PageElementResource("用户管理-重置密码", "userManagerResetPassword");
        PageElementResource userManagerSuspend = new PageElementResource("用户管理-挂起", "userManagerSuspend");
        PageElementResource userManagerActivate = new PageElementResource("用户管理-激活", "userManagerActivate");
        PageElementResource userManagerQuery = new PageElementResource("用户管理-查询", "userManagerQuery");

        return Lists.newArrayList(//
                userManagerAdd,//
                userManagerUpdate,//
                userManagerTerminate,//
                userManagerGrantRole,//
                userManagerGrantPermission,//
                userManagerResetPassword,//
                userManagerSuspend,//
                userManagerActivate,//
                userManagerQuery
        );
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

    private List<MenuResource> createMenuResource() {
    	List<MenuResource> menuResources = new ArrayList<MenuResource>();
    	for (SystemInit.MenuResource each : getParentMenuResources()) {
    		MenuResource menuResource = transformMenuResourceEntity(each);
    		securityConfigApplication.createSecurityResource(menuResource);
    		menuResources.add(menuResource);
    		createChildrenMenuResource(menuResource, each, menuResources);
    	}
        return menuResources;
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
    			menuResources.add(children);
    			securityConfigApplication.createChildToParent(children, menuResource.getId());
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

    /**
     * 菜单相关的URL
     * @return
     */
    private List<UrlAccessResource> createMenuResourceUrls() {
        UrlAccessResource allMenuUrls = new UrlAccessResource("所有的菜单资源", "auth/menu/**");
        UrlAccessResource addMenuUrls = new UrlAccessResource("添加菜单资源", "auth/menu/add.koala");
        UrlAccessResource addChildMenuUrls = new UrlAccessResource("添加子菜单资源", "auth/menu/addChildToParent.koala");
        UrlAccessResource changePropsMenuUrls = new UrlAccessResource("修改菜单资源", "auth/menu/update.koala");
        UrlAccessResource terminateMenuUrls = new UrlAccessResource("撤销菜单资源", "auth/menu/terminate.koala");
        UrlAccessResource findAllMenuTreeUrls = new UrlAccessResource("查找菜单树", "auth/menu/findAllMenusTree.koala");
        UrlAccessResource grantPermisssionsToMenuResourceUrls = new UrlAccessResource("菜单授权权限", "auth/menu/grantPermisssionsToMenuResource.koala");
        UrlAccessResource terminatePermissionsFromMenuResource = new UrlAccessResource("菜单撤销权限", "auth/menu/terminatePermissionsFromMenuResource.koala");
        UrlAccessResource pagingQueryGrantPermissionsByMenuResourceIdUrls = new UrlAccessResource("根据菜单查询已经授权的权限", "auth/menu/pagingQueryGrantPermissionsByMenuResourceId.koala");
        UrlAccessResource pagingQueryNotGrantPermissionsByMenuResourceId = new UrlAccessResource("根据菜单查询还未授权的权限", "auth/menu/pagingQueryNotGrantPermissionsByMenuResourceId.koala");

        return Lists.newArrayList(
                allMenuUrls,//
                addMenuUrls,//
                addChildMenuUrls,//
                changePropsMenuUrls,//
                terminateMenuUrls,//
                findAllMenuTreeUrls,//
                grantPermisssionsToMenuResourceUrls,//
                terminatePermissionsFromMenuResource,//
                pagingQueryGrantPermissionsByMenuResourceIdUrls,//
                pagingQueryNotGrantPermissionsByMenuResourceId);

    }

    /**
     * 页面元素相关的URL
     * @return
     */
    private List<UrlAccessResource> createPageElementResourceUrls() {
        UrlAccessResource allPageUrls = new UrlAccessResource("所有的页面元素资源", "/auth/page/**");
        UrlAccessResource addPageUrls = new UrlAccessResource("添加页面元素资源", "/auth/page/add.koala");
        UrlAccessResource changePropsPageUrls = new UrlAccessResource("修改页面元素资源", "/auth/page/update.koala");
        UrlAccessResource terminatePageUrls = new UrlAccessResource("撤销页面元素资源", "/auth/page/terminate.koala");
        UrlAccessResource pagingQueryPageUrls = new UrlAccessResource("分页查询页面元素权限资源", "/auth/page/pagingQuery.koala");
        UrlAccessResource grantPermisssionsToPageElementResource = new UrlAccessResource("为页面元素资源授予权限", "/auth/page/grantPermisssionsToPageElementResource.koala");
        UrlAccessResource terminatePermissionsFromPageElementResource = new UrlAccessResource("从页面元素资源中撤销权限", "/auth/page/terminatePermissionsFromPageElementResource.koala");
        UrlAccessResource pagingQueryGrantPermissionsByPageElementResourceId = new UrlAccessResource("根据页面元素权限资源ID分页查询已经授权的权限", "/auth/page/pagingQueryNotGrantPermissionsByPageElementResourceId.koala");
        UrlAccessResource pagingQueryNotGrantPermissionsByPageElementResourceId = new UrlAccessResource("根据页面元素权限资源ID分页查询还未授权的权限", "/auth/page/pagingQueryNotGrantPermissionsByPageElementResourceId.koala");
        return Lists.newArrayList(
                allPageUrls,//
                addPageUrls,//
                changePropsPageUrls,//
                terminatePageUrls,//
                pagingQueryPageUrls,//
                grantPermisssionsToPageElementResource,//
                terminatePermissionsFromPageElementResource,//
                pagingQueryGrantPermissionsByPageElementResourceId,//
                pagingQueryNotGrantPermissionsByPageElementResourceId);
    }

    /**
     * 权限相关的URL
     * @return
     */
    private List<UrlAccessResource> createPermissionUrls() {
        UrlAccessResource allPermissionUrls = new UrlAccessResource("所有的权限", "/auth/permission/**");
        UrlAccessResource addPermissionUrls = new UrlAccessResource("添加权限", "auth/permission/add.koala");
        UrlAccessResource changePropsPermissionUrls = new UrlAccessResource("修改权限", "auth/permission/update.koala");
        UrlAccessResource terminatePermissionUrls = new UrlAccessResource("撤销权限", "auth/permission/terminate.koala");
        UrlAccessResource pagingQueryPermissionUrls = new UrlAccessResource("分页查询权限", "auth/permission/pagingQuery.koala");
        return Lists.newArrayList(
                allPermissionUrls,//
                addPermissionUrls,//
                changePropsPermissionUrls,//
                terminatePermissionUrls,//
                pagingQueryPermissionUrls);
    }


    /**
     * 角色相关的URL
     * @return
     */
    private List<UrlAccessResource> createRoleUrls() {
        UrlAccessResource addRoleUrls = new UrlAccessResource("添加角色", "/auth/role/add.koala");
        UrlAccessResource updateRoleUrls = new UrlAccessResource("修改角色", "/auth/role/update.koala");
        UrlAccessResource terminateRoleUrls = new UrlAccessResource("撤销角色", "/auth/role/terminate.koala");
        UrlAccessResource pagingQueryRoleUrls = new UrlAccessResource("查询所有得角色", "/auth/role/pagingQuery.koala");
        UrlAccessResource findMenuResourceTreeSelectItemByRoleId = new UrlAccessResource("根据角色查询所有带选中的菜单", "/auth/role/findMenuResourceTreeSelectItemByRoleId.koala");
        UrlAccessResource grantMenuResourcesToRole = new UrlAccessResource("为角色授权菜单", "/auth/role/grantMenuResourcesToRole.koala");
        UrlAccessResource grantUrlAccessResourcesToRole = new UrlAccessResource("为角色授权URL访问资源", "/auth/role/grantUrlAccessResourcesToRole.koala");
        UrlAccessResource terminateUrlAccessResourcesFromRole = new UrlAccessResource("从角色中撤销Url访问权限资源", "/auth/role/terminateUrlAccessResourcesFromRole.koala");
        UrlAccessResource pagingQueryGrantUrlAccessResourcesByRoleId = new UrlAccessResource("查出已经授权的URL访问权限资源", "/auth/role/pagingQueryGrantUrlAccessResourcesByRoleId.koala");
        UrlAccessResource pagingQueryNotGrantUrlAccessResourcesByRoleId = new UrlAccessResource("查出没有授权的URL访问权限资源", "/auth/role/pagingQueryNotGrantUrlAccessResourcesByRoleId.koala");
        UrlAccessResource terminatePermissionsFromRole = new UrlAccessResource("从角色中撤销权限", "/auth/role/terminatePermissionsFromRole.koala");
        UrlAccessResource pagingQueryGrantPermissionsByRoleId = new UrlAccessResource("根据角色ID分页查询已经授权的权限", "/auth/role/pagingQueryGrantPermissionsByRoleId.koala");
        UrlAccessResource pagingQueryNotGrantPermissionsByRoleId = new UrlAccessResource("根据角色ID分页查询还未授权的权限", "/auth/role/pagingQueryNotGrantPermissionsByRoleId.koala");
        UrlAccessResource grantPageElementResourcesToRole = new UrlAccessResource("为角色授权页面元素权限资源", "/auth/role/grantPageElementResourcesToRole.koala");
        UrlAccessResource terminatePageElementResourcesFromRole = new UrlAccessResource("从角色中撤销页面元素权限资源", "/auth/role/terminatePageElementResourcesFromRole.koala");
        UrlAccessResource pagingQueryGrantPageElementResourcesByRoleId = new UrlAccessResource(" 根据角色ID分页查询已经授权的页面元素权限资源", "/auth/role/pagingQueryGrantPageElementResourcesByRoleId.koala");
        UrlAccessResource pagingQueryNotGrantPageElementResourcesByRoleId = new UrlAccessResource("根据角色ID分页查询还未授权的页面元素权限资源", "/auth/role/pagingQueryNotGrantPageElementResourcesByRoleId.koala");

        return Lists.newArrayList(
//                allRoleUrls,//
                addRoleUrls,//
                updateRoleUrls,//
                terminateRoleUrls,//
                pagingQueryRoleUrls,//
                findMenuResourceTreeSelectItemByRoleId,//
                grantMenuResourcesToRole,//
                grantUrlAccessResourcesToRole,//
                terminateUrlAccessResourcesFromRole,//
                pagingQueryGrantUrlAccessResourcesByRoleId,//
                pagingQueryNotGrantUrlAccessResourcesByRoleId,//
                terminatePermissionsFromRole,//
                pagingQueryGrantPermissionsByRoleId,//
                pagingQueryNotGrantPermissionsByRoleId,//
                grantPageElementResourcesToRole,//
                terminatePageElementResourcesFromRole,//
                pagingQueryGrantPageElementResourcesByRoleId,//
                pagingQueryNotGrantPageElementResourcesByRoleId);

    }

    /**
     * URL访问资源本身的URL
     * @return
     */
    private List<UrlAccessResource> createUrlAccessResourceUrls() {
        UrlAccessResource allUrls = new UrlAccessResource("所有的URL访问资源", "/auth/url/**");
        UrlAccessResource addUrls = new UrlAccessResource("添加URL资源", "/auth/url/add.koala");
        UrlAccessResource updateUrls = new UrlAccessResource("更新URL访问资源", "/auth/url/update.koala");
        UrlAccessResource terminateUrls = new UrlAccessResource("撤销URL访问资源", "/auth/url/terminate.koala");
        UrlAccessResource pagingQueryUrls = new UrlAccessResource("分页查询URL资源", "/auth/url/pagingQuery.koala");
        UrlAccessResource grantPermisssionsToUrlAccessResource = new UrlAccessResource("为URL访问权限资源授权权限", "/auth/url/grantPermisssionsToUrlAccessResource.koala");
        UrlAccessResource terminatePermissionsFromUrlAccessResource = new UrlAccessResource("从URL访问权限资源中撤销权限", "/auth/url/terminatePermissionsFromUrlAccessResource.koala");
        UrlAccessResource pagingQueryGrantPermissionsByUrlAccessResourceId = new UrlAccessResource("通过URL访问权限资源分页查询已经授权的权限", "/auth/url/pagingQueryGrantPermissionsByUrlAccessResourceId.koala");
        UrlAccessResource pagingQueryNotGrantPermissionsByUrlAccessResourceId = new UrlAccessResource("通过URL访问权限资源分页查询还未授权的权限", "/auth/url/pagingQueryNotGrantPermissionsByUrlAccessResourceId.koala");
        return Lists.newArrayList(
                allUrls,//
                addUrls,//
                updateUrls,//
                terminateUrls,//
                pagingQueryUrls,//
                grantPermisssionsToUrlAccessResource,//
                terminatePermissionsFromUrlAccessResource,//
                pagingQueryGrantPermissionsByUrlAccessResourceId,//
                pagingQueryNotGrantPermissionsByUrlAccessResourceId);
    }

    /**
     * 用户相关的URL。
     * @return
     */
    private List<UrlAccessResource> createUserUrls() {
        UrlAccessResource allUserUrl = new UrlAccessResource("用户管理", "/auth/user/**");
        UrlAccessResource userAddUrl = new UrlAccessResource("用户管理-添加", "/auth/user/add.koala");
        UrlAccessResource userUpdateUrl = new UrlAccessResource("用户管理-更新", "/auth/user/update.koala");
        UrlAccessResource userTerminateUrl = new UrlAccessResource("用户管理-撤销", "/auth/user/terminate.koala");
        UrlAccessResource userPagingqueryUrl = new UrlAccessResource("用户管理-分页查询", "/auth/user/pagingQuery.koala");
        UrlAccessResource userUpdatePasswordUrl = new UrlAccessResource("用户管理-更新密码", "/auth/user/updatePassword.koala");
        UrlAccessResource userResetPasswordUrl = new UrlAccessResource("用户管理-重置密码", "/auth/user/resetPassword.koala");
        UrlAccessResource userActivateUrl = new UrlAccessResource("用户管理-激活", "/auth/user/activate.koala");
        UrlAccessResource userActivatesUrl = new UrlAccessResource("用户管理-激动所有", "/auth/user/activates.koala");
        UrlAccessResource userSuspendsUrl = new UrlAccessResource("用户管理-挂起所有", "/auth/user/suspends.koala");
        UrlAccessResource userGrantRoleUrl = new UrlAccessResource("用户管理-授权一个角色", "/auth/user/grantRole.koala");
        UrlAccessResource userGrantRolesUrl = new UrlAccessResource("用户管理-授权多个角色", "/auth/user/grantRoles.koala");
        UrlAccessResource userGrantPermissionUrl = new UrlAccessResource("用户管理-授权一个权限", "/auth/user/grantPermission.koala");
        UrlAccessResource userGrantPermissionsUrl = new UrlAccessResource("用户管理-授权多个权限", "/auth/user/grantPermissions.koala");
        UrlAccessResource userTerminateRoleByUserUrl = new UrlAccessResource("用户管理-撤销一个角色",
                "/auth/user/terminateRoleByUser.koala");
        UrlAccessResource userTerminatePermissionByUserUrl = new UrlAccessResource("用户管理-撤销一个权限",
                "/auth/user/terminatePermissionByUser.koala");
        UrlAccessResource userTerminateRolesByUserUrl = new UrlAccessResource("用户管理-撤销多个角色",
                "/auth/user/suspend/terminateRolesByUser.koala");
        UrlAccessResource userTerminatePermissionsByUser = new UrlAccessResource("用户管理-撤销多个权限",
                "/auth/user/terminatePermissionsByUser.koala");
        UrlAccessResource userPagingQueryGrantRoleByUserIdUrl = new UrlAccessResource("用户管理-查找授权的角色",
                "/auth/user/pagingQueryGrantRoleByUserId.koala");
        UrlAccessResource userPagingQueryGrantPermissionByUserIdUrl = new UrlAccessResource("用户管理-查找授权的权限",
                "/auth/user/pagingQueryGrantPermissionByUserId.koala");
        UrlAccessResource userPagingQueryNotGrantRolesUrl = new UrlAccessResource("用户管理-查找没有授权的角色",
                "/auth/user/pagingQueryNotGrantRoles.koala");
        UrlAccessResource userPagingQueryNotGrantPermissionsUrl = new UrlAccessResource("用户管理-查找没有授权的权限",
                "/auth/user/pagingQueryNotGrantPermissions.koala");

        return Lists.newArrayList(allUserUrl,//
                userAddUrl,//
                userUpdateUrl,//
                userTerminateUrl,//
                userPagingqueryUrl,//
                userUpdatePasswordUrl,//
                userTerminateUrl,//
                userPagingqueryUrl,//
                userResetPasswordUrl,//
                userGrantRolesUrl,//
                userTerminatePermissionsByUser,//
                userTerminateRoleByUserUrl,//
                userTerminateRolesByUserUrl,//
                userGrantPermissionsUrl,//
                userActivateUrl,//
                userActivatesUrl,//
                userSuspendsUrl,//
                userGrantRoleUrl,//
                userGrantPermissionUrl,//
                userTerminatePermissionByUserUrl,//
                userPagingQueryGrantRoleByUserIdUrl,//
                userPagingQueryGrantPermissionByUserIdUrl,//
                userPagingQueryNotGrantRolesUrl,//
                userPagingQueryNotGrantPermissionsUrl);
    }

}
