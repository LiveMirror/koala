package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/url")
public class UrlAccessController {

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	/**
	 * 添加
	 * 
	 * @param urlAccessResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(UrlAccessResourceDTO urlAccessResourceDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveUrlAccessResourceDTO(urlAccessResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 更新
	 * 
	 * @param urlAccessResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(UrlAccessResourceDTO urlAccessResourceDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updateUrlAccessResourceDTO(urlAccessResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 撤销
	 * 
	 * @param urlAccessResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminate(@RequestBody UrlAccessResourceDTO[] urlAccessResourceDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateUrlAccessResourceDTOs(urlAccessResourceDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 根据条件分页查询Url资源。
	 * 
	 * @param page
	 * @param pagesize
	 * @param urlAccessResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<UrlAccessResourceDTO> pagingQuery(int page, int pagesize, UrlAccessResourceDTO urlAccessResourceDTO) {
		Page<UrlAccessResourceDTO> results = securityAccessFacade.pagingQueryUrlAccessResources(page, pagesize,
				urlAccessResourceDTO);
		return results;
	}

	@ResponseBody
	@RequestMapping("list")
	public Map<String, Object> list() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<UrlAccessResourceDTO> accessResourceDTOs = securityAccessFacade.findAllUrlAccessResources();
		dataMap.put("data", accessResourceDTOs);
		return dataMap;
	}

	/**
	 * 为URL访问资源授权权限Permission
	 * 
	 * @param PermissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermisssionsToUrlAccessResource")
	public Map<String, Object> grantPermisssionsToUrlAccessResource(Long[] PermissionIds, Long urlAccessResourceId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermisssionsToUrlAccessResource(PermissionIds, urlAccessResourceId);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 从URL访问资源中撤销权限Permission
	 * 
	 * @param PermissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionsFromUrlAccessResource")
	public Map<String, Object> terminatePermissionsFromUrlAccessResource(Long[] PermissionIds, Long urlAccessResourceId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminatePermissionsFromUrlAccessResource(PermissionIds, urlAccessResourceId);
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
	@RequestMapping("/pagingQueryGrantPermissionsByUrlAccessResourceId")
	public Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResource(int page, int pagesize,
			Long UrlAccessResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByUrlAccessResourceId(page,
				pagesize, UrlAccessResourceId);
		return results;
	}

	@ResponseBody
	@RequestMapping("/pagingQueryNotGrantPermissionsByUrlAccessResourceId")
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResource(int page, int pagesize,
			Long UrlAccessResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByUrlAccessResourceId(page,
				pagesize, UrlAccessResourceId);
		return results;
	}

}
