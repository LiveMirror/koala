package org.openkoala.organisation.application;

import org.dayatang.querychannel.Page;
import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Organization;

import java.util.Set;

public interface PostApplication {

	Page<PostDTO> pagingQueryPosts(PostDTO example, int currentPage, int pagesize);
	
	Page<PostDTO> pagingQueryPostsOfOrganizatoin(Organization organization, PostDTO example, int currentPage, int pagesize);
	
	PostDTO getPostById(Long id);
	
	Set<PostDTO> findPostsByOrganizationId(Long organizationId);
}
