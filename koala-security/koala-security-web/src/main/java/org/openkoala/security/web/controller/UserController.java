package org.openkoala.security.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.dayatang.querychannel.Page;
import org.openkoala.security.core.EmailIsExistedException;
import org.openkoala.security.core.TelePhoneIsExistedException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/user")
public class UserController {

	private static final String INIT_PASSWORD = "888888";

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/**
	 * TODO 修改登陆时间
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
		Map<String, Object> results = new HashMap<String, Object>();
		// 这个以后可以扩展
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

		try {
			SecurityUtils.getSubject().login(usernamePasswordToken);
			results.put("message", "登陆成功");
			results.put("success", Boolean.TRUE);
		} catch (AuthenticationException e) {
			results.put("message", "登陆失败");
		}
		return results;
	}

	/**
	 * 添加用户
	 * 
	 * @param userDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(UserDTO userDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			securityConfigFacade.saveUserDTO(userDTO);
			// } catch (UserAccountIsExistedException e) {
			// dataMap.put("result",MessageFormat.format("user.userAccount.exist", userDTO.getUserAccount()));
		} catch (EmailIsExistedException e) {
			dataMap.put("result", "邮箱：" + userDTO.getEmail() + "已经存在！");

		} catch (TelePhoneIsExistedException e) {
			dataMap.put("result", "联系电话：" + userDTO.getTelePhone() + "已经存在！");
		} catch (Exception e) {
			dataMap.put("result", "保存失败");
		}
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 更新 TODO AOP做异常。
	 * 
	 * @param userDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(UserDTO userDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			securityAccessFacade.updateUserDTO(userDTO);
		} catch (UserAccountIsExistedException e) {
			dataMap.put("result", "账户：" + userDTO.getUserAccount() + "已经存在！");
		} catch (EmailIsExistedException e) {
			dataMap.put("result", "邮箱：" + userDTO.getEmail() + "已经存在！");

		} catch (TelePhoneIsExistedException e) {
			dataMap.put("result", "联系电话：" + userDTO.getTelePhone() + "已经存在！");
		} catch (Exception e) {
			dataMap.put("result", "更新失败");
		}
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 支持批量删除,待优化。
	 * 
	 * @param userDTOs
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminate(UserDTO[] userDTOs) {
		// TODO 3、刷新缓存
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateUserDTOs(userDTOs);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 更加条件分页查询用户。
	 * 
	 * @param page
	 * @param pagesize
	 * @param userDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<UserDTO> pagingQuery(int page, int pagesize, UserDTO userDTO) {
		Page<UserDTO> results = securityAccessFacade.pagingQueryUsers(page, pagesize, userDTO);
		return results;
	}

	/**
	 * 更新密码
	 * 
	 * @param oldPassword
	 * @param userPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatePassword")
	public Map<String, Object> updatePassword(@RequestParam String oldUserPassword, @RequestParam String userPassword) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		// TODO 加密
		UserDTO userDTO = new UserDTO(userAccount, userPassword);
		if (securityAccessFacade.updatePassword(userDTO, oldUserPassword)) {
			dataMap.put("result", "success");
			// TODO 刷新缓存
		} else {
			dataMap.put("result", "failure");
		}
		return dataMap;
	}

	/**
	 * 重置密码
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetPassword")
	public Map<String, Object> resetPassword(Long userId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);
		userDTO.setUserPassword(INIT_PASSWORD);
		securityConfigFacade.resetPassword(userDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public Map<String, Object> logout() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		SecurityUtils.getSubject().logout();
		dataMap.put("success", true);
		dataMap.put("message", "成功退出");
		return dataMap;
	}

	/**
	 * 激活
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activate")
	public Map<String, Object> activate(Long userId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.activate(userId);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 挂起
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/suspend")
	public Map<String, Object> suspend(Long userId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.suspend(userId);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 批量激活
	 * 
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activates")
	public Map<String, Object> activates(Long[] userIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.activate(userIds);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 批量挂起
	 * 
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/suspends")
	public Map<String, Object> suspends(Long[] userIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.suspend(userIds);
		dataMap.put("success", true);
		return dataMap;
	}

	// ======================添加授权=======================
	/**
	 * 为用户授权一个角色。
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantRole")
	public Map<String, Object> grantRoleToUser(Long userId, Long roleId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantRoleToUser(userId, roleId);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 为用户授权多个角色。
	 * 
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantRoles")
	public Map<String, Object> grantRolesToUser(Long userId, Long[] roleIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantRolesToUser(userId, roleIds);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 为用户授权一个权限。
	 * 
	 * @param userId
	 * @param permissionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermission")
	public Map<String, Object> grantPermissionToUser(Long userId, Long permissionId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermissionToUser(userId, permissionId);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 为用户授权多个权限。
	 * 
	 * @param userId
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grantPermissions")
	public Map<String, Object> grantPermissionsToUser(Long userId, Long[] permissionIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermissionsToUser(userId, permissionIds);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 撤销用户的一个角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminateRoleByUser")
	public Map<String, Object> terminateAuthorizationByRole(Long userId, Long roleId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateAuthorizationByRole(userId, roleId);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 撤销用户的一个权限
	 * 
	 * @param userId
	 * @param permissionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionByUser")
	public Map<String, Object> terminateAuthorizationByPermission(Long userId, Long permissionId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateAuthorizationByPermission(userId, permissionId);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 撤销用户的多个角色。
	 * 
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminateRolesByUser")
	public Map<String, Object> terminateAuthorizationsByRoles(Long userId, Long[] roleIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateAuthorizationsByRoles(userId, roleIds);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 批量用户的多个权限。
	 * 
	 * @param userId
	 * @param permissionIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminatePermissionsByUser")
	public Map<String, Object> terminateAuthorizationsByPermissions(Long userId, Long[] permissionIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.terminateAuthorizationsByPermissions(userId, permissionIds);
		dataMap.put("success", true);
		return dataMap;
	}

	/**
	 * 根据条件分页查询没有授权的角色
	 * 
	 * @param page
	 * @param pageSize
	 * @param queryRoleCondition
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryNotGrantRoles")
	public Page<RoleDTO> pagingQueryNotGrantRoles(int page, int pagesize, RoleDTO queryRoleCondition, Long userId) {
		// String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		Page<RoleDTO> results = securityAccessFacade.pagingQueryNotGrantRoles(page, pagesize, queryRoleCondition,
				userId);
		return results;
	}

	/**
	 * 根据用户ID分页查找没有授权的权限。
	 * 
	 * @param page
	 * @param pagesize
	 * @param queryPermissionCondition
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingQueryNotGrantPermissions")
	public Page<PermissionDTO> pagingQueryNotGrantPermissions(int page, int pagesize,
			PermissionDTO queryPermissionCondition, Long userId) {
		// String userAccount = (String) SecurityUtils.getSubject().getPrincipal();
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantRoles(page, pagesize,
				queryPermissionCondition, userId);
		return results;
	}

	// ============== TODO ==Scope=================
	@ResponseBody
	@RequestMapping("grantRoleInScope")
	public Map<String, Object> grantRoleInScope(Long userId, Long roleId, Long scopeId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantRoleInScope(userId, roleId, scopeId);
		dataMap.put("success", true);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("grantRolesInScope")
	public Map<String, Object> grantRolesInScope(Long userId, Long[] roleIds, Long scopeId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantRolesInScope(userId, roleIds, scopeId);
		dataMap.put("success", true);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("grantPermissionInScope")
	public Map<String, Object> grantPermissionInScope(Long userId, Long permissionId, Long scopeId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermissionInScope(userId, permissionId, scopeId);
		dataMap.put("success", true);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("grantPermissionsInScope")
	public Map<String, Object> grantPermissionsInScope(Long userId, Long[] permissionIds, Long scopeId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		securityConfigFacade.grantPermissionsInScope(userId, permissionIds, scopeId);
		dataMap.put("success", true);
		return dataMap;
	}
}
