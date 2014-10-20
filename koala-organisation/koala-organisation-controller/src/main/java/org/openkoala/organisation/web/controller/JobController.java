package org.openkoala.organisation.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.JobFacade;
import org.openkoala.organisation.facade.dto.JobDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 职务管理
 * 
 * @author xmfang
 * 
 */
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

	@Inject
	private JobFacade jobFacade;

	/**
	 * 分页查询职务
	 * 
	 * @param page
	 * @param pagesize
	 * @param jobDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page<JobDTO> pagingQuery(int page, int pagesize, JobDTO jobDto) {
		return jobFacade.pagingQueryJobs(jobDto, page, pagesize);
	}

	/**
	 * 查询所有职务
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query-all")
	public List<JobDTO> queryAllJobs() {
		return jobFacade.findAllJobs();
	}

	/**
	 * 创建一个职务
	 * 
	 * @param jobDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public InvokeResult createJob(JobDTO jobDto) {
		return jobFacade.createJob(jobDto);
	}

	/**
	 * 更新职务信息
	 * 
	 * @param jobDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public InvokeResult updateJob(JobDTO jobDto) {
		return jobFacade.updateJobInfo(jobDto);
	}

	/**
	 * 根据ID获得职务
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}")
	public JobDTO get(@PathVariable("id") Long id) {
		return jobFacade.getJobById(id);
	}

	/**
	 * 撤销某个职务
	 * 
	 * @param jobDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public InvokeResult  terminateJob(JobDTO jobDto) {
		return jobFacade.terminateJob(jobDto);
	}

	/**
	 * 同时撤销多个职务
	 * 
	 * @param jobDtos
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate-jobs", method = RequestMethod.POST)
	public InvokeResult terminateJobs(@RequestBody JobDTO[] jobDtos) {
		return jobFacade.terminateJobs(jobDtos);
	}

}
