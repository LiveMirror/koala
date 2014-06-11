package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 角色。角色是权限的集合。
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("ROLE")
public class Role extends Authority {

	private static final long serialVersionUID = 4327840654680779887L;

	@ManyToMany
	@JoinTable(name = "KS_ROLE_PERMISSION_MAP", //
	joinColumns = @JoinColumn(name = "ROLE_ID"), //
	inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
	private Set<Permission> permissions = new HashSet<Permission>();

	Role() {
	}

	public Role(String name) {
		super(name);
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public static Set<Role> findByUser(User user) {
		Set<Role> results = new HashSet<Role>();
		List<Authorization> authorizations = Authorization.findByActor(user);
		for (Authorization authorization : authorizations) {
			Authority authority = authorization.getAuthority();
			if (authority instanceof Role) {
				results.add((Role) authority);
			}
		}
		return results;
	}

	@Override
	public void update() {
		isExisted();
		Role role = Role.get(Role.class, this.getId());
		role.setName(this.getName());
		role.setDescription(this.getDescription());
	}

	public Set<Authority> findAuthoritiesBy() {
		Set<Authority> results = new HashSet<Authority>();
		results.add(this);
		results.addAll(this.getPermissions());
		return results;
	}

}