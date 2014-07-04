package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/page")
public class PageElementController {
	
	@Inject
	private SecurityAccessFacade securityAccessFacade;
	
	@Inject
	private SecurityConfigFacade securityConfigFacade;
	
	@ResponseBody
	@RequestMapping("/add")
	public Map<String,Object> add(PageElementResourceDTO pageElementResourceDTO){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.savePageElementResourceDTO(pageElementResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Map<String,Object> update(PageElementResourceDTO pageElementResourceDTO){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updatePageElementResourceDTO(pageElementResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String,Object> terminate(@RequestBody PageElementResourceDTO[] pageElementResourceDTOs){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminatePageElementResourceDTOs(pageElementResourceDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/pagingQuery")
	public Page<PageElementResourceDTO> pagingQuery(int page, int pagesize, PageElementResourceDTO pageElementResourceDTO){
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryPageElementResources(page, pagesize,
				pageElementResourceDTO);
		return results;
	}
	
	/**
	 * 为页面元素资源授予权限Permission
	 * 
	 * @param permissionIds
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermisssionsToPageElementResource")
	public Map<String, Object> grantPermisssionsToPageElementResource(Long[] permissionIds, Long pageElementResourceId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermisssionsToPageElementResource(permissionIds, pageElementResourceId);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 从页面元素资源中撤销权限Permission
	 * 
	 * @param permissionIds
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionsFromPageElementResource")
	public Map<String, Object> terminatePermissionsFromPageElementResource(Long[] permissionIds, Long pageElementResourceId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminatePermissionsFromPageElementResource(permissionIds, pageElementResourceId);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 分页查询
	 * @param page
	 * @param pagesize
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryGrantPermissionsByPageElementResourceId")
	public Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByPageElementResourceId(page,
				pagesize, pageElementResourceId);
		return results;
	}

	@ResponseBody
	@RequestMapping("/pagingQueryNotGrantPermissionsByPageElementResourceId")
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByPageElementResourceId(page,
				pagesize, pageElementResourceId);
		return results;
	}
}
