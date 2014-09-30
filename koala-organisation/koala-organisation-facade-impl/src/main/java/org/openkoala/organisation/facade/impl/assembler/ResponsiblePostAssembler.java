package org.openkoala.organisation.facade.impl.assembler;

import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.facade.dto.ResponsiblePostDTO;

public class ResponsiblePostAssembler {
	
	public static ResponsiblePostDTO toEntity(Post post, boolean principal) {
		ResponsiblePostDTO result = new ResponsiblePostDTO();
		result.setPrincipal(principal);
		result.setPostId(post.getId());
		result.setPostName(post.getName());
		result.setPostSn(post.getSn());
		result.setPostDescription(post.getDescription());
		return result;
	}
	
}
