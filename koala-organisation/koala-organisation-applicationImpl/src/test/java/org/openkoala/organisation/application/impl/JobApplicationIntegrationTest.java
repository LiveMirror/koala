package org.openkoala.organisation.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dayatang.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.application.JobApplication;
import org.openkoala.organisation.application.impl.utils.OrganisationUtils;
import org.openkoala.organisation.domain.Job;

/**
 * 职务应用实现集成测试
 * @author xmfang
 *
 */
public class JobApplicationIntegrationTest extends AbstractIntegrationTest {
	
	private Job job1;
	private Job job2;
	
	@Inject
	private JobApplication jobApplication;
	
	@Before
	public void subSetup() {
		OrganisationUtils organisationUtils = new OrganisationUtils();
		Date date = DateUtils.date(2013, 1, 1);
		job1 = organisationUtils.createJob("总经理", "JOB-XXX1", date);
		job2 = organisationUtils.createJob("副总经理", "JOB-XXX2", date);
	}

	@Test
	public void testPagingQueryJobs() {
		List<Job> jobs1 = jobApplication.pagingQueryJobs(new Job(), 0, 1).getData();
		assertTrue(jobs1.size() >= 1);
		
		List<Job> jobs2 = jobApplication.pagingQueryJobs(new Job(), 0, 10).getData();
		assertTrue(jobs2.size() >= 2);
	}
	
}
