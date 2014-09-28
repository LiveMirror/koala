package org.openkoala.organisation.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.openkoala.organisation.facade.OrganizationFacade;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.InvokeResult;
import org.openkoala.organisation.facade.dto.OrganizationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 机构管理Controller
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
     * @param parentId
     * @param company
     * @return
     */
    @ResponseBody
    @RequestMapping("/create-company")
    public Map<String, Object> createCompany(Long parentId, OrganizationDTO companyDto) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
		companyDto.setOrganizationType(OrganizationDTO.COMPANY);
		InvokeResult invokeResult = organizationFacade.createCompany(parentId, companyDto);
    	dataMap.put("result", invokeResult.getMessage());
    	dataMap.put("id", invokeResult.getData());
    	return dataMap;
    }
    
    /**
     * 在某个机构下创建一个部门
     * @param parentId
     * @param parentType
     * @param department
     * @return
     */
    @ResponseBody
    @RequestMapping("/create-department")
    public Map<String, Object> createDepartment(Long parentId, OrganizationDTO departmentDto) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	departmentDto.setOrganizationType(OrganizationDTO.DEPARTMENT);
		InvokeResult invokeResult = organizationFacade.createDepartment(parentId, departmentDto);
    	dataMap.put("result", invokeResult.getMessage());
    	dataMap.put("id", invokeResult.getData());
    	return dataMap;
    }
    
    /**
     * 更新公司信息
     * @param company
     * @return
     */
    @ResponseBody
    @RequestMapping("/update-company")
    public Map<String, Object> updateCompany(OrganizationDTO companyDto) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
		companyDto.setOrganizationType(OrganizationDTO.COMPANY);
    	dataMap.put("result", organizationFacade.updateOrganization(companyDto).getMessage());
    	return dataMap;
    }
    
    /**
     * 更新部门信息
     * @param department
     * @return
     */
    @ResponseBody
    @RequestMapping("/update-department")
    public Map<String, Object> updateDepartment(OrganizationDTO departmentDto) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
		departmentDto.setOrganizationType(OrganizationDTO.DEPARTMENT);
    	dataMap.put("result", organizationFacade.updateOrganization(departmentDto).getMessage());
    	return dataMap;
    }
   
    /**
     * 获取组织机构树
     * @return
     */
    @ResponseBody
    @RequestMapping("/orgTree")
    public Map<String, Object> getOrgTree() {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	dataMap.put("orgTree", organizationFacade.getOrganizationTree());
    	return dataMap;
    }
    
    /**
     * 根据ID获得机构
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOrg")
    public Map<String, Object> getOrganization(long id) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	OrganizationDTO organizationDTO = organizationFacade.getOrganizationById(id);
    	dataMap.put("org", organizationDTO);
    	return dataMap;
    }
	
	/**
	 * 撤销某个机构与某些员工的责任关系
	 * @param employeeDtos
	 * @param organizationId
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/terminate_eoRelations", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminateEmployeeOrganizationRelation(@RequestBody EmployeeDTO[] employeeDtos, Long organizationId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		organizationFacade.terminateEmployeeOrganizationRelation(organizationId, employeeDtos);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 撤销一个公司
	 * @param company
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/terminate-company")
	public Map<String, Object> terminateCompany(OrganizationDTO companyDto) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		companyDto.setOrganizationType(OrganizationDTO.COMPANY);
		dataMap.put("result", organizationFacade.terminateOrganization(companyDto));
		return dataMap;
	}

	/**
	 * 撤销一个部门
	 * @param department
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/terminate-department")
	public Map<String, Object> terminateDepartment(OrganizationDTO departmentDto) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		departmentDto.setOrganizationType(OrganizationDTO.DEPARTMENT);
		dataMap.put("result", organizationFacade.terminateOrganization(departmentDto));
		return dataMap;
	}

}
