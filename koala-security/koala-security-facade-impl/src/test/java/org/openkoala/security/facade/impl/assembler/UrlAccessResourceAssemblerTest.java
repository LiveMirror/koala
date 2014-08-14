package org.openkoala.security.facade.impl.assembler;

import static org.junit.Assert.assertNotNull;
import static org.openkoala.security.facade.impl.util.CommandHelper.assertEqualsUrlAccessResourceAndCommand;
import static org.openkoala.security.facade.impl.util.CommandHelper.initCreateUrlAccessResourceCommand;

import org.junit.Test;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.facade.command.CreateUrlAccessResourceCommand;

public class UrlAccessResourceAssemblerTest {

	@Test
	public void testToUrlAccessResource() {
		CreateUrlAccessResourceCommand command = initCreateUrlAccessResourceCommand();
		UrlAccessResource urlAccessResource = UrlAccessResourceAssembler.toUrlAccessResource(command);
		assertNotNull(urlAccessResource);
		assertEqualsUrlAccessResourceAndCommand(command, urlAccessResource);
	}

}
