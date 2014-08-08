package org.openkoala.security.facade.assembler;

import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.facade.command.CreatePermissionCommand;

public class PermissionAssembler {

	public static Permission toPermission(CreatePermissionCommand command) {
		Permission result = new Permission(command.getName(), command.getIdentifier());
		result.setDescription(command.getDescription());
		return result;
	}
}
