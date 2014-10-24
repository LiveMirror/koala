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
import org.dayatang.querychannel.QueryChannelService;
import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.core.NameExistException;
import org.openkoala.organisation.core.SnIsExistException;
import org.openkoala.organisation.core.TheJobHasPostAccountabilityException;
import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.facade.JobFacade;
import org.openkoala.organisation.facade.dto.JobDTO;
import org.openkoala.organisation.facade.impl.assembler.JobAssembler;

@Named
public class JobFacadeImpl implements JobFacade {

	@Inject
	private BaseApplication baseApplication;
	
	private QueryChannelService queryChannel;

	private QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_org");
		}
		return queryChannel;
	}

	@SuppressWarnings("unchecked")
	
	public Page<JobDTO> pagingQueryJobs(JobDTO jobSearchExample, int currentPage, int pageSize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select NEW org.openkoala.organisation.facade.dto.JobDTO"
				+ "(_job.id, _job.name, _job.createDate, _job.terminateDate, _job.sn, _job.version, _job.description)"
				+ " from Job _job where _job.createDate <= ? and _job.terminateDate > ?");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		if (!StringUtils.isBlank(jobSearchExample.getName())) {
			jpql.append(" and _job.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getName()));
		}
		if (!StringUtils.isBlank(jobSearchExample.getSn())) {
			jpql.append(" and _job.sn like ?");
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getSn()));
		}
		if (!StringUtils.isBlank(jobSearchExample.getDescription())) {
			jpql.append(" and _job.description like ?");
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getDescription()));
		}

		return getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pageSize).pagedList();
	}

	
	public List<JobDTO> findAllJobs() {
		List<JobDTO> results = new ArrayList<JobDTO>();
		for (Job job : baseApplication.findAll(Job.class)) {
			results.add(JobAssembler.toDTO(job));
		}
		return results;
	}

	
	public InvokeResult createJob(JobDTO jobDto) {
		try {
			baseApplication.saveParty(JobAssembler.toEntity(jobDto));
			return InvokeResult.success();
		} catch (SnIsExistException exception) {
			return InvokeResult.failure("职务编码: " + jobDto.getSn() + " 已被使用！");
		} catch (NameExistException exception) {
			return InvokeResult.failure("职务名称: " + jobDto.getName() + " 已经存在！");
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("保存失败！");
		}
	}

	
	public InvokeResult updateJobInfo(JobDTO jobDTO) {
		try {
			baseApplication.updateParty(JobAssembler.toEntity(jobDTO));
			return InvokeResult.success();
		} catch (SnIsExistException exception) {
			return InvokeResult.failure("职务编码: " + jobDTO.getSn() + " 已被使用！");
		} catch (NameExistException exception) {
			return InvokeResult.failure("职务名称: " + jobDTO.getName() + " 已经存在！");
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("修改失败！");
		}
	}

	
	public JobDTO getJobById(Long id) {
		return JobAssembler.toDTO(baseApplication.getEntity(Job.class, id));
	}

	
	public InvokeResult terminateJob(JobDTO jobDTO) {
		try {
			baseApplication.terminateParty(JobAssembler.toEntity(jobDTO));
			return InvokeResult.success();
		} catch (TheJobHasPostAccountabilityException e) {
			return InvokeResult.failure("该职务已被使用!");
		}
	}

	
	public InvokeResult terminateJobs(JobDTO[] jobDtos) {
		Set<Job> jobs = new HashSet<Job>();
		for (JobDTO jobDTO : jobDtos) {
			jobs.add(JobAssembler.toEntity(jobDTO));
		}
		try {
			baseApplication.terminateParties(jobs);
			return InvokeResult.success();
		} catch (TheJobHasPostAccountabilityException e) {
			return InvokeResult.failure("该职务已被使用!");
		}
	}

}
