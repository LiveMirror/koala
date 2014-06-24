package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/permission")
public class PermissionController {

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加权限
	 * 
	 * @param permissionDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(PermissionDTO permissionDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.savePermissionDTO(permissionDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 更新权限
	 * 
	 * @param permissionDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(PermissionDTO permissionDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updatePermissionDTO(permissionDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 撤销权限
	 * 
	 * @param permissionDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(PermissionDTO[] permissionDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminatePermissionDTOs(permissionDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 根据条件分页查询权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param permissionDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<PermissionDTO> pagingQuery(int page, int pagesize, PermissionDTO permissionDTO) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissions(page, pagesize, permissionDTO);
		return results;
	}

	/**
	 * 根据用户ID分页查询权限
	 * 
	 * @param page
	 * @param pagesize
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryByUserId")
	public Page<PermissionDTO> pagingQueryPermissionsByUserAccount(int page, int pagesize, Long userId) {
		// String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissionsByUserAccount(page, pagesize, userId);
		return results;
	}

	/**
	 * 根据角色ID分页查询权限
	 * 
	 * @param page
	 * @param pagesize
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryByRoleId")
	public Page<PermissionDTO> pagingQueryPermissionsByRole(int page, int pagesize, Long roleId) {
		// String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissionsByRole(page, pagesize, roleId);
		return results;
	}
}
