package org.openkoala.security.core.domain;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.CorrelationException;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.NullArgumentException;

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
		@NamedQuery(name = "Authority.findAllAuthoritiesByUserAccount", query = "SELECT _authority FROM Authorization _authorization JOIN  _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.userAccount = :userAccount AND TYPE(_authority) = :authorityType GROUP BY _authority.id"),
		@NamedQuery(name = "Authority.getAuthorityByName", query = "SELECT _authority FROM Authority _authority WHERE TYPE(_authority) = :authorityType AND _authority.name = :name") })
public abstract class Authority extends SecurityAbstractEntity {

	private static final long serialVersionUID = -5570169700634882013L;

	/**
	 * 名称
	 */
	@NotNull
	@Column(name = "NAME")
	private String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	protected Authority() {}

	public Authority(String name) {
		checkArgumentIsNull("name", name);
		isExistedName(name);
		this.name = name;
	}

	@Override
	public void remove() {

        // authority cannot remove it
		if (!Authorization.findByAuthority(this).isEmpty()) {
			throw new CorrelationException("authority has actor, so can't remove authority.");
		}

        //authority can remove it.
        for(ResourceAssignment resourceAssignment : ResourceAssignment.findByAuthority(this))
        {
            resourceAssignment.remove();
        }
		super.remove();
	}

	/*public static Set<MenuResource> findTopMenuResourceByAuthority(Authority authority) {
		List<MenuResource> menuResources = getRepository().createNamedQuery("findTopMenuResourceByAuthority")//
				.addParameter("authorityId", authority.getId())//
				.list();

		return new HashSet<MenuResource>(menuResources);
	}*/

	public abstract Authority getBy(String name);

	/**
	 * 维护多对多关联 为可授权体添加一个权限资源， 需要显示的调用update方法。
	 *
	 * @param securityResource
	 */
	public void addSecurityResource(SecurityResource securityResource) {
        new ResourceAssignment(this,securityResource).save();
	}

	/**
	 * 为可授权体添加多个权限资源，需要显示的调用update方法。
	 *
	 * @param securityResources
	 */
	public void addSecurityResources(List<? extends SecurityResource> securityResources) {
		for (SecurityResource securityResource : securityResources) {
            this.addSecurityResource(securityResource);
		}
	}

	/**
     * @param securityResource
     */
    public void terminateSecurityResource(SecurityResource securityResource) {
        ResourceAssignment resourceAssignment = ResourceAssignment.findByResourceInAuthority(this, securityResource);
        resourceAssignment.remove();
    }

	/**
	 * 维护多对多关系 为可授权体撤销多个权限资源，显示调用save方法。
	 *
	 * @param
	 */
	public void terminateSecurityResources(Set<? extends SecurityResource> securityResources) {
		for(SecurityResource securityResource : securityResources){
            this.terminateSecurityResource(securityResource);
        }
	}

    public static boolean checkHasPageElementResource(Set<Authority> authorities, String identifier) {

		List<Authority> results = getRepository()//
				.createNamedQuery("ResourceAssignment.checkHasSecurityResource")//
				.addParameter("authorities", authorities)//
				.addParameter("securityResourceType", PageElementResource.class)//
				.addParameter("identifier", identifier)//
				.list();
		return !results.isEmpty();
	}


	public void changeName(String name){
		checkArgumentIsNull("name", name);
		if(!name.equals(this.getName())){
			isExistedName(name);
			this.name = name;
			this.save();
		}
	}

	protected static void checkArgumentIsNull(String nullMessage, String argument) {
		if (StringUtils.isBlank(argument)) {
			throw new NullArgumentException(nullMessage);
		}
	}
	
	/*------------- Private helper methods  -----------------*/

	private void isExistedName(String name) {
		if (getBy(name) != null) {
			throw new NameIsExistedException("authority.name.exist");
		}
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

    public List<MenuResource> findMenuResourceByAuthority() {
        return ResourceAssignment.findMenuResourceByAuthority(this);
    }

    public List<UrlAccessResource> findUrlAccessResourceByAuthority(){
        return ResourceAssignment.findUrlAccessResourcesByAuthority(this);
    }

    public static List<MenuResource> findMenuResourceByAuthorities(Set<Authority> authorities) {
        return ResourceAssignment.findMenuResourceByAuthorities(authorities);
    }

    public static <T extends Authority> T getBy(Long authorityId) {
        return (T) Authority.get(Authority.class,authorityId);
    }
}