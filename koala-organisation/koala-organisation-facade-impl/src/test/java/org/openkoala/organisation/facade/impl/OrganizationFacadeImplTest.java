package org.openkoala.organisation.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.core.SnIsExistException;
import org.openkoala.organisation.core.TerminateNotEmptyOrganizationException;
import org.openkoala.organisation.core.TerminateRootOrganizationException;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.OrganizationDTO;
import org.openkoala.organisation.facade.impl.assembler.EmployeeAssembler;
import org.openkoala.organisation.facade.impl.assembler.OrganizationAssembler;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * OrganizationController单元测试
 * @author xmfang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OrganizationAssembler.class})
public class OrganizationFacadeImplTest {
	
	@Mock
	private OrganizationApplication organizationApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private OrganizationFacadeImpl organizationFacadeImpl = new OrganizationFacadeImpl();
	
	private Long parentId = 1L;
	private Company parent = new Company("总公司公司", "COM-XXX1");
	private Company company = new Company("广州分公司", "COM-XXX2");
	private OrganizationDTO companyDTO = new OrganizationDTO("广州分公司", "COM-XXX2", "");
	private Department department = new Department("财务部", "DEP-XXX2");
	private OrganizationDTO departmentDTO = new OrganizationDTO("财务部", "DEP-XXX2", "");
	
	@Before
	public void setup() {
		parent.setId(parentId);
		companyDTO.setOrganizationType(OrganizationDTO.COMPANY);
		departmentDTO.setOrganizationType(OrganizationDTO.DEPARTMENT);
	}
	
	@Test
	public void testCreateCompany() {
		when(baseApplication.getEntity(Company.class, parentId)).thenReturn(parent);
		organizationFacadeImpl.createCompany(parentId, companyDTO);
		verify(organizationApplication, only()).createCompany(parent, company);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenCreateCompany() {
		when(baseApplication.getEntity(Company.class, parentId)).thenReturn(parent);
		doThrow(new SnIsExistException()).when(organizationApplication).createCompany(parent, company);
		assertEquals("机构编码: " + company.getSn() + " 已被使用！", 
				organizationFacadeImpl.createCompany(parentId, companyDTO).getErrorMessage());
	}
	
	@Test
	public void testCatchExceptionWhenCreateCompany() {
		when(baseApplication.getEntity(Company.class, parentId)).thenReturn(parent);
		doThrow(new RuntimeException()).when(organizationApplication).createCompany(parent, company);
		assertEquals("创建公司失败！", organizationFacadeImpl.createCompany(parentId, companyDTO).getErrorMessage());
	}
	
	@Test
	public void testCreateDepartment() {
		when(baseApplication.getEntity(Organization.class, parentId)).thenReturn(parent);
		organizationFacadeImpl.createDepartment(parentId, departmentDTO);
		verify(organizationApplication, only()).createDepartment(parent, department);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenCreateDepartment() {
		when(baseApplication.getEntity(Organization.class, parentId)).thenReturn(parent);
		doThrow(new SnIsExistException()).when(organizationApplication).createDepartment(parent, department);
		assertEquals("机构编码: " + department.getSn() + " 已被使用！",
				organizationFacadeImpl.createDepartment(parentId, departmentDTO).getErrorMessage());
	}
	
	@Test
	public void testExceptionWhenCreateDepartment() {
		when(baseApplication.getEntity(Organization.class, parentId)).thenReturn(parent);
		doThrow(new RuntimeException()).when(organizationApplication).createDepartment(parent, department);
		assertEquals("创建部门失败！", 
				organizationFacadeImpl.createDepartment(parentId, departmentDTO).getErrorMessage());
	}
	
	@Test
	public void testUpdateOrganization() {
		organizationFacadeImpl.updateOrganization(companyDTO);
		verify(baseApplication, only()).updateParty(company);
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenUpdateCompany() {
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(company);
		assertEquals("机构编码: " + company.getSn() + " 已被使用！", 
				organizationFacadeImpl.updateOrganization(companyDTO).getErrorMessage());
	}
	
	@Test
	public void testExceptionWhenUpdateCompany() {
		doThrow(new RuntimeException()).when(baseApplication).updateParty(company);
		assertEquals("修改公司信息失败！", organizationFacadeImpl.updateOrganization(companyDTO).getErrorMessage());
	}
	
	@Test
	public void testGetOrganization() {
		Long organizationId = 1L;
		OrganizationDTO organizationDTO = new OrganizationDTO(organizationId, "总公司");
		organizationDTO.setSn("xxx");
		organizationDTO.setOrganizationType(OrganizationDTO.COMPANY);
		Organization organization = OrganizationAssembler.toEntity(organizationDTO);
		
		PowerMockito.mockStatic(OrganizationAssembler.class);
		PowerMockito.when(OrganizationAssembler.toDTO(organization)).thenReturn(organizationDTO);

		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(organization);
		assertEquals(organizationDTO, organizationFacadeImpl.getOrganizationById(organizationId));
	}
	
	@Test
	public void testTerminateEmployeeOrganizationRelation() {
		Company company = new Company("广州分公司", "COM-XXX2");
		Long organizationId = 1L;
		company.setId(organizationId);
		
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName("张三");
		employeeDTO.setSn("XXX");
		
		EmployeeDTO[] employeeDTOs = new EmployeeDTO[1];
		employeeDTOs[0] = employeeDTO;
		
		Set<Employee> employees = new HashSet<Employee>();
		employees.add(EmployeeAssembler.toEntity(employeeDTO));
		
		when(baseApplication.getEntity(Organization.class, organizationId)).thenReturn(company);
		organizationFacadeImpl.terminateEmployeeOrganizationRelation(organizationId, employeeDTOs);
		verify(organizationApplication, only()).terminateEmployeeOrganizationRelation(company, employees);
	}
	
	@Test
	public void testTerminateOrganization() {
		organizationFacadeImpl.terminateOrganization(companyDTO);
		verify(baseApplication, only()).terminateParty(company);
	}
	
	@Test
	public void testCatchTerminateRootOrganizationExceptionWhenTerminateCompany() {
		doThrow(new TerminateRootOrganizationException()).when(baseApplication).terminateParty(company);
		assertEquals("不能撤销根机构！", organizationFacadeImpl.terminateOrganization(companyDTO).getErrorMessage());
	}
	
	@Test
	public void testCatchTerminateNotEmptyOrganizationExceptionWhenTerminateCompany() {
		doThrow(new TerminateNotEmptyOrganizationException()).when(baseApplication).terminateParty(company);
		assertEquals("该机构下还有员工，不能撤销！", 
				organizationFacadeImpl.terminateOrganization(companyDTO).getErrorMessage());
	}
	
	@Test
	public void testCatchTerminateNotEmptyOrganizationExceptionWhenTerminateDepartment() {
		doThrow(new TerminateNotEmptyOrganizationException()).when(baseApplication).terminateParty(department);
		assertEquals("该机构下还有员工，不能撤销！",
				organizationFacadeImpl.terminateOrganization(departmentDTO).getErrorMessage());
	}

}
