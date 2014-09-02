package org.openkoala.security.facade.impl.assembler;

import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.facade.command.CreatePermissionCommand;
import org.openkoala.security.facade.dto.PermissionDTO;

public class PermissionAssembler {

	public static Permission toPermission(CreatePermissionCommand command) {
		Permission result = new Permission(command.getName(), command.getIdentifier());
		result.setDescription(command.getDescription());
		return result;
	}

    public static PermissionDTO toPermissionDTO(Permission permission) {
        PermissionDTO result = new PermissionDTO(permission.getId(),permission.getName(),permission.getIdentifier(),permission.getDescription());
        result.setVersion(permission.getVersion());
        return result;
    }
}
