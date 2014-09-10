package org.openkoala.security.controller;


import javax.inject.Inject;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeMenuResourcePropsCommand;
import org.openkoala.security.facade.command.CreateChildMenuResourceCommand;
import org.openkoala.security.facade.command.CreateMenuResourceCommand;
import org.openkoala.security.shiro.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单权限资源控制器。
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/menu")
public class MenuResourceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuResourceController.class);

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加菜单权限资源。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public InvokeResult add(CreateMenuResourceCommand command) {
		return securityConfigFacade.createMenuResource(command);
	}

	/**
	 * 选择父菜单权限资源， 为其添加子菜单权限资源。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addChildToParent", method = RequestMethod.POST)
	public InvokeResult addChildToParent(CreateChildMenuResourceCommand command) {
		return securityConfigFacade.createChildMenuResouceToParent(command);
	}

	/**
	 * 更新菜单权限资源。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public InvokeResult update(ChangeMenuResourcePropsCommand command) {
		return securityConfigFacade.changeMenuResourceProps(command);
	}

	/**
	 * 批量撤销菜单 TODO 捕获详细异常。
	 * 
	 * @param menuResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public InvokeResult terminate(Long[] menuResourceIds) {
		return securityConfigFacade.terminateMenuResources(menuResourceIds);
	}

	/**
	 * 查找菜单树。
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAllMenusTree", method = RequestMethod.GET)
	public InvokeResult findAllMenusTree() {
			return securityAccessFacade.findAllMenusTree();
	}

	/**
	 * 查找用户在某个角色下得所有菜单权限资源。
	 * 
	 * @param roleName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAllMenusByUserAsRole", method = RequestMethod.GET)
	public InvokeResult findAllMenusByUserAsRole(String roleName) {
        System.out.println("roleName = [" + roleName + "]");
        return  securityAccessFacade.findMenuResourceByUserAsRole(CurrentUser.getUserAccount(), roleName);
			//CurrentUser.setRoleName(roleName);
//            CustomAuthoringRealm.ShiroUser shiroUser = CurrentUser.getPrincipal();
//            SimpleAuthorizationInfo simpleAuthorizationInfo =  (SimpleAuthorizationInfo)shiroUser.getAuthorizationInfo();
//            simpleAuthorizationInfo.setRoles(customAuthoringRealm.getRoles(roleName));
//            simpleAuthorizationInfo.setStringPermissions(customAuthoringRealm.getPermissions(CurrentUser.getUserAccount(),roleName));

			//jsonResult.setMessage("查找" + CurrentUser.getUserAccount() + " 在某个角色下得所有菜单权限资源成功。");
			//jsonResult.setMessage("查找" + CurrentUser.getUserAccount() + " 在某个角色下得所有菜单权限资源失败。");
	}

	/**
	 * 为菜单权限资源资源授予权限Permission。
	 * 
	 * @param permissionId
	 * @param menuResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPermisssionsToMenuResource", method = RequestMethod.POST)
	public InvokeResult grantPermisssionsToMenuResource(Long permissionId, Long menuResourceId) {
		return	securityConfigFacade.grantPermisssionsToMenuResource(permissionId, menuResourceId);
	}

	/**
	 * 从菜单权限资源中撤销权限Permission。
	 * 
	 * @param permissionId
	 * @param menuResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePermissionsFromMenuResource", method = RequestMethod.POST)
	public InvokeResult terminatePermissionsFromMenuResource(Long permissionId, Long menuResourceId) {		
		return	securityConfigFacade.terminatePermissionsFromMenuResource(permissionId, menuResourceId);
	}

	/**
	 * 通过菜单权限资源ID分页查询已经授权的Permission。
	 * 
	 * @param page
	 * @param pagesize
	 * @param menuResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantPermissionsByMenuResourceId", method = RequestMethod.GET)
	public InvokeResult pagingQueryGrantPermissionsByMenuResourceId(int page, int pagesize, Long menuResourceId) {
		return  securityAccessFacade.pagingQueryGrantPermissionsByMenuResourceId(page, pagesize,menuResourceId);
	}

	/**
	 * 通过菜单权限资源ID分页查询还未授权的Permission。
	 * 
	 * @param page
	 * @param pagesize
	 * @param menuResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantPermissionsByMenuResourceId", method = RequestMethod.GET)
	public InvokeResult pagingQueryNotGrantPermissionsByMenuResourceId(int page, int pagesize,	Long menuResourceId) {
		return securityAccessFacade.pagingQueryNotGrantPermissionsByMenuResourceId(page,pagesize, menuResourceId);
	}
}
