package org.openkoala.security.controller;

import javax.inject.Inject;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeRolePropsCommand;
import org.openkoala.security.facade.command.CreateRoleCommand;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.openkoala.security.shiro.extend.ShiroFilterChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 角色控制器
 *
 * @author luzhao
 *
 */
@Controller
@RequestMapping("/auth/role")
public class RoleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	@Inject
	private ShiroFilterChainManager shiroFilterChainManager;

	/**
	 * 添加角色
	 *
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public InvokeResult add(CreateRoleCommand command) {
		return securityConfigFacade.createRole(command);
	}

	/**
	 * 更新角色
	 *
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public InvokeResult update(ChangeRolePropsCommand command) {
		return securityConfigFacade.changeRoleProps(command);
	}

	/**
	 * TODO 支持批量撤销,待优化。
	 *
	 * @param roleIds
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public InvokeResult terminate(Long[] roleIds) {
		return securityConfigFacade.terminateRoles(roleIds);
	}

	/**
	 * 根据条件分页查询所有的角色
	 *
	 * @param page
	 * @param pagesize
	 * @param roleDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
	public InvokeResult pagingQuery(int page, int pagesize, RoleDTO roleDTO) {
		return securityAccessFacade.pagingQueryRoles(page, pagesize, roleDTO);
	}

	/**
	 * 根据用户名查找所有的角色。
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findRolesByUsername", method = RequestMethod.GET)
	public InvokeResult findRoleDtosByUsername() {
		return securityAccessFacade.findRolesByUserAccount(CurrentUser.getUserAccount());
	}

	/**
	 * 根据角色ID查询菜单权限资源树带有已经选中项。
	 *
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMenuResourceTreeSelectItemByRoleId", method = RequestMethod.GET)
	public InvokeResult findMenuResourceTreeSelectItemByRoleId(Long roleId) {
			return  securityAccessFacade.findMenuResourceTreeSelectItemByRoleId(roleId);
		}

	/**
	 * 为角色授权菜单资源。
	 *
	 * @param roleId
	 * @param menuResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantMenuResourcesToRole", method = RequestMethod.POST)
	public InvokeResult grantMenuResourcesToRole(Long roleId, Long[] menuResourceIds) {
			return securityConfigFacade.grantMenuResourcesToRole(roleId, menuResourceIds);
		}

	/**
	 * 为角色授权URL访问权限资源
	 *
	 * @param roleId
	 * @param urlAccessResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantUrlAccessResourcesToRole", method = RequestMethod.POST)
	public InvokeResult grantUrlAccessResourcesToRole(Long roleId, Long[] urlAccessResourceIds) {
		shiroFilterChainManager.initFilterChain();
		return	securityConfigFacade.grantUrlAccessResourcesToRole(roleId, urlAccessResourceIds);
	}

	/**
	 * 从角色中撤销Url访问权限资源。
	 *
	 * @param roleId
	 * @param urlAccessResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminateUrlAccessResourcesFromRole", method = RequestMethod.POST)
	public InvokeResult terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds) {
			shiroFilterChainManager.initFilterChain();
			return securityConfigFacade.terminateUrlAccessResourcesFromRole(roleId, urlAccessResourceIds);
	}

	/**
	 * 查出已经授权的URL访问权限资源。
	 *
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantUrlAccessResourcesByRoleId", method = RequestMethod.GET)
	public InvokeResult pagingQueryGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId) {
		return securityAccessFacade.pagingQueryGrantUrlAccessResourcesByRoleId(page,pagesize, roleId);
	}

	/**
	 * 查出没有授权的URL访问权限资源。
	 *
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantUrlAccessResourcesByRoleId", method = RequestMethod.GET)
	public InvokeResult pagingQueryNotGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId) {
		return securityAccessFacade.pagingQueryNotGrantUrlAccessResourcesByRoleId(page,	pagesize, roleId);
	}

	/**
	 * 为角色授权权限
	 *
	 * @param roleId
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPermissionsToRole", method = RequestMethod.POST)
	public InvokeResult grantPermissionsToRole(Long roleId, Long[] permissionIds) {
		return securityConfigFacade.grantPermissionsToRole(roleId, permissionIds);
	}

	/**
	 * 从角色中撤销权限
	 *
	 * @param roleId
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePermissionsFromRole", method = RequestMethod.POST)
	public InvokeResult terminatePermissions(Long roleId, Long[] permissionIds) {
			return	securityConfigFacade.terminatePermissionsFromRole(roleId, permissionIds);
	}

	/**
	 * 根据角色ID分页查询已经授权的权限
	 *
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantPermissionsByRoleId", method = RequestMethod.GET)
	public InvokeResult pagingQueryPermissionsByRoleId(int page, int pagesize, Long roleId) {
		return securityAccessFacade.pagingQueryGrantPermissionsByRoleId(page, pagesize, roleId);
	}

	/**
	 * 根据角色ID分页查询还未授权的权限
	 *
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantPermissionsByRoleId", method = RequestMethod.GET)
	public InvokeResult pagingQueryNotGrantPermissionsByRoleId(int page, int pagesize, Long roleId) {
		return securityAccessFacade.pagingQueryNotGrantPermissionsByRoleId(page, pagesize,	roleId);
	}

	/**
	 * 为角色授权页面元素权限资源
	 *
	 * @param roleId
	 * @param pageElementResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPageElementResourcesToRole", method = RequestMethod.POST)
	public InvokeResult grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds) {
		return securityConfigFacade.grantPageElementResourcesToRole(roleId, pageElementResourceIds);
	}

	/**
	 * 从角色中撤销页面元素权限资源。
	 *
	 * @param roleId
	 * @param pageElementResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePageElementResourcesFromRole", method = RequestMethod.POST)
	public InvokeResult terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds) {
		return	securityConfigFacade.terminatePageElementResourcesFromRole(roleId, pageElementResourceIds);
	}

	/**
	 * 根据角色ID分页查询已经授权的页面元素权限资源
	 *
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantPageElementResourcesByRoleId", method = RequestMethod.GET)
	public InvokeResult pagingQueryGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId) {
		return securityAccessFacade.pagingQueryGrantPageElementResourcesByRoleId(page,	pagesize, roleId);
	}

	/**
	 * 根据角色ID分页查询还未授权的页面元素权限资源
	 *
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantPageElementResourcesByRoleId", method = RequestMethod.GET)
	public InvokeResult pagingQueryNotGrantPageElementResourcesByRoleId(int page, int pagesize,
			Long roleId) {
		return securityAccessFacade.pagingQueryNotGrantPageElementResourcesByRoleId(page, pagesize, roleId);
	}

	// ==================TODO==================

	/**
	 * TODO 还未实现 为角色授权方法调用权限资源。
	 *
	 * @param roleId
	 * @param menuResourceIds
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/grantMethodInvocationResourcesToRole", method = RequestMethod.GET)
	public JsonResult grantMethodInvocationResourcesToRole(Long roleId, Long[] menuResourceIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantMethodInvocationResourcesToRole(roleId, menuResourceIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为角色授权方法调用权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为角色授权方法调用权限资源失败");
		}
		return jsonResult;
	}*/
}