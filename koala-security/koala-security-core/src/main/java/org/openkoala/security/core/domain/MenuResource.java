package org.openkoala.security.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 菜单权限资源
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("MENU_RESOURCE")
public class MenuResource extends SecurityResource {

	private static final long serialVersionUID = 2065808982375385340L;

	/**
	 * 菜单图标
	 */
	@Column(name = "MENU_ICON")
	private String menuIcon;

	/**
	 * 用于菜单级别
	 */
	@Column(name = "LEVEL")
	private int level = 0;

	/**
	 * 菜单排序位置号
	 */
	@Column(name = "POSITION")
	private int position = 0;

	@ManyToOne
	@JoinTable(name = "KS_MENU_RESOURCE_RELATION", //
	joinColumns = @JoinColumn(name = "CHILD_ID"), //
	inverseJoinColumns = @JoinColumn(name = "PARENT_ID"))
	private MenuResource parent;

	@OneToMany(mappedBy = "parent")
	private Set<MenuResource> children = new HashSet<MenuResource>();

	protected MenuResource() {}

	public MenuResource(String name) {
		super(name);
	}

	@Override
	public void save() {
		isNameExisted();
		super.save();
	}

	public void addChild(MenuResource child) {
		child.setLevel(level + 1);
		child.save();
		this.children.add(child);
		child.setParent(this);
	}

	public void removeChild(MenuResource child) {
		children.remove(child);
		child.remove();
	}

	/**
	 * TODO 可能删除对象不存在。
	 */
	@Override
	public void remove() {
		for (MenuResource child : children) {
			child.remove();
		}
		super.remove();
	}

	public static MenuResource getBy(Long id) {
		return MenuResource.get(MenuResource.class, id);
	}

	@Override
	public SecurityResource findByName(String name) {
		return getRepository()//
				.createNamedQuery("SecurityResource.findByName")//
				.addParameter("securityResourceType", MenuResource.class)//
				.addParameter("name", name)//
				.addParameter("disabled", false)//
				.singleResult();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getId())//
				.append(getName())//
				.append(getUrl())//
				.build();
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public MenuResource getParent() {
		return parent;
	}

	public void setParent(MenuResource parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	private void setLevel(int level) {
		this.level = level;
	}

	public Set<MenuResource> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	public void setChildren(Set<MenuResource> children) {
		this.children = children;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}