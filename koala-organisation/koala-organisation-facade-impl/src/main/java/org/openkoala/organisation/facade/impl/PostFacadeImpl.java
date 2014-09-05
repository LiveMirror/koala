package org.openkoala.organisation.facade.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.organisation.NameExistException;
import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.PostExistException;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.TerminateHasEmployeePostException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;
import org.openkoala.organisation.facade.PostFacade;
import org.openkoala.organisation.facade.dto.InvokeResult;
import org.openkoala.organisation.facade.dto.PostDTO;
import org.openkoala.organisation.facade.impl.assembler.PostDtoAssembler;
import org.springframework.transaction.annotation.Transactional;

@Named
//@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
//@Stateless(name = "PostApplication")
//@Remote
public class PostFacadeImpl implements PostFacade {

	@Inject
	private BaseApplication baseApplication;
	
	@Inject
	private PostApplication postApplication;
	
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
	public Page<PostDTO> pagingQueryPostsOfOrganizatoin(Long organizationId, PostDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _post from Post _post"
				+ " where _post.organization = ? and _post.createDate <= ? and _post.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(baseApplication.getEntity(Organization.class, organizationId));
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
			results.add(PostDtoAssembler.assemDto(post));
		}
		return results;
	}

	@Override
	public PostDTO getPostById(Long id) {
		Post post = baseApplication.getEntity(Post.class, id);
		return post == null ? null : PostDtoAssembler.assemDto(post);
	}

	@Override
	public Set<PostDTO> findPostsByOrganizationId(Long organizationId) {
		Set<PostDTO> results = new HashSet<PostDTO>();
		for (Post post : postApplication.findPostsByOrganizationId(organizationId)) {
			results.add(PostDtoAssembler.assemDto(post));
		}
		return results;
	}

	@Override
	public InvokeResult createPost(PostDTO postDTO) {
		try {
			baseApplication.saveParty(PostDtoAssembler.assemEntity(postDTO));
			return InvokeResult.success();
		} catch (NameExistException exception) {
			return InvokeResult.failure("岗位名称: " + postDTO.getName() + " 已经存在！");
        } catch (PostExistException exception) {
        	return InvokeResult.failure("请不要在相同机构中创建相同职务的岗位！");
        } catch (OrganizationHasPrincipalYetException exception) {
        	return InvokeResult.failure("该机构已经有负责岗位！");
        } catch (SnIsExistException exception) {
        	return InvokeResult.failure("岗位编码: " + postDTO.getSn() + " 已被使用！");
        } catch (Exception e) {
        	e.printStackTrace();
        	return InvokeResult.failure("保存失败！");
        }
	}

	@Override
	public InvokeResult updatePostInfo(PostDTO postDTO) {
		try {
			baseApplication.updateParty(PostDtoAssembler.assemEntity(postDTO));
			return InvokeResult.success();
		} catch (PostExistException exception) {
			return InvokeResult.failure("请不要在相同机构中创建相同职务的岗位！");
		} catch (OrganizationHasPrincipalYetException exception) {
			return InvokeResult.failure("该机构已经有负责岗位！");
		} catch (SnIsExistException exception) {
			return InvokeResult.failure("岗位编码: " + postDTO.getSn() + " 已被使用！");
		} catch (NameExistException exception) {
			return InvokeResult.failure("岗位名称: " + postDTO.getName() + " 已经存在！");
        } catch (Exception e) {
			e.printStackTrace();
        	return InvokeResult.failure("修改失败！");
		}
	}

	@Override
	public InvokeResult terminatePost(PostDTO postDTO) {
		try {
			baseApplication.terminateParty(PostDtoAssembler.assemEntity(postDTO));
			return InvokeResult.success();
		} catch (TerminateHasEmployeePostException exception) {
            return InvokeResult.failure("还有员工在此岗位上任职，不能撤销！");
        } catch (Exception e) {
            e.printStackTrace();
        	return InvokeResult.failure("撤销员工岗位失败！");
        }
	}

	@Override
	public InvokeResult terminatePosts(PostDTO[] postDtos) {
		Set<Post> posts = new HashSet<Post>();
		for (PostDTO postDTO : postDtos) {
			posts.add(PostDtoAssembler.assemEntity(postDTO));
		}
		
		try {
			baseApplication.terminateParties(posts);
			return InvokeResult.success();
		} catch (TerminateHasEmployeePostException exception) {
			return InvokeResult.failure("还有员工在此岗位上任职，不能撤销！");
        } catch (Exception e) {
            e.printStackTrace();
        	return InvokeResult.failure("撤销员工岗位失败！");
        }
	}

}
