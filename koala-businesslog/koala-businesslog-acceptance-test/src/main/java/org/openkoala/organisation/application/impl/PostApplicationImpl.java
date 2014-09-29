package org.openkoala.organisation.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.dayatang.utils.Page;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.application.dto.PostDTO;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional(value = "transactionManager_org")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "PostApplication")
@Remote
public class PostApplicationImpl implements PostApplication {

	private QueryChannelService queryChannel;

	private QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_org");
		}
		return queryChannel;
	}

	@Override
	public Page<PostDTO> pagingQueryPosts(PostDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _post from Post _post where _post.createDate <= ? and _post.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_post", conditionVals, currentPage, pagesize);
	}

	@Override
	public Page<PostDTO> pagingQueryPostsOfOrganizatoin(Organization organization, PostDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _post from Post _post"
				+ " where _post.organization = ? and _post.createDate <= ? and _post.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(organization);
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_post", conditionVals, currentPage, pagesize);
	}

	private Page<PostDTO> queryResult(PostDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals, int currentPage, int pagesize) {
		assembleJpqlAndConditionValues(example, jpql, conditionPrefix, conditionVals);
		Page<Post> postPage = getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize).pagedList();

		return new Page<PostDTO>(postPage.getStart(), postPage.getResultCount(), pagesize, transformToDtos(postPage.getData()));
	}

	private void assembleJpqlAndConditionValues(PostDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals) {
		String andCondition = " and " + conditionPrefix;
		if (!StringUtils.isBlank(example.getName())) {
			jpql.append(andCondition);
			jpql.append(".name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getName()));
		}
		if (!StringUtils.isBlank(example.getSn())) {
			jpql.append(andCondition);
			jpql.append(".sn like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getSn()));
		}
		if (!StringUtils.isBlank(example.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getDescription()));
		}
	}

	private List<PostDTO> transformToDtos(List<Post> posts) {
		List<PostDTO> results = new ArrayList<PostDTO>();
		for (Post post : posts) {
			results.add(PostDTO.generateDtoBy(post));
		}
		return results;
	}

	@Override
	public PostDTO getPostById(Long id) {
		Post post = Post.get(Post.class, id);
		return post == null ? null : PostDTO.generateDtoBy(post);
	}

	@Override
	public Set<PostDTO> findPostsByOrganizationId(Long organizationId) {
		Organization organization = Organization.get(Organization.class, organizationId);
		Set<PostDTO> results = new HashSet<PostDTO>();
		for (Post post : organization.getPosts(new Date())) {
			results.add(PostDTO.generateDtoBy(post));
		}
		return results;
	}

}
