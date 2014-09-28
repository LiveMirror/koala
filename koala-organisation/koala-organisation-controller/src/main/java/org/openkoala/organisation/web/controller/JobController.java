package org.openkoala.organisation.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.organisation.facade.JobFacade;
import org.openkoala.organisation.facade.dto.JobDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 职务管理Controller
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
	 * @param job
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page pagingQuery(int page, int pagesize, JobDTO jobDto) {
		return jobFacade.pagingQueryJobs(jobDto, page, pagesize);
	}

	/**
	 * 查询所有职务
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query-all")
	public Map<String, Object> queryAllJobs() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", jobFacade.findAllJobs());
		return dataMap;
	}

	/**
	 * 创建一个职务
	 * 
	 * @param job
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/create")
	public Map<String, Object> createJob(JobDTO jobDto) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", jobFacade.createJob(jobDto).getMessage());
		return dataMap;
	}

	/**
	 * 更新职务信息
	 * 
	 * @param job
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> updateJob(JobDTO jobDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", jobFacade.updateJobInfo(jobDTO).getMessage());
		return dataMap;
	}

	/**
	 * 根据ID获得职务
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable("id") Long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", jobFacade.getJobById(id));
		return dataMap;
	}

	/**
	 * 撤销某个职务
	 * 
	 * @param jobDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminateJob(JobDTO jobDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", jobFacade.terminateJob(jobDTO).getMessage());
		return dataMap;
	}

	/**
	 * 同时撤销多个职务
	 * 
	 * @param jobs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminateJobs", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminateJobs(@RequestBody JobDTO[] jobDtos) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", jobFacade.terminateJobs(jobDtos).getMessage());
		return dataMap;
	}

}
