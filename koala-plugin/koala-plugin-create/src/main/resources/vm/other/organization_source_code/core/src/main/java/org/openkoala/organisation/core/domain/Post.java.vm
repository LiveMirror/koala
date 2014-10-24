package org.openkoala.organisation.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.CriteriaQuery;
import org.openkoala.organisation.core.NameExistException;
import org.openkoala.organisation.core.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.core.PostExistException;
import org.openkoala.organisation.core.TerminateHasEmployeePostException;

@Entity
@DiscriminatorValue("POST")
@NamedQueries({ 
	@NamedQuery(name = "findByOrganization", query = "select o from Post o where o.organization = :organization and o.createDate <= :date and o.terminateDate > :date"),
	@NamedQuery(name = "findByJob", query = "select o from Post o where o.job = :job and o.createDate <= :date and o.terminateDate > :date"),
    @NamedQuery(name = "findCountOfOrganization", query = "SELECT COUNT(p) FROM Post p WHERE p.organization.id = :organizationId AND p.organizationPrincipal = true AND p.createDate <= :queryDate AND p.terminateDate > :queryDate")
})
public class Post extends Party {

	private static final long serialVersionUID = -2205967098970951498L;

	@ManyToOne
	@JoinColumn(name = "ORG_ID")
	private Organization organization;

	@ManyToOne
	@JoinColumn(name = "JOB_ID")
	private Job job;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ORG_PRINCIPAL")
	private boolean organizationPrincipal;

	public Post() {
		super();
	}

	public Post(String name) {
		super(name);
	}

	public Post(String name, String sn) {
		super(name, sn);
	}

	public Post(String name, String sn, Job job, Organization organization) {
		super(name, sn);
		this.job = job;
		this.organization = organization;
	}

	public Long getEmployeeCount(Date date) {
		return EmployeePostHolding.getEmployeeCountOfPost(this, date);
	}

	/**
	 * 查找某个职务的相关岗位
	 * 
	 * @param job
	 * @param date
	 * @return
	 */
	public static List<Post> findByJob(Job job, Date date) {
		return getRepository().createNamedQuery("findByJob").addParameter("job", job).addParameter("date", date).list();
	}

	/**
	 * 查找某个机构的相关岗位
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	public static List<Post> findByOrganization(Organization organization, Date date) {
		return getRepository().createNamedQuery("findByOrganization").addParameter("organization", organization).addParameter("date", date).list();
	}

	/**
	 * 获得岗位下的员工
	 * 
	 * @param date
	 * @return
	 */
	public Set<Employee> getEmployees(Date date) {
		return new HashSet<Employee>(EmployeePostHolding.findEmployeesOfPost(this, date));
	}

	/**
	 * 查询某个机构是否已经有主负责岗位
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	public static boolean hasPrincipalPostOfOrganization(Organization organization, Date date) {
		Long count = getRepository()
                .createNamedQuery("findCountOfOrganization")
                .addParameter("organizationId", organization.getId())
                .addParameter("queryDate", date).singleResult();
		return count > 0;
	}

	@Override
	public void save() {
		if (postExist()) {
			throw new PostExistException();
		}
		if (nameExist()) {
			throw new NameExistException();
		}

		if (organizationPrincipal && hasPrincipalPostOfOrganization(organization, new Date())) {
			if (getId() != null) {
				if (!isOrganizationPrincipalBefore()) {
					throw new OrganizationHasPrincipalYetException();
				}
			} else {
				throw new OrganizationHasPrincipalYetException();
			}
		}
		super.save();
	}

	@Transient
	private boolean isOrganizationPrincipalBefore() {
		return getRepository().get(Post.class, getId()).isOrganizationPrincipal();
	}

	private boolean nameExist() {
		Date now = new Date();
		CriteriaQuery query = getRepository().createCriteriaQuery(Post.class).eq("name", getName()).le("createDate", now).gt("terminateDate", now);

		if (getId() != null) {
			query.notEq("id", getId());
		}
		return query.singleResult() != null;
	}

	private boolean postExist() {
		Date now = new Date();
		CriteriaQuery query = getRepository().createCriteriaQuery(Post.class).eq("organization", organization).eq("job", job).le("createDate", now).gt("terminateDate", now);

		if (getId() != null) {
			query.notEq("id", getId());
		}
		return query.singleResult() != null;
	}

	@Override
	public void terminate(Date date) {
		if (hasEmployee(date)) {
			throw new TerminateHasEmployeePostException();
		}
		super.terminate(date);
	}

	private boolean hasEmployee(Date date) {
		return getEmployeeCount(date) > 0;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOrganizationPrincipal() {
		return organizationPrincipal;
	}

	public void setOrganizationPrincipal(boolean organizationPrincipal) {
		this.organizationPrincipal = organizationPrincipal;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Post)) {
			return false;
		}
		Post that = (Post) other;
		return new EqualsBuilder().append(this.getSn(), that.getSn()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getSn()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
