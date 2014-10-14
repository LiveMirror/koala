package org.openkoala.organisation.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 员工与岗位的责任关系
 * 
 * @author xmfang
 * 
 */
@Entity
@DiscriminatorValue("EMPLOYEEPOSTHOLDING")
@NamedQueries({
		@NamedQuery(name = "getPostsOfEmployee", query = "select o.commissioner from EmployeePostHolding o where o.responsible = :employee and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "getEmployeesOfPost", query = "select o.responsible from EmployeePostHolding o where o.commissioner = :post and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "getEmployeesOfJob", query = "select o.responsible from EmployeePostHolding o where o.commissioner in (select p from Post p where p.job = :job and p.createDate <= :date and p.terminateDate > :date) and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "getManagerOfOrganization", query = "select o.responsible from EmployeePostHolding o where o.commissioner in (select p from Post p where p.organization = :organization and organizationPrincipal = :principal and p.createDate <= :date and p.terminateDate > :date) and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "getEmployeeCountOfPost", query = "select count(o) from EmployeePostHolding o where o.commissioner = :post and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "getAdditionalPostsOfEmployee", query = "select o.commissioner from EmployeePostHolding o where o.responsible = :employee and o.principal = :principal and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "findEmployeesOfOrganization", query = "select distinct o.responsible from EmployeePostHolding o where o.commissioner in (select p from Post p where p.organization in :organizations and p.createDate <= :date and p.terminateDate > :date) and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "getOrganizationOfEmployee", query = "select o.commissioner.organization from EmployeePostHolding o where o.responsible = :employee and o.principal = :principal and o.fromDate <= :date and o.toDate > :date") })
public class EmployeePostHolding extends Accountability<Post, Employee> {

	private static final long serialVersionUID = 7390804525640459582L;

	@Column(name = "IS_PRINCIPAL")
	private boolean principal = true;

	EmployeePostHolding() {
	}

	public EmployeePostHolding(Post post, Employee employee, Date date) {
		super(post, employee, date);
	}

	public EmployeePostHolding(Post post, Employee employee, boolean isPrincipal, Date date) {
		super(post, employee, date);
		setPrincipal(isPrincipal);
	}

	/**
	 * 获得某个员工的任职岗位
	 * 
	 * @param employee
	 * @param date
	 * @return
	 */
	public static List<Post> findPostsOfEmployee(Employee employee, Date date) {
		return getRepository().createNamedQuery("getPostsOfEmployee").addParameter("employee", employee).addParameter("date", date).list();
	}

	/**
	 * 获得某个员工的主任职岗位
	 * 
	 * @param employee
	 * @param date
	 * @return
	 */
	public static Post getPrincipalPostByEmployee(Employee employee, Date date) {
		EmployeePostHolding holding = getRepository().createCriteriaQuery(EmployeePostHolding.class).eq("responsible", employee).eq("principal", true).le("fromDate", date).gt("toDate", date)
				.singleResult();
		return holding == null ? null : holding.getCommissioner();
	}

	/**
	 * 查找某个员工的兼职职务
	 * 
	 * @param employee
	 * @param queryDate
	 * @return
	 */
	public static List<Post> findAdditionalPostsByEmployee(Employee employee, Date queryDate) {
		return getRepository().createNamedQuery("getAdditionalPostsOfEmployee").addParameter("employee", employee).addParameter("date", queryDate).addParameter("principal", false).list();
	}

	/**
	 * 获得任职某个岗位的员工
	 * 
	 * @param post
	 * @param date
	 * @return
	 */
	public static List<Employee> findEmployeesOfPost(Post post, Date date) {
		return getRepository().createNamedQuery("getEmployeesOfPost").addParameter("post", post).addParameter("date", date).list();
	}

	/**
	 * 找出在指定日期担任指定职务的所有员工
	 * 
	 * @param job
	 * @param date
	 * @return
	 */
	public static List<Employee> findEmployeesOfJob(Job job, Date date) {
		return getRepository().createNamedQuery("getEmployeesOfJob").addParameter("job", job).addParameter("date", date).list();
	}

	/**
	 * 根据某个员工和某个岗位的责任关系
	 * 
	 * @param post
	 * @param employee
	 * @param date
	 * @return
	 */
	public static EmployeePostHolding getByPostAndEmployee(Post post, Employee employee, Date date) {
		return Accountability.getByCommissionerAndResponsible(EmployeePostHolding.class, post, employee, date);
	}

	/**
	 * 查找某个员工的任职责任信息
	 * 
	 * @param employee
	 * @param date
	 * @return
	 */
	public static List<EmployeePostHolding> getByEmployee(Employee employee, Date date) {
		return getRepository().createCriteriaQuery(EmployeePostHolding.class).eq("responsible", employee).le("fromDate", date).gt("toDate", date).list();
	}

	/**
	 * 查找某个岗位相关的任职责任信息
	 * 
	 * @param post
	 * @param date
	 * @return
	 */
	public static List<EmployeePostHolding> getByPost(Post post, Date date) {
		return getRepository().createCriteriaQuery(EmployeePostHolding.class).eq("commissioner", post).le("fromDate", date).gt("toDate", date).list();
	}

	/**
	 * 取得指定机构的负责人(一般为一人，但也可能有多个，如某些大公司的联合CEO)。
	 * 
	 * @param organization
	 *            机构
	 * @param date
	 *            查询日期
	 * @return 在指定查询日期指定机构的负责人。
	 */
	public static List<Employee> getManagerOfOrganization(Organization organization, Date date) {
		return getRepository().createNamedQuery("getManagerOfOrganization").addParameter("organization", organization).addParameter("principal", true).addParameter("date", date).list();
	}

	/**
	 * 获得某个任职某个岗位的员工人数
	 * 
	 * @param post
	 * @param date
	 * @return
	 */
	public static Long getEmployeeCountOfPost(Post post, Date date) {
		return getRepository().createNamedQuery("getEmployeeCountOfPost").addParameter("post", post).addParameter("date", date).singleResult();
	}

	/**
	 * 获得在某个时间某个机构下的所有员工
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	public static List<Employee> findEmployeesOfOrganization(Organization organization, Date date) {
		Set<Organization> organizations = new HashSet<Organization>();
		organizations.add(organization);
		organizations.addAll(organization.getAllChildren(date));
		return getRepository().createNamedQuery("findEmployeesOfOrganization").addParameter("organizations", organizations).addParameter("date", date).list();
	}

	/**
	 * 获得某个员工的所属机构
	 * 
	 * @param employee
	 * @param date
	 * @return
	 */
	public static Organization getOrganizationOfEmployee(Employee employee, Date date) {
		return getRepository().createNamedQuery("getOrganizationOfEmployee").addParameter("employee", employee).addParameter("principal", true).addParameter("date", date).singleResult();
	}

	/**
	 * 撤销某个机构与某些员工的关系
	 * 
	 * @param organization
	 * @param employees
	 */
	public static void terminateEmployeeOrganizationRelation(Organization organization, Set<Employee> employees) {
		Date now = new Date();
		Set<Post> postsOfOrganization = organization.getPosts(now);

		for (Employee employee : employees) {
			Set<Post> postOfEmployee = employee.getPosts(now);
			postOfEmployee.retainAll(postsOfOrganization);
			for (Post post : postOfEmployee) {
				EmployeePostHolding holding = EmployeePostHolding.getByPostAndEmployee(post, employee, now);
				holding.terminate(now);
			}
		}
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EmployeePostHolding)) {
			return false;
		}
		EmployeePostHolding that = (EmployeePostHolding) other;
		return new EqualsBuilder().append(this.getCommissioner(), that.getCommissioner()).append(this.getResponsible(), that.getResponsible()).append(this.getFromDate(), that.getFromDate())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getCommissioner()).append(getResponsible()).append(getFromDate()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getCommissioner()).append(getResponsible()).build();
	}

}
