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

	/**
	 * 初始化用户
	 * 
	 * @return
	 */
	User initUser();

	/**
	 * 初始化角色
	 * 
	 * @return
	 */
	Role initRole();

	/**
	 * TODO 初始化权限
	 * 
	 * @return
	 */
	List<Permission> initPermissions();

	/**
	 * 初始化菜单资源
	 * 
	 * @return
	 */
	List<MenuResource> initMenuResources();

	/**
	 * 初始化页面元素资源
	 * 
	 * @return
	 */
	List<PageElementResource> initPageElementResources();

	/**
	 * 初始化URL访问资源
	 * 
	 * @return
	 */
	List<UrlAccessResource> initUrlAccessResources();

	/**
	 * 创建参与者
	 * 
	 * @param actor
	 */
	void initActor(Actor actor);
}
