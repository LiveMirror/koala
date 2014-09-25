package org.openkoala.security.org.facade.impl;

import com.google.common.collect.Lists;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.application.SecurityDBInitApplication;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.domain.*;
import org.openkoala.security.org.core.domain.OrganisationScope;
import org.openkoala.security.org.facade.command.*;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;
import org.openkoala.security.org.facade.impl.assembler.EmployeeUserAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

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

    @Inject
    private SecurityDBInitApplication securityDBInitApplication;

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
            if(command.getEmployeeId() != null){
                Employee employee =  baseApplication.getEntity(Employee.class,command.getEmployeeId());
                employeeUser.setEmployee(employee);
            }
            securityConfigApplication.createActor(employeeUser);
            return InvokeResult.success();
		} catch (NullArgumentException e) {
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

    @Override
    public InvokeResult grantRolesToUserInScope(AuthorizationCommand command) {
        Actor actor = securityAccessApplication.getActorById(command.getActorId());
        Authority authority = securityAccessApplication.getAuthority(command.getAuthorityId());

        Organization organization = organizationApplication.getOrganizationById(command.getOrganizationId());
        Scope scope = findOrganizationScope(organization);

        if(scope == null){
            scope = new OrganisationScope(command.getOrganizationName(),organization);
            securityConfigApplication.createScope(scope);
        }

        securityConfigApplication.grantActorToAuthorityInScope(actor,authority,scope);

        return InvokeResult.success();
    }

    private OrganisationScope findOrganizationScope(Organization organization){
       OrganisationScope organisationScope = (OrganisationScope) getQueryChannelService()//
                .createNamedQuery("OrganisationScope.hasOrganizationOfScope")//
                .addParameter("organization",organization)//
                .singleResult();
        return organisationScope;
    }

    @Override
    public InvokeResult changeEmployeeUserProps(ChangeEmployeeUserPropsCommand command) {
        try {
            EmployeeUser employeeUser = securityAccessApplication.getActorById(command.getId());
            // 可能不选择员工。
            if(command.getEmployeeId() != null){
                Employee employee =  baseApplication.getEntity(Employee.class,command.getEmployeeId());
                employeeUser.setEmployee(employee);
            }
            employeeUser.setName(command.getName());
            employeeUser.setDescription(command.getDescription());
            securityConfigApplication.createActor(employeeUser);
            return InvokeResult.success();
        } catch (NullArgumentException e) {
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
        if(securityAccessApplication.hasUserExisted()){
            return;
        }
        EmployeeUser employeeUser = createEmployeeUser();
        securityDBInitApplication.initActor(employeeUser);
        Role role = securityDBInitApplication.initRole();
        List<MenuResource> menuResources = securityDBInitApplication.initMenuResources();
        List<PageElementResource> pageElementResources = securityDBInitApplication.initPageElementResources();
        List<UrlAccessResource> urlAccessResources = securityDBInitApplication.initUrlAccessResources();
        List<MenuResource> orgMenuResources = createOrgMenuResource();
        securityConfigApplication.grantAuthorityToActor(role,employeeUser);
        securityConfigApplication.grantSecurityResourcesToAuthority(menuResources,role);
        securityConfigApplication.grantSecurityResourcesToAuthority(pageElementResources,role);
        securityConfigApplication.grantSecurityResourcesToAuthority(urlAccessResources,role);
        securityConfigApplication.grantSecurityResourcesToAuthority(orgMenuResources,role);

    }

    private EmployeeUser createEmployeeUser() {
        EmployeeUser employeeUser = new EmployeeUser("张三", "zhangsan");
        employeeUser.setCreateOwner("admin");
        employeeUser.setDescription("普通用户");
        return employeeUser;
    }

    private List<MenuResource> createOrgMenuResource() {
        String menuIcon = "glyphicon  glyphicon-list-alt";
        MenuResource rootMenuResouce = new MenuResource("组织机构管理");
        rootMenuResouce.setMenuIcon(menuIcon);
        rootMenuResouce.setDescription("组织机构管理菜单");
        rootMenuResouce.save();

        MenuResource departmentMenuResouce = new MenuResource("机构管理");
        departmentMenuResouce.setUrl("/pages/organisation/departmentList.jsp");
        departmentMenuResouce.setMenuIcon(menuIcon);
        rootMenuResouce.addChild(departmentMenuResouce);

        MenuResource jobMenuResouce = new MenuResource("职务管理");
        jobMenuResouce.setUrl("/pages/organisation/jobList.jsp");
        jobMenuResouce.setMenuIcon(menuIcon);
        rootMenuResouce.addChild(jobMenuResouce);

        MenuResource positionMenuResouce = new MenuResource("岗位管理");
        positionMenuResouce.setUrl("/pages/organisation/positionList.jsp");
        positionMenuResouce.setMenuIcon(menuIcon);
        rootMenuResouce.addChild(positionMenuResouce);

        MenuResource employeeMenuResouce = new MenuResource("人员管理");
        employeeMenuResouce.setUrl("/pages/organisation/employeeList.jsp");
        employeeMenuResouce.setMenuIcon(menuIcon);
        rootMenuResouce.addChild(employeeMenuResouce);

        return Lists.newArrayList(rootMenuResouce,//
                departmentMenuResouce,//
                jobMenuResouce,//
                positionMenuResouce,//
                employeeMenuResouce);
    }

}
