package org.openkoala.security.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeUrlAccessResourcePropsCommand;
import org.openkoala.security.facade.command.CreateUrlAccessResourceCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.shiro.extend.ShiroFilterChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * URL访问权限资源。 XXX 当删除资源的时候需要确保没有关联关系。
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
	private ShiroFilterChainManager shiroFilterChainManager;

	/**
	 * 添加URL访问权限资源。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(CreateUrlAccessResourceCommand command) {
		return securityConfigFacade.createUrlAccessResource(command);
	}

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(ChangeUrlAccessResourcePropsCommand command) {
		return securityConfigFacade.changeUrlAccessResourceProps(command);
	}

	/**
	 * 撤销URL访问权限资源。
	 * 
	 * @param urlAccessResourceDTOs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public JsonResult terminate(Long[] urlAccessResourceIds) {
		JsonResult result = securityConfigFacade.terminateUrlAccessResources(urlAccessResourceIds);
		shiroFilterChainManager.initFilterChain();
		return result;
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
	@RequestMapping(value="/pagingQuery", method = RequestMethod.GET)
	public Page<UrlAccessResourceDTO> pagingQuery(int page, int pagesize, UrlAccessResourceDTO urlAccessResourceDTO) {
		Page<UrlAccessResourceDTO> results = securityAccessFacade.pagingQueryUrlAccessResources(page, pagesize,
				urlAccessResourceDTO);
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
	@RequestMapping(value = "/grantPermisssionsToUrlAccessResource", method = RequestMethod.POST)
	public JsonResult grantPermisssionsToUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermisssionsToUrlAccessResource(permissionId, urlAccessResourceId);
			shiroFilterChainManager.initFilterChain();
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
	@RequestMapping(value = "/terminatePermissionsFromUrlAccessResource", method = RequestMethod.POST)
	public JsonResult terminatePermissionsFromUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminatePermissionsFromUrlAccessResource(permissionId, urlAccessResourceId);
			shiroFilterChainManager.initFilterChain();
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
	@RequestMapping(value="/pagingQueryGrantPermissionsByUrlAccessResourceId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByUrlAccessResourceId(page,
				pagesize, urlAccessResourceId);
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
	@RequestMapping(value="/pagingQueryNotGrantPermissionsByUrlAccessResourceId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByUrlAccessResourceId(page,
				pagesize, urlAccessResourceId);
		return results;
	}

}
