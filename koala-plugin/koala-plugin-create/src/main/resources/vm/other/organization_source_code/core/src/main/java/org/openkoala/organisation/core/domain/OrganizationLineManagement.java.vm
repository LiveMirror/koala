package org.openkoala.organisation.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 机构间直线责任关系
 * 
 * @author xmfang
 * 
 */
@Entity
@DiscriminatorValue("ORGANIZATIONLINEMANAGEMENT")
@NamedQueries({
		@NamedQuery(name = "getParentOfOrganization", query = "select o.commissioner from OrganizationLineManagement o where o.responsible = :organization and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "findChildrenOfOrganization", query = "select o.responsible from OrganizationLineManagement o where o.commissioner = :organization and o.fromDate <= :date and o.toDate > :date"),
		@NamedQuery(name = "findByResponsible", query = "select o from OrganizationLineManagement o where o.responsible = :organization and o.fromDate <= :date and o.toDate > :date") })
public class OrganizationLineManagement extends Accountability<Organization, Organization> {

	private static final long serialVersionUID = 7390804525640459582L;

	OrganizationLineManagement() {
	}

	public OrganizationLineManagement(Organization parent, Organization child, Date date) {
		super(parent, child, date);
	}

	/**
	 * 获得某个机构的父机构
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	public static Organization getParentOfOrganization(Organization organization, Date date) {
		return getRepository().createNamedQuery("getParentOfOrganization").addParameter("organization", organization).addParameter("date", date).singleResult();
	}

	/**
	 * 获得某个机构的子机构
	 * 
	 * @param organization
	 * @param date
	 * @return
	 */
	public static List<Organization> findChildrenOfOrganization(Organization organization, Date date) {
		return getRepository().createNamedQuery("findChildrenOfOrganization").addParameter("organization", organization).addParameter("date", date).list();
	}

	/**
	 * 获得所有机构
	 * 
	 * @return
	 */
	public static List<OrganizationLineManagement> findAll() {
		return getRepository().findAll(OrganizationLineManagement.class);
	}

	/**
	 * 获得某个机构作为下级机构的机构责任关系
	 * 
	 * @param responsible
	 * @param date
	 * @return
	 */
	public static OrganizationLineManagement getByResponsible(Organization responsible, Date date) {
		return getRepository().createNamedQuery("findByResponsible").addParameter("organization", responsible).addParameter("date", date).singleResult();
	}

	/**
	 * 获得顶级机构
	 * 
	 * @param queryDate
	 * @return
	 */
	public static Organization getTopOrganization(Date queryDate) {
		OrganizationLineManagement organizationLineManagement = getRepository().createCriteriaQuery(OrganizationLineManagement.class).isNull("commissioner").gt("toDate", queryDate)
				.le("fromDate", queryDate).singleResult();
		return organizationLineManagement == null ? null : organizationLineManagement.getResponsible();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OrganizationLineManagement)) {
			return false;
		}
		OrganizationLineManagement that = (OrganizationLineManagement) other;
		return new EqualsBuilder().append(this.getCommissioner(), that.getCommissioner()).append(this.getResponsible(), that.getResponsible()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getCommissioner()).append(getResponsible()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getCommissioner()).append(getResponsible()).build();
	}

}
