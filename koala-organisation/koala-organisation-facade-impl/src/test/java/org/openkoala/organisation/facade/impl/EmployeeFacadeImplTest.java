package org.openkoala.organisation.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dayatang.utils.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.EmployeeApplication;
import org.openkoala.organisation.core.IdNumberIsExistException;
import org.openkoala.organisation.core.SnIsExistException;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.ResponsiblePostDTO;
import org.openkoala.organisation.facade.impl.assembler.EmployeeAssembler;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * EmployeeController单元测试
 * 
 * @author xmfang
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EmployeeAssembler.class})
public class EmployeeFacadeImplTest {

	@Mock
	private EmployeeApplication employeeApplication;

	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private EmployeeFacadeImpl employeeFacadeImpl = new EmployeeFacadeImpl();

	private Page<EmployeeDTO> employeePage;
	private EmployeeDTO employeeDTO;
	private Long employeeId = 5L;
	private Post post;
	private Long postId = 3L;
	private Map<Post, Boolean> responsiblePosts;
	private ResponsiblePostDTO[] responsiblePostDTOs;

	@Test
	public void testCreateEmployee() {
		initPost();
		initEmployeeDto();
		
		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		employeeFacadeImpl.createEmployeeWithPost(employeeDTO, postId);
		verify(employeeApplication, only()).createEmployeeWithPost(EmployeeAssembler.toEntity(employeeDTO),
				post);
	}	

	@Test
	public void testCatchSnIsExistExceptionWhenCreateEmployee() {
		initPost();
		initEmployeeDto();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		doThrow(new SnIsExistException()).when(employeeApplication)
				.createEmployeeWithPost(EmployeeAssembler.toEntity(employeeDTO), post);
		assertEquals("员工编号: " + employeeDTO.getSn() + " 已被使用！", employeeFacadeImpl
				.createEmployeeWithPost(employeeDTO, postId).getErrorMessage());
	}

	@Test
	public void testIdNumberIsExistExceptionWhenCreateEmployee() {
		initPost();
		initEmployeeDto();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		doThrow(new IdNumberIsExistException()).when(employeeApplication)
				.createEmployeeWithPost(EmployeeAssembler.toEntity(employeeDTO), post);
		assertEquals(
				"不能使用与其他人一样的证件号码！",
				employeeFacadeImpl.createEmployeeWithPost(employeeDTO, postId).getErrorMessage());
	}

	@Test
	public void testExceptionWhenCreateEmployee() {
		initPost();
		initEmployeeDto();

		when(baseApplication.getEntity(Post.class, postId)).thenReturn(post);
		doThrow(new RuntimeException()).when(employeeApplication)
				.createEmployeeWithPost(EmployeeAssembler.toEntity(employeeDTO), post);
		assertEquals(
				"保存失败！",
				employeeFacadeImpl.createEmployeeWithPost(employeeDTO, postId).getErrorMessage());
	}

	private void initPost() {
		post = new Post("总经理", "EMP-XXX");
		post.setId(postId);
	}

	private void initEmployeeDto() {
		employeeDTO = new EmployeeDTO();
		employeeDTO.setName("张三");
		employeeDTO.setSn("EMP-XXX");
		employeeDTO.setCreateDate(new Date());
		employeeDTO.setId(employeeId);
	}

	private void initResponsiblePostDTOs() {
		ResponsiblePostDTO dto1 = new ResponsiblePostDTO();
		dto1.setPostId(1L);
		dto1.setPrincipal(true);
		responsiblePostDTOs[0] = dto1;

		ResponsiblePostDTO dto2 = new ResponsiblePostDTO();
		dto2.setPostId(2L);
		dto2.setPrincipal(false);
		responsiblePostDTOs[1] = dto2;
	}

	@Test
	public void testUpdateEmployee() {
		initEmployeeDto();

		employeeFacadeImpl.updateEmployeeInfo(employeeDTO);
		verify(baseApplication, only()).updateParty(EmployeeAssembler.toEntity(employeeDTO));
	}

	@Test
	public void testSnIsExistExceptionWhenUpdateEmployee() {
		initEmployeeDto();

		doThrow(new SnIsExistException()).when(baseApplication)
			.updateParty(EmployeeAssembler.toEntity(employeeDTO));
		assertEquals("员工编号: " + employeeDTO.getSn() + " 已被使用！", employeeFacadeImpl
				.updateEmployeeInfo(employeeDTO).getErrorMessage());
	}

	@Test
	public void testIdNumberIsExistExceptionWhenUpdateEmployee() {
		initEmployeeDto();

		doThrow(new IdNumberIsExistException()).when(baseApplication)
				.updateParty(EmployeeAssembler.toEntity(employeeDTO));
		assertEquals("不能使用与其他人一样的证件号码！",
				employeeFacadeImpl.updateEmployeeInfo(employeeDTO).getErrorMessage());
	}

	@Test
	public void testExceptionWhenUpdateEmployee() {
		initEmployeeDto();

		doThrow(new RuntimeException()).when(baseApplication).updateParty(
				EmployeeAssembler.toEntity(employeeDTO));
		assertEquals("修改失败！",
				employeeFacadeImpl.updateEmployeeInfo(employeeDTO).getErrorMessage());
	}

//	@Test
//	public void testTransformPost() {
//		initEmployee();
//		initResponsiblePostDTOs();
//		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
//				EmployeeDtoAssembler.assemEntity(employeeDTO));
//
//		employeeFacadeImpl.transformPost(employeeId, responsiblePostDTOs);
//		verify(employeeApplication).transformPost(
//				employeeDTO, responsiblePostDTOs)));
//	}
//
//	@Test
//	public void testHasPrincipalPostYetExceptionWhenTransformPost() {
//		initEmployee();
//		initResponsiblePostDTOs();
//		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
//				EmployeeDtoAssembler.assemEntity(employeeDTO));
//
//		doThrow(new HasPrincipalPostYetException()).when(employeeApplication)
//				.transformPost(responsiblePostDTOs));
//		assertEquals("该员工已经有主任职岗位！",
//				employeeFacadeImpl.transformPost(employeeId,
//						responsiblePostDTOs).getErrorMessage());
//	}
//
//	@Test
//	public void testEmployeeMustHaveAtLeastOnePostExceptionWhenTransformPost() {
//		initEmployee();
//		initResponsiblePostDTOs();
//		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
//				employeeDTO);
//
//		doThrow(new EmployeeMustHaveAtLeastOnePostException()).when(
//				employeeApplication).transformPost(
//				employeeDTO,
//				new HashSet<ResponsiblePostDTO>(Arrays
//						.asList(responsiblePostDTOs)));
//		assertEquals(
//				"必须保证每名员工至少在一个岗位上任职！",
//				employeeFacadeImpl.transformPost(employeeId,
//						responsiblePostDTOs).get("result"));
//	}
//
//	@Test
//	public void testExceptionWhenTransformPost() {
//		initEmployee();
//		initResponsiblePostDTOs();
//		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(
//				employeeDTO);
//
//		doThrow(new RuntimeException()).when(employeeApplication)
//				.transformPost(
//						employeeDTO,
//						new HashSet<ResponsiblePostDTO>(Arrays
//								.asList(responsiblePostDTOs)));
//		assertEquals(
//				"调整职务失败！",
//				employeeFacadeImpl.transformPost(employeeId,
//						responsiblePostDTOs).get("result"));
//	}

	@Test
	public void testGet() {
		initEmployeeDto();
		Employee employee = EmployeeAssembler.toEntity(employeeDTO);
		when(baseApplication.getEntity(Employee.class, employeeId)).thenReturn(employee);
		PowerMockito.mockStatic(EmployeeAssembler.class);
		PowerMockito.when(EmployeeAssembler.toDTO(employee)).thenReturn(
				employeeDTO);
		assertEquals(employeeDTO, employeeFacadeImpl.getEmployeeById(employeeId));
	}

	@Test
	public void testGetGendens() {
		Map<String, String> genders = new HashMap<String, String>();
		genders.put("MALE", "男");
		genders.put("FEMALE", "女");
		assertEquals(genders, employeeFacadeImpl.getGenders());
	}

	@Test
	public void testTerminateEmployee() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setName("张三");
		dto.setSn("EMP-XXX");
		dto.setId(employeeId);

		employeeFacadeImpl.terminateEmployee(dto);
		verify(baseApplication, only()).terminateParty(EmployeeAssembler.toEntity(dto));
	}

	@Test
	public void testTerminateEmployees() {
		EmployeeDTO[] dtos = new EmployeeDTO[2];

		EmployeeDTO dto1 = new EmployeeDTO();
		dto1.setName("张三");
		dto1.setSn("EMP-XXX");
		dto1.setId(employeeId);
		dtos[0] = dto1;

		EmployeeDTO dto2 = new EmployeeDTO();
		dto2.setName("李四");
		dto2.setSn("EMP-XXX1");
		dto2.setId(6L);
		dtos[1] = dto2;

		Set<Employee> employees = new HashSet<Employee>();
		for (EmployeeDTO employeeDTO : dtos) {
			employees.add(EmployeeAssembler.toEntity(employeeDTO));
		}

		employeeFacadeImpl.terminateEmployees(dtos);
		verify(baseApplication, only()).terminateParties(employees);
	}

}
