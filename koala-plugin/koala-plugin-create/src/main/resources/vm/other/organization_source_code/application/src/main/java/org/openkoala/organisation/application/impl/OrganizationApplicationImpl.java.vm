package org.openkoala.organisation.application.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.organisation.application.OrganizationApplication;
import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.EmployeePostHolding;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.core.domain.OrganizationAbstractEntity;
import org.openkoala.organisation.core.domain.OrganizationLineManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织机构应用实现层类
 * 
 * @author xmfang
 * 
 */
@Named
@Transactional(value = "transactionManager_org")
public class OrganizationApplicationImpl implements OrganizationApplication {

	public boolean isTopOrganizationExists() {
		return Organization.getTopOrganization() != null;
	}

	
	public void createAsTopOrganization(Company company) {
		company.createAsTopOrganization();
	}

	
	public Company createCompany(Company parent, Company company) {
		company.createUnder(parent);
		return company;
	}

	
	public void assignChildOrganization(Organization parent, Organization child, Date date) {
        new OrganizationLineManagement(parent, child, date).save();
	}

	public Organization getParentOfOrganization(Organization organization, Date date) {
		return OrganizationLineManagement.getParentOfOrganization(organization, date);
	}

	public List<Organization> findChildrenOfOrganization(Organization organization, Date date) {
		return OrganizationLineManagement.findChildrenOfOrganization(organization, date);
	}

	
	public Department createDepartment(Organization parent, Department department) {
		department.createUnder(parent);
		return department;
	}

	
	public void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees) {
		EmployeePostHolding.terminateEmployeeOrganizationRelation(organization, employees);
	}

	
	public Organization getOrganizationById(Long id) {
		return OrganizationAbstractEntity.get(Organization.class, id);
	}

	
	public void updateOrganization(Organization organization) {
		organization.update();
	}

}
