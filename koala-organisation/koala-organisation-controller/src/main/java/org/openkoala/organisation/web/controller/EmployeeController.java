package org.openkoala.organisation.web.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
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
 * 员工管理
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
	public Page<EmployeeDTO> pagingQuery(int page, int pagesize, EmployeeDTO example) {
		return employeeFacade.pagingQueryEmployees(example, page, pagesize);
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
	public Page<EmployeeDTO> pagingQueryByOrganization(int page, int pagesize, EmployeeDTO example, Long organizationId, boolean queryAllChildren) {
		if (organizationId == 0) {
			return employeeFacade.pagingQueryEmployeesWhoNoPost(example, page, pagesize);
		}
		if (queryAllChildren) {
			return employeeFacade.pagingQueryEmployeesByOrganizationAndChildren(example, organizationId, page, pagesize);
		}
		return employeeFacade.pagingQueryEmployeesByOrganization(example, organizationId, page, pagesize);
	}

    /**
     * 创建一个员工
     * @param employeeDto
     * @param postId
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public InvokeResult createEmployee(EmployeeDTO employeeDto, Long postId) {
		return employeeFacade.createEmployeeWithPost(employeeDto, postId);
	}

    /**
     * 更新某个员工的信息
     * @param employeeDto
     * @return
     */
	@ResponseBody
	@RequestMapping("/update")
	public InvokeResult updateEmployee(EmployeeDTO employeeDto) {
		return employeeFacade.updateEmployeeInfo(employeeDto);
	}

	/**
	 * 调整某个员工的任职信息
	 * 
	 * @param employeeId
	 * @param responsibleJobHoldings
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/transform-post", method = RequestMethod.POST)
	public InvokeResult transformPost(Long employeeId, @RequestBody ResponsiblePostDTO[] responsibleJobHoldings) {
		return employeeFacade.transformPost(employeeId, responsibleJobHoldings);
	}

	/**
	 * 根据ID号获得员工
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}")
	public EmployeeDTO get(@PathVariable("id") Long id) {
		return employeeFacade.getEmployeeById(id);
	}

	/**
	 * 获取性别列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/genders")
	public Map<String, String> getGendens() {
		return employeeFacade.getGenders();
	}

	/**
	 * 获得某个员工的任职信息
	 * 
	 * @param employeeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get-posts-by-employee")
	public List<ResponsiblePostDTO> getPostsByEmployee(Long employeeId) {
		return employeeFacade.getPostsByEmployee(employeeId);
	}

	/**
	 * 解雇某名员工
	 * 
	 * @param employeeDTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate", method = RequestMethod.POST)
	public InvokeResult terminateEmployee(EmployeeDTO employeeDTO) {
		return employeeFacade.terminateEmployee(employeeDTO);
	}

	/**
	 * 同时解雇多名员工
	 * 
	 * @param employeeDtos
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate-employees", method = RequestMethod.POST)
	public InvokeResult terminateEmployees(@RequestBody EmployeeDTO[] employeeDtos) {
		return employeeFacade.terminateEmployees(employeeDtos);
	}

}
