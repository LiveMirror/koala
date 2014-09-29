package org.openkoala.security.application;

import org.openkoala.security.core.domain.*;

import java.util.List;

/**
 * 权限数据初始化应用层
 * 
 * @author luzhao
 * 
 */
public interface SecurityDBInitApplication {
	
    User initUser();

	Role initRole();

    /**
     * TODO 赞时没有初始化数据
     * @return
     */
	List<Permission> initPermissions();

	List<MenuResource> initMenuResources();

	List<PageElementResource> initPageElementResources();

	List<UrlAccessResource> initUrlAccessResources();

    void initActor(Actor actor);
}
