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
		@NamedQuery(
				name = "Authority.findAllAuthoritiesByUserAccount", 
				query = "SELECT _authority FROM Authorization _authorization JOIN  _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.userAccount = :userAccount AND TYPE(_authority) = :authorityType ORDER BY _authority.id"),
		@NamedQuery(
				name = "Authority.checkHasSecurityResource", 
				query = "SELECT _authority FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _authority IN (:authorities) AND TYPE(_securityResource) = :securityResourceType  AND _securityResource = :securityResource") 
		})
public abstract class Authority extends SecurityAbstractEntity {

	private static final long serialVersionUID = -5570169700634882013L;

	/**
	 * 名称[用于判断存储中是否已经存在]
	 */
	@Column(name = "NAME")
	private String name;

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

	Authority() {
	}

	public Authority(String name) {
		// Assert.isBlank(name, "名称不能为空");
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

	public Authority getAuthorityBy(String name) {
		Authority authority = getRepository().createCriteriaQuery(Authority.class)//
				.eq("name", this.name)//
				.singleResult();
		return authority != null ? authority : null;
	}

	@Override
	public final void save() {
		// isExisted();
		super.save();
	}

	protected void isExisted() {
		if (isExistName(this.name)) {
			throw new NameIsExistedException("name.exist");
		}
	}

	private boolean isExistName(String name) {
		return getAuthorityBy(name) != null;
	}

	public abstract void update();

	@Override
	public void remove() {
		for (Authorization authorization : Authorization.findByAuthority(this)) {
			authorization.remove();
		}
		super.remove();
	}

	public void addSecurityResource(SecurityResource securityResource) {
		this.securityResources.add(securityResource);
	}

	public void addSecurityResources(List<? extends SecurityResource> securityResources) {
		this.securityResources.addAll(securityResources);
	}

	public void terminateSecurityResource(SecurityResource securityResource) {
		this.securityResources.remove(securityResource);
	}

	public void terminateSecurityResources(List<? extends SecurityResource> securityResources) {
		this.securityResources.removeAll(securityResources);
	}
	
	public static boolean checkHasPageElementResource(Set<Authority> authorities,
			PageElementResource pageElementResource) {
		List<Authority> results = getRepository()//
				.createNamedQuery("Authority.checkHasSecurityResource")//
				.addParameter("authorities", authorities)//
				.addParameter("securityResourceType", PageElementResource.class)//
				.addParameter("securityResource", pageElementResource)//
				.list();
		return results.size() > 0 ? true : false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "Authority [name=" + name + ", description=" + description + "]";
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name", "description" };
	}
}