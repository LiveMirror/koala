package org.openkoala.security.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.CriteriaQuery;
import org.openkoala.security.core.AuthorizationIsNotExisted;
import org.openkoala.security.core.NullArgumentException;

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

	protected Authorization() {}

	public Authorization(Actor actor, Authority authority) {
		if (actor == null) {
			throw new NullArgumentException("actor");
		}
		if (authority == null) {
			throw new NullArgumentException("authority");
		}
		this.actor = actor;
		this.authority = authority;
	}

	public Authorization(Actor actor, Authority authority, Scope scope) {
		this(actor, authority);
		if (scope == null) {
			throw new NullArgumentException("scope");
		}
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
            if(authorization.getAuthority() instanceof Role){
                Role role = (Role)authorization.getAuthority();
                results.addAll(role.getPermissions());
            }
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
		Authorization result = getRepository()//
				.createCriteriaQuery(Authorization.class)//
				.eq("actor", actor)//
				.eq("authority", authority)//
				.singleResult();

		return result;
	}

	public static void checkAuthorization(Actor actor, Authority authority) {
		if (!exists(actor, authority)) {
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
	
	protected static boolean exists(Actor actor, Authority authority) {
		return exists(actor, authority, null);
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
	public String[] businessKeys() {
		return new String[]{"actor","authority"};
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(actor)//
				.append(authority)//
				.append(scope)//
				.build();
	}
	
	public Actor getActor() {
		return actor;
	}

	public Authority getAuthority() {
		return authority;
	}

	public Scope getScope() {
		return scope;
	}
}
