package org.openkoala.organisation.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.organisation.facade.EmployeeFacade;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.ResponsiblePostDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 员工管理controller
 * 
 * @author xmfang
 * 
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController extends BaseController {

	@Inject
	private EmployeeFacade employeeFacade;

	/**
	 * 分页查询员工
	 * 
	 * @param page
	 * @param pagesize
	 * @param example
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery")
	public Page pagingQuery(int page, int pagesize, EmployeeDTO example) {
		Page<EmployeeDTO> employees = employeeFacade.pagingQueryEmployees(example, page, pagesize);
		return employees;
	}

	/**
	 * 分页查询某个机构下的员工
	 * 
	 * @param page
	 * @param pagesize
	 * @param example
	 * @param organizationId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pagingquery-by-org")
	public Page pagingQueryByOrganization(int page, int pagesize, EmployeeDTO example, Long organizationId, boolean queryAllChildren) {
		Page<EmployeeDTO> employees = null;
		if (organizationId == 0) {
			employees = employeeFacade.pagingQueryEmployeesWhoNoPost(example, page, pagesize);
		} else {
			if (queryAllChildren) {
				employees = employeeFacade.pagingQueryEmployeesByOrganizationAndChildren(example, organizationId, page, pagesize);
			} else {
				employees = employeeFacade.pagingQueryEmployeesByOrganization(example, organizationId, page, pagesize);
			}
		}

		return employees;
	}

	/**
	 * 创建一个员工
	 * 
	 * @param employee
	 * @param jobId
	 * @param organizationId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/create")
	public Map<String, Object> createEmployee(EmployeeDTO employeeDto, Long postId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", employeeFacade.createEmployeeWithPost(employeeDto, postId).getMessage());
		return dataMap;
	}

	/**
	 * 更新某个员工的信息
	 * 
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> updateEmployee(EmployeeDTO employeeDto) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", employeeFacade.updateEmployeeInfo(employeeDto).getMessage());
		return dataMap;
	}

	/**
	 * 调整某个员工的任职信息
	 * 
	 * @param employeeId
	 * @param responsibleJobHoldings
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/transform-post", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> transformPost(Long employeeId, @RequestBody ResponsiblePostDTO[] responsibleJobHoldings) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", 
				employeeFacade.transformPost(employeeId, responsibleJobHoldings).getMessage());
		return dataMap;
	}

	/**
	 * 根据ID号获得员工
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable("id") Long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			dataMap.put("data", employeeFacade.getEmployeeById(id));
		} catch (Exception e) {
			dataMap.put("error", "查询指定职务失败！");
			e.printStackTrace();
		}
		return dataMap;
	}

	/**
	 * 获取性别列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/genders")
	public Map<String, Object> getGendens() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", employeeFacade.getGenders());
		return dataMap;
	}

	/**
	 * 获得某个员工的任职信息
	 * 
	 * @param employeeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get-posts-by-employee")
	public Map<String, Object> getPostsByEmployee(Long employeeId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", employeeFacade.getPostsByEmployee(employeeId));
		return dataMap;
	}

	/**
	 * 解雇某名员工
	 * 
	 * @param employeeDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public Map<String, Object> terminateEmployee(EmployeeDTO employeeDTO) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		employeeFacade.terminateEmployee(employeeDTO);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 同时解雇多名员工
	 * 
	 * @param employeeDtos
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate-employees", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminateEmployees(@RequestBody EmployeeDTO[] employeeDtos) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		employeeFacade.terminateEmployees(employeeDtos);
		dataMap.put("result", "success");
		return dataMap;
	}

}
