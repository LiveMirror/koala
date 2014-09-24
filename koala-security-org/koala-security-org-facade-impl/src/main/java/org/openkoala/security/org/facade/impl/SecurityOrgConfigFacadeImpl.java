package org.openkoala.security.org.facade.impl;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.UserAccountIsExistedException;
import org.openkoala.security.core.domain.*;
import org.openkoala.security.org.core.domain.OrganisationScope;
import org.openkoala.security.org.facade.command.*;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.org.facade.impl.assembler.EmployeeUserAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;

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
	public void saveOrganization(OrganizationScopeDTO organizationScopeDTO) {

	}

	@Override
	public void updateOrganization(OrganizationScopeDTO organizationScopeDTO) {

	}

	@Override
	public void terminateOrganizations(OrganizationScopeDTO[] organizationScopeDTOs) {

	}

	@Override
	public void saveChildToParent(OrganizationScopeDTO child, Long parentId) {

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
	public InvokeResult grantRolesToUserInScope(Long userId, GrantRoleToUserInScopeCommand[] commands) {
//		EmployeeUser employeeUser = securityAccessApplication.getActorById(userId);
//
//		for (GrantRoleToUserInScopeCommand command : commands) {
//			Long roleId = command.getRoleId();
//			Set<Long> scopeIds = command.getScopeIds();
//			Role role = securityAccessApplication.getRoleBy(roleId);
//			if(!scopeIds.isEmpty()){
//                for (Long scopeId : scopeIds) {
//                    OrganisationScope scope = securityAccessApplication.getScope(scopeId);
//                    securityConfigApplication.checkRoleOfUserInScope(employeeUser,role,scope);
//                    if(scope == null){
//                        scope.setId(scopeId);
//                        securityConfigApplication.createScope(scope);
//			            securityConfigApplication.grantActorToAuthorityInScope(employeeUser, role, scope);
//                    }
//
//                }
//            }
//            //TODO 等待测试。
//		}
		return InvokeResult.success();
	}

	@Override
	public InvokeResult grantPermissionToUserInScope(Long userId, GrantPermissionToUserInScopeCommand[] commands) {
        EmployeeUser employeeUser = securityAccessApplication.getActorById(userId);
		for (GrantPermissionToUserInScopeCommand command : commands) {
			Long permissionId = command.getPermissionId();
			Set<Long> scopeIds = command.getScopeIds();
			Permission permission = securityAccessApplication.getPermissionBy(permissionId);
            if(!scopeIds.isEmpty()){
                for(Long scopeId : scopeIds){
                    OrganisationScope scope = securityAccessApplication.getScope(scopeId);
                    if(scope == null){
                        scope.setId(scopeId);
                        securityConfigApplication.createScope(scope);
            			securityConfigApplication.grantActorToAuthorityInScope(employeeUser, permission, scope);
                    }
                }
            }

		}
		return InvokeResult.success();
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
    public InvokeResult grantRolesToUserInScope(AuthorizationCommand[] authorizations) {
//
//        Long actorId = authorizations[0].getActorId();
//
//        List<AuthorizationCommand> targetOwnerAuthorizations =  Arrays.asList(authorizations);
//
//        List<AuthorizationCommand> originalAuthorizations = organizationApplication.findAllAuthorizationsByActorId(actorId);
//
//        List<AuthorizationCommand> tmpList = Lists.newArrayList(targetOwnerAuthorizations);
//
//        // 待添加的
//        List<AuthorizationCommand> waitingAddList = new ArrayList<AuthorizationCommand>();
//
//        // 带删除的
//        List<AuthorizationCommand> waitingDelList = new ArrayList<AuthorizationCommand>();
//
//        // 得到相同的菜单
//        targetOwnerAuthorizations.retainAll(originalAuthorizations);
//
//        // 原有菜单删除相同菜单
//        originalAuthorizations.removeAll(targetOwnerAuthorizations);
//
//        // 得到待删除的菜单
//        waitingDelList.addAll(originalAuthorizations);
//
//        // 现有菜单删除相同菜单
//        tmpList.removeAll(targetOwnerAuthorizations);
//
//        // 得到带添加的菜单
//        waitingAddList.addAll(tmpList);
//
//
//        Actor actor = securityAccessApplication.getActorById(actorId);
//
//        for(AuthorizationCommand waitingDel : waitingAddList){
//            Authority authority = securityAccessApplication.getAuthority(waitingDel.getAuthorityId());
//            Scope scope = securityAccessApplication.getScope(waitingDel.getScopeId());
//            securityConfigApplication.terminateActorFromAuthorityInScope(actor,authority,scope);
//        }
//
//        for(AuthorizationCommand waitingAdd : waitingAddList){
//            Authority authority = securityAccessApplication.getAuthority(waitingAdd.getAuthorityId());
//            Scope scope = securityAccessApplication.getScope(waitingAdd.getScopeId());
//            if(scope == null){
//                scope.setId(waitingAdd.getScopeId());
//                securityConfigApplication.createScope(scope);
//            }
//            securityConfigApplication.grantActorToAuthorityInScope(actor,authority,scope);
//        }

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


}
