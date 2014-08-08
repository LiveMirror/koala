package org.openkoala.security.facade.assembler;

import org.openkoala.security.core.domain.Role;
import org.openkoala.security.facade.command.CreateRoleCommand;

public class RoleAssembler {

	public static Role toRole(CreateRoleCommand command) {
		Role result = new Role(command.getName());
		result.setDescription(command.getDescription());
		return result;
	}
}
