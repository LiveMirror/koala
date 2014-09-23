package org.openkoala.security.org.controller;

import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * 组织机构范围控制器
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/scope/organization")
public class ScopeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScopeController.class);

	@Inject
	private SecurityOrgConfigFacade securityOrgConfigFacade;

	@Inject
	private SecurityOrgAccessFacade securityOrgAccessFacade;

	/**
	 * 添加组织机构范围。
	 * 
	 * @param organizationDTO
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(OrganizationScopeDTO organizationDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityOrgConfigFacade.saveOrganization(organizationDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加组织机构范围失败。");
		}
		return jsonResult;
	}

	*//**
	 * 选择父组织机构范围， 为其添加子组织机构范围。
	 * 
	 * @param organizationDTO
	 * @param parentId
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/addChildToParent")
	public JsonResult addChildToParent(OrganizationScopeDTO organizationDTO, Long parentId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityOrgConfigFacade.saveChildToParent(organizationDTO, parentId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加子组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加子组织机构范围失败。");
		}
		return jsonResult;
	}

	*//**
	 * 更新组织机构范围。
	 * 
	 * @param organizationDTO
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(OrganizationScopeDTO organizationDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityOrgConfigFacade.updateOrganization(organizationDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新组织机构范围失败。");
		}
		return jsonResult;
	}

	*//**
	 * 撤销组织结构范围。
	 * 
	 * @param organizationDTOs
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public JsonResult terminate(@RequestBody OrganizationScopeDTO[] organizationDTOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityOrgConfigFacade.terminateOrganizations(organizationDTOs);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新组织机构范围失败。");
		}
		return jsonResult;
	}

	*//**
	 * 查找所有的组织机构范围树。
	 * 
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/findAllOrganizationScopesTree")
	public JsonResult findAllOrganizationScopesTree() {
		JsonResult jsonResult = new JsonResult();
		try {
			List<OrganizationScopeDTO> results = securityOrgAccessFacade.findAllOrganizationScopesTree();
			jsonResult.setData(results);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新组织机构范围失败。");
		}
		return jsonResult;
	}*/
}
