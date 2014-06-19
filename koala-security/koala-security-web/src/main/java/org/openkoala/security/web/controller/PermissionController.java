package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
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

	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(PermissionDTO permissionDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.savePermissionDTO(permissionDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(PermissionDTO permissionDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updatePermissionDTO(permissionDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(PermissionDTO[] permissionDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminatePermissionDTOs(permissionDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<PermissionDTO> pagingQuery(int currentPage, int pageSize, PermissionDTO permissionDTO) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissions(currentPage, pageSize, permissionDTO);
		return results;
	}
	
	@ResponseBody
	@RequestMapping("/pagingQueryByUserId")
	public Page<PermissionDTO> pagingQueryPermissionsByUserAccount(int currentPage, int pageSize,Long userId){
//		String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissionsByUserAccount(currentPage,pageSize,userId);
		return results;
	}
}
