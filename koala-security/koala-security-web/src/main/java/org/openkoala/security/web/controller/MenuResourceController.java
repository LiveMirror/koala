package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.springframework.stereotype.Controller;
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

	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(MenuResourceDTO menuResourceDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveMenuResourceDTO(menuResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(MenuResourceDTO menuResourceDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updateMenuResourceDTO(menuResourceDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(MenuResourceDTO[] menuResourceDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateMenuResourceDTOs(menuResourceDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<MenuResourceDTO> pagingQuery(int currentPage, int pageSize, MenuResourceDTO menuResourceDTO) {
		Page<MenuResourceDTO> results = securityAccessFacade.pagingQueryMenuResources(currentPage, pageSize, menuResourceDTO);
		return results;
	}
	
	@ResponseBody
	@RequestMapping(value="/findMenuByUser",method=RequestMethod.GET)
	public Map<String, Object> findMenuByUser() {
		Map<String, Object> result = new HashMap<String, Object>();
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		List<MenuResourceDTO> menuResourceDtos = securityAccessFacade.findMenuResourceDtoByUsername(username);
		result.put("data", menuResourceDtos);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/findMenuByUserInRole",method=RequestMethod.GET)
	public Map<String, Object> findMenuByUserInRole(RoleDTO roleDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		List<MenuResourceDTO> menuResourceDtos = securityAccessFacade.findMenuResourceDTOByUserAccountInRoleDTO(userAccount,roleDTO);
		result.put("data", menuResourceDtos);
		return result;
	}
}
