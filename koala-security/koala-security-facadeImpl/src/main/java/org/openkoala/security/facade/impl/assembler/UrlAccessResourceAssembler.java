package org.openkoala.security.facade.impl.assembler;

import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.facade.command.CreateUrlAccessResourceCommand;

public class UrlAccessResourceAssembler {

	public static UrlAccessResource toUrlAccessResource(CreateUrlAccessResourceCommand command) {
		UrlAccessResource result = new UrlAccessResource(command.getName(), command.getUrl());
		result.setDescription(command.getDescription());
		return result;
	}
}
