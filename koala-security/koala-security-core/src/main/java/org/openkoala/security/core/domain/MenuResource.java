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
 * 菜单权限资源
 * @author luzhao
 *
 */
@Entity
@DiscriminatorValue("MENU_RESOURCE")
public class MenuResource extends SecurityResource {

	private static final long serialVersionUID = 2065808982375385340L;

	private String icon;

	@ManyToOne
	@JoinTable(name = "KS_MENU_RESOURCE_RELATION", //
	joinColumns = @JoinColumn(name = "PARENT_ID"), //
	inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
	private MenuResource parent;

	@OneToMany(mappedBy = "parent")
	private Set<MenuResource> children = new HashSet<MenuResource>();

	public MenuResource() {
	}

	public MenuResource(String name, boolean isValid, String description, String icon,String url) {
		super(name, isValid, description,url);
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public MenuResource getParent() {
		return parent;
	}

	public void setParent(MenuResource parent) {
		this.parent = parent;
	}

	public Set<MenuResource> getChildren() {
		return children;
	}

	public void setChildren(Set<MenuResource> children) {
		this.children = children;
	}
	
}