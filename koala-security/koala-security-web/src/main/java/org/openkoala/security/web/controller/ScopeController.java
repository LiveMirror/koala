package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/scope/organization")
public class ScopeController {

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(OrganizationScopeDTO organizationDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveOrganizationDTO(organizationDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/addChildToParent")
	public Map<String, Object> addChildToParent(OrganizationScopeDTO organizationDTO, Long parentId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveChildToParent(organizationDTO, parentId);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(OrganizationScopeDTO organizationDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updateOrganizationDTO(organizationDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(OrganizationScopeDTO[] organizationDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateOrganizationDTOs(organizationDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/findAllOrganizationScopesTree")
	public Map<String, Object> findAllOrganizationScopesTree() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<OrganizationScopeDTO> organizationScopeDTOs = securityAccessFacade.findAllOrganizationScopesTree();
		dataMap.put("data", organizationScopeDTOs);
		return dataMap;
	}
}
