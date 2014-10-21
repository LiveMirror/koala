package org.openkoala.organisation.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.dayatang.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.core.SnIsExistException;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.core.domain.Party;
import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.utils.OrganisationUtils;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Party集成测试
 * 
 * @author xmfang
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public class PartyIntegrationTest extends AbstractIntegrationTest {

	private Company company;
	private Department department;
	private Job job;
	private Post post;
	private Employee employee;
	private Date date = DateUtils.date(2013, 1, 1);
	private Date now = new Date();

	private OrganisationUtils organisationUtils = new OrganisationUtils();

	@Before
	public void subSetup() {
		company = organisationUtils.createCompany("总公司", "JG-XXX1", date);
		department = organisationUtils.createDepartment("财务部", "JG-XXX2", company, date);
		job = organisationUtils.createJob("总经理", "JOB-XXX1", date);
		post = organisationUtils.createPost("会计", "POST-XXX1", job, department, date);
		employee = organisationUtils.createEmployee("张三", "XXXXXXXX", "EMP-XXX", post, date);
	}

	@Test
	public void testFindAll() {
		List<Party> parties = Party.findAll(Party.class, now);
		assertTrue(parties.contains(company));
		assertTrue(parties.contains(department));
		assertTrue(parties.contains(job));
		assertTrue(parties.contains(post));
		assertTrue(parties.contains(employee));
	}

	@Test(expected = SnIsExistException.class)
	public void testSave() {
		Party party = new Company("TestCompany", "JG-XXX1");
		party.save();
	}

	@Test
	public void testIsExistSn() {
		assertTrue(Party.isExistSn(Party.class, "JG-XXX1", now));
	}

	@Test
	public void testIsActive() {
		assertTrue(employee.isActive(now));
		employee.terminate(now);
		assertFalse(employee.isActive(now));
	}

	@Test
	public void testTerminate() {
		assertFalse(post.getEmployees(now).isEmpty());
		employee.terminate(now);
		assertTrue(post.getEmployees(now).isEmpty());
	}

}
