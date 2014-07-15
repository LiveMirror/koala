package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.CriteriaQuery;
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

	public Authorization(Actor actor, Authority authority) {
		this.actor = actor;
		this.authority = authority;
	}

	public Authorization(Actor actor, Authority authority, Scope scope) {
		this.actor = actor;
		this.authority = authority;
		this.scope = scope;
	}

	@Override
	public void save() {
		if (exists(actor, authority, scope)) {
			return;
		}
		super.save();
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

	public static Set<Authority> findAuthoritiesByActor(Actor actor) {
		Set<Authority> results = new HashSet<Authority>();
		Set<Authorization> authorizations = findAuthorizationsByActor(actor);
		for (Authorization authorization : authorizations) {
			results.add(authorization.getAuthority());
		}
		return results;
	}

	/**
	 * @param actor
	 * @param authority
	 * @return
	 */
	public static Authorization findByActorInAuthority(Actor actor, Authority authority) {
		Authorization authorization = getRepository()//
				.createCriteriaQuery(Authorization.class)//
				.eq("actor", actor)//
				.eq("authority", authority)//
				.singleResult();

		return authorization;
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
	/*
	 * public static Set<Permission> findAllPermissionsByUserAccount(User user) { Set<Permission> results = new
	 * HashSet<Permission>(); Set<Authorization> authorizations = findAuthorizationsByActor(user); for (Authorization
	 * authorization : authorizations) { Authority authority = authorization.getAuthority(); if (authority instanceof
	 * Permission) { results.add((Permission) authority); } else { results.addAll(((Role) authority).getPermissions());
	 * } } return results; }
	 */

	public static void checkAuthorization(Actor actor, Authority authority) {
		if (!exists(actor, authority, null)) {
			throw new AuthorizationIsNotExisted();
		}
	}

	public static void checkAuthorization(Actor actor, Authority authority, Scope scope) {
		if (!exists(actor, authority, scope)) {
			throw new AuthorizationIsNotExisted();
		}
	}

	/**
	 * 判断参与者actor是否已经被授予了在某个范围scope下得authority权限
	 * 
	 * @param actor
	 * @param authority
	 * @param scope
	 * @return
	 */
	protected static boolean exists(Actor actor, Authority authority, Scope scope) {

		CriteriaQuery criteriaQuery = new CriteriaQuery(getRepository(), Authorization.class);
		criteriaQuery.eq("actor", actor);
		criteriaQuery.eq("authority", authority);
		if (scope != null) {
			criteriaQuery.eq("scope", scope);
		}

		return criteriaQuery.singleResult() != null;
	}

	private static Set<Authorization> findAuthorizationsByActor(Actor actor) {
		Set<Authorization> results = new HashSet<Authorization>();
		List<Authorization> authorizations = getRepository().createCriteriaQuery(Authorization.class)//
				.eq("actor", actor)//
				.list();

		results.addAll(authorizations);

		return results;
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
	public String[] businessKeys() {
		if (this.getScope() != null) {
			return new String[] { getActor().toString() + getAuthority().toString() + getScope().toString() };
		} else {
			return new String[] { getActor().toString() + getAuthority().toString() };
		}
	}

	@Override
	public String toString() {
		return "Authorization [actor=" + actor + ", authority=" + authority + ", scope=" + scope + "]";
	}

}
