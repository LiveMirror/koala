package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.web.util.AuthUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/menu")
public class MenuResourceController {

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加菜单。
	 * 
	 * @param menuResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(MenuResourceDTO menuResourceDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveMenuResourceDTO(menuResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 选择父菜单，为其添加子菜单。
	 * 
	 * @param child
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addChildToParent")
	public Map<String, Object> addChildToParent(MenuResourceDTO child, Long parentId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveChildToParent(child, parentId);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 更新菜单
	 * 
	 * @param menuResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(MenuResourceDTO menuResourceDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updateMenuResourceDTO(menuResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 批量撤销菜单
	 * 
	 * @param menuResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminate(@RequestBody MenuResourceDTO[] menuResourceDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateMenuResourceDTOs(menuResourceDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 查找菜单树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllMenusTree")
	public Map<String, Object> findAllMenusTree() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<MenuResourceDTO> menuResourceDTOs = securityAccessFacade.findAllMenusTree();
		dataMap.put("data", menuResourceDTOs);
		return dataMap;
	}

	/**
	 * 查找用户在某个角色下得所有菜单资源。
	 * 
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAllMenusByUserAsRole", method = RequestMethod.GET)
	public Map<String, Object> findAllMenusByUserAsRole(RoleDTO roleDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<MenuResourceDTO> menuResourceDtos = securityAccessFacade.findMenuResourceDTOByUserAccountAsRole(
				AuthUserUtil.getUserAccount(), roleDTO.getRoleId());
		AuthUserUtil.setRoleName(roleDTO.getRoleName());
		dataMap.put("data", menuResourceDtos);
		return dataMap;
	}
	
	/**
	 * 为菜单资源授予权限Permission
	 * 
	 * @param PermissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermisssionsToMenuResource")
	public Map<String, Object> grantPermisssionsToMenuResource(Long[] permissionIds, Long menuResourceId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermisssionsToMenuResource(permissionIds, menuResourceId);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 从菜单资源中撤销权限Permission
	 * 
	 * @param permissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionsFromMenuResource")
	public Map<String, Object> terminatePermissionsFromMenuResource(Long[] permissionIds, Long menuResourceId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminatePermissionsFromMenuResource(permissionIds, menuResourceId);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 分页查询
	 * @param page
	 * @param pagesize
	 * @param UrlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryGrantPermissionsByMenuResourceId")
	public Page<PermissionDTO> pagingQueryGrantPermissionsByMenuResourceId(int page, int pagesize,
			Long menuResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByMenuResourceId(page,
				pagesize, menuResourceId);
		return results;
	}

	@ResponseBody
	@RequestMapping("/pagingQueryNotGrantPermissionsByMenuResourceId")
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByMenuResourceId(int page, int pagesize,
			Long menuResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByMenuResourceId(page,
				pagesize, menuResourceId);
		return results;
	}
}
