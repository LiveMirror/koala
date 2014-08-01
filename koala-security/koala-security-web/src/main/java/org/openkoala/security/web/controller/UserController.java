package org.openkoala.security.web.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.dayatang.querychannel.Page;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.UserNotExistedException;
import org.openkoala.security.core.domain.Authorization;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.JsonResult;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.shiro.util.AuthUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	 * TODO 验证码错误不应该在这里处理。 用户登陆，支持多种方式。账号、邮箱、电话等
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JsonResult login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
		JsonResult jsonResult = new JsonResult();
		String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
		if (!StringUtils.isBlank(shiroLoginFailure)) {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("验证码错误。");
		} else {
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
			try {
				SecurityUtils.getSubject().login(usernamePasswordToken);
				securityConfigFacade.updateUserLastLoginTime(AuthUserUtil.getCurrentUser().getId());
				jsonResult.setSuccess(true);
				jsonResult.setMessage("登陆成功。");
			} catch (NullArgumentException e) {
				LOGGER.error(e.getMessage());
				jsonResult.setSuccess(false);
				jsonResult.setMessage("用户名或者密码为空。");
			} catch (UserNotExistedException e) {
				LOGGER.error(e.getMessage());
				jsonResult.setSuccess(false);
				jsonResult.setMessage("用户名或者密码不正确。");
			} catch (AuthenticationException e) {
				LOGGER.error(e.getMessage());
				jsonResult.setSuccess(false);
				jsonResult.setMessage("登录失败。");
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				jsonResult.setSuccess(false);
				jsonResult.setMessage("登录失败。");
			}
		}
		return jsonResult;
	}

	/**
	 * 用户退出。
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public JsonResult logout() {
		JsonResult jsonResult = new JsonResult();
		try {
			SecurityUtils.getSubject().logout();
			jsonResult.setSuccess(true);
			jsonResult.setMessage("用户退出成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("用户退出失败。");
		}
		return jsonResult;
	}

	/**
	 * 添加用户。
	 * 
	 * @param userDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(UserDTO userDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.saveUser(userDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("添加用户成功。");
		} catch (UserAccountIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("用户账号:" + userDTO.getUserAccount() + "已经存在。");
		} catch (EmailIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("用户邮箱：" + userDTO.getEmail() + "已经存在。");
		} catch (TelePhoneIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("用户联系电话：" + userDTO.getTelePhone() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("添加用户失败。");
		}
		return jsonResult;
	}

	/**
	 * 更新用户。
	 * 
	 * @param userDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(UserDTO userDTO) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.updateUser(userDTO);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新用户成功。");
		} catch (UserAccountIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("账户：" + userDTO.getUserAccount() + "已经存在。");
		} catch (EmailIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("邮箱：" + userDTO.getEmail() + "已经存在。");
		} catch (TelePhoneIsExistedException e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("联系电话：" + userDTO.getTelePhone() + "已经存在。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新用户失败。");
		}
		return jsonResult;
	}

	/**
	 * 撤销用户。
	 * 
	 * @param userDTOs
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST, consumes = "application/json")
	public JsonResult terminate(@RequestBody UserDTO[] userDTOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.terminateUsers(userDTOs);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("撤销更新成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("撤销更新失败。");
		}
		return jsonResult;
	}

	/**
	 * 根据条件分页查询用户。
	 * 
	 * @param page
	 * @param pagesize
	 * @param userDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQuery")
	public Page<UserDTO> pagingQuery(int page, int pagesize, UserDTO userDTO) {
		Page<UserDTO> results = securityAccessFacade.pagingQueryUsers(page, pagesize, userDTO);
		return results;
	}

	/**
	 * 更新用户密码。
	 * 
	 * @param oldPassword
	 * @param userPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatePassword")
	public JsonResult updatePassword(@RequestParam String oldUserPassword, @RequestParam String userPassword) {
		JsonResult jsonResult = new JsonResult();
		if (securityConfigFacade.updatePassword(AuthUserUtil.getUserAccount(), userPassword, oldUserPassword)) {
			jsonResult.setSuccess(true);
			jsonResult.setMessage("更新用户密码成功。");
		} else {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("更新用户密码失败。");
		}
		return jsonResult;
	}

	/**
	 * 重置用户密码。
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetPassword")
	public JsonResult resetPassword(Long userId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.resetPassword(userId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("重置用户密码成功，密码：888888");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("重置用户密码失败。");
		}
		return jsonResult;
	}

	/**
	 * 激活用户。
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activate")
	public JsonResult activate(Long userId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.activate(userId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("激活用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("激活用户失败。");
		}
		return jsonResult;
	}

	/**
	 * 挂起用户。
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/suspend")
	public JsonResult suspend(Long userId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.suspend(userId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("激活用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("激活用户失败。");
		}
		return jsonResult;
	}

	/**
	 * 批量激活用户。
	 * 
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activates")
	public JsonResult activates(Long[] userIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.activate(userIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("批量激活用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("批量激活用户失败。");
		}
		return jsonResult;
	}

	/**
	 * 批量挂起用户。
	 * 
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/suspends")
	public JsonResult suspends(Long[] userIds) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.suspend(userIds);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("批量挂起用户成功。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("批量挂起用户失败。");
		}
		return jsonResult;
	}

	/**
	 * 为用户授权一个角色。
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantRoleToUser")
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
	@RequestMapping("/grantRolesToUser")
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
	@RequestMapping("/grantPermissionToUser")
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
	@RequestMapping("/grantPermissionsToUser")
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
	@RequestMapping("/terminateAuthorizationByUserInRole")
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
	@RequestMapping("/terminateAuthorizationByUserInPermission")
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
	@RequestMapping("/terminateAuthorizationByUserInRoles")
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
	@RequestMapping("/terminatePermissionsByUser")
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
	 * 根据用户ID分页查找已经授权的角色。
	 * 
	 * @param page
	 * @param pagesize
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryGrantRoleByUserId")
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
	@RequestMapping("/pagingQueryGrantPermissionByUserId")
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
	@RequestMapping("/pagingQueryNotGrantRoles")
	public Page<RoleDTO> pagingQueryNotGrantRoles(int page, int pagesize, Long userId, RoleDTO queryRoleCondition) {
		Page<RoleDTO> results = securityAccessFacade.pagingQueryNotGrantRoles(page, pagesize, queryRoleCondition, userId);
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
	@RequestMapping("/pagingQueryNotGrantPermissions")
	public Page<PermissionDTO> pagingQueryNotGrantPermissions(int page, int pagesize, PermissionDTO queryPermissionCondition, Long userId) {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByUserId(page, pagesize, queryPermissionCondition, userId);
		return results;
	}

	// ~ 为与组织机构集成准备

	/**
	 * 在某个范围中为用户授权一个角色。
	 * 
	 * @param userId
	 * @param roleId
	 * @param scopeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantRoleToUserInScope")
	public JsonResult grantRoleToUserInScope(Long userId, Long roleId, Long scopeId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantRoleToUserInScope(userId, roleId, scopeId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("在某个范围中为用户授权一个角色成功。");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("在某个范围中为用户授权一个角色失败。");
		}
		return jsonResult;
	}

	/**
	 * 在某个范围中为用户授权多个角色。
	 * 
	 * @param userId
	 * @param roleIds
	 * @param scopeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantRolesInScope")
	public JsonResult grantRolesToUserInScope(Long userId, Long[] roleIds, Long scopeId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantRolesToUserInScope(userId, roleIds, scopeId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("在某个范围中为用户授权多个角色成功");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("在某个范围中为用户授权多个角色失败");
		}
		return jsonResult;
	}

	/**
	 * 在某个范围中为用户授权一个权限。
	 * 
	 * @param userId
	 * @param permissionId
	 * @param scopeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermissionsToUserInScope")
	public JsonResult grantPermissionsToUserInScope(Long userId, Long permissionId, Long scopeId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermissionToUserInScope(userId, permissionId, scopeId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("在某个范围中为用户授权一个权限成功。");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("在某个范围中为用户授权一个权限失败。");
		}
		return jsonResult;
	}

	/**
	 * 在某个范围中为用户授权多个权限。
	 * 
	 * @param userId
	 * @param permissionIds
	 * @param scopeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermissionToUserInScope")
	public JsonResult grantPermissionToUserInScope(Long userId, Long[] permissionIds, Long scopeId) {
		JsonResult jsonResult = new JsonResult();
		try {
			securityConfigFacade.grantPermissionsToUserInScope(userId, permissionIds, scopeId);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("在某个范围中为用户授权多个权限成功。");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			jsonResult.setSuccess(false);
			jsonResult.setMessage("在某个范围中为用户授权多个权限失败。");
		}
		return jsonResult;
	}
}
