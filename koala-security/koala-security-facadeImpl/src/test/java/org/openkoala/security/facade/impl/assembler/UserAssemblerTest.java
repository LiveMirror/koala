package org.openkoala.security.facade.impl.assembler;

import static org.junit.Assert.*;
import static org.openkoala.security.facade.impl.util.CommandHelper.assertEqualsUserAndCommand;
import static org.openkoala.security.facade.impl.util.CommandHelper.initCreateUserCommand;
import org.junit.Test;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.command.CreateUserCommand;
import org.openkoala.security.facade.impl.AbstractFacadeIntegrationTestCase;

public class UserAssemblerTest extends AbstractFacadeIntegrationTestCase{

	@Test
	public void testToUser() {
		CreateUserCommand command = initCreateUserCommand();
		User user = UserAssembler.toUser(command);
		assertNotNull(user);
		assertEqualsUserAndCommand(command, user);
	}

}
