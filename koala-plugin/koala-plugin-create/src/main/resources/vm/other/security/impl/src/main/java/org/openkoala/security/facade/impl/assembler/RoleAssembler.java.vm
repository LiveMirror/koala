package org.openkoala.security.facade.impl.assembler;

import org.openkoala.security.core.domain.Role;
import org.openkoala.security.facade.command.CreateRoleCommand;
import org.openkoala.security.facade.dto.RoleDTO;

public class RoleAssembler {

	public static Role toRole(CreateRoleCommand command) {
		Role result = new Role(command.getName());
		result.setDescription(command.getDescription());
		return result;
	}

    public static RoleDTO toRoleDTO(Role role) {
        RoleDTO result = new RoleDTO(role.getId(), role.getName());
        result.setVersion(role.getVersion());
        result.setDescription(role.getDescription());
        return result;
    }
}
