package org.openkoala.security.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO 为后面与组织机构集成做准备。 范围控制器
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/scope/organization")
public class ScopeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScopeController.class);

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	/**
	 * 添加组织机构范围。
	 * 
	 * @param organizationDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(OrganizationScopeDTO organizationDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.saveOrganization(organizationDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加组织机构范围失败。");
		}
		return jsonResult;
	}

	/**
	 * 选择父组织机构范围， 为其添加子组织机构范围。
	 * 
	 * @param organizationDTO
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addChildToParent")
	public JsonResult addChildToParent(OrganizationScopeDTO organizationDTO, Long parentId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.saveChildToParent(organizationDTO, parentId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加子组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加子组织机构范围失败。");
		}
		return jsonResult;
	}

	/**
	 * 更新组织机构范围。
	 * 
	 * @param organizationDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(OrganizationScopeDTO organizationDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.updateOrganization(organizationDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新组织机构范围失败。");
		}
		return jsonResult;
	}

	/**
	 * 撤销组织结构范围。
	 * 
	 * @param organizationDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public JsonResult terminate(@RequestBody OrganizationScopeDTO[] organizationDTOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateOrganizations(organizationDTOs);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新组织机构范围失败。");
		}
		return jsonResult;
	}

	/**
	 * 查找所有的组织机构范围树。
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllOrganizationScopesTree")
	public JsonResult findAllOrganizationScopesTree() {
		JsonResult jsonResult = new JsonResult();
		try {
			List<OrganizationScopeDTO> results = securityAccessFacade.findAllOrganizationScopesTree();
			jsonResult.setObject(results);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新组织机构范围成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新组织机构范围失败。");
		}
		return jsonResult;
	}
}
