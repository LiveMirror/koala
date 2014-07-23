package org.openkoala.security.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * 参与者，是User和UserGroup的共同基类，可以对Actor授予角色与权限
 * 
 * TODO 扩展UserGroup
 * 
 * @author luzhao
 * 
 */
@Entity
@Table(name = "KS_ACTORS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
public abstract class Actor extends SecurityAbstractEntity {

	private static final long serialVersionUID = -6279345771754150467L;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LAST_MODIFY_TIME")
	private Date lastModifyTime;

	@Column(name = "CREATE_OWNER")
	private String createOwner;

	@Column(name = "CREATE_DATE")
	private Date createDate = new Date();

	@Column(name = "DESCRIPTION")
	private String description;

	@Override
	public void remove() {
		for (Authorization authorization : Authorization.findByActor(this)) {
			authorization.remove();
		}
		super.remove();
	}

	public void grant(Authority authority, Scope scope) {
		if (Authorization.exists(this, authority, scope)) {
			return;
		}
		new Authorization(this, authority, scope).save();
	}

	public Set<Permission> getPermissions(Scope scope) {
		Set<Permission> results = new HashSet<Permission>();
		for (Authority authority : getAuthorities(scope)) {
			if (authority instanceof Permission) {
				results.add((Permission) authority);
			} else {
				Role role = (Role) authority;
				results.addAll(role.getPermissions());
			}
		}
		return results;
	}

	/*------------- Private helper methods  -----------------*/
	
	private Set<Authority> getAuthorities(Scope scope) {
		return Authorization.findAuthoritiesByActorInScope(this, scope);
	}

	public abstract void update();
	
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

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	protected void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getCreateOwner() {
		return createOwner;
	}

	public void setCreateOwner(String createOwner) {
		this.createOwner = createOwner;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
