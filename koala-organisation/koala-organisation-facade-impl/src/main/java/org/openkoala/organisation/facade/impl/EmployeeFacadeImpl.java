package org.openkoala.organisation.facade.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.organisation.HasPrincipalPostYetException;
import org.openkoala.organisation.IdNumberIsExistException;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.application.EmployeeApplication;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Gender;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.Post;
import org.openkoala.organisation.facade.EmployeeFacade;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.InvokeResult;
import org.openkoala.organisation.facade.dto.OrganizationDTO;
import org.openkoala.organisation.facade.dto.ResponsiblePostDTO;
import org.openkoala.organisation.facade.impl.assembler.EmployeeDtoAssembler;
import org.openkoala.organisation.facade.impl.assembler.OrganizationDtoAssembler;
import org.openkoala.organisation.facade.impl.assembler.ResponsiblePostDtoAssembler;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工应用实现层类
 * 
 */
@Named
//@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
//@Stateless(name = "EmployeeApplication")
//@Remote
public class EmployeeFacadeImpl implements EmployeeFacade {
	
	@Inject
	private BaseApplication baseApplication;
	
	@Inject
	private EmployeeApplication employeeApplication;
	
	private QueryChannelService queryChannel;

	private QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_org");
		}
		return queryChannel;
	}

	@Override
	public OrganizationDTO getOrganizationOfEmployee(Long employeeId, Date date) {
		Employee employee = baseApplication.getEntity(Employee.class, employeeId);
		if (employee == null) return null;
		return OrganizationDtoAssembler.assemDto(employee.getOrganization(date));
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployees(EmployeeDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _employee from Employee _employee where _employee.createDate <= ?1 and _employee.terminateDate > ?2");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_employee", conditionVals, currentPage, pagesize);
	}

	@Override
	public InvokeResult createEmployeeWithPost(EmployeeDTO employeeDto, Long postId) {
		try {
			if (postId == null) {
				return InvokeResult.failure("请选择岗位");
			}
			employeeApplication.createEmployeeWithPost(EmployeeDtoAssembler.assemEntity(employeeDto), baseApplication.getEntity(Post.class, postId));
			return InvokeResult.success();
		} catch (SnIsExistException exception) {
			return InvokeResult.failure("员工编号: " + employeeDto.getSn() + " 已被使用！");
		} catch (IdNumberIsExistException exception) {
			return InvokeResult.failure("不能使用与其他人一样的证件号码！");
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("保存失败！");
		}
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployeesByOrganization(EmployeeDTO example, Long orgId, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select distinct (_holding.responsible) from EmployeePostHolding _holding " + "where _holding.commissioner in"
				+ " (select p from Post p where p.organization = ?1 and p.createDate <= ?2 and p.terminateDate > ?3)"
				+ " and _holding.fromDate <= ?4 and _holding.toDate > ?5");
		Date now = new Date();
		conditionVals.add(baseApplication.getEntity(Organization.class, orgId));
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_holding.responsible", conditionVals, currentPage, pagesize);
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployeesByOrganizationAndChildren(EmployeeDTO example, Long orgId, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select distinct (_holding.responsible) from EmployeePostHolding _holding " + "where _holding.commissioner in"
				+ " (select p from Post p where p.organization in ?1 and p.createDate <= ?2 and p.terminateDate > ?3)"
				+ " and _holding.fromDate <= ?4 and _holding.toDate > ?5");
		Date now = new Date();
		
		Organization organizaton = baseApplication.getEntity(Organization.class, orgId);
		List<Organization> organizations = new ArrayList<Organization>();
		organizations.add(organizaton);
		organizations.addAll(organizaton.getAllChildren(now));

		conditionVals.add(organizations);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_holding.responsible", conditionVals, currentPage, pagesize);
	}

	@Override
	public Page<EmployeeDTO> pagingQueryEmployeesWhoNoPost(EmployeeDTO example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _employee from Employee _employee"
				+ " where _employee.createDate <= ?1 and _employee.terminateDate > ?2");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		jpql.append(" and _employee Not In"
				+ " (select _holding.responsible from EmployeePostHolding _holding where _holding.fromDate <= ?3 and _holding.toDate > ?4)");
		conditionVals.add(now);
		conditionVals.add(now);

		return queryResult(example, jpql, "_employee", conditionVals, currentPage, pagesize);
	}

	private Page<EmployeeDTO> queryResult(EmployeeDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals, int currentPage,
			int pagesize) {
		assembleJpqlAndConditionValues(example, jpql, conditionPrefix, conditionVals);
		Page<Employee> employeePage = getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize)
				.pagedList();
		return new Page<EmployeeDTO>(employeePage.getStart(), employeePage.getResultCount(), pagesize, transformToDtos(employeePage.getData()));
	}

	private void assembleJpqlAndConditionValues(EmployeeDTO example, StringBuilder jpql, String conditionPrefix, List<Object> conditionVals) {
		String andCondition = " and " + conditionPrefix;
		int initialConditionIndex = conditionVals.size();
		if (!StringUtils.isBlank(example.getName())) {
			jpql.append(andCondition);
			jpql.append(".name like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getName()));
		}
		if (!StringUtils.isBlank(example.getSn())) {
			jpql.append(andCondition);
			jpql.append(".sn like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getSn()));
		}
		if (!StringUtils.isBlank(example.getIdNumber())) {
			jpql.append(andCondition);
			jpql.append(".person.idNumber like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getSn()));
		}
		if (!StringUtils.isBlank(example.getEmail())) {
			jpql.append(andCondition);
			jpql.append(".person.email like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getEmail()));
		}
		if (!StringUtils.isBlank(example.getMobilePhone())) {
			jpql.append(andCondition);
			jpql.append(".person.mobilePhone like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getMobilePhone()));
		}
		if (!StringUtils.isBlank(example.getFamilyPhone())) {
			jpql.append(andCondition);
			jpql.append(".person.familyPhone like ?" + ++initialConditionIndex);
			conditionVals.add(MessageFormat.format("%{0}%", example.getFamilyPhone()));
		}
	}

	private List<EmployeeDTO> transformToDtos(List<Employee> employees) {
		List<EmployeeDTO> results = new ArrayList<EmployeeDTO>();
		for (Employee employee : employees) {
			if (results.contains(employee)) {
				continue;
			}
			results.add(EmployeeDtoAssembler.assemDto(employee));
		}
		return results;
	}

	@Override
	public InvokeResult transformPost(Long employeeId, ResponsiblePostDTO[] dtos) {
		if (dtos.length == 0) {
			return InvokeResult.failure("必须保证每名员工至少在一个岗位上任职！");
		}

		Map<Post, Boolean> posts = new HashMap<Post, Boolean>();
		for (ResponsiblePostDTO dto : dtos) {
			posts.put(baseApplication.getEntity(Post.class, dto.getPostId()), dto.isPrincipal());
		}
		
		try {
			employeeApplication.transformPost(baseApplication.getEntity(Employee.class, employeeId), posts);
			return InvokeResult.success();
		} catch (HasPrincipalPostYetException e) {
			return InvokeResult.failure("该员工已经有主任职岗位！");
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("调整职务失败！");
		}
	}

	@Override
	public List<ResponsiblePostDTO> getPostsByEmployee(Long employeeId) {
		List<ResponsiblePostDTO> results = new ArrayList<ResponsiblePostDTO>();
		Map<Post, Boolean> postsMap = employeeApplication.getPostsByEmployee(baseApplication.getEntity(Employee.class, employeeId));
		for (Post post : postsMap.keySet()) {
			results.add(ResponsiblePostDtoAssembler.assemEntity(post, postsMap.get(post)));
		}
		return results;
	}

	@Override
	public EmployeeDTO getEmployeeById(Long id) {
		Employee employee = baseApplication.getEntity(Employee.class, id);
		return employee == null ? null : EmployeeDtoAssembler.assemDto(employee);
	}

	@Override
	public InvokeResult updateEmployeeInfo(EmployeeDTO employeeDto) {
		try {
			baseApplication.updateParty(EmployeeDtoAssembler.assemEntity(employeeDto));
			return InvokeResult.success();
		} catch (SnIsExistException exception) {
			return InvokeResult.failure("员工编号: " + employeeDto.getSn() + " 已被使用！");
		} catch (IdNumberIsExistException exception) {
			return InvokeResult.failure("不能使用与其他人一样的证件号码！");
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("修改失败！");
		}
	}

	@Override
	public void terminateEmployee(EmployeeDTO employeeDto) {
		baseApplication.terminateParty(EmployeeDtoAssembler.assemEntity(employeeDto));
	}

	@Override
	public void terminateEmployees(EmployeeDTO[] employeeDtos) {
		Set<Employee> employees = new HashSet<Employee>();
		for (EmployeeDTO employeeDTO : employeeDtos) {
			employees.add(EmployeeDtoAssembler.assemEntity(employeeDTO));
		}
		baseApplication.terminateParties(employees);
	}

	@Override
	public Map<String, String> getGenders() {
		Map<String, String> genders = new HashMap<String, String>();
		for (Gender gender : Gender.values()) {
			genders.put(gender.name(), gender.getLabel());
		}
		return genders;
	}
}
