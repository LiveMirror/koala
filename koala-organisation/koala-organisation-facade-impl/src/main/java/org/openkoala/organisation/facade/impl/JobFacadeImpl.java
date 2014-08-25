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
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.organisation.NameExistException;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.TheJobHasPostAccountabilityException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.domain.Job;
import org.openkoala.organisation.facade.JobFacade;
import org.openkoala.organisation.facade.dto.InvokeResult;
import org.openkoala.organisation.facade.dto.JobDTO;
import org.openkoala.organisation.facade.impl.assembler.JobDtoAssembler;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional(value = "transactionManager_org")
//@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
//@Stateless(name = "JobApplication")
//@Remote
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

	@Override
	public Page<JobDTO> pagingQueryJobs(JobDTO jobSearchExample, int currentPage, int pageSize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _job from Job _job where _job.createDate <= ? and _job.terminateDate > ?");
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
			conditionVals.add(MessageFormat.format("%{0}%", jobSearchExample.getSn()));
		}

		return getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pageSize).pagedList();
	}

	@Override
	public List<JobDTO> findAllJobs() {
		List<JobDTO> results = new ArrayList<JobDTO>();
		for (Job job : baseApplication.findAll(Job.class)) {
			results.add(JobDtoAssembler.assemDto(job));
		}
		return results;
	}

	@Override
	public InvokeResult createJob(JobDTO jobDto) {
		try {
			baseApplication.saveParty(JobDtoAssembler.assemEntity(jobDto));
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

	@Override
	public InvokeResult updateJobInfo(JobDTO jobDTO) {
		try {
			baseApplication.updateParty(JobDtoAssembler.assemEntity(jobDTO));
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

	@Override
	public JobDTO getJobById(Long id) {
		return JobDtoAssembler.assemDto(baseApplication.getEntity(Job.class, id));
	}

	@Override
	public InvokeResult terminateJob(JobDTO jobDTO) {
		try {
			baseApplication.terminateParty(JobDtoAssembler.assemEntity(jobDTO));
			return InvokeResult.success();
		} catch (TheJobHasPostAccountabilityException e) {
			return InvokeResult.failure("职务：" + jobDTO.getName() + "已经被相关关联岗位，不能被撤销！");
		}
	}

	@Override
	public InvokeResult terminateJobs(JobDTO[] jobDtos) {
		Set<Job> jobs = new HashSet<Job>();
		for (JobDTO jobDTO : jobDtos) {
			jobs.add(JobDtoAssembler.assemEntity(jobDTO));
		}
		try {
			baseApplication.terminateParties(jobs);
			return InvokeResult.success();
		} catch (TheJobHasPostAccountabilityException e) {
			return InvokeResult.failure("该职务已经被相关关联岗位，不能被撤销！");
		}
	}

}
