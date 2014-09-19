package org.openkoala.organisation.facade.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 职务DTO
 */
public class JobDTO implements Serializable {

	private static final long serialVersionUID = -6538960709126681092L;

	private Long id;

	private String name;

	private Date createDate;

	private Date terminateDate;

	private String sn;

	private int version;

	private String description;
	
	public JobDTO() {}

	public JobDTO(Long id, String name, Date createDate, Date terminateDate, String sn, int version, String description) {
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.terminateDate = terminateDate;
		this.sn = sn;
		this.version = version;
		this.description = description;
	}

	public JobDTO(Long id, String name, String sn) {
		this.id = id;
		this.name = name;
		this.sn = sn;
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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof JobDTO)) {
			return false;
		}
		JobDTO that = (JobDTO) other;
		return new EqualsBuilder().append(this.getId(), that.getId()).append(this.getSn(), that.getSn()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getSn()).toHashCode();
	}

	@Override
	public String toString() {
		return "JobDTO [id=" + id + ", name=" + name + "]";
	}

}
