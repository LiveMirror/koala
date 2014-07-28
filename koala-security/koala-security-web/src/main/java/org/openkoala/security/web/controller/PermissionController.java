package org.openkoala.security.web.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.core.IdentifierIsExistedException;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限控制器
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/permission")
public class PermissionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加权限。
	 * 
	 * @param permissionDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(PermissionDTO permissionDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.savePermission(permissionDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加权限成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加权限失败，权限名称：" + permissionDTO.getPermissionName() + " 已经存在。");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加权限失败，权限名称：" + permissionDTO.getIdentifier() + " 已经存在。");
		}
		return jsonResult;
	}

	/**
	 * 更新权限
	 * 
	 * @param permissionDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(PermissionDTO permissionDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.updatePermission(permissionDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新权限成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新权限失败，权限名称：" + permissionDTO.getPermissionName() + " 已经存在。");
		} catch (IdentifierIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新权限失败，权限名称：" + permissionDTO.getIdentifier() + " 已经存在。");
		}
		return jsonResult;
	}

	/**
	 * 撤销权限
	 * 
	 * @param permissionDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public JsonResult terminate(@RequestBody PermissionDTO[] permissionDTOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissions(permissionDTOs);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 根据条件分页查询权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param permissionDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQuery")
	public Page<PermissionDTO> pagingQuery(int page, int pagesize, PermissionDTO permissionDTO) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissions(page, pagesize, permissionDTO);
		return results;
	}

}
