package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openkoala.security.core.AuthorizationIsNotExisted;

/**
 * 授权，在指定范围内将某种权限（Permission）或权限集合（Role）授予Actor
 * 
 * @author luzhao
 * 
 */
@Entity
@Table(name = "KS_AUTHORIZATIONS")
public class Authorization extends SecurityAbstractEntity {

	private static final long serialVersionUID = -7604610067031217444L;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ACTOR_ID")
	private Actor actor;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "AUTHORITY_ID")
	private Authority authority;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "SCOPE_ID")
	private Scope scope;

	Authorization() {
	}

	public Authorization(Actor actor, Authority authority, Scope scope) {
		this.actor = actor;
		this.authority = authority;
		this.scope = scope;
	}

	public static List<Authorization> findByActor(Actor actor) {
		return getRepository().createCriteriaQuery(Authorization.class) //
				.eq("actor", actor) //
				.list();
	}

	public static List<Authorization> findByAuthority(Authority authority) {
		return getRepository().createCriteriaQuery(Authorization.class)//
				.eq("authority", authority)//
				.list();
	}

	/**
	 * 判断参与者actor是否已经被授予了在某个范围scope下得authority权限
	 * 
	 * @param actor
	 * @param authority
	 * @param scope
	 * @return
	 */
	public static boolean exists(Actor actor, Authority authority, Scope scope) {

		Authorization authorization = getRepository().createCriteriaQuery(Authorization.class) //
				.eq("actor", actor) //
				.eq("authority", authority) //
				.eq("scope", scope) //
				.singleResult();

		return authorization != null;
	}

	public static Set<Authority> findAuthoritiesByActorInScope(Actor actor, Scope scope) {

		Set<Authority> results = new HashSet<Authority>();

		Set<Authorization> authorizations = findAuthorizationsByActor(actor);

		for (Authorization authorization : authorizations) {
			if (authorization.getScope().contains(scope)) {
				results.add(authorization.getAuthority());
			}
		}

		return results;
	}

	private static Set<Authorization> findAuthorizationsByActor(Actor actor) {
		Set<Authorization> results = new HashSet<Authorization>();
		List<Authorization> authorizations = getRepository().createCriteriaQuery(Authorization.class)//
				.eq("actor", actor)//
				.list();

		results.addAll(authorizations);

		return results;
	}

	@Override
	public void save() {
		if (exists(actor, authority, scope)) {
			return;
		}
		super.save();
	}

	@Override
	public String[] businessKeys() {
		return new String[] { getActor().toString() + getAuthority().toString() + getScope().toString() };
	}

	public static Set<Authority> findAuthoritiesByActor(User user) {
		Set<Authority> results = new HashSet<Authority>();
		Set<Authorization> authorizations = findAuthorizationsByActor(user);
		for (Authorization authorization : authorizations) {
			results.add(authorization.getAuthority());
		}
		return results;
	}

	/**
	 * <pre>
	 * 1、获取本身的Permission。
	 * 2、获取Role中的所有Permission。
	 * </pre>
	 * 
	 * @param user
	 * @return
	 */
	/*public static Set<Permission> findAllPermissionsByUserAccount(User user) {
		Set<Permission> results = new HashSet<Permission>();
		Set<Authorization> authorizations = findAuthorizationsByActor(user);
		for (Authorization authorization : authorizations) {
			Authority authority = authorization.getAuthority();
			if (authority instanceof Permission) {
				results.add((Permission) authority);
			} else {
				results.addAll(((Role) authority).getPermissions());
			}
		}
		return results;
	}*/

	/**
	 * TODO 异常信息
	 * 
	 * @param user
	 * @param role
	 */
	public static void checkAuthorization(User user, Role role) {
		if (!exists(user, role, null)) {
			throw new AuthorizationIsNotExisted();
		}
	}
	
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "Authorization [actor=" + actor + ", authority=" + authority + ", scope=" + scope + "]";
	}

}
