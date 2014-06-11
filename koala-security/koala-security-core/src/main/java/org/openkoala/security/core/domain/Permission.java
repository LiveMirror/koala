package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 * 权限。代表系统的一项操作或功能。
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("PERMISSION")
public class Permission extends Authority {

	private static final long serialVersionUID = 4631351008490511334L;

	@ManyToMany(mappedBy = "permissions", cascade = CascadeType.REMOVE)
	private Set<Role> roles = new HashSet<Role>();

	public Permission() {
	}

	public Permission(String name, String description) {
		super(name, description);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public void update() {
		isExisted();
		Permission permission = Role.get(Permission.class, this.getId());
		permission.setName(this.getName());
		permission.setDescription(this.getDescription());
	}

}