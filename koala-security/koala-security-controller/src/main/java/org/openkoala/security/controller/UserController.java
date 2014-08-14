package org.openkoala.security.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeUserPasswordCommand;
import org.openkoala.security.facade.command.ChangeUserPropsCommand;
import org.openkoala.security.facade.command.CreateUserCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户控制器。
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * 添加用户。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(CreateUserCommand command) {
		String createOwner = CurrentUser.getUserAccount();
		command.setCreateOwner(createOwner);
		return securityConfigFacade.createUser(command);
	}

	/**
	 * 更改用户。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult changeUserProps(ChangeUserPropsCommand command) {
		return securityConfigFacade.changeUserProps(command);
	}

	/**
	 * 撤销用户。
	 * 
	 * @param userIds
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public JsonResult terminate(Long[] userIds) {
		return securityConfigFacade.terminateUsers(userIds);
	}

	/**
	 * 重置用户密码。
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public JsonResult resetPassword(Long userId) {
		return securityConfigFacade.resetPassword(userId);
	}

	/**
	 * 激活用户。
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	public JsonResult activate(Long userId) {
		return securityConfigFacade.activate(userId);
	}

	/**
	 * 挂起用户。
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/suspend", method = RequestMethod.POST)
	public JsonResult suspend(Long userId) {
		return securityConfigFacade.suspend(userId);
	}

	/**
	 * 批量激活用户。
	 * 
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/activates", method = RequestMethod.POST)
	public JsonResult activates(Long[] userIds) {
		return securityConfigFacade.activate(userIds);
	}

	/**
	 * 批量挂起用户。
	 * 
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/suspends", method = RequestMethod.POST)
	public JsonResult suspends(Long[] userIds) {
		return securityConfigFacade.suspend(userIds);
	}

	// ~ 授权
	/**
	 * 为用户授权一个角色。
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantRoleToUser", method = RequestMethod.POST)
	public JsonResult grantRoleToUser(Long userId, Long roleId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantRoleToUser(userId, roleId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为用户授权一个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为用户授权一个角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 为用户授权多个角色。
	 * 
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantRolesToUser", method = RequestMethod.POST)
	public JsonResult grantRolesToUser(Long userId, Long[] roleIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantRolesToUser(userId, roleIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为用户授权多个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为用户授权多个角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 为用户授权一个权限。
	 * 
	 * @param userId
	 * @param permissionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPermissionToUser", method = RequestMethod.POST)
	public JsonResult grantPermissionToUser(Long userId, Long permissionId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermissionToUser(userId, permissionId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为用户授权一个权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为用户授权一个权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 为用户授权多个权限。
	 * 
	 * @param userId
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/grantPermissionsToUser", method = RequestMethod.POST)
	public JsonResult grantPermissionsToUser(Long userId, Long[] permissionIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermissionsToUser(userId, permissionIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("为用户授权多个权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("为用户授权多个权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 通过角色下的用户撤销一个授权中心{@link Authorization}。
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminateAuthorizationByUserInRole", method = RequestMethod.POST)
	public JsonResult terminateAuthorizationByUserInRole(Long userId, Long roleId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateAuthorizationByUserInRole(userId, roleId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销用户的一个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销用户的一个角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 通过权限下的用户撤销一个授权中心{@link Authorization}。
	 * 
	 * @param userId
	 * @param permissionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminateAuthorizationByUserInPermission", method = RequestMethod.POST)
	public JsonResult terminateAuthorizationByUserInPermission(Long userId, Long permissionId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateAuthorizationByUserInPermission(userId, permissionId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销用户的一个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销用户的一个角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 通过角色下的用户撤销多个授权中心{@link Authorization}。
	 * 
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminateAuthorizationByUserInRoles", method = RequestMethod.POST)
	public JsonResult terminateAuthorizationByUserInRoles(Long userId, Long[] roleIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateAuthorizationByUserInRoles(userId, roleIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销用户的多个角色成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销用户的多个角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 通过权限下的用户撤销多个授权中心{@link Authorization}。。
	 * 
	 * @param userId
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminatePermissionsByUser", method = RequestMethod.POST)
	public JsonResult terminateAuthorizationsByPermissions(Long userId, Long[] permissionIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateAuthorizationByUserInPermissions(userId, permissionIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销用户的多个权限成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销用户的多个权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 根据条件分页查询用户。
	 * 
	 * @param page
	 * @param pagesize
	 * @param queryUserCondition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
	public Page<UserDTO> pagingQuery(int page, int pagesize, UserDTO queryUserCondition) {
		Page<UserDTO> results = securityAccessFacade.pagingQueryUsers(page, pagesize, queryUserCondition);
		return results;
	}

	/**
	 * 根据用户ID分页查找已经授权的角色。
	 * 
	 * @param page
	 * @param pagesize
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantRoleByUserId", method = RequestMethod.GET)
	public Page<RoleDTO> pagingQueryRolesByUserId(int page, int pagesize, Long userId) {
		Page<RoleDTO> results = securityAccessFacade.pagingQueryGrantRolesByUserId(page, pagesize, userId);
		return results;
	}

	/**
	 * 根据用户ID分页查询已经授权的权限
	 * 
	 * @param page
	 * @param pagesize
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryGrantPermissionByUserId", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryGrantPermissionByUserId(int page, int pagesize, Long userId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionByUserId(page, pagesize, userId);
		return results;
	}

	/**
	 * 根据条件分页查询还未授权的角色
	 * 
	 * @param page
	 * @param pageSize
	 * @param queryRoleCondition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantRoles", method = RequestMethod.GET)
	public Page<RoleDTO> pagingQueryNotGrantRoles(int page, int pagesize, Long userId, RoleDTO queryRoleCondition) {
		Page<RoleDTO> results = securityAccessFacade.pagingQueryNotGrantRoles(page, pagesize, queryRoleCondition,
				userId);
		return results;
	}

	/**
	 * 根据用户ID分页查找还未授权的权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param queryPermissionCondition
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pagingQueryNotGrantPermissions", method = RequestMethod.GET)
	public Page<PermissionDTO> pagingQueryNotGrantPermissions(int page, int pagesize,
			PermissionDTO queryPermissionCondition, Long userId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByUserId(page, pagesize,
				queryPermissionCondition, userId);
		return results;
	}
}
