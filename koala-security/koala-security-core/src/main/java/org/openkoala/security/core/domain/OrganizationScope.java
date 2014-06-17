package org.openkoala.security.core.domain;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * TODO 不应该在核心模块。目前为了方便测试。
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("ORGANIZATION_SCOPE")
public class OrganizationScope extends Scope {

	private static final long serialVersionUID = 4668728765272500424L;

	@ManyToOne
	@JoinTable(name = "KS_OS__RELATION", //
	joinColumns = @JoinColumn(name = "PARENT_ID"), //
	inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
	private OrganizationScope parent;

	@OneToMany(mappedBy = "parent")
	private Set<OrganizationScope> children;

	public void setParent(OrganizationScope parent) {
		this.parent = parent;
	}

	public void setChildren(Set<OrganizationScope> children) {
		this.children = children;
	}

	@Override
	public Scope getParent() {
		return parent;
	}

	@Override
	public Set<OrganizationScope> getChildren() {
		return children;
	}
}
