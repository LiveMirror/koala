package org.openkoala.organisation.application.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.application.dto.OrganizationDTO;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.EmployeePostHolding;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.organisation.domain.OrganizationAbstractEntity;
import org.openkoala.organisation.domain.OrganizationLineManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织机构应用实现层类
 * 
 */
@Named
@Transactional(value = "transactionManager_org")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "OrganizationApplication")
@Remote
public class OrganizationApplicationImpl implements OrganizationApplication {

	public boolean isTopOrganizationExists() {
		return Organization.getTopOrganization() == null ? false : true;
	}

	@Override
	public void createAsTopOrganization(Company company) {
		company.createAsTopOrganization();
	}

	@Override
	public Company createCompany(Company parent, Company company) {
		company.createUnder(parent);
		return company;
	}

	@Override
	public void assignChildOrganization(Organization parent, Organization child, Date date) {
		OrganizationLineManagement organizationLineManagement = new OrganizationLineManagement(parent, child, date);
		organizationLineManagement.save();
	}

	public Organization getParentOfOrganization(Organization organization, Date date) {
		return OrganizationLineManagement.getParentOfOrganization(organization, date);
	}

	public List<Organization> findChildrenOfOrganization(Organization organization, Date date) {
		return OrganizationLineManagement.findChildrenOfOrganization(organization, date);
	}

	@Override
	public OrganizationDTO getOrganizationTree() {
		// Organization organization = Organization.getTopOrganization();
		// OrganizationDTO top = OrganizationDTO.generateDtoBy(organization);
		// findChildrenOfOrganization(top, organization);

		OrganizationDTO top = _getOrganizationTree();
		return top;
	}

	/**
	 * 递归查询顶级机构的所有层次的子机构
	 * 
	 * @param organizationDTO
	 * @param organization
	 */
	private void findChildrenOfOrganization(OrganizationDTO organizationDTO, Organization organization) {
		List<Organization> children = organization.getChildren(new Date());
		if (children != null && !children.isEmpty()) {
			for (Organization each : children) {
				OrganizationDTO childDto = OrganizationDTO.generateDtoBy(each);
				organizationDTO.getChildren().add(childDto);
				findChildrenOfOrganization(childDto, each);
			}
		}
	}

	@Override
	public Department createDepartment(Organization parent, Department department) {
		department.createUnder(parent);
		return department;
	}

	@Override
	public void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees) {
		EmployeePostHolding.terminateEmployeeOrganizationRelation(organization, employees);
	}

	@Override
	public OrganizationDTO getOrganizationById(Long id) {
		Organization organization = OrganizationAbstractEntity.get(Organization.class, id);
		return organization == null ? null : OrganizationDTO.generateDtoBy(organization);
	}

	public OrganizationDTO _getOrganizationTree() {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.organisation.application.dto.OrganizationDTO"
				+ "(r.id,olm.commissioner.id, r.name, r.sn,r.createDate, r.description, r.category) "
				+ "FROM OrganizationLineManagement olm LEFT JOIN olm.responsible r "
				+ "WHERE olm.commissioner is null AND olm.toDate > :queryDate AND olm.fromDate <= :queryDate");
		QueryChannelService queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_org");
		OrganizationDTO top = (OrganizationDTO) queryChannel.createJpqlQuery(jpql.toString()).addParameter("queryDate", new Date()).singleResult();

		jpql = new StringBuilder("SELECT NEW org.openkoala.organisation.application.dto.OrganizationDTO"
				+ "(r.id,olm.commissioner.id, r.name, r.sn,r.createDate, r.description, r.category) "
				+ "FROM OrganizationLineManagement olm LEFT JOIN olm.responsible r "
				+ "WHERE olm.commissioner is not null AND  olm.toDate > :queryDate AND olm.fromDate <= :queryDate");
		@SuppressWarnings("unchecked")
		List<OrganizationDTO> all = queryChannel.createJpqlQuery(jpql.toString()).addParameter("queryDate", new Date()).list();
		Map<Long, OrganizationDTO> map = new HashMap<Long, OrganizationDTO>();
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

		// _findChildrenOfOrganization(top, all);
		return top;
	}

	private void _findChildrenOfOrganization(OrganizationDTO parent, List<OrganizationDTO> all) {
		for (OrganizationDTO organizationDTO : all) {
			if (organizationDTO.getPid() == parent.getId()) {
				parent.getChildren().add(organizationDTO);
				_findChildrenOfOrganization(organizationDTO, all);
			}
		}
	}

}
