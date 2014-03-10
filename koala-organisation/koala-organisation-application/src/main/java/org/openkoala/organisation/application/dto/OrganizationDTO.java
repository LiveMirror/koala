package org.openkoala.organisation.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openkoala.organisation.domain.Company;
import org.openkoala.organisation.domain.Department;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Organization;

/**
 * 组织机构DTO
 * 
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Oct 9, 2013 3:35:10 PM
 */
public class OrganizationDTO implements Serializable {

	private static final long serialVersionUID = -6538960709126681092L;

	public static final String COMPANY = "company";
	public static final String DEPARTMENT = "department";

	private Long id;

	private String name;

	private String description;

	private Date createDate;

	private Date terminateDate;

	private Set<OrganizationDTO> children = new HashSet<OrganizationDTO>();

	private String organizationType;

	private String sn;

	private String principalName;

	private int version;

	private Long pid;

	public OrganizationDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getTerminateDate() {
		return terminateDate;
	}

	public void setTerminateDate(Date terminateDate) {
		this.terminateDate = terminateDate;
	}

	public Set<OrganizationDTO> getChildren() {
		return children;
	}

	public void setChildren(Set<OrganizationDTO> children) {
		this.children = children;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public static OrganizationDTO generateDtoBy(Organization organization) {
		OrganizationDTO dto = new OrganizationDTO(organization.getId(), organization.getName());
		dto.setSn(organization.getSn());
		dto.setCreateDate(organization.getCreateDate());
		dto.setDescription(organization.getDescription());
		dto.setVersion(organization.getVersion());

		if (organization instanceof Company) {
			dto.setOrganizationType(COMPANY);
		} else {
			dto.setOrganizationType(DEPARTMENT);
		}

		List<Employee> principals = organization.getPrincipal(new Date());
		if (!principals.isEmpty()) {
			String separator = "/";
			StringBuilder principal = new StringBuilder();
			for (Employee employee : principals) {
				principal.append(employee.getName());
				principal.append(separator);
			}
			dto.setPrincipalName(principal.substring(0, principal.length() - separator.length()));
		}

		return dto;
	}

	public OrganizationDTO(Long id, Long pid, String name, String sn, Date createDate, String description, String organizationType) {
		this.setId(id);
		this.setPid(pid);
		this.setName(name);
		this.setSn(sn);
		this.setCreateDate(createDate);
		this.setDescription(description);
		this.setOrganizationType(organizationType);
	}

	public Organization transFormToOrganization() {
		Organization result = null;
		if (organizationType.equals(COMPANY)) {
			result = new Company(name, sn);
		} else {
			result = new Department(name, sn);
		}

		result.setId(id);
		result.setCreateDate(createDate);
		result.setSn(sn);
		result.setDescription(description);
		result.setTerminateDate(terminateDate);
		result.setVersion(version);

		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OrganizationDTO)) {
			return false;
		}
		OrganizationDTO that = (OrganizationDTO) other;
		return new EqualsBuilder().append(this.getId(), that.getId()).append(this.getSn(), that.getSn()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getSn()).toHashCode();
	}

	@Override
	public String toString() {
		return "OrganizationDTO [id=" + id + ", name=" + name + ", children=" + children + "]";
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

}
