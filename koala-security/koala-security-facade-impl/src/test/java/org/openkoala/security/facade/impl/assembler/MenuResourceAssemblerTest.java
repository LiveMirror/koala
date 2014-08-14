package org.openkoala.security.facade.impl.assembler;

import static org.junit.Assert.assertNotNull;
import static org.openkoala.security.facade.impl.util.CommandHelper.assertEqualsMenuResourceAndCommand;
import static org.openkoala.security.facade.impl.util.CommandHelper.initCreateMenuResourceCommand;

import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.facade.command.CreateMenuResourceCommand;
import org.openkoala.security.facade.impl.AbstractFacadeIntegrationTestCase;

public class MenuResourceAssemblerTest extends AbstractFacadeIntegrationTestCase {

	@Test
	public void testToMenuResource() {
		CreateMenuResourceCommand command = initCreateMenuResourceCommand();
		MenuResource menuResource = MenuResourceAssembler.toMenuResource(command);
		assertNotNull(menuResource);
		assertEqualsMenuResourceAndCommand(command, menuResource);
	}

}
