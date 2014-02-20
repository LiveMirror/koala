package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.dayatang.utils.DateUtils;

/**
 * 角色实体类
 * 
 * @author lingen
 * 
 */
@Entity
public class Role extends Identity {

	private static final long serialVersionUID = -8345993710464457036L;

	
	private String roleDesc;

	@Column(name = "ROLE_DESC")
	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
	public Role() {
		
	}
	
	public Role(String name, String desc) {
		this.setAbolishDate(DateUtils.MAX_DATE);
		this.setCreateDate(new Date());
		this.setName(name);
		this.roleDesc = desc;
	}

	/**
	 * 为角色分配用户
	 * @param user
	 */
	public void assignUser(User user) {
		RoleUserAuthorization roleUserAssignment = new RoleUserAuthorization();
		roleUserAssignment.setCreateDate(new Date());
		roleUserAssignment.setAbolishDate(DateUtils.MAX_DATE);
		roleUserAssignment.setRole(this);
		roleUserAssignment.setUser(user);
		roleUserAssignment.save();
	}

	/**
	 * 为角色分配资源
	 * @param resource
	 */
	public void assignResource(Resource resource) {
		IdentityResourceAuthorization idres = new IdentityResourceAuthorization();
		idres.setIdentity(this);
		idres.setResource(resource);
		idres.setAbolishDate(DateUtils.MAX_DATE);
		idres.setCreateDate(new Date());
		idres.setScheduledAbolishDate(new Date());
		idres.save();
	}

	/**
	 * 废除角色所关联的资源
	 * @param resource
	 */
	public void abolishResource(Resource resource) {
		String jpql = "select m from IdentityResourceAuthorization m where m.identity.id=:identityId and " //
				+ "m.resource.id=:resourceId and m.abolishDate>:abolishDate";
		
		
		List<IdentityResourceAuthorization> authorizations = getRepository().createJpqlQuery(jpql)
				.addParameter("identityId", this.getId()).addParameter("resourceId", resource.getId()).addParameter("abolishDate", new Date()).list();
				
			
		for (IdentityResourceAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}
	}

	/**
	 * 废除角色所关联的用户
	 * @param user
	 */
	public void abolishUser(User user) {
		String jpql ="select m from RoleUserAuthorization m where m.user.id=:userId and m.role.id=:roleId and m.abolishDate>:abolishDate";
		
		List<RoleUserAuthorization> authorizations = getRepository().createJpqlQuery(jpql).addParameter("userId", user.getId())
				.addParameter("roleId", getId()).addParameter("abolishDate", new Date()).list();
		for (RoleUserAuthorization authorization : authorizations) {
			authorization.setAbolishDate(new Date());
		}
	}
	
	/**
	 * 根据角色名称查找角色
	 * @param roleName
	 * @return
	 */
	public static Role findRoleByName(String roleName){
		return getRepository().createNamedQuery("findRoleByName").addParameter("roleName", roleName).addParameter("abolishDate", new Date()).singleResult();
	}

	/**
	 * 查找用户所拥有的角色
	 * @param userAccount
	 * @return
	 */
	public static List<Role> findRoleByUserAccount(String userAccount) {
		return getRepository().createNamedQuery("findRoleByUserAccount").addParameter("userAccount", userAccount).addParameter("abolishDate", new Date()).list();
	}

	/**
	 * 角色是否存在
	 * 
	 * @return
	 */
	@Transient
	public boolean isRoleExist() {
		return !getRepository().createNamedQuery("isRoleExist").addParameter("name", getName()).addParameter("abolishDate", new Date()).list().isEmpty();
		
	}
	
	/**
	 * 获取角色的资源授权
	 */
	public List<IdentityResourceAuthorization> findIdentityResourceAuthorizations() {
		return IdentityResourceAuthorization.findAuthorizationByRole(getId());
	}
	
	/**
	 * 获取用户角色的授权
	 * @return
	 */
	public List<RoleUserAuthorization> findRoleUserAuthorizations() {
		return RoleUserAuthorization.findUserAuthorizationByRole(getId());
	}
	
	public static List<Role> findAllRoles() {
		
		return getRepository().createNamedQuery("findAllRoles").addParameter("abolishDate", new Date()).list();
	}

	@Override
	public String[] businessKeys() {
		return new String[]{getName(),getAbolishDate().toString()};
	}

	@Override
	public String toString() {
		return "Role [roleDesc=" + roleDesc + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result
				+ ((getAbolishDate() == null) ? 0 : getAbolishDate().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Role))
			return false;
		Role other = (Role) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		
		if (getAbolishDate() == null) {
			if (other.getAbolishDate() != null)
				return false;
		} else if (!getAbolishDate().equals(other.getAbolishDate()))
			return false;
		
		return true;
	}
	
	
}
