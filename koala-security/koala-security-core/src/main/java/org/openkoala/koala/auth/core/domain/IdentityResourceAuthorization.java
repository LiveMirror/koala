package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author lingen
 * 授权关系
 * 责任方：角色 ROLE
 * 委托方：资源 Resources
 * 
 */
@Entity
@Table(name = "KS_IDENTITY_RESOURCE_AUTH")
public class IdentityResourceAuthorization extends Accountability {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5527620335383954438L;
	
	private Identity identity;

	
	private Resource resource;
	
	@ManyToOne
	@JoinColumn(name = "IDENTITY_ID", nullable = false)
	public Identity getIdentity() {
		return identity;
	}
	
	public static List<IdentityResourceAuthorization> findAllRelaitionByResource(Long resId)
	{
		return IdentityResourceAuthorization.getRepository().createNamedQuery("findAllRelaitionByResource").addParameter("resourceId", resId).list();

	}


	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID", nullable = false)
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * 查找所有资源和角色
	 * @return
	 */
	public static List<IdentityResourceAuthorization> findAllReourcesAndRoles(){
	    return IdentityResourceAuthorization.getRepository().findAll(IdentityResourceAuthorization.class);
	}
	
	/**
	 * 查找所有资源和角色标
	 * @returnfindAllAuthorization
	 */
	public static List<Object[]> findAllResourceIdentifierAndRoleId(){
		return getRepository().createNamedQuery("findAllResourceIdentifierAndRoleId").addParameter("abolishDate",new Date()).list();
	}
	
	/**
	 * 根据角色ID获取角色的授权信息
	 * @param roleId
	 * @return
	 */
	public static List<IdentityResourceAuthorization> findAuthorizationByRole(long roleId) {
		return  getRepository().createNamedQuery("findAuthorizationByRole").addParameter("roleId", roleId).addParameter("abolishDate",new Date()).list();
		
	}
	
	/**
	 * 根据角色ID获取角色的授权信息
	 * @param roleId
	 * @return
	 */
	public static List<String> findAuthorizationByResourceIdentifier(String identifier) {
		return  getRepository().createNamedQuery("findAuthorizationByResourceIdentifier").addParameter("identifier", identifier).addParameter("abolishDate",new Date()).list();
		
	}

	@Override
	public String[] businessKeys() {
		return new String[] {};
	}

	@Override
	public String toString() {
		return "IdentityResourceAuthorization [identity=" + identity
				+ ", resource=" + resource + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((identity == null) ? 0 : identity.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentityResourceAuthorization other = (IdentityResourceAuthorization) obj;
		if (identity == null) {
			if (other.identity != null)
				return false;
		} else if (!identity.equals(other.identity))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}
	
	

}
