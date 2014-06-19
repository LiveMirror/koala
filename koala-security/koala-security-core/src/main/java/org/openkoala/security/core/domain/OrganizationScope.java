package org.openkoala.security.core.domain;

import java.util.HashSet;
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
	joinColumns = @JoinColumn(name = "CHILD_ID"), //
	inverseJoinColumns = @JoinColumn(name = "PARENT_ID"))
	private OrganizationScope parent;

	@OneToMany(mappedBy = "parent")
	private Set<OrganizationScope> children = new HashSet<OrganizationScope>();

	OrganizationScope() {
	}
	
	public OrganizationScope(String name) {
		super(name);
	}

	/**
	 * XXX 维护方为parent 待确定
	 * */
	public void addChild(OrganizationScope child) {
		child.setLevel(this.getLevel() + 1);
		child.save();
		children.add(child);
		child.setParent(this);
	}

	/**
	 * XXX 维护方为parent 待确定
	 * */
	public void removeChild(OrganizationScope child) {
		children.remove(child);
		// child.setParent(null);
		child.remove();
	}

	@Override
	public void remove() {
		for (OrganizationScope child : children) {
			child.remove();
		}
		super.remove();
	}
	
	@Override
	public void update() {
		OrganizationScope organizationScope = OrganizationScope.get(OrganizationScope.class, this.getId());
		organizationScope.setName(this.getName());
		organizationScope.setDescription(this.getDescription());
	}

	@Override
	public OrganizationScope getParent() {
		return parent;
	}

	public void setParent(OrganizationScope parent) {
		this.parent = parent;
	}

	@Override
	public Set<OrganizationScope> getChildren() {
		return children;
	}

	public void setChildren(Set<OrganizationScope> children) {
		this.children = children;
	}

}
