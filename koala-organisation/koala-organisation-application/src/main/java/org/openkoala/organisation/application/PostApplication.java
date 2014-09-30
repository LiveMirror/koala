package org.openkoala.organisation.application;

import java.util.Set;

import org.openkoala.organisation.core.domain.Post;

/**
 * 岗位应用接口
 * 
 * @author xmfang
 * 
 */
public interface PostApplication {

	/**
	 * 根据机构id获取该机构下的岗位
	 * 
	 * @param organizationId
	 * @return
	 */
	Set<Post> findPostsByOrganizationId(Long organizationId);
}
