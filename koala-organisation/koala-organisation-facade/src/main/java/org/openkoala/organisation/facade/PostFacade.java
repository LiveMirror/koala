package org.openkoala.organisation.facade;

import java.util.Set;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.dto.PostDTO;

/**
 * 岗位应用接口
 * 
 * @author xmfang
 * 
 */
public interface PostFacade {

	/**
	 * 分页查询岗位
	 * 
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<PostDTO> pagingQueryPosts(PostDTO example, int currentPage, int pagesize);

	/**
	 * 分页查询某个机构下的岗位
	 * 
	 * @param organizationId
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<PostDTO> pagingQueryPostsOfOrganizatoin(Long organizationId, PostDTO example, int currentPage, int pagesize);

	/**
	 * 根据id获取岗位
	 * 
	 * @param id
	 * @return
	 */
	PostDTO getPostById(Long id);

	/**
	 * 根据机构id获取该机构下的岗位
	 * 
	 * @param organizationId
	 * @return
	 */
	Set<PostDTO> findPostsByOrganizationId(Long organizationId);

	/**
	 * 创建一个岗位
	 * 
	 * @param postDTO
	 */
	InvokeResult createPost(PostDTO postDTO);

	/**
	 * 更新某个岗位的信息
	 * 
	 * @param postDTO
	 */
	InvokeResult updatePostInfo(PostDTO postDTO);

	/**
	 * 撤销一个岗位
	 * 
	 * @param postDTO
	 */
	InvokeResult terminatePost(PostDTO postDTO);

	/**
	 * 撤销一批岗位
	 * 
	 * @param postDtos
	 */
	InvokeResult terminatePosts(PostDTO[] postDtos);
}
