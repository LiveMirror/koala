package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(UrlAccessResourceDTO[] urlAccessResourceDTOs) {
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
}
