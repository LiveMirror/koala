package org.openkoala.security.controller;

import javax.inject.Inject;

import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.ChangeUserAccountCommand;
import org.openkoala.security.facade.command.ChangeUserEmailCommand;
import org.openkoala.security.facade.command.ChangeUserPasswordCommand;
import org.openkoala.security.facade.command.ChangeUserTelePhoneCommand;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.openkoala.security.shiro.realm.CustomAuthoringRealm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 当前用户的一些操作
 * 
 * @author luzhao
 * 
 */
@Controller
@RequestMapping("/auth/currentUser")
public class CurrentUserController {

	@Inject
	private SecurityConfigFacade securityConfigFacade;

    @Inject
    private SecurityAccessFacade securityAccessFacade;

    @Inject
    private CustomAuthoringRealm customAuthoringRealm;
	
	/**
	 * 更改用户账号。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeUserAccount", method = RequestMethod.POST)
	public InvokeResult changeUserAccount(ChangeUserAccountCommand command) {
		return  securityConfigFacade.changeUserAccount(command);

	}

	/**
	 * 更改用户邮箱。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeUserEmail", method = RequestMethod.POST)
	public InvokeResult changeUserEmail(ChangeUserEmailCommand command) {

		return securityConfigFacade.changeUserEmail(command);
	}

	/**
	 * 更改用户联系电话。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeUserTelePhone", method = RequestMethod.POST)
	public InvokeResult changeUserTelePhone(ChangeUserTelePhoneCommand command) {
		return securityConfigFacade.changeUserTelePhone(command);
	}
	
	/**
	 * 更新用户密码。
	 * 
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public InvokeResult changeUserPassword(ChangeUserPasswordCommand command) {
		command.setUserAccount(CurrentUser.getUserAccount());
		return securityConfigFacade.changeUserPassword(command);
	}

    /**分页查询用户的角色
     *
     * @param page 当前页
     * @param pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pagingQueryRolesOfUser", method = RequestMethod.GET)
    public InvokeResult pagingQueryRolesOfUser(int page,int pagesize){
        String userAccount = CurrentUser.getUserAccount();
        return securityAccessFacade.pagingQueryRolesOfUser(page,pagesize,userAccount);
    }

    /**
     * 切换角色
     * @param roleName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/switchOverRoleOfUser", method = RequestMethod.POST)
    public InvokeResult switchOverRoleOfUser(String roleName){
        return customAuthoringRealm.switchOverRoleOfUser(roleName);
    }

    @ResponseBody
    @RequestMapping(value="/getUserDetail", method = RequestMethod.GET)
    public InvokeResult getUserDetail(){
        String userAccount = CurrentUser.getUserAccount();
        return securityAccessFacade.getuserDetail(userAccount);
    }

}
