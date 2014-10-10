package org.openkoala.organisation.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.OrganizationFacade;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.OrganizationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 机构管理
 * 
 * @author xmfang
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

	/**
	 * 组织机构应用接口
	 */
	@Inject
	private OrganizationFacade organizationFacade;

	/**
	 * 在某个公司下创建一个分公司
	 * 
	 * @param parentId
	 * @param companyDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create-company", method = RequestMethod.POST)
	public InvokeResult createCompany(Long parentId, OrganizationDTO companyDto) {
		companyDto.setOrganizationType(OrganizationDTO.COMPANY);
		return organizationFacade.createCompany(parentId, companyDto);
	}

	/**
	 * 在某个机构下创建一个部门
	 * 
	 * @param parentId
	 * @param departmentDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create-department", method = RequestMethod.POST)
	public InvokeResult createDepartment(Long parentId, OrganizationDTO departmentDto) {
		departmentDto.setOrganizationType(OrganizationDTO.DEPARTMENT);
		return organizationFacade.createDepartment(parentId, departmentDto);
	}

	/**
	 * 更新公司信息
	 * 
	 * @param companyDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update-company", method = RequestMethod.POST)
	public InvokeResult updateCompany(OrganizationDTO companyDto) {
		companyDto.setOrganizationType(OrganizationDTO.COMPANY);
		return organizationFacade.updateOrganization(companyDto);
	}

	/**
	 * 更新部门信息
	 * 
	 * @param departmentDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update-department", method = RequestMethod.POST)
	public InvokeResult updateDepartment(OrganizationDTO departmentDto) {
		departmentDto.setOrganizationType(OrganizationDTO.DEPARTMENT);
		return organizationFacade.updateOrganization(departmentDto);
	}

	/**
	 * 获取组织机构树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/org-tree")
	public Map<String, Object> getOrgTree() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("orgTree", organizationFacade.getOrganizationTree());
		return dataMap;
	}

	/**
	 * 根据ID获得机构
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get")
	public OrganizationDTO getOrganization(Long id) {
		return organizationFacade.getOrganizationById(id);
	}

	/**
	 * 撤销某个机构与某些员工的责任关系
	 * 
	 * @param employeeDtos
	 * @param organizationId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate-eo-relations", method = RequestMethod.POST)
	public InvokeResult terminateEmployeeOrganizationRelation(@RequestBody EmployeeDTO[] employeeDtos, Long organizationId) {
		return organizationFacade.terminateEmployeeOrganizationRelation(organizationId, employeeDtos);
	}

	/**
	 * 撤销一个公司
	 * 
	 * @param companyDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate-company", method = RequestMethod.POST)
	public InvokeResult terminateCompany(OrganizationDTO companyDto) {
		companyDto.setOrganizationType(OrganizationDTO.COMPANY);
		return organizationFacade.terminateOrganization(companyDto);
	}

	/**
	 * 撤销一个部门
	 * 
	 * @param departmentDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/terminate-department", method = RequestMethod.POST)
	public InvokeResult terminateDepartment(OrganizationDTO departmentDto) {
		departmentDto.setOrganizationType(OrganizationDTO.DEPARTMENT);
		return organizationFacade.terminateOrganization(departmentDto);
	}

}
