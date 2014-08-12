package org.openkoala.security.facade.impl.assembler;

import static org.junit.Assert.assertNotNull;
import static org.openkoala.security.facade.impl.util.CommandHelper.assertEqualsPageElementResourceAndCommand;
import static org.openkoala.security.facade.impl.util.CommandHelper.initCreatePageElementResourceCommand;

import org.junit.Test;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.facade.command.CreatePageElementResourceCommand;
import org.openkoala.security.facade.impl.AbstractFacadeIntegrationTestCase;

public class PageElementResourceAssemblerTest extends AbstractFacadeIntegrationTestCase{

	@Test
	public void testToPageElementResource() {
		CreatePageElementResourceCommand command = initCreatePageElementResourceCommand();
		PageElementResource pageElementResource = PageElementResourceAssembler.toPageElementResource(command);
		assertNotNull(pageElementResource);
		assertEqualsPageElementResourceAndCommand(command, pageElementResource);
	}

}
