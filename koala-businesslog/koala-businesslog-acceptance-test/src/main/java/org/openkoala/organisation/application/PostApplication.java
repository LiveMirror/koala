package org.openkoala.organisation.application;

import java.util.Set;

import org.dayatang.utils.Page;
import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Organization;

public interface PostApplication {

	Page<PostDTO> pagingQueryPosts(PostDTO example, int currentPage, int pagesize);
	
	Page<PostDTO> pagingQueryPostsOfOrganizatoin(Organization organization, PostDTO example, int currentPage, int pagesize);
	
	PostDTO getPostById(Long id);
	
	Set<PostDTO> findPostsByOrganizationId(Long organizationId);
}
