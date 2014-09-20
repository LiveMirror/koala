package org.openkoala.security.org.facade;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.command.*;
import org.openkoala.security.org.facade.dto.AuthorizationDTO;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "transactionManager_security")
public interface SecurityOrgConfigFacade {

	/**
	 * 保存组织范围
	 * 
	 * @param organizationScopeDTO
	 */
	void saveOrganization(OrganizationScopeDTO organizationScopeDTO);

	void updateOrganization(OrganizationScopeDTO organizationScopeDTO);

	void terminateOrganizations(OrganizationScopeDTO[] organizationScopeDTOs);

	void saveChildToParent(OrganizationScopeDTO child, Long parentId);

	InvokeResult createEmployeeUser(CreateEmpolyeeUserCommand command);

	InvokeResult grantRolesToUserInScope(Long userId, GrantRoleToUserInScopeCommand[] commands);

	InvokeResult grantPermissionToUserInScope(Long userId, GrantPermissionToUserInScopeCommand[] commands);

    InvokeResult terminateUserFromRoleInScope(Long userId, TerminateUserFromRoleInScopeCommand[] commands);

    InvokeResult terminateUserFromPermissionInScope(Long userId, TerminateUserFromPermissionInScopeCommand[] commands);

    InvokeResult grantRolesToUserInScope(AuthorizationDTO[] authorizations);

    InvokeResult changeEmployeeUserProps(ChangeEmployeeUserPropsCommand command);
}
