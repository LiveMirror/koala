package org.openkoala.organisation.facade;

import java.util.List;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.dto.JobDTO;

/**
 * 职务门面层接口
 * 
 * @author xmfang
 * 
 */
public interface JobFacade {

	/**
	 * 分页查询职务信息
	 * 
	 * @param jobSearchExample
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<JobDTO> pagingQueryJobs(JobDTO jobSearchExample, int currentPage, int pageSize);

	/**
	 * 查询所有的职务
	 * 
	 * @return
	 */
	List<JobDTO> findAllJobs();

	/**
	 * 创建一个职务
	 * 
	 * @param jobDto
	 */
	InvokeResult createJob(JobDTO jobDto);

	/**
	 * 更新职务信息
	 * 
	 * @param jobDTO
	 */
	InvokeResult updateJobInfo(JobDTO jobDTO);

	/**
	 * 根据id查找一个职务
	 * 
	 * @param id
	 * @return
	 */
	JobDTO getJobById(Long id);

	/**
	 * 撤销一个职务
	 * 
	 * @param jobDTO
	 */
	InvokeResult terminateJob(JobDTO jobDTO);

	/**
	 * 撤销一批职务
	 * 
	 * @param jobDtos
	 */
	InvokeResult terminateJobs(JobDTO[] jobDtos);
}
