package org.openkoala.security.controller;

import java.util.List;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeRolePropsCommand;
import org.openkoala.security.facade.command.CreateRoleCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
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
	public JsonResult add(CreateRoleCommand command) {
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
	public JsonResult update(ChangeRolePropsCommand command) {
		return securityConfigFacade.changeRoleProps(command);
	}

	/**
	 * TODO 支持批量撤销,待优化。
	 * 
	 * @param roleIds
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public JsonResult terminate(Long[] roleIds) {
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
	public Page<RoleDTO> pagingQuery(int page, int pagesize, RoleDTO roleDTO) {
		Page<RoleDTO> results = securityAccessFacade.pagingQueryRoles(page, pagesize, roleDTO);
		return results;
	}

	/**
	 * 根据用户名查找所有的角色。
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findRolesByUsername", method = RequestMethod.GET)
	public JsonResult findRoleDtosByUsername() {
		JsonResult jsonResult = new JsonResult();
		try {
			List<RoleDTO> results = securityAccessFacade.findRolesByUserAccount(CurrentUser.getUserAccount());
			jsonResult.setData(results);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("根据用户名查找所有的角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("根据用户名查找所有的角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 根据角色ID查询菜单权限资源树带有已经选中项。
	 * 
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMenuResourceTreeSelectItemByRoleId", method = RequestMethod.GET)
	public JsonResult findMenuResourceTreeSelectItemByRoleId(Long roleId) {
		JsonResult jsonResult = new JsonResult();
		try {
			List<MenuResourceDTO> results = securityAccessFacade.findMenuResourceTreeSelectItemByRoleId(roleId);
			jsonResult.setData(results);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("根据角色ID查询菜单权限资源树带有已经选中项成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("根据角色ID查询菜单权限资源树带有已经选中项失败");
		}
		return jsonResult;
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
	public JsonResult grantMenuResourcesToRole(Long roleId, Long[] menuResourceIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantMenuResourcesToRole(roleId, menuResourceIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为角色授权菜单资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为角色授权菜单资源失败");
		}
		return jsonResult;
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
	public JsonResult grantUrlAccessResourcesToRole(Long roleId, Long[] urlAccessResourceIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantUrlAccessResourcesToRole(roleId, urlAccessResourceIds);
			shiroFilterChainManager.initFilterChain();
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为角色授权URL访问权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为角色授权URL访问权限资源失败");
		}
		return jsonResult;
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
	public JsonResult terminateUrlAccessResourcesFromRole(Long roleId, Long[] urlAccessResourceIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateUrlAccessResourcesFromRole(roleId, urlAccessResourceIds);
			shiroFilterChainManager.initFilterChain();
			jsonResult.setSuccess(true);
			jsonResult.setMessage("从角色中撤销URL访问权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("从角色中撤销URL访问权限资源失败");
		}
		return jsonResult;
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
	public Page<UrlAccessResourceDTO> pagingQueryGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId) {
		Page<UrlAccessResourceDTO> results = securityAccessFacade.pagingQueryGrantUrlAccessResourcesByRoleId(page,
				pagesize, roleId);
		return results;
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
	public Page<UrlAccessResourceDTO> pagingQueryNotGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId) {
		Page<UrlAccessResourceDTO> results = securityAccessFacade.pagingQueryNotGrantUrlAccessResourcesByRoleId(page,
				pagesize, roleId);
		return results;
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
	public JsonResult grantPermissionsToRole(Long roleId, Long[] permissionIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermissionsToRole(roleId, permissionIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为角色授权权限成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为角色授权权限失败");
		}
		return jsonResult;
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
	public JsonResult terminatePermissions(Long roleId, Long[] permissionIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissionsFromRole(roleId, permissionIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("从角色中撤销权限成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("从角色中撤销权限失败");
		}
		return jsonResult;
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
	public Page<PermissionDTO> pagingQueryPermissionsByRoleId(int page, int pagesize, Long roleId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByRoleId(page, pagesize, roleId);
		return results;
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
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByRoleId(int page, int pagesize, Long roleId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByRoleId(page, pagesize,
				roleId);
		return results;
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
	public JsonResult grantPageElementResourcesToRole(Long roleId, Long[] pageElementResourceIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPageElementResourcesToRole(roleId, pageElementResourceIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为角色授权页面元素权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为角色授权页面元素权限资源失败");
		}
		return jsonResult;
	}

	/**
	 * 从角色中撤销页面元素权限资源。
	 * 
	 * @param roleId
	 * @param PageElementResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePageElementResourcesFromRole", method = RequestMethod.POST)
	public JsonResult terminatePageElementResourcesFromRole(Long roleId, Long[] pageElementResourceIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePageElementResourcesFromRole(roleId, pageElementResourceIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("从角色中撤销页面元素权限资源成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("从角色中撤销页面元素权限资源失败");
		}
		return jsonResult;
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
	public Page<PageElementResourceDTO> pagingQueryGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId) {
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryGrantPageElementResourcesByRoleId(page,
				pagesize, roleId);
		return results;
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
	public Page<PageElementResourceDTO> pagingQueryNotGrantPageElementResourcesByRoleId(int page, int pagesize,
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
	@ResponseBody
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
	}
}
