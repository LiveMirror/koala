package org.openkoala.organisation.facade.impl.assembler;

import java.util.Date;

import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.facade.dto.PostDTO;

public class PostAssembler {

	public static Post toEntity(PostDTO postDTO) {
        if (postDTO == null ){
            return null;
        }

		Post result = new Post(postDTO.getName());
		result.setId(postDTO.getId());

		if (postDTO.getCreateDate() != null) {
			result.setCreateDate(postDTO.getCreateDate());
		}
		if (postDTO.getTerminateDate() != null) {
			result.setTerminateDate(postDTO.getTerminateDate());
		}
		
		result.setSn(postDTO.getSn());
		result.setDescription(postDTO.getDescription());
		result.setOrganizationPrincipal(postDTO.isOrganizationPrincipal());
		result.setVersion(postDTO.getVersion());
		
		if (postDTO.getCreateDate() != null) {
			result.setCreateDate(postDTO.getCreateDate());
		}
		if (postDTO.getTerminateDate() != null) {
			result.setTerminateDate(postDTO.getTerminateDate());
		}

		Job job = Job.get(Job.class, postDTO.getJobId());
		result.setJob(job);
		
		Organization organization = Organization.get(Organization.class, postDTO.getOrganizationId());
		result.setOrganization(organization);
		
		return result;
	}
	
	public static PostDTO toDTO(Post post) {
		if (post == null) {
			return null;
		}
		
		PostDTO result = new PostDTO();
		result.setId(post.getId());
		result.setName(post.getName());
		result.setSn(post.getSn());
		result.setDescription(post.getDescription());
		result.setOrganizationPrincipal(post.isOrganizationPrincipal());
		result.setVersion(post.getVersion());
		result.setCreateDate(post.getCreateDate());
		result.setTerminateDate(post.getTerminateDate());
		result.setEmployeeCount(post.getEmployeeCount(new Date()));
		
		Job job = post.getJob();
		result.setJobId(job.getId());
		result.setJobName(job.getName());
		
		Organization organization = post.getOrganization();
		result.setOrganizationId(organization.getId());
		result.setOrganizationName(organization.getName());
		
		return result;
	}
	
}
