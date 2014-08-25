package org.openkoala.organisation.application.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.organisation.application.OrganizationApplication;
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
	public Department createDepartment(Organization parent, Department department) {
		department.createUnder(parent);
		return department;
	}

	@Override
	public void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees) {
		EmployeePostHolding.terminateEmployeeOrganizationRelation(organization, employees);
	}

	@Override
	public Organization getOrganizationById(Long id) {
		return OrganizationAbstractEntity.get(Organization.class, id);
	}

	@Override
	public void updateOrganization(Organization organization) {
		organization.update();
	}
	
}
