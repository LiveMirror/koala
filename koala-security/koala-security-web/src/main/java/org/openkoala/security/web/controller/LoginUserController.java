package org.openkoala.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.openkoala.security.facade.command.LoginCommand;
import org.openkoala.security.facade.dto.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登陆用户控制器
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/loginUser")
public class LoginUserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserController.class);
	
	/**
	 * 用户登陆
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JsonResult login(HttpServletRequest request, LoginCommand command) {
		JsonResult result = doCaptcha(request);// 处理验证码
		if (!result.isSuccess()) {
			return result;
		}
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(//
				command.getUsername(),//
				command.getPassword(),//
				command.getRememberMe());
		try {
			SecurityUtils.getSubject().login(usernamePasswordToken);
			result.setSuccess(true);
			result.setMessage("登陆成功。");
		} catch (UnknownAccountException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("账号或者密码不存在。");
		} catch (LockedAccountException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("该账号已经挂起，请联系管理员。");
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("账号或者密码不正确。");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("登录失败。");
		}
		return result;
	}

	/**
	 * TODO 用户退出。
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
	 * 处理验证码
	 * 
	 * @param request
	 * @return
	 */
	private JsonResult doCaptcha(HttpServletRequest request) {
		JsonResult result = new JsonResult();
		String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
		if (!StringUtils.isBlank(shiroLoginFailure)) {
			result.setSuccess(false);
			result.setMessage("验证码错误。");
		}
		return result;
	}
}
