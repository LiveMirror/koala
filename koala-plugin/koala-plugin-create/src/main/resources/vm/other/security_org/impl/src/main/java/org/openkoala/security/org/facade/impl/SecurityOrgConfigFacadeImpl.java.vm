package org.openkoala.security.org.facade.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;

import org.openkoala.security.application.systeminit.SystemInit;
import org.openkoala.security.application.systeminit.SystemInitFactory;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.domain.*;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.core.domain.OrganisationScope;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.command.ChangeEmployeeUserPropsCommand;
import org.openkoala.security.org.facade.command.CreateEmpolyeeUserCommand;
import org.openkoala.security.org.facade.command.TerminateUserFromPermissionInScopeCommand;
import org.openkoala.security.org.facade.command.TerminateUserFromRoleInScopeCommand;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;
import org.openkoala.security.org.facade.impl.assembler.EmployeeUserAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "transactionManager_security")
@Named
public class SecurityOrgConfigFacadeImpl implements SecurityOrgConfigFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityOrgConfigFacadeImpl.class);

    @Inject
    private BaseApplication baseApplication;

    @Inject
    private SecurityConfigApplication securityConfigApplication;

    @Inject
    private SecurityAccessApplication securityAccessApplication;

    @Inject
    private OrganizationApplication organizationApplication;

    private QueryChannelService queryChannelService;

    public QueryChannelService getQueryChannelService() {
        if (queryChannelService == null) {
            queryChannelService = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_security");
        }
        return queryChannelService;
    }

    @Override
    public InvokeResult createEmployeeUser(CreateEmpolyeeUserCommand command) {
        try {
            EmployeeUser employeeUser = EmployeeUserAssembler.toEmployeeUser(command);
            // 可能不选择员工。
            if (command.getEmployeeId() != null) {
                Employee employee = baseApplication.getEntity(Employee.class, command.getEmployeeId());
                employeeUser.setEmployee(employee);
            }
            securityConfigApplication.createActor(employeeUser);
            return InvokeResult.success();
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            return InvokeResult.failure("名称或者账户不能为空。");
        } catch (UserAccountIsExistedException e) {
            LOGGER.error(e.getMessage(), e);
            return InvokeResult.failure("账号已经存在。");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return InvokeResult.failure("添加用户失败。");
        }
    }

    @Override
    public InvokeResult terminateUserFromRoleInScope(Long userId, TerminateUserFromRoleInScopeCommand[] commands) {
        EmployeeUser employeeUser = securityAccessApplication.getActorById(userId);
        for (TerminateUserFromRoleInScopeCommand command : commands) {
            Long roleId = command.getRoleId();
            Long scopeId = command.getScopeId();
            Role role = securityAccessApplication.getRoleBy(roleId);
            OrganisationScope scope = securityAccessApplication.getScope(scopeId);
            securityConfigApplication.terminateActorFromAuthorityInScope(employeeUser, role, scope);
        }
        return InvokeResult.success();
    }

    @Override
    public InvokeResult terminateUserFromPermissionInScope(Long userId, TerminateUserFromPermissionInScopeCommand[] commands) {
        EmployeeUser employeeUser = securityAccessApplication.getActorById(userId);
        for (TerminateUserFromPermissionInScopeCommand command : commands) {
            Long permissionId = command.getPermissionId();
            Long scopeId = command.getScopeId();
            Permission permission = securityAccessApplication.getPermissionBy(permissionId);
            OrganisationScope scope = securityAccessApplication.getScope(scopeId);
            securityConfigApplication.terminateActorFromAuthorityInScope(employeeUser, permission, scope);
        }
        return InvokeResult.success();
    }

    private OrganisationScope findOrganizationScope(Organization organization) {
        OrganisationScope organisationScope = (OrganisationScope) getQueryChannelService()
                .createNamedQuery("OrganisationScope.hasOrganizationOfScope")
                .addParameter("organization", organization)
                .singleResult();
        return organisationScope;
    }

    @Override
    public InvokeResult changeEmployeeUserProps(ChangeEmployeeUserPropsCommand command) {
        try {
            EmployeeUser employeeUser = securityAccessApplication.getActorById(command.getId());
            // 可能不选择员工。
            if (command.getEmployeeId() != null) {
                Employee employee = baseApplication.getEntity(Employee.class, command.getEmployeeId());
                employeeUser.setEmployee(employee);
            }
            employeeUser.setName(command.getName());
            employeeUser.setDescription(command.getDescription());

            securityConfigApplication.changeLastModifyTimeOfUser(employeeUser);

            securityConfigApplication.createActor(employeeUser);
            return InvokeResult.success();
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            return InvokeResult.failure("名称或者账户不能为空。");
        } catch (UserAccountIsExistedException e) {
            LOGGER.error(e.getMessage(), e);
            return InvokeResult.failure("账号已经存在。");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return InvokeResult.failure("添加用户失败。");
        }
    }

    @Override
    public void initSecurityOrgSystem() {
        if (securityAccessApplication.hasUserExisted()) {
            return;
        }
        SystemInit init = SystemInitFactory.INSTANCE.getSystemInit("/META-INF/securityOrgSystemInit/systemInit.xml");
        EmployeeUser employeeUser = intiEmployeeUser(init.getUser());
        Role role = init.createRole();
        List<MenuResource> menuResources = init.createMenuResourceAndReturnNeedGrant();
        List<PageElementResource> pageElementResources = init.createPageElementResources();
        List<UrlAccessResource> urlAccessResources = init.createUrlAccessResources();
        securityConfigApplication.grantAuthorityToActor(role, employeeUser);
        securityConfigApplication.grantSecurityResourcesToAuthority(menuResources, role);
        securityConfigApplication.grantSecurityResourcesToAuthority(pageElementResources, role);
        securityConfigApplication.grantSecurityResourcesToAuthority(urlAccessResources, role);
    }

    @Override
    public InvokeResult grantAuthorityToActorInScope(AuthorizationCommand command) {
        Actor actor = securityAccessApplication.getActorById(command.getActorId());
        for (Long authorityId : command.getAuthorityIds()) {
            Authority authority = securityAccessApplication.getAuthority(authorityId);
            if (command.getOrganizationId() != null) {
                Organization organization = organizationApplication.getOrganizationById(command.getOrganizationId());
                Scope scope = findOrganizationScope(organization);
                if (scope == null) {
                    scope = new OrganisationScope(command.getOrganizationName(), organization);
                    securityConfigApplication.createScope(scope);
                }
                securityConfigApplication.grantActorToAuthorityInScope(actor, authority, scope);
            } else {
                securityConfigApplication.grantAuthorityToActor(authority, actor);
            }
        }
        return InvokeResult.success();
    }

    private EmployeeUser intiEmployeeUser(SystemInit.User initUser) {
        EmployeeUser employeeUser = new EmployeeUser(initUser.getName(), initUser.getUsername());
        employeeUser.setCreateOwner(initUser.getCreateOwner());
        employeeUser.setDescription(initUser.getDescription());
        securityConfigApplication.createActor(employeeUser);
        return employeeUser;
    }
}
