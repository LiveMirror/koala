package org.openkoala.organisation.application.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.core.domain.Post;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional(value = "transactionManager_org")
public class PostApplicationImpl implements PostApplication {

	
	public Set<Post> findPostsByOrganizationId(Long organizationId) {
		Organization organization = Organization.get(Organization.class, organizationId);
		return organization != null ? organization.getPosts(new Date()) : new HashSet<Post>();
	}

}
