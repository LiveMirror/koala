package org.openkoala.security.facade.impl.assembler;

import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.facade.command.CreatePageElementResourceCommand;

public class PageElementResourceAssembler {

	public static PageElementResource toPageElementResource(CreatePageElementResourceCommand command) {
		PageElementResource result = new PageElementResource(command.getName(), command.getIdentifier());
		result.setDescription(command.getDescription());
		return result;
	}
}
