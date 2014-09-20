package org.openkoala.security.org.controller;

import javax.inject.Inject;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.command.*;
import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.dto.AuthorizationDTO;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 更改属性，撤销、重置密码，激活、挂起、都在UserController中。
 */
@Controller
@RequestMapping("/auth/employeeUser")
public class EmployeeUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeUserController.class);

    @Inject
    private SecurityOrgConfigFacade securityOrgConfigFacade;

    @Inject
    private SecurityOrgAccessFacade securityOrgAccessFacade;

    /**
     * 添加用户
     *
     * @param command
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public InvokeResult add(CreateEmpolyeeUserCommand command) {
        String createOwner = CurrentUser.getUserAccount();
        command.setCreateOwner(createOwner);
        return securityOrgConfigFacade.createEmployeeUser(command);
    }

    @ResponseBody
    @RequestMapping(value =  "/update", method = RequestMethod.POST)
    public InvokeResult update(ChangeEmployeeUserPropsCommand command){
        return securityOrgConfigFacade.changeEmployeeUserProps(command);
    }

    // ~ 授权

    /**
     * TODO 查询出用户默认的范围。默认范围又员工查询。
     * 为用户授权某个范围下的角色。
     *
     * @param userId
     * @param commands
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/grantRolesToUserInScope", method = RequestMethod.POST)
    public InvokeResult grantRolesToUserInScope(Long userId, GrantRoleToUserInScopeCommand[] commands) {
        return securityOrgConfigFacade.grantRolesToUserInScope(userId, commands);
    }

    public InvokeResult grantRolesToUserInScope(AuthorizationDTO[] authorizations){
        return securityOrgConfigFacade.grantRolesToUserInScope(authorizations);
    }

    /**
     * TODO 查询出用户默认的范围。默认范围又员工查询。
     * 为用户授权某个范围下的权限。
     *
     * @param userId
     * @param commands
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/grantPermissionsToUserInScope", method = RequestMethod.POST)
    public InvokeResult grantPermissionsToUserInScope(Long userId, GrantPermissionToUserInScopeCommand[] commands) {
        return securityOrgConfigFacade.grantPermissionToUserInScope(userId, commands);
    }

    @ResponseBody
    @RequestMapping(value = "/terminateUserFromRoleInScope", method = RequestMethod.POST)
    public InvokeResult terminateUserFromRoleInScope(Long userId, TerminateUserFromRoleInScopeCommand[] commands) {
        return securityOrgConfigFacade.terminateUserFromRoleInScope(userId, commands);
    }

    @ResponseBody
    @RequestMapping(value = "/terminateUserFromPermissionInScope", method = RequestMethod.POST)
    public InvokeResult terminateUserFromPermissionInScope(Long userId, TerminateUserFromPermissionInScopeCommand[] commands) {
        return securityOrgConfigFacade.terminateUserFromPermissionInScope(userId, commands);
    }

    @ResponseBody
    @RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
    public InvokeResult pagingQuery(int page, int pagesize, EmployeeUserDTO queryEmployeeUserCondition) {
        return securityOrgAccessFacade.pagingQueryEmployeeUsers(page,pagesize,queryEmployeeUserCondition);
    }



}
