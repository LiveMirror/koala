package org.openkoala.organisation.application.impl;

import java.util.Date;

import javax.inject.Inject;

import org.dayatang.utils.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.openkoala.organisation.application.PostApplication;
import org.openkoala.organisation.application.impl.utils.OrganisationUtils;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.core.domain.Post;

/**
 * 岗位应用接口实现集成测试
 * 
 * @author xmfang
 * 
 */
@SuppressWarnings("unused")
@Ignore
public class PostApplicationImplIntegrationTest extends AbstractIntegrationTest {

	private Company company1;
	private Company company2;
	private Department department;
	private Post post1;
	private Post post2;
	private Date date = DateUtils.date(2013, 1, 1);

	private OrganisationUtils organisationUtils = new OrganisationUtils();

	@Inject
	private PostApplication postApplication;

	@Before
	public void subSetup() {
		company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);

		Job job1 = organisationUtils.createJob("会计", "JOB-XXX1", date);
		Job job2 = organisationUtils.createJob("分公司总经理", "JOB-XXX2", date);
		post1 = organisationUtils.createPost("会计", "POST-XXX1", job1, department, date);
		post2 = organisationUtils.createPost("广州分公司总经理", "POST-XXX2", job2, company2, date);
	}

	// @Test
	// public void testPagingQueryPosts() {
	// List<PostDTO> postDTOs = postApplication.pagingQueryPosts(new PostDTO(),
	// 0, 1).getData();
	// assertTrue(postDTOs.size() >= 1);
	//
	// List<PostDTO> postDTOs2 = postApplication.pagingQueryPosts(new PostDTO(),
	// 0, 10).getData();
	// assertTrue(postDTOs2.size() >= 2);
	// }
	//
	// @Test
	// public void testPagingQueryPostsOfOrganizatoin() {
	// Job job3 = organisationUtils.createJob("出纳", "JOB-XXX3", date);
	// Post post3 = organisationUtils.createPost("出纳", "POST-XXX3", job3,
	// department, date);
	//
	// List<PostDTO> postDTOs =
	// postApplication.pagingQueryPostsOfOrganizatoin(department, new PostDTO(),
	// 0, 1).getData();
	// assertEquals(1, postDTOs.size());
	//
	// List<PostDTO> postDTOs2 =
	// postApplication.pagingQueryPostsOfOrganizatoin(department, new PostDTO(),
	// 0, 10).getData();
	// assertEquals(2, postDTOs2.size());
	// assertTrue(postDTOs2.contains(PostDTO.generateDtoBy(post1)));
	// assertTrue(postDTOs2.contains(PostDTO.generateDtoBy(post3)));
	// }
	//
}
