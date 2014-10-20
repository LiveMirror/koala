package org.openkoala.organisation.web.controller;

import java.util.Set;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.PostFacade;
import org.openkoala.organisation.facade.dto.PostDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 岗位管理
 *
 * @author xmfang
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {

    @Inject
    private PostFacade postFacade;

    /**
     * 分页查询职务
     *
     * @param page
     * @param pagesize
     * @param postDto
     * @return
     */
    @ResponseBody
    @RequestMapping("/pagingquery")
    public Page<PostDTO> pagingQuery(int page, int pagesize, PostDTO postDto) {
    	return postFacade.pagingQueryPosts(postDto, page, pagesize);
    }

    /**
     * 创建一个岗位
     *
     * @param postDto
     * @param organizationId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InvokeResult createPost(PostDTO postDto, Long organizationId) {
    	postDto.setOrganizationId(organizationId);
        return postFacade.createPost(postDto);
    }

	/**
	 * 更新岗位信息
	 *
     * @param postDto
     * @param organizationId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public InvokeResult updatePost(PostDTO postDto, Long organizationId) {
		postDto.setOrganizationId(organizationId);
		return postFacade.updatePostInfo(postDto);
	}

    @ResponseBody
    @RequestMapping("/query-post-by-org")
    public Set<PostDTO> queryPostsOfOrganization(Long organizationId) {
        return postFacade.findPostsByOrganizationId(organizationId);
    }

    @ResponseBody
    @RequestMapping("/paging-query-post-by-org")
    public Page<PostDTO> pagingQueryPostsOfOrganization(Long organizationId, PostDTO example, int page, int pagesize) {
        return postFacade.pagingQueryPostsOfOrganizatoin(organizationId, example, page, pagesize);
    }

    /**
     * 根据ID获得职务
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/get/{id}")
    public PostDTO get(@PathVariable("id") Long id) {
        return postFacade.getPostById(id);
    }

    /**
     * 撤销某个职务
     *
     * @param postDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/terminate", method = RequestMethod.POST)
    public InvokeResult terminatePost(PostDTO postDto) {
        return postFacade.terminatePost(postDto);
    }

    /**
     * 同时撤销多个职务
     *
     * @param postDtos
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/terminate-posts", method = RequestMethod.POST)
    public InvokeResult terminatePosts(@RequestBody PostDTO[] postDtos) {
        return postFacade.terminatePosts(postDtos);
    }
    
}
