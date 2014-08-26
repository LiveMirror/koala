package org.openkoala.security.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangePermissionPropsCommand;
import org.openkoala.security.facade.command.CreatePermissionCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.springframework.stereotype.Controller;
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

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加权限。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(CreatePermissionCommand command) {
		return securityConfigFacade.createPermission(command);
	}

	/**
	 * 更新权限
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(ChangePermissionPropsCommand command) {
		return securityConfigFacade.changePermissionProps(command);
	}

	/**
	 * 撤销权限
	 * 
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public JsonResult terminate(Long[] permissionIds) {
		return securityConfigFacade.terminatePermissions(permissionIds);
	}

	/**
	 * 根据条件分页查询权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param queryPermissionCondition
	 *            查询权限条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQuery(int page, int pagesize, PermissionDTO queryPermissionCondition) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryPermissions(page, pagesize,
				queryPermissionCondition);
		return results;
	}

}
