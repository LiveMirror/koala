package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.RoleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/role")
public class RoleController {

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	@ResponseBody
	@RequestMapping("/findRoleDtosByUsername")
	public Map<String, Object> findRoleDtosByUsername() {
		Map<String, Object> result = new HashMap<String, Object>();
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		List<RoleDTO> roleDtos = securityAccessFacade.findRoleDtosBy(username);
		result.put("result", roleDtos);
		return result;
	}

	/**
	 * 添加角色
	 * 
	 * @param roleDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(RoleDTO roleDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.saveRoleDTO(roleDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 
	 * @param roleDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(RoleDTO roleDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.updateRoleDTO(roleDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * TODO 支持批量删除,待优化。
	 * 
	 * @param userDTOs
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(RoleDTO[] roleDTOs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateRoleDTOs(roleDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<RoleDTO> pagingQuery(int currentPage, int pageSize, RoleDTO roleDTO) {
		Page<RoleDTO> results = securityAccessFacade.pagingQueryRoles(currentPage, pageSize, roleDTO);
		return results;
	}
	
}
