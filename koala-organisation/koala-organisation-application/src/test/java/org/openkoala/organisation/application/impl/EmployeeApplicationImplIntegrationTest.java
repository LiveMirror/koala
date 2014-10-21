package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.dayatang.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.application.EmployeeApplication;
import org.openkoala.organisation.application.impl.utils.OrganisationUtils;
import org.openkoala.organisation.core.EmployeeMustHaveAtLeastOnePostException;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.EmployeePostHolding;
import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.core.domain.Person;
import org.openkoala.organisation.core.domain.Post;

/**
 * 员工应用实现集成测试
 * @author xmfang
 *
 */
public class EmployeeApplicationImplIntegrationTest extends AbstractIntegrationTest {
	
	private Company company1;
	private Company company2;
	private Department department;
	private Post post1;
	private Post post2;
	private Employee employee1;
	private Employee employee2;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Inject
	private EmployeeApplication employeeApplication;
	
	@Before
	public void subSetup() {
		company1 = organisationUtils.createCompany("总公司", "JG-XXX", date);
		company2 = organisationUtils.createCompany("华南分公司", "JG-XXX2", company1, date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX3", company2, date);
		
		Job job1 = organisationUtils.createJob("会计", "JOB-XXX1", date);
		Job job2 = organisationUtils.createJob("分公司副总经理", "JOB-XXX2", date);
		post1 = organisationUtils.createPost("会计", "POST-XXX1", job1, department, date);
		post2 = organisationUtils.createPost("广州分公司副总经理", "POST-XXX2", job2, company2, date);
		employee1 = organisationUtils.createEmployee("张三", "XXXXXXXX1", "EMP-XXX1", post2, date);
		new EmployeePostHolding(post1, employee1, false, date).save();
		employee2 = organisationUtils.createEmployee("李四", "XXXXXXXX2", "EMP-XXX2", post1, date);
	}

	@Test
	public void testGetOrganizationOfEmployee() {
		assertEquals(company2, employeeApplication.getOrganizationOfEmployee(employee1, now));
	}
	
//	@Test
//	public void testPagingQueryEmployees() {
//		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployees(new EmployeeDTO(), 0, 1).getData();
//		assertEquals(1, employeeDTOs.size());
//		
//		List<EmployeeDTO> employeeDTOs2 = employeeApplication.pagingQueryEmployees(new EmployeeDTO(), 0, 10).getData();
//		assertTrue(employeeDTOs2.size() <= 10);
//	}
	
	@Test
	public void testCreateEmployeeWithPost() {
		Person person = new Person("王五");
		Employee employee3 = new Employee(person, "EMP-XXX3", date);
		employeeApplication.createEmployeeWithPost(employee3, post1);
		
		Set<Post> allPosts = employee3.getPosts(now);
		assertEquals(1, allPosts.size());
		assertTrue(allPosts.contains(post1));
	}
	
//	@Test
//	public void testPagingQueryEmployeesByOrganization() {
//		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployeesByOrganization(new EmployeeDTO(), department, 0, 1).getData();
//		assertEquals(1, employeeDTOs.size());
//		
//		List<EmployeeDTO> employeeDTOs2 = employeeApplication.pagingQueryEmployeesByOrganization(new EmployeeDTO(), department, 0, 10).getData();
//		assertEquals(2, employeeDTOs2.size());
//		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee1)));
//		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee2)));
//	}
//	
//	@Test
//	public void testPagingQueryEmployeesByOrganizationAndChildren() {
//		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployeesByOrganizationAndChildren(new EmployeeDTO(), company1, 0, 1).getData();
//		assertEquals(1, employeeDTOs.size());
//		
//		List<EmployeeDTO> employeeDTOs2 = employeeApplication.pagingQueryEmployeesByOrganizationAndChildren(new EmployeeDTO(), company1, 0, 10).getData();
//		assertEquals(2, employeeDTOs2.size());
//		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee1)));
//		assertTrue(employeeDTOs2.contains(EmployeeDTO.generateDtoBy(employee2)));
//	}
	
//	@Test
//	public void testPagingQueryEmployeesWhoNoPost() {
//		organisationUtils.createEmployee("王五", "XXXXXX3", "EMP-XXX3", date);
//		organisationUtils.createEmployee("朱八", "XXXXXX4", "EMP-XXX4", date);
//		
//		List<EmployeeDTO> employeeDTOs = employeeApplication.pagingQueryEmployeesWhoNoPost(new EmployeeDTO(), 0, 10).getData();
//		assertTrue(employeeDTOs.size() >= 2);
//		assertTrue(employeeDTOs.size() <= 10);
//	}
	
	@Test
	public void testTransformPost() {
		Map<Post, Boolean> postsForTransform = new HashMap<Post, Boolean>();
		postsForTransform.put(post1, false);
		postsForTransform.put(post2, true);
		
		employeeApplication.transformPost(employee2, postsForTransform);
		
		Set<Post> posts = employee2.getPosts(now);
		assertEquals(2, posts.size());
		assertTrue(posts.contains(post1));
		assertTrue(posts.contains(post2));
	}
	
	@Test(expected = EmployeeMustHaveAtLeastOnePostException.class)
	public void testEmployeeMustHaveAtLeastOnePostExceptionWhenTransformPost() {
		employeeApplication.transformPost(employee2, new HashMap<Post, Boolean>());
	}
	
	@Test
	public void testGetPostsByEmployee() {
		Map<Post, Boolean> posts = employeeApplication.getPostsByEmployee(employee1);
		assertEquals(2, posts.size());
		assertTrue(posts.containsKey(post1));
		assertFalse(posts.get(post1));
		assertTrue(posts.containsKey(post2));
		assertTrue(posts.get(post2));
	}

}
