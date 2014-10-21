package org.openkoala.organisation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.dayatang.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.core.domain.Accountability;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.EmployeePostHolding;
import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.core.domain.OrganizationLineManagement;
import org.openkoala.organisation.core.domain.Person;
import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Party集成测试
 * @author xmfang
 *
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class AccountabilityIntegrationTest extends AbstractIntegrationTest {
	
	private Company company;
	private Department department;
	private Post post;
	private Employee employee;
	
	private OrganizationLineManagement orgLineManagement;
	private EmployeePostHolding employeePostHolding;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();
	
	private OrganisationUtils organisationUtils = new OrganisationUtils();
	
	@Before
	public void subSetup() {
		company = new Company("总公司", "JG-XXX1");
		company.setCreateDate(date);
		company.save();
		
		department = new Department("财务部", "JG-XXX2");
		department.setCreateDate(date);
		department.save();
		
		orgLineManagement = new OrganizationLineManagement(company, department, date);
		orgLineManagement.save();
		
		Job job = organisationUtils.createJob("总经理", "JOB-XXX1", date);
		post = organisationUtils.createPost("会计", "POST-XXX1", job, department, date);
		
		Person person = new Person("张三");
		employee = new Employee(person, "EMP-XXX", date);
		employee.save();
		
		employeePostHolding = new EmployeePostHolding(post, employee, false, date);
		employeePostHolding.save();
	}

	@Test
	public void testGetByCommissionerAndResponsible() {
		assertEquals(orgLineManagement, Accountability.getByCommissionerAndResponsible(Accountability.class, company, department, now));
		assertEquals(employeePostHolding, Accountability.getByCommissionerAndResponsible(Accountability.class, post, employee, now));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testFindAccountabilitiesByParty() {
		List<Accountability> accountabilitiesOfCompany = Accountability.findAccountabilitiesByParty(company, now);
		assertEquals(1, accountabilitiesOfCompany.size());
		assertTrue(accountabilitiesOfCompany.contains(orgLineManagement));
		
		List<Accountability> accountabilitiesOfEmployee = Accountability.findAccountabilitiesByParty(employee, now);
		assertEquals(1, accountabilitiesOfEmployee.size());
		assertTrue(accountabilitiesOfEmployee.contains(employeePostHolding));
	}
	
}
