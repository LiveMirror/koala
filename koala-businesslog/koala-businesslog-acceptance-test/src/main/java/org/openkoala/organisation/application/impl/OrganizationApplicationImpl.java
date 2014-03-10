package org.openkoala.organisation.application.impl;

import org.openkoala.businesslog.utils.MethodAlias;
import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.application.dto.OrganizationDTO;
import org.openkoala.organisation.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 组织机构应用实现层类
 *
 */
@Named
@Transactional(value="transactionManager_org")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "OrganizationApplication")
@Remote
public class OrganizationApplicationImpl implements OrganizationApplication {
	
	public boolean isTopOrganizationExists(){
		return Organization.getTopOrganization()==null?false:true;
	}

    @MethodAlias("OrganizationApplicationImpl_createAsTopOrganization")
	@Override
	public void createAsTopOrganization(Company company) {
		company.createAsTopOrganization();
	}

    @MethodAlias("OrganizationApplicationImpl_createCompany")
    @Override
	public Company createCompany(Company parent, Company company) {
		company.createUnder(parent);
		return company;
	}

    @MethodAlias("OrganizationApplicationImpl_assignChildOrganization")
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
		Organization organization = Organization.getTopOrganization();
		OrganizationDTO top = OrganizationDTO.generateDtoBy(organization);
		findChildrenOfOrganization(top, organization);
		return top;
	}

	/**
	 * 递归查询顶级机构的所有层次的子机构
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

    @MethodAlias("OrganizationApplicationImpl_createDepartment")
    @Override
	public Department createDepartment(Organization parent, Department department) {
		department.createUnder(parent);
		return department;
	}

    @MethodAlias("OrganizationApplicationImpl_terminateEmployeeOrganizationRelation")
    @Override
	public void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees) {
		EmployeePostHolding.terminateEmployeeOrganizationRelation(organization, employees);
	}

	@Override
	public OrganizationDTO getOrganizationById(Long id) {
		Organization organization = OrganizationAbstractEntity.get(Organization.class, id);
		return organization == null ? null : OrganizationDTO.generateDtoBy(organization);
	}

}
