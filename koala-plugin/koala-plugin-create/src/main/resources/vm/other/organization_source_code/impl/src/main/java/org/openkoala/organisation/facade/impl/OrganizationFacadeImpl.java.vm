package org.openkoala.organisation.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.core.NameExistException;
import org.openkoala.organisation.core.SnIsExistException;
import org.openkoala.organisation.core.TerminateNotEmptyOrganizationException;
import org.openkoala.organisation.core.TerminateRootOrganizationException;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.facade.OrganizationFacade;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.OrganizationDTO;
import org.openkoala.organisation.facade.impl.assembler.EmployeeAssembler;
import org.openkoala.organisation.facade.impl.assembler.OrganizationAssembler;

/**
 * 组织机构应用实现层类
 * 
 */
@SuppressWarnings("unchecked")
@Named
public class OrganizationFacadeImpl implements OrganizationFacade {

	@Inject
	private BaseApplication baseApplication;
	
	@Inject
	private OrganizationApplication organizationApplication;
	
	
	public boolean isTopOrganizationExists() {
		return organizationApplication.isTopOrganizationExists();
	}

    
	public void createAsTopOrganization(OrganizationDTO company) {
    	organizationApplication.createAsTopOrganization((Company) OrganizationAssembler.toEntity(company));
	}

	
	public InvokeResult createCompany(Long parentId, OrganizationDTO companyDTO) {
		Company parent = baseApplication.getEntity(Company.class, parentId);
		try {
			return InvokeResult.success(organizationApplication.createCompany(parent, 
					(Company) OrganizationAssembler.toEntity(companyDTO)).getId());
		} catch (SnIsExistException exception) {
    		return InvokeResult.failure("机构编码: " + companyDTO.getSn() + " 已被使用！");
    	} catch (NameExistException exception) {
    		return InvokeResult.failure(parent.getName() + "下已经存在名称为: " + companyDTO.getName() + "的机构！");
    	} catch (Exception exception) {
    		exception.printStackTrace();
    		return InvokeResult.failure("创建公司失败！");
    		
    	}
	}

	
	public void assignChildOrganization(OrganizationDTO parent, OrganizationDTO child, Date date) {
		organizationApplication.assignChildOrganization(OrganizationAssembler.toEntity(parent), 
				OrganizationAssembler.toEntity(child), date);
	}

	
	public OrganizationDTO getParentOfOrganization(OrganizationDTO organizationDTO, Date date) {
		return OrganizationAssembler.toDTO(organizationApplication.getParentOfOrganization(OrganizationAssembler.toEntity(organizationDTO), date));
	}

	
	public List<OrganizationDTO> findChildrenOfOrganization(OrganizationDTO organizationDTO, Date date) {
		List<OrganizationDTO> results = new ArrayList<OrganizationDTO>();
		for (Organization organization : organizationApplication.findChildrenOfOrganization(OrganizationAssembler.toEntity(organizationDTO), date)) {
			results.add(OrganizationAssembler.toDTO(organization));
		}
		return results;
	}

	
	public OrganizationDTO getOrganizationTree() {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.organisation.facade.dto.OrganizationDTO"
				+ "(r.id, olm.commissioner.id, r.name, r.sn, r.createDate, r.terminateDate, r.description, r.category, r.version) "
				+ "FROM OrganizationLineManagement olm LEFT JOIN olm.responsible r "
				+ "WHERE olm.commissioner is null AND olm.toDate > :queryDate AND olm.fromDate <= :queryDate ORDER BY r.id ASC");
		QueryChannelService queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_org");
		OrganizationDTO top = (OrganizationDTO) queryChannel.createJpqlQuery(jpql.toString()).addParameter("queryDate", new Date()).singleResult();

		jpql = new StringBuilder("SELECT NEW org.openkoala.organisation.facade.dto.OrganizationDTO"
				+ "(r.id, olm.commissioner.id, r.name, r.sn, r.createDate, r.terminateDate, r.description, r.category, r.version) "
				+ "FROM OrganizationLineManagement olm LEFT JOIN olm.responsible r "
				+ "WHERE olm.commissioner is not null AND  olm.toDate > :queryDate AND olm.fromDate <= :queryDate ORDER BY r.id ASC");

		List<OrganizationDTO> all = queryChannel.createJpqlQuery(jpql.toString()).addParameter("queryDate", new Date()).list();
		LinkedHashMap<Long, OrganizationDTO> map = new LinkedHashMap<Long, OrganizationDTO>();
		for (OrganizationDTO organizationDTO : all) {
			map.put(organizationDTO.getId(), organizationDTO);
		}
		map.put(top.getId(), top);
		for (OrganizationDTO organizationDTO : map.values()) {
			Long pid = organizationDTO.getPid();
			if (pid == null || map.get(pid) == null) {
				continue;
			}
			map.get(pid).getChildren().add(organizationDTO);
		}
		return top;
	}

	
	public InvokeResult createDepartment(Long parentId, OrganizationDTO departmentDTO) {
		Organization parent = baseApplication.getEntity(Organization.class, parentId);
		try {
			return InvokeResult.success(organizationApplication.createDepartment(parent, 
					(Department) OrganizationAssembler.toEntity(departmentDTO)).getId());
		} catch (SnIsExistException exception) {
    		return InvokeResult.failure("机构编码: " + departmentDTO.getSn() + " 已被使用！");
    	} catch (NameExistException exception) {
    		return InvokeResult.failure(parent.getName() + "下已经存在名称为: " + departmentDTO.getName() + "的机构！");
    	} catch (Exception exception) {
    		return InvokeResult.failure("创建部门失败！");
    	}
	}

	
	public InvokeResult terminateEmployeeOrganizationRelation(Long organizationId, EmployeeDTO[] employeeDtos) {
		try {
			Set<Employee> employees = new HashSet<Employee>();
			for (EmployeeDTO employeeDTO : employeeDtos) {
				employees.add(EmployeeAssembler.toEntity(employeeDTO));
			}
			organizationApplication.terminateEmployeeOrganizationRelation(baseApplication.getEntity(Organization.class, organizationId), employees);
			return InvokeResult.success();
		} catch (Exception e) {
			return InvokeResult.failure(e.getMessage());
		}
	}

	
	public OrganizationDTO getOrganizationById(Long id) {
		return OrganizationAssembler.toDTO(baseApplication.getEntity(Organization.class, id));
	}

	
	public InvokeResult updateOrganization(OrganizationDTO organizationDTO) {
		try {
			baseApplication.updateParty(OrganizationAssembler.toEntity(organizationDTO));
			return InvokeResult.success();
		} catch (SnIsExistException exception) {
    		return InvokeResult.failure("机构编码: " + organizationDTO.getSn() + " 已被使用！");
    	} catch (NameExistException exception) {
    		return InvokeResult.failure("同级机构下已经存在名称为: " + organizationDTO.getName() + "的机构！");
    	} catch (Exception exception) {
    		exception.printStackTrace();
    		return InvokeResult.failure("修改公司信息失败！");
    	}
	}

	
	public InvokeResult terminateOrganization(OrganizationDTO organizationDTO) {
		try {
			baseApplication.terminateParty(OrganizationAssembler.toEntity(organizationDTO));
			return InvokeResult.success();
		} catch (TerminateRootOrganizationException exception) {
			return InvokeResult.failure("不能撤销根机构！");
		} catch (TerminateNotEmptyOrganizationException exception) {
			return InvokeResult.failure("该机构下还有员工，不能撤销！");
		}
	}

}
