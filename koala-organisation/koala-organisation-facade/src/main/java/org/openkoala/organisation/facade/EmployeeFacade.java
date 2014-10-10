package org.openkoala.organisation.facade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dayatang.utils.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.OrganizationDTO;
import org.openkoala.organisation.facade.dto.ResponsiblePostDTO;

/**
 * 员工门面层接口
 * 
 */
public interface EmployeeFacade {

	/**
	 * 根据员工id获取某个员工的所属机构
	 * 
	 * @param employeeId
	 * @param date
	 * @return
	 */
	OrganizationDTO getOrganizationOfEmployee(Long employeeId, Date date);

	/**
	 * 分页查询员工
	 * 
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployees(EmployeeDTO example, int currentPage, int pagesize);

	/**
	 * 分页查询某个机构的直属员工
	 * 
	 * @param example
	 * @param orgId
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployeesByOrganization(EmployeeDTO example, Long orgId, int currentPage, int pagesize);

	/**
	 * 分页查询某个机构及其下属机构的员工
	 * 
	 * @param example
	 * @param orgId
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployeesByOrganizationAndChildren(EmployeeDTO example, Long orgId, int currentPage, int pagesize);

	/**
	 * 分页查询没有所属机构的员工
	 * 
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<EmployeeDTO> pagingQueryEmployeesWhoNoPost(EmployeeDTO example, int currentPage, int pagesize);

	/**
	 * 创建员工的任职责任信息
	 * 
	 * @param employee
	 * @param postId
	 */
	InvokeResult createEmployeeWithPost(EmployeeDTO employee, Long postId);

	/**
	 * 调整某个员工的任职信息
	 * 
	 * @param employeeId
	 * @param dtos
	 */
	InvokeResult transformPost(Long employeeId, ResponsiblePostDTO[] dtos);

	/**
	 * 获得某个员工的任职职务信息
	 * 
	 * @param employeeId
	 * @return
	 */
	List<ResponsiblePostDTO> getPostsByEmployee(Long employeeId);

	/**
	 * 根据id获得员工信息
	 * 
	 * @param id
	 * @return
	 */
	EmployeeDTO getEmployeeById(Long id);

	/**
	 * 更新员工信息
	 * 
	 * @param employeeDTO
	 */
	InvokeResult updateEmployeeInfo(EmployeeDTO employeeDTO);

	/**
	 * 解雇某名员工
	 * 
	 * @param employeeDTO
	 */
	InvokeResult terminateEmployee(EmployeeDTO employeeDTO);

	/**
	 * 解雇一批员工
	 * 
	 * @param employeeDtos
	 */
	InvokeResult terminateEmployees(EmployeeDTO[] employeeDtos);

	/**
	 * 获取系统的性别选项集
	 * 
	 * @return
	 */
	Map<String, String> getGenders();
}
