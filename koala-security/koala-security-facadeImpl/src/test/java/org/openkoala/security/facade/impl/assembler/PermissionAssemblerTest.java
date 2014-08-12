package org.openkoala.security.facade.impl.assembler;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.facade.command.CreatePermissionCommand;
import org.openkoala.security.facade.impl.AbstractFacadeIntegrationTestCase;

import static org.openkoala.security.facade.impl.util.CommandHelper.initCreatePermissionCommand;
import static org.openkoala.security.facade.impl.util.CommandHelper.assertEqualsPermissionAndCommand;;

public class PermissionAssemblerTest extends AbstractFacadeIntegrationTestCase{

	@Test
	public void testToPermission() {
		CreatePermissionCommand command = initCreatePermissionCommand();
		Permission permission = PermissionAssembler.toPermission(command);
		assertNotNull(permission);
		assertEqualsPermissionAndCommand(command, permission);
		
	}

}
