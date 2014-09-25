package org.openkoala.security.org.facade;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.command.*;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "transactionManager_security")
public interface SecurityOrgConfigFacade {

    InvokeResult createEmployeeUser(CreateEmpolyeeUserCommand command);

    InvokeResult terminateUserFromRoleInScope(Long userId, TerminateUserFromRoleInScopeCommand[] commands);

    InvokeResult terminateUserFromPermissionInScope(Long userId, TerminateUserFromPermissionInScopeCommand[] commands);

    InvokeResult grantRolesToUserInScope(AuthorizationCommand authorization);

    InvokeResult changeEmployeeUserProps(ChangeEmployeeUserPropsCommand command);

    void initSecurityOrgSystem();
}
