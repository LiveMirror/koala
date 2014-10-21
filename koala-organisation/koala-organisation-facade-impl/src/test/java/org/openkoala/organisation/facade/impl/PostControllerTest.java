package org.openkoala.organisation.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.core.NameExistException;
import org.openkoala.organisation.core.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.core.SnIsExistException;
import org.openkoala.organisation.core.TerminateHasEmployeePostException;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.facade.dto.PostDTO;
import org.openkoala.organisation.facade.impl.assembler.PostAssembler;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * PostController单元测试
 * @author xmfang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PostAssembler.class})
public class PostControllerTest {
	
	@Mock
	private PostApplication postApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private PostFacadeImpl postFacadeImpl = new PostFacadeImpl();
	
	private Long organizationId = 1L;
	private Organization organization = new Company("总公司", "COM-XXX");
	private Post post = new Post("CEO", "XXX");
	private PostDTO postDTO = new PostDTO();

	@Before
	public void setup() {
		postDTO.setName("CEO");
		postDTO.setSn("XXX");
	}
	
	@Test
	public void testCreatePost() {
		staticMockAssemPost();
		postFacadeImpl.createPost(postDTO);
		verify(baseApplication, only()).saveParty(post);
	}
	
	private void staticMockAssemPost() {
		PowerMockito.mockStatic(PostAssembler.class);
		when(PostAssembler.toEntity(postDTO)).thenReturn(post);
	}

	@Test
	public void testCatchOrganizationHasPrincipalYetExceptionWhenCreatePost() {
		staticMockAssemPost();
		doThrow(new OrganizationHasPrincipalYetException()).when(baseApplication).saveParty(post);
		assertEquals("该机构已经有负责岗位！", postFacadeImpl.createPost(postDTO).getErrorMessage());
	}

	@Test
	public void testCatchSnIsExistExceptionWhenCreatePost() {
		staticMockAssemPost();
		doThrow(new SnIsExistException()).when(baseApplication).saveParty(post);
		assertEquals("岗位编码: " + post.getSn() + " 已被使用！", postFacadeImpl.createPost(postDTO).getErrorMessage());
	}

	@Test
	public void testCatchExceptionWhenCreatePost() {
		staticMockAssemPost();
		doThrow(new RuntimeException()).when(baseApplication).saveParty(post);
		assertEquals("保存失败！", postFacadeImpl.createPost(postDTO).getErrorMessage());
	}

	@Test
	public void testUpdatePost() {
		staticMockAssemPost();
		postFacadeImpl.updatePostInfo(postDTO);
		verify(baseApplication, only()).updateParty(post);
	}

	@Test
	public void testCatchSnIsExistExceptionWhenUpdatePost() {
		staticMockAssemPost();
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(post);
        assertEquals("岗位编码: " + post.getSn() + " 已被使用！", postFacadeImpl.updatePostInfo(postDTO).getErrorMessage());
	}

    @Test
    public void testCatchNameExistExceptionWhenUpdatePost() {
		staticMockAssemPost();
        doThrow(new NameExistException()).when(baseApplication).updateParty(post);
        assertEquals("岗位名称: " + post.getName() + " 已经存在！", postFacadeImpl.updatePostInfo(postDTO).getErrorMessage());
    }

	@Test
	public void testCatchExceptionWhenUpdatePost() {
		staticMockAssemPost();
		doThrow(new RuntimeException()).when(baseApplication).updateParty(post);
		assertEquals("修改失败！", postFacadeImpl.updatePostInfo(postDTO).getErrorMessage());
	}
	
	@Test
	public void testFindPostsByOrganizationId() {
		Set<Post> posts = new HashSet<Post>();
		posts.add(post);

		Set<PostDTO> postDtos = new HashSet<PostDTO>();
		postDtos.add(postDTO);

		PowerMockito.mockStatic(PostAssembler.class);
		when(PostAssembler.toDTO(post)).thenReturn(postDTO);
		
		when(postApplication.findPostsByOrganizationId(organizationId)).thenReturn(posts);
		assertEquals(postDtos, postFacadeImpl.findPostsByOrganizationId(organizationId));
	}
	
	@Test
	public void testGetPostById() {
		Long postId = 1L;
		
		PowerMockito.mockStatic(PostAssembler.class);
		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		when(PostAssembler.toDTO(post)).thenReturn(postDTO);
		
		assertEquals(postDTO, postFacadeImpl.getPostById(postId));
	}

	@Test
	public void testTerminatePost() {
		staticMockAssemPost();
		postFacadeImpl.terminatePost(postDTO);
		verify(baseApplication, only()).terminateParty(post);
	}
	
	@Test
	public void testCatchTerminateHasEmployeePostExceptionWhenTerminatePost() {
		staticMockAssemPost();
		doThrow(new TerminateHasEmployeePostException()).when(baseApplication).terminateParty(post);
		assertEquals("还有员工在此岗位上任职，不能撤销！", postFacadeImpl.terminatePost(postDTO).getErrorMessage());
	}
	
	@Test
	public void testCatchExceptionWhenTerminatePost() {
		staticMockAssemPost();
		doThrow(new RuntimeException()).when(baseApplication).terminateParty(post);
		assertEquals("撤销员工岗位失败！", postFacadeImpl.terminatePost(postDTO).getErrorMessage());
	}
	
	@Test
	public void testTerminatePosts() {
		PostDTO[] postDtos = new PostDTO[1];
		postDtos[0] = postDTO;
		
		Set<Post> posts = new HashSet<Post>();
		posts.add(post);
		
		staticMockAssemPost();
		postFacadeImpl.terminatePosts(postDtos);
		verify(baseApplication, only()).terminateParties(posts);
	}
	
	@Test
	public void testCatchTerminateHasEmployeePostExceptionWhenTerminatePosts() {
		PostDTO[] postDtos = new PostDTO[1];
		postDtos[0] = postDTO;
		
		Set<Post> posts = new HashSet<Post>();
		posts.add(post);
		
		staticMockAssemPost();
		doThrow(new TerminateHasEmployeePostException()).when(baseApplication).terminateParties(posts);
		assertEquals("还有员工在此岗位上任职，不能撤销！", postFacadeImpl.terminatePosts(postDtos).getErrorMessage());
	}
	
	@Test
	public void testCatchExceptionWhenTerminatePosts() {
		PostDTO[] postDtos = new PostDTO[1];
		postDtos[0] = postDTO;
		
		Set<Post> posts = new HashSet<Post>();
		posts.add(post);
		
		staticMockAssemPost();
		doThrow(new RuntimeException()).when(baseApplication).terminateParties(posts);
		assertEquals("撤销员工岗位失败！", postFacadeImpl.terminatePosts(postDtos).getErrorMessage());
	}

}
