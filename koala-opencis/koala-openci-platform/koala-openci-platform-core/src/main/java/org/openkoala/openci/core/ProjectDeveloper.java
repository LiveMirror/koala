package org.openkoala.openci.core;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "project_developers")
public class ProjectDeveloper extends AbstractEntity {

	private static final long serialVersionUID = -5833807367997029745L;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "project_developer_role_relations", joinColumns = { @JoinColumn(name = "project_developer_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	
	@OneToOne
	@JoinColumn(name = "developer_id")
	private Developer developer;

	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	ProjectDeveloper() {

	}

	public ProjectDeveloper(Set<Role> roles, Developer developer, Project project) {
		this.roles = roles;
		this.developer = developer;
		this.project = project;
	}

	public void assignRole(Role role) {
		roles.add(role);
		save();
	}

	public Developer getDeveloper() {
		return developer;
	}

	
	public Set<Role> getRoles() {
		return roles;
	}
	
	
	public Project getProject() {
		return project;
	}
	
	

	protected void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	protected void setDeveloper(Developer developer) {
		this.developer = developer;
	}

	protected void setProject(Project project) {
		this.project = project;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProjectDeveloper)) {
			return false;
		}
		ProjectDeveloper that = (ProjectDeveloper) other;
		return new EqualsBuilder().append(getDeveloper(), that.getDeveloper()).append(getRoles(), that.getRoles()).isEquals();

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getDeveloper()).append(getRoles()).hashCode();
	}

	@Override
	public String toString() {
		return getDeveloper().toString();
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
