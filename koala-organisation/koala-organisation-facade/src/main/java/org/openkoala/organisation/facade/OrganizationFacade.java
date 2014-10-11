package org.openkoala.organisation.facade;

import java.util.Date;
import java.util.List;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.facade.dto.EmployeeDTO;
import org.openkoala.organisation.facade.dto.OrganizationDTO;

/**
 * 组织机构门面层接口
 * 
 */
public interface OrganizationFacade {

	/**
	 * 查询系统中是否存在组织机构的根，即最顶级的机构。
	 * 
	 * @return
	 */
	boolean isTopOrganizationExists();

	/**
	 * 创建组织机构根机构，即最顶级机构。
	 * 
	 * @param company
	 */
	void createAsTopOrganization(OrganizationDTO company);

	/**
	 * 在某个公司下面创建一个分公司
	 * 
	 * @param parentId
     * @param company
	 * @return
	 */
	InvokeResult createCompany(Long parentId, OrganizationDTO company);

	/**
	 * 在某个组织机构下创建下级部门
	 * 
	 * @param parentId
	 * @param department
	 * @return
	 */
	InvokeResult createDepartment(Long parentId, OrganizationDTO department);

	/**
	 * 把一个机构分配到另一个机构下，即从属于另一个机构
	 * 
	 * @param parent
	 * @param child
	 * @param date
	 */
	void assignChildOrganization(OrganizationDTO parent, OrganizationDTO child, Date date);

	/**
	 * 获取组织机构的父机构
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	OrganizationDTO getParentOfOrganization(OrganizationDTO organization, Date date);

	/**
	 * 查找某个机构的所有子机构
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	List<OrganizationDTO> findChildrenOfOrganization(OrganizationDTO organization, Date date);

	/**
	 * 获取机构树
	 * 
	 * @return
	 */
	OrganizationDTO getOrganizationTree();

	/**
	 * 撤销某个机构与一批员工的责任关系
	 * 
	 * @param organizationId
	 * @param employees
	 */
	InvokeResult terminateEmployeeOrganizationRelation(Long organizationId, EmployeeDTO[] employees);

	/**
	 * 根据id获得机构信息
	 * 
	 * @param id
	 * @return
	 */
	OrganizationDTO getOrganizationById(Long id);

	/**
	 * 修改某个组织机构信息
	 * 
	 * @param organization
	 */
	InvokeResult updateOrganization(OrganizationDTO organization);

	/**
	 * 撤销一个机构
	 * 
	 * @param organizationDTO
	 */
	InvokeResult terminateOrganization(OrganizationDTO organizationDTO);

}
