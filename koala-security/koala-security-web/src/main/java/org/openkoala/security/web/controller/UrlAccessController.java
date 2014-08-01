package org.openkoala.security.web.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.UrlIsExistedException;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.shiro.extend.ShiroFilerChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * URL访问权限资源。
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/url")
public class UrlAccessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlAccessController.class);

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private ShiroFilerChainManager shiroFilerChainManager;

	/**
	 * 添加URL访问权限资源。
	 * 
	 * @param urlAccessResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(UrlAccessResourceDTO urlAccessResourceDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.saveUrlAccessResource(urlAccessResourceDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加URL访问权限资源成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加URL访问权限资源名称：" + urlAccessResourceDTO.getName() + "已经存在。");
		} catch (UrlIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加URL访问权限资源名称：" + urlAccessResourceDTO.getUrl()+ "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加URL访问权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 更新URL访问权限资源。
	 * 
	 * @param urlAccessResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(UrlAccessResourceDTO urlAccessResourceDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.updateUrlAccessResource(urlAccessResourceDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新URL访问权限资源成功。");
		} catch (NameIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新URL访问权限资源名称：" + urlAccessResourceDTO.getName() + "已经存在。");
		} catch (UrlIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新URL访问权限资源名称：" + urlAccessResourceDTO.getUrl()+ "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新URL访问权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 撤销URL访问权限资源。
	 * 
	 * @param urlAccessResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public JsonResult terminate(@RequestBody UrlAccessResourceDTO[] urlAccessResourceDTOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateUrlAccessResources(urlAccessResourceDTOs);
			shiroFilerChainManager.initFilterChain();
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销URL访问权限资源成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销URL访问权限资源失败。");
		}
		return jsonResult;
	}

	/**
	 * 根据条件分页查询URL访问权限资源。
	 * 
	 * @param page
	 * @param pagesize
	 * @param urlAccessResourceDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQuery")
	public Page<UrlAccessResourceDTO> pagingQuery(int page, int pagesize, UrlAccessResourceDTO urlAccessResourceDTO) {
		Page<UrlAccessResourceDTO> results = securityAccessFacade.pagingQueryUrlAccessResources(page, pagesize, urlAccessResourceDTO);
		return results;
	}

	/**
	 * 为URL访问权限资源授权权限Permission
	 * 
	 * @param permissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermisssionsToUrlAccessResource")
	public JsonResult grantPermisssionsToUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermisssionsToUrlAccessResource(permissionIds, urlAccessResourceId);
			shiroFilerChainManager.initFilterChain();
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为URL访问权限资源授权权限失败。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为URL访问权限资源授权权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 从URL访问权限资源中撤销权限Permission
	 * 
	 * @param permissionIds
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionsFromUrlAccessResource")
	public JsonResult terminatePermissionsFromUrlAccessResource(Long[] permissionIds, Long urlAccessResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissionsFromUrlAccessResource(permissionIds, urlAccessResourceId);
			shiroFilerChainManager.initFilterChain();
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为URL访问权限资源授权权限失败。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为URL访问权限资源授权权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 通过URL访问权限资源分页查询已经授权的权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryGrantPermissionsByUrlAccessResourceId")
	public Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResourceId(int page, int pagesize, Long urlAccessResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByUrlAccessResourceId(page, pagesize, urlAccessResourceId);
		return results;
	}

	/**
	 * 通过URL访问权限资源分页查询还未授权的权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryNotGrantPermissionsByUrlAccessResourceId")
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int page, int pagesize, Long urlAccessResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByUrlAccessResourceId(page, pagesize, urlAccessResourceId);
		return results;
	}

}
