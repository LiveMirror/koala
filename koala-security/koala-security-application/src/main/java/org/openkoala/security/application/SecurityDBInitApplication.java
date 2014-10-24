package org.openkoala.security.application;

import org.openkoala.security.core.domain.*;

import java.util.List;

/**
 * 权限功能初始化，它能够让系统可以正常使用。
 *
 * @author lucas
 */
public interface SecurityDBInitApplication {

    /**
     * 初始化用户
     *
     * @return 返回被初始化的用户
     */
    User initUser();

    /**
     * 初始化角色
     *
     * @return 返回被初始化的角色
     */
    Role initRole();

    /**
     * 初始化菜单资源
     *
     * @return 返回初始化菜单资源的集合
     */
    List<MenuResource> initMenuResources();

    /**
     * 初始化页面元素资源
     *
     * @return 返回初始化页面元素资源的集合
     */
    List<PageElementResource> initPageElementResources();

    /**
     * 初始化URL访问资源
     *
     * @return 返回初始化URL访问资源的集合
     */
    List<UrlAccessResource> initUrlAccessResources();
}
