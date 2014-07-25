package org.openkoala.security.web.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping("/add")
	public JsonResult add(PageElementResourceDTO pageElementResourceDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.savePageElementResource(pageElementResourceDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加页面元素权限资源成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加页面元素权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 更新页面元素权限资源。
	 * 
	 * @param pageElementResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(PageElementResourceDTO pageElementResourceDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.updatePageElementResource(pageElementResourceDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新页面元素权限资源失败。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新页面元素权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 撤销页面元素权限资源。
	 * 
	 * @param pageElementResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public JsonResult terminate(@RequestBody PageElementResourceDTO[] pageElementResourceDTOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePageElementResources(pageElementResourceDTOs);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销页面元素权限资源成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销页面元素权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 为页面元素资源授予权限Permission
	 * 
	 * @param permissionIds
	 * @param pageElementResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermisssionsToPageElementResource")
	public JsonResult grantPermisssionsToPageElementResource(Long[] permissionIds, Long pageElementResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermisssionsToPageElementResource(permissionIds, pageElementResourceId);
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
	@RequestMapping("/terminatePermissionsFromPageElementResource")
	public JsonResult terminatePermissionsFromPageElementResource(Long[] permissionIds, Long pageElementResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissionsFromPageElementResource(permissionIds, pageElementResourceId);
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
	 * 分页查询页面元素权限资源，
	 * 可根据页面元素权限资源{@link PageElementResourceDTO}条件进行查询。
	 * 
	 * @param page
	 * @param pagesize
	 * @param pageElementResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQuery")
	public Page<PageElementResourceDTO> pagingQuery(int page, int pagesize, PageElementResourceDTO pageElementResourceDTO) {
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryPageElementResources(page, pagesize, pageElementResourceDTO);
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
	@RequestMapping("/pagingQueryGrantPermissionsByPageElementResourceId")
	public Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int page, int pagesize, Long pageElementResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByPageElementResourceId(page, pagesize, pageElementResourceId);
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
	@RequestMapping("/pagingQueryNotGrantPermissionsByPageElementResourceId")
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int page, int pagesize, Long pageElementResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByPageElementResourceId(page, pagesize, pageElementResourceId);
		return results;
	}
}
