package org.openkoala.security.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeMenuResourcePropsCommand;
import org.openkoala.security.facade.command.CreateChildMenuResourceCommand;
import org.openkoala.security.facade.command.CreateMenuResourceCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.shiro.util.AuthUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
	 * @param menuResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(CreateMenuResourceCommand command) {
		return securityConfigFacade.createMenuResource(command);
	}

	/**
	 * 选择父菜单权限资源， 为其添加子菜单权限资源。
	 * 
	 * @param child
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addChildToParent")
	public JsonResult addChildToParent(CreateChildMenuResourceCommand command) {
		return securityConfigFacade.createChildMenuResouceToParent(command);
	}

	/**
	 * 更新菜单权限资源。
	 * 
	 * @param menuResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(ChangeMenuResourcePropsCommand command) {
		return securityConfigFacade.changeMenuResourceProps(command);
	}

	/**
	 * 批量撤销菜单 TODO 捕获详细异常。
	 * 
	 * @param menuResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public JsonResult terminate(@RequestBody Long[] menuResourceIds) {
		return securityConfigFacade.terminateMenuResources(menuResourceIds);
	}

	/**
	 * 查找菜单树。
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllMenusTree")
	public JsonResult findAllMenusTree() {
		JsonResult jsonResult = new JsonResult();
		try {
			List<MenuResourceDTO> results = securityAccessFacade.findAllMenusTree();
			jsonResult.setData(results);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("查找菜单权限资源树成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("查找菜单权限资源树失败。");
		}
		return jsonResult;
	}

	/**
	 * 查找用户在某个角色下得所有菜单权限资源。
	 * 
	 * @param roleDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAllMenusByUserAsRole", method = RequestMethod.GET)
	public JsonResult findAllMenusByUserAsRole(RoleDTO roleDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			List<MenuResourceDTO> results = securityAccessFacade.findMenuResourceByUserAsRole(
					AuthUserUtil.getUserAccount(), roleDTO.getRoleId());
			AuthUserUtil.setRoleName(roleDTO.getRoleName());
			jsonResult.setData(results);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("查找" + AuthUserUtil.getUserAccount() + " 在某个角色下得所有菜单权限资源成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("查找" + AuthUserUtil.getUserAccount() + " 在某个角色下得所有菜单权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 为菜单权限资源资源授予权限Permission。
	 * 
	 * @param PermissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermisssionsToMenuResource")
	public JsonResult grantPermisssionsToMenuResource(Long permissionId, Long menuResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermisssionsToMenuResource(permissionId, menuResourceId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为菜单权限资源授予权限Permission成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为菜单权限资源授予权限Permission失败");
		}
		return jsonResult;
	}

	/**
	 * 从菜单权限资源中撤销权限Permission。
	 * 
	 * @param permissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionsFromMenuResource")
	public JsonResult terminatePermissionsFromMenuResource(Long permissionId, Long menuResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissionsFromMenuResource(permissionId, menuResourceId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("从菜单权限资源中撤销权限Permission成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("从菜单权限资源中撤销权限Permission失败");
		}
		return jsonResult;
	}

	/**
	 * 通过菜单权限资源ID分页查询已经授权的Permission。
	 * 
	 * @param page
	 * @param pagesize
	 * @param UrlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryGrantPermissionsByMenuResourceId")
	public Page<PermissionDTO> pagingQueryGrantPermissionsByMenuResourceId(int page, int pagesize, Long menuResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByMenuResourceId(page, pagesize,
				menuResourceId);
		return results;
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
	@RequestMapping("/pagingQueryNotGrantPermissionsByMenuResourceId")
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByMenuResourceId(int page, int pagesize,
			Long menuResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByMenuResourceId(page,
				pagesize, menuResourceId);
		return results;
	}
}
