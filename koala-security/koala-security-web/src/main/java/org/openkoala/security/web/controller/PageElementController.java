package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
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
	@RequestMapping("pagingQuery")
	public Page<PageElementResourceDTO> pagingQuery(int page, int pagesize, PageElementResourceDTO pageElementResourceDTO){
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryPageElementResources(page, pagesize,
				pageElementResourceDTO);
		return results;
	}
}
