package org.openkoala.openci.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "tools")
public class Tool extends AbstractEntity {

	private static final long serialVersionUID = -7321784040020043756L;

	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	
	@ManyToOne
	@JoinColumn(name = "tool_configuration_id")
	private ToolConfiguration toolConfiguration;

	
	@Enumerated(EnumType.STRING)
	private ToolIntegrationStatus status;

	
	@OneToMany(mappedBy = "tool", fetch = FetchType.EAGER)
	private Set<ToolInterfaceImplement> toolInterfaceImplements = new HashSet<ToolInterfaceImplement>();

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "integration_date")
	private Date integrationDate = new Date();

	Tool() {
	}
	
	
	protected Project getProject() {
		return project;
	}



	protected void setProject(Project project) {
		this.project = project;
	}



	protected void setToolConfiguration(ToolConfiguration toolConfiguration) {
		this.toolConfiguration = toolConfiguration;
	}



	protected void setStatus(ToolIntegrationStatus status) {
		this.status = status;
	}



	protected void setToolInterfaceImplements(
			Set<ToolInterfaceImplement> toolInterfaceImplements) {
		this.toolInterfaceImplements = toolInterfaceImplements;
	}



	protected void setIntegrationDate(Date integrationDate) {
		this.integrationDate = integrationDate;
	}



	public Tool(ToolConfiguration toolConfiguration, Project project) {
		this.toolConfiguration = toolConfiguration;
		this.project = project;
		this.status = ToolIntegrationStatus.ONGOING;
	}

	public void updateToolIntegrationStatus() {
		status = ToolIntegrationStatus.SUCCESS;
		for (ToolInterfaceImplement each : getToolInterfaceImplements()) {
			if (!each.isSuccess()) {
				status = ToolIntegrationStatus.FAILURE;
			}
		}
		save();
	}

	public ToolConfiguration getToolConfiguration() {
		return toolConfiguration;
	}

	public ToolIntegrationStatus getStatus() {
		return status;
	}

	public Set<ToolInterfaceImplement> getToolInterfaceImplements() {
		return toolInterfaceImplements;
	}

	public Date getIntegrationDate() {
		return integrationDate;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Developer)) {
			return false;
		}
		Tool that = (Tool) other;
		return new EqualsBuilder().append(getToolConfiguration(), that.getToolConfiguration()).append(getIntegrationDate(), that.getIntegrationDate()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getToolConfiguration()).append(getIntegrationDate()).hashCode();
	}

	@Override
	public String toString() {
		return getToolConfiguration().toString();
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
