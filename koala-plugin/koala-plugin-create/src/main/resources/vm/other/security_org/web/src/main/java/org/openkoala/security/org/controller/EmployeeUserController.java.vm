package org.openkoala.security.org.controller;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.command.ChangeEmployeeUserPropsCommand;
import org.openkoala.security.org.facade.command.CreateEmpolyeeUserCommand;
import org.openkoala.security.org.facade.command.TerminateUserFromPermissionInScopeCommand;
import org.openkoala.security.org.facade.command.TerminateUserFromRoleInScopeCommand;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.org.facade.dto.OrgPermissionDTO;
import org.openkoala.security.org.facade.dto.OrgRoleDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户员工控制器。
 * 分页都将采用POST请求方式，因GET请求搜索时携带中文会导致乱码。
 *
 * @author lucas
 */
@Controller
@RequestMapping("/auth/employeeUser")
public class EmployeeUserController {

    @Inject
    private SecurityOrgConfigFacade securityOrgConfigFacade;

    @Inject
    private SecurityOrgAccessFacade securityOrgAccessFacade;

    /**
     * 添加用户
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public InvokeResult add(CreateEmpolyeeUserCommand command) {
        String createOwner = CurrentUser.getUserAccount();
        command.setCreateOwner(createOwner);
        return securityOrgConfigFacade.createEmployeeUser(command);
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public InvokeResult update(ChangeEmployeeUserPropsCommand command) {
        return securityOrgConfigFacade.changeEmployeeUserProps(command);
    }

    // ~ 授权

    @ResponseBody
    @RequestMapping(value = "/pagingQueryGrantRoleByUserId", method = RequestMethod.POST)
    public Page<OrgRoleDTO> pagingQueryRolesByUserId(int page, int pagesize, Long userId) {
        return securityOrgAccessFacade.pagingQueryGrantRolesByUserId(page, pagesize, userId);
    }


    /**
     * 根据用户ID分页查询已经授权的权限
     */
    @ResponseBody
    @RequestMapping(value = "/pagingQueryGrantPermissionByUserId", method = RequestMethod.POST)
    public Page<OrgPermissionDTO> pagingQueryGrantPermissionByUserId(int page, int pagesize, Long userId) {
        return securityOrgAccessFacade.pagingQueryGrantPermissionsByUserId(page, pagesize, userId);
    }

    @ResponseBody
    @RequestMapping(value = "/grantAuthorityToActorInScope", method = RequestMethod.POST)
    public InvokeResult grantAuthorityToActorInScope(AuthorizationCommand command) {
        return securityOrgConfigFacade.grantAuthorityToActorInScope(command);
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
    @RequestMapping(value = "/pagingQuery", method = RequestMethod.POST)
    public Page<EmployeeUserDTO> pagingQuery(int page, int pagesize, EmployeeUserDTO queryEmployeeUserCondition) {
        return securityOrgAccessFacade.pagingQueryEmployeeUsers(page, pagesize, queryEmployeeUserCondition);
    }
}
