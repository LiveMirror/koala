package org.openkoala.security.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangePageElementResourcePropsCommand;
import org.openkoala.security.facade.command.CreatePageElementResourceCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 页面元素权限资源控制器
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/page")
public class PageElementController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageElementController.class);

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加页面元素权限资源。
	 * 
	 * @param pageElementResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(CreatePageElementResourceCommand command) {
		return securityConfigFacade.createPageElementResource(command);
	}

	/**
	 * 更新页面元素权限资源。
	 * 
	 * @param pageElementResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(ChangePageElementResourcePropsCommand command) {
		return securityConfigFacade.changePageElementResourceProps(command);
	}

	/**
	 * 撤销页面元素权限资源。
	 * 
	 * @param pageElementResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public JsonResult terminate(Long[] pageElementResourceIds) {
		return securityConfigFacade.terminatePageElementResources(pageElementResourceIds);
	}

	/**
	 * 为页面元素资源授予权限Permission
	 * 
	 * @param permissionIds
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPermisssionsToPageElementResource", method = RequestMethod.POST)
	public JsonResult grantPermisssionsToPageElementResource(Long permissionId, Long pageElementResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermisssionsToPageElementResource(permissionId, pageElementResourceId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为页面元素资源授予权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为页面元素资源授予权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 从页面元素资源中撤销权限Permission
	 * 
	 * @param permissionIds
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePermissionsFromPageElementResource", method = RequestMethod.POST)
	public JsonResult terminatePermissionsFromPageElementResource(Long permissionId, Long pageElementResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissionsFromPageElementResource(permissionId, pageElementResourceId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("从页面元素资源中撤销权限。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("从页面元素资源中撤销权限。");
		}
		return jsonResult;
	}

	/**
	 * 分页查询页面元素权限资源， 可根据页面元素权限资源{@link PageElementResourceDTO}条件进行查询。
	 * 
	 * @param page
	 * @param pagesize
	 * @param pageElementResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
	public Page<PageElementResourceDTO> pagingQuery(int page, int pagesize, PageElementResourceDTO queryPageElementResourceCondition) {
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryPageElementResources(page, pagesize, queryPageElementResourceCondition);
		return results;
	}

	/**
	 * 根据页面元素权限资源ID分页查询已经授权的权限Permission。
	 * 
	 * @param page
	 * @param pagesize
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantPermissionsByPageElementResourceId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByPageElementResourceId(page,
				pagesize, pageElementResourceId);
		return results;
	}

	/**
	 * 根据页面元素权限资源ID分页查询还未授权的权限Permission。
	 * 
	 * @param page
	 * @param pagesize
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantPermissionsByPageElementResourceId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByPageElementResourceId(page,
				pagesize, pageElementResourceId);
		return results;
	}
}
