package org.openkoala.security.core.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.NameIsExistedException;

/**
 * 可授权实体，代表某种权限（Permission）或权限集合（Role），可被授予Actor。
 * 
 * @author luzhao
 * 
 */
@Entity
@Table(name = "KS_AUTHORITIES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
		@NamedQuery(name = "Authority.findAllAuthoritiesByUserAccount", query = "SELECT _authority FROM Authorization _authorization JOIN  _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.userAccount = :userAccount AND TYPE(_authority) = :authorityType ORDER BY _authority.id"),
		@NamedQuery(name = "Authority.checkHasSecurityResource", query = "SELECT _authority FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _authority IN (:authorities) AND TYPE(_securityResource) = :securityResourceType  AND _securityResource = :securityResource"),
		@NamedQuery(name = "Authority.getAuthorityByName", query = "SELECT _authority FROM Authority _authority WHERE TYPE(_authority) = :authorityType AND _authority.name = :name") })
public abstract class Authority extends SecurityAbstractEntity {

	private static final long serialVersionUID = -5570169700634882013L;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	protected String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToMany
	@JoinTable(name = "KS_AS_MAP", //
	joinColumns = @JoinColumn(name = "AUTHORITY_ID"), //
	inverseJoinColumns = @JoinColumn(name = "SECURITYRESOURCE_ID"))
	private Set<SecurityResource> securityResources = new HashSet<SecurityResource>();

	protected Authority() {
	}

	public Authority(String name) {
		this.name = name;
	}

	public void addSecurityResource(SecurityResource... securityResource) {
		this.securityResources.addAll(Arrays.asList(securityResource));
	}

	public static Set<MenuResource> findMenuResourceByAuthorities(Set<? extends Authority> authorities) {
		Set<MenuResource> results = new HashSet<MenuResource>();

		for (Authority authority : authorities) {
			results.addAll(findMenuResourceByAuthority(authority));
		}

		return results;
	}

	public static Set<MenuResource> findMenuResourceByAuthority(Authority authority) {
		Set<MenuResource> results = new HashSet<MenuResource>();
		if (authority instanceof Role) {
			Role role = (Role) authority;
			Set<Permission> permissions = role.getPermissions();
			results.addAll(findMenuResourceByAuthorities(permissions));
		}
		Set<SecurityResource> securityResources = authority.getSecurityResources();
		for (SecurityResource securityResource : securityResources) {
			if (securityResource instanceof MenuResource) {
				results.add((MenuResource) securityResource);
			}
		}
		return results;
	}

	public static Set<MenuResource> findTopMenuResourceByAuthority(Authority authority) {
		List<MenuResource> menuResources = getRepository().createNamedQuery("findTopMenuResourceByAuthority")//
				.addParameter("authorityId", authority.getId())//
				.list();

		return new HashSet<MenuResource>(menuResources);
	}

	public abstract Authority getAuthorityBy(String name);

	protected void isNameExisted() {
		if (isExistName(this.name)) {
			throw new NameIsExistedException("authority.name.exist");
		}
	}

	public abstract void update();

	@Override
	public void remove() {
		for (Authorization authorization : Authorization.findByAuthority(this)) {
			authorization.remove();
		}
		super.remove();
	}

	/**
	 * 为可授权体添加一个权限资源， 需要显示的调用update方法。
	 * 
	 * @param securityResource
	 */
	public void addSecurityResource(SecurityResource securityResource) {
		this.securityResources.add(securityResource);
		this.update();
	}

	/**
	 * 为可授权体添加多个权限资源，需要显示的调用update方法。
	 * 
	 * @param securityResources
	 */
	public void addSecurityResources(List<? extends SecurityResource> securityResources) {
		this.securityResources.addAll(securityResources);
		this.update();
	}

	/**
	 * 为可授权体撤销一个权限资源，需要显示的调用update方法。
	 * 
	 * @param securityResource
	 */
	public void terminateSecurityResource(SecurityResource securityResource) {
		this.securityResources.remove(securityResource);
		this.update();
	}

	/**
	 * 为可授权体撤销多个权限资源，显示调用update方法。
	 * 
	 * @param securityResources
	 */
	public void terminateSecurityResources(List<? extends SecurityResource> securityResources) {
		this.securityResources.removeAll(securityResources);
		this.update();
	}

	public static boolean checkHasPageElementResource(Set<Authority> authorities,
			PageElementResource pageElementResource) {
		List<Authority> results = getRepository()//
				.createNamedQuery("Authority.checkHasSecurityResource")//
				.addParameter("authorities", authorities)//
				.addParameter("securityResourceType", PageElementResource.class)//
				.addParameter("securityResource", pageElementResource)//
				.list();
		return results.isEmpty() ? false : true;
	}

	/*------------- Private helper methods  -----------------*/

	private boolean isExistName(String name) {
		return getAuthorityBy(name) != null;
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name" };
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(name)//
				.append(description)//
				.build();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<SecurityResource> getSecurityResources() {
		return Collections.unmodifiableSet(securityResources);
	}

	public void setSecurityResources(Set<SecurityResource> securityResources) {
		this.securityResources = securityResources;
	}
}