package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.List;
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

	Permission() {
	}

	public Permission(String name) {
		super(name);
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

	/**
	 * TODO 是否直接写JPQL查询?
	 * 
	 * @param user
	 * @return
	 */
	public static Set<Permission> findByUser(User user) {
		Set<Permission> results = new HashSet<Permission>();
		List<Authorization> authorizations = Authorization.findByActor(user);
		for (Authorization authorization : authorizations) {
			Authority authority = authorization.getAuthority();
			if (authority instanceof Permission) {
				results.add((Permission) authority);
			}
		}
		return results;
	}

}