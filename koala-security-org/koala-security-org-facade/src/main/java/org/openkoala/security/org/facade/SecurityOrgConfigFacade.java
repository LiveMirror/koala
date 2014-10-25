package org.openkoala.security.org.facade;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.command.ChangeEmployeeUserPropsCommand;
import org.openkoala.security.org.facade.command.CreateEmpolyeeUserCommand;
import org.openkoala.security.org.facade.command.TerminateUserFromPermissionInScopeCommand;
import org.openkoala.security.org.facade.command.TerminateUserFromRoleInScopeCommand;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;

public interface SecurityOrgConfigFacade {

    InvokeResult createEmployeeUser(CreateEmpolyeeUserCommand command);

    InvokeResult terminateUserFromRoleInScope(Long userId, TerminateUserFromRoleInScopeCommand[] commands);

    InvokeResult terminateUserFromPermissionInScope(Long userId, TerminateUserFromPermissionInScopeCommand[] commands);

    InvokeResult changeEmployeeUserProps(ChangeEmployeeUserPropsCommand command);

    void initSecurityOrgSystem();

    InvokeResult grantAuthorityToActorInScope(AuthorizationCommand command);
}
