package org.openkoala.openci.core;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.AbstractEntity;
import org.openkoala.openci.EntityNullException;

@Entity
@Table(name = "projects")
public class Project extends AbstractEntity {

	private static final long serialVersionUID = -1381157577442931544L;

	private String name;

	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "project_detail_id")
	private ProjectDetail projectDetail;

	
	@Enumerated(EnumType.STRING)
	@Column(name = "project_status")
	private ProjectStatus projectStatus;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
	private Set<ProjectDeveloper> developers = new HashSet<ProjectDeveloper>();

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
	private Set<Tool> tools = new HashSet<Tool>();

	
	@SuppressWarnings("deprecation")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate = new Date();

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	public void addTool(Tool tool) {
		if (tool == null) {
			throw new EntityNullException();
		}
		tools.add(tool);
		save();
	}
	
	public void updateProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
		save();
	}

	public static boolean isExixtByName(String name) {
		List<Project> projects = getRepository().createCriteriaQuery(Project.class).eq("name", name).list();
		return projects.size() > 0;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDevelopers(Set<ProjectDeveloper> developers) {
		this.developers = developers;
	}

	public void setTools(Set<Tool> tools) {
		this.tools = tools;
	}

	public Set<ProjectDeveloper> getDevelopers() {
		return developers;
	}

	public Set<Tool> getTools() {
		return tools;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public ProjectDetail getProjectDetail() {
		return projectDetail;
	}

	public void setProjectDetail(ProjectDetail projectDetail) {
		this.projectDetail = projectDetail;
	}

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	

	protected void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Project)) {
			return false;
		}
		Project that = (Project) other;
		return new EqualsBuilder().append(getName(), that.getName()).append(getCreateDate(), that.getCreateDate()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(getCreateDate()).hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
