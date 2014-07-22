package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.StringUtils;

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

	/**
	 * 查询Role需要级联的查询出Permission
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "KS_ROLE_PERMISSION_MAP", //
	joinColumns = @JoinColumn(name = "ROLE_ID"), //
	inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
	private Set<Permission> permissions = new HashSet<Permission>();

	Role() {
	}

	public Role(String name) {
		super(name);
	}

	@Override
	public void save() {
		isNameExisted();
		super.save();
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
		Role role = getBy(this.getId());

		if (!StringUtils.isBlank(this.getName()) && !role.getName().equals(this.getName())) {
			isNameExisted();
			role.name = this.getName();
		}
		role.setDescription(this.getDescription());
	}

	public Set<Authority> findAuthoritiesBy() {
		Set<Authority> results = new HashSet<Authority>();
		results.add(this);
		results.addAll(this.getPermissions());
		return results;
	}

	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}

	public void addPermissions(List<Permission> permissions) {
		this.permissions.addAll(permissions);
	}

	public void terminatePermission(Permission permission) {
		this.permissions.remove(permission);
	}

	public void terminatePermissions(List<Permission> permissions) {
		this.permissions.removeAll(permissions);
	}

	public static List<Role> findAll(){
		return Role.findAll(Role.class);
	}
	public static Role getBy(String name) {
		return getRepository()//
				.createCriteriaQuery(Role.class)//
				.eq("name", name)//
				.singleResult();
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public Authority getAuthorityBy(String name) {
		return getRepository()//
				.createNamedQuery("Authority.getAuthorityByName")//
				.addParameter("authorityType", Role.class)//
				.addParameter("name", name)//
				.singleResult();
	}

	public static Role getBy(Long id) {
		return Role.get(Role.class, id);
	}

}
