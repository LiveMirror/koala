package org.openkoala.security.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.security.core.NullArgumentException;

/**
 * 参与者, 它是一个抽象的概念。
 * 是 {@link User} 和  <code>UserGroup </code>的共同基类。
 * 可以对该类进行扩展，以达到您所希望的业务。
 * 可以对其授予角色 {@link Role} 和 权限 {@link Permission}。
 *
 * @author lucas
 */
@Entity
@Table(name = "KS_ACTORS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
public abstract class Actor extends SecurityAbstractEntity {

	private static final long serialVersionUID = -6279345771754150467L;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 最后更新时间
	 */
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFY_TIME")
	private Date lastModifyTime;

	/**
	 * 创建者
	 */
	@Column(name = "CREATE_OWNER")
	private String createOwner;

	/**
	 * 创建时间
	 */
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	private Date createDate = new Date();

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	public Actor() {
	}

	public Actor(String name) {
		checkArgumentIsNull("name", name);
		this.name = name;
	}

	/**
	 * 撤销~级联撤销{@link Authorization }
	 */
	@Override
	public void remove() {
		for (Authorization authorization : Authorization.findByActor(this)) {
			authorization.remove();
		}
		super.remove();
	}

	/**
	 * 在某个范围下{@link Scope}为参与者{@link Actor}授权可授权体{@link Authority}
	 * 
	 * @param authority
	 *            可授权体
	 * @param scope
	 *            范围
	 */
	public void grant(Authority authority, Scope scope) {
        // 有可能授权的时候已经是有了，所以是需要修改的。
        if(Authorization.exists(this,authority)){
            Authorization authorization = Authorization.findByActorInAuthority(this,authority);
            authorization.changeScope(scope);
        }
		if (Authorization.exists(this, authority, scope)) {
			return;
		}
		new Authorization(this, authority, scope).save();
	}

	/**
	 * 为参与者授权可授权体。
	 * 
	 * @param authority
	 */
	public void grant(Authority authority) {
		if (Authorization.exists(this, authority)) {
			return;
		}
		new Authorization(this, authority).save();
	}

	public void terminate(Authority authority) {
		Authorization authorization = Authorization.findByActorInAuthority(this, authority);
		authorization.remove();
	}
	
	/**
	 * 得到在某个范围下{@link Scope}参与者{@link Actor}的所有权限{@link Permission}
	 * 
	 * @param scope
	 *            范围
	 * @return
	 */
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

    public void changeLastModifyTime(){
        this.lastModifyTime = new Date();
    };

	protected static void checkArgumentIsNull(String nullMessage, String argument) {
		if (StringUtils.isBlank(argument)) {
			throw new NullArgumentException(nullMessage);
		}
	}

	/*------------- Private helper methods  -----------------*/

	private Set<Authority> getAuthorities(Scope scope) {
		return Authorization.findAuthoritiesByActorInScope(this, scope);
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name" };
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

	public Date getLastModifyTime() {
		return lastModifyTime;
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

    public void terminateAuthorityInScope(Authority authority, Scope scope) {
        Authorization authorization = Authorization.findByActorOfAuthorityInScope(this,authority,scope);
        authorization.remove();
    }

}
