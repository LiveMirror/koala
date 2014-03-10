package org.openkoala.organisation.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.CriteriaQuery;
import org.openkoala.organisation.NameExistException;
import org.openkoala.organisation.OrganizationHasPrincipalYetException;
import org.openkoala.organisation.PostExistException;
import org.openkoala.organisation.TerminateHasEmployeePostException;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue("Post")
@NamedQueries({
	@NamedQuery(name = "findByOrganization", query = "select o from Post o where o.organization = :organization and o.createDate <= :date and o.terminateDate > :date"),
	@NamedQuery(name = "findByJob", query = "select o from Post o where o.job = :job and o.createDate <= :date and o.terminateDate > :date")})
public class Post extends Party {

	private static final long serialVersionUID = -2205967098970951498L;

	
	private Organization organization;

	
	private Job job;

	
	private String description;

	
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

	@ManyToOne
	@JoinColumn(name = "org_id")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@ManyToOne
	@JoinColumn(name = "job_id")
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "org_principal")
	public boolean isOrganizationPrincipal() {
		return organizationPrincipal;
	}

	public void setOrganizationPrincipal(boolean organizationPrincipal) {
		this.organizationPrincipal = organizationPrincipal;
	}

	public Long getEmployeeCount(Date date) {
		return EmployeePostHolding.getEmployeeCountOfPost(this, date);
	}
	
	/**
	 * 查找某个职务的相关岗位
	 * @param job
	 * @param date
	 * @return
	 */
	public static List<Post> findByJob(Job job, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("job", job);
		params.put("date", date);
		return getRepository().createNamedQuery("findByJob").addParameter("job", job).addParameter("date", date).list();
		
	}
	
	/**
	 * 查找某个机构的相关岗位
	 * @param organization
	 * @param date
	 * @return
	 */
	public static List<Post> findByOrganization(Organization organization, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organization", organization);
		params.put("date", date);
		return getRepository().createNamedQuery("findByOrganization").addParameter("organization", organization).addParameter("date", date).list();
			
	}

	/**
	 * 获得岗位下的员工
	 * @param date
	 * @return
	 */
	public Set<Employee> getEmployees(Date date) {
		return new HashSet<Employee>(EmployeePostHolding.findEmployeesOfPost(this, date));
	}

	/**
	 * 查询某个机构是否已经有主负责岗位
	 * @param organization
	 * @param date
	 * @return
	 */
	public static boolean hasPrincipalPostOfOrganization(Organization organization, Date date) {
		List<Post> posts = getRepository().createCriteriaQuery(Post.class)
				.eq("organization", organization)
				.eq("organizationPrincipal", true)
				.le("createDate", date)
				.gt("terminateDate", date).list();
		return posts.isEmpty() ? false : true;
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
		CriteriaQuery query = getRepository().createCriteriaQuery(Post.class).eq("name", getName())
			.le("createDate", now)
			.gt("terminateDate", now);
		
		if (getId() != null) {
			query.notEq("id", getId());
		}
		
		Post post =query.singleResult();
		return post != null;
	}
	
	private boolean postExist() {
		Date now = new Date();
		CriteriaQuery query = getRepository().createCriteriaQuery(Post.class).eq("organization", organization)
			.eq("job", job)
			.le("createDate", now)
			.gt("terminateDate", now);
		
		if (getId() != null) {
			query.notEq("id", getId());
		}
		
		Post post = query.singleResult();
		return post != null;
	}
	
	@Override
	public void terminate(Date date) {
		if (hasEmployee(date)) {
			throw new TerminateHasEmployeePostException();
		}
		super.terminate(date);
	}
	
	private boolean hasEmployee(Date date) {
		List<EmployeePostHolding> holdings = EmployeePostHolding.getByPost(this, date);
		return holdings.isEmpty() ? false : true;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Post)) {
			return false;
		}
		Post that = (Post) other;
		return new EqualsBuilder().append(this.getSn(), that.getSn())
				.isEquals();
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
