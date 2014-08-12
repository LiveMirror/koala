package org.openkoala.security.facade.impl.assembler;

import static org.junit.Assert.assertNotNull;
import static org.openkoala.security.facade.impl.util.CommandHelper.assertEqualsRoleAndCommand;
import static org.openkoala.security.facade.impl.util.CommandHelper.initCreateRoleCommand;

import org.junit.Test;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.facade.command.CreateRoleCommand;
import org.openkoala.security.facade.impl.AbstractFacadeIntegrationTestCase;

public class RoleAssemblerTest extends AbstractFacadeIntegrationTestCase{

	@Test
	public void testToRole() {
		CreateRoleCommand command = initCreateRoleCommand();
		Role role = RoleAssembler.toRole(command);
		assertNotNull(role);
		assertEqualsRoleAndCommand(command, role);
	}

}
