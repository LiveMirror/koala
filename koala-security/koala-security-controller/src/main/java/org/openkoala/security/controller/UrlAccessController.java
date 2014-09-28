package org.openkoala.security.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeUrlAccessResourcePropsCommand;
import org.openkoala.security.facade.command.CreateUrlAccessResourceCommand;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.shiro.extend.ShiroFilterChainManager;
import org.springframework.stereotype.Controller;
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
	public InvokeResult add(CreateUrlAccessResourceCommand command) {
		return securityConfigFacade.createUrlAccessResource(command);
	}

	/**
	 * 更新URL访问权限资源。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public InvokeResult update(ChangeUrlAccessResourcePropsCommand command) {
		return securityConfigFacade.changeUrlAccessResourceProps(command);
	}

	/**
	 * 撤销URL访问权限资源。
	 * 
	 * @param urlAccessResourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public InvokeResult terminate(Long[] urlAccessResourceIds) {
		shiroFilterChainManager.initFilterChain();
		return  securityConfigFacade.terminateUrlAccessResources(urlAccessResourceIds);
	}

	/**
	 * 根据条件分页查询URL访问权限资源。
	 * 
	 * @param page
	 * @param pagesize
	 * @param queryUrlAccessResourceCondition
	 *            查询URL访问资源条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
	public Page<UrlAccessResourceDTO> pagingQuery(int page, int pagesize,
			UrlAccessResourceDTO queryUrlAccessResourceCondition) {
		return  securityAccessFacade.pagingQueryUrlAccessResources(page, pagesize,queryUrlAccessResourceCondition);
	}

	/**
	 * 为URL访问权限资源授权权限Permission
	 * 
	 * @param permissionId
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPermisssionsToUrlAccessResource", method = RequestMethod.POST)
	public InvokeResult grantPermisssionsToUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
		shiroFilterChainManager.initFilterChain();
		return securityConfigFacade.grantPermisssionsToUrlAccessResource(permissionId, urlAccessResourceId);
	}

	/**
	 * 从URL访问权限资源中撤销权限Permission
	 * 
	 * @param permissionId
	 * @param urlAccessResourceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePermissionsFromUrlAccessResource", method = RequestMethod.POST)
	public InvokeResult terminatePermissionsFromUrlAccessResource(Long permissionId, Long urlAccessResourceId) {
			shiroFilterChainManager.initFilterChain();
			return securityConfigFacade.terminatePermissionsFromUrlAccessResource(permissionId, urlAccessResourceId);
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
	@RequestMapping(value = "/pagingQueryGrantPermissionsByUrlAccessResourceId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId) {
		 return securityAccessFacade.pagingQueryGrantPermissionsByUrlAccessResourceId(page,
				pagesize, urlAccessResourceId);
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
	@RequestMapping(value = "/pagingQueryNotGrantPermissionsByUrlAccessResourceId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int page, int pagesize,Long urlAccessResourceId, PermissionDTO queryPermissionCondition) {
		return securityAccessFacade.pagingQueryNotGrantPermissionsByUrlAccessResourceId(page,pagesize, urlAccessResourceId, queryPermissionCondition);
	}

}
