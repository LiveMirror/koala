package org.openkoala.organisation.application;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Organization;

/**
 * 组织机构应用层接口
 * 
 */
public interface OrganizationApplication {

	/**
	 * 查询系统中是否存在组织机构的根，即最顶级的机构
	 * 
	 * @return
	 */
	boolean isTopOrganizationExists();

	/**
	 * 创建组织机构根机构，即最顶级机构
	 * 
	 * @param company
	 */
	void createAsTopOrganization(Company company);

	/**
	 * 在某个公司下面创建一个分公司
	 * 
	 * @param parent
     * @param company
	 * @return
	 */
	Company createCompany(Company parent, Company company);

	/**
	 * 在某个组织机构下创建下级部门
	 * 
	 * @param parent
	 * @param department
	 * @return
	 */
	Department createDepartment(Organization parent, Department department);

	/**
	 * 把一个机构分配到另一个机构下，即从属于另一个机构
	 * 
	 * @param parent
	 * @param child
	 * @param date
	 */
	void assignChildOrganization(Organization parent, Organization child, Date date);

	/**
	 * 获取组织机构的父机构
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	Organization getParentOfOrganization(Organization organization, Date date);

	/**
	 * 查找某个机构的所有子机构
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	List<Organization> findChildrenOfOrganization(Organization organization, Date date);

	/**
	 * 撤销某个机构与一批员工的责任关系
	 * 
	 * @param organization
	 * @param employees
	 */
	void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees);

	/**
	 * 根据id获得机构信息
	 * 
	 * @param id
	 * @return
	 */
	Organization getOrganizationById(Long id);

	/**
	 * 修改某个组织机构信息
	 * 
	 * @param organization
	 */
	void updateOrganization(Organization organization);

}
