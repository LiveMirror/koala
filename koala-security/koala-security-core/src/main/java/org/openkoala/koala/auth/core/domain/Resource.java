package org.openkoala.koala.auth.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.dayatang.utils.DateUtils;

/**
 * 资源的父类
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "KS_RESOURCE")
public class Resource extends Party {

	private static final long serialVersionUID = 7184025731035256920L;

	/** 是否有效 **/
	
	private boolean isValid;

	/** 资源标识 **/
	
	private String identifier;

	/** 资源级别 **/
	
	private String level;

	/** 资源图标 **/
	
	private String menuIcon;

	/** 资源描述 **/
	
	private String desc;

	/** 角色资源授权关系 **/
	private Set<IdentityResourceAuthorization> authorizations = new HashSet<IdentityResourceAuthorization>();
	
	
	private Resource parent;
	
	@Transient
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	@Transient
	public List<Resource> getChildren() {
		return children;
	}

	public void addChild(Resource child) {
		this.children.add(child);
	}

	
	private List<Resource> children = new ArrayList<Resource>();

	@Column(name = "IDENTIFIER", nullable = false)
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	
	@Column(name = "[LEVEL]")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name = "DESCRIPTION")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
	public Set<IdentityResourceAuthorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(Set<IdentityResourceAuthorization> authorizations) {
		this.authorizations = authorizations;
	}

	@Column(name = "ISVALID")
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Column(name = "MENU_ICON")
	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	/**
	 * 使资源失效
	 */
	public void disableResource() {
		this.isValid = false;
		this.save();
	}

	/**
	 * 使资源有效
	 */
	public void enableResource() {
		this.isValid = true;
		this.save();
	}

	/**
	 * 为资源分配角色
	 * @param role
	 */
	public void assignRole(Role role) {
		IdentityResourceAuthorization idres = new IdentityResourceAuthorization();
		idres.setIdentity(role);
		idres.setResource(this);
		idres.setAbolishDate(DateUtils.MAX_DATE);
		idres.setCreateDate(new Date());
		idres.setScheduledAbolishDate(new Date());
		idres.save();
	}

	/**
	 * 根据角色ID查找资源
	 * @param roleId
	 * @return
	 */
	public static List<Resource> findResourceByRole(Long roleId) {
		return getRepository().createNamedQuery("findResourceByRole").addParameter("roleId", roleId).addParameter("abolishDate", new Date()).list();
	}

	/**
	 * 根据父资源ID查找子资源
	 * @param parentId
	 * @return
	 */
	public static List<Resource> findChildByParent(Long parentId) {
		if (parentId == null) {
			return getRepository().createNamedQuery("findTopLevelResource").addParameter("abolishDate", new Date()).list();
		} 
		return getRepository().createNamedQuery("findChildByParent").addParameter("parentId", parentId).addParameter("abolishDate", new Date()).list();
	}

	/**
	 * 根据父资源ID和用户账号查找子资源
	 * @param parentId
	 * @param userAccount
	 * @return
	 */
	public static List<Resource> findChildByParentAndUser(Long parentId, String userAccount) {
		if (parentId == null) {
			if ("".equals(userAccount)) {
				return getRepository().createNamedQuery("findTopLevelResource").addParameter("abolishDate",  new Date() ).list();
			}
			return getRepository().createNamedQuery("findTopLevelResourceByUser").addParameter("userAccount", userAccount).addParameter("roleUserAuthorizationAbolishDate", new Date()).addParameter("identityResourceAuthorizationAbolishDate", new Date()).list();
		} else {
			if ("".equals(userAccount)) {
				return getRepository().createNamedQuery("findChildByParent").addParameter("parentId", parentId).addParameter("abolishDate", new Date()).list();
			} 
			return getRepository().createNamedQuery("findChildByParentAndUser").addParameter("parentId", parentId).addParameter("userAccount", userAccount).addParameter("identityResourceAuthorizationAbolishDate", new Date()).addParameter("roleUserAuthorizationAbolishDate", new Date()).list();
		}
	}

	/**
	 * 删除资源
	 */
	public void removeResource() {
		removeResourceLineAssignment(this.getId());
		removeIdentityResourceAuthorization();
		removeResourceTypeAssignment();
		this.setAbolishDate(new Date());
	}

	/**
	 * 删除与资源类型的关系 
	 */
	private void removeResourceTypeAssignment() {
		ResourceTypeAssignment.findByResource(this.getId()).setAbolishDate(new Date());
	}

	/**
	 * 删除授权关系
	 */
	private void removeIdentityResourceAuthorization() {
		for (IdentityResourceAuthorization authorization : IdentityResourceAuthorization //
				.findAllRelaitionByResource(this.getId())) {
			authorization.setAbolishDate(new Date());
		}
	}

	/**
	 * 删除垂直关系
	 */
	private void removeResourceLineAssignment(Long id) {
		for (ResourceLineAssignment assignment : ResourceLineAssignment.findRelationByResource(id)) {
			assignment.setAbolishDate(new Date());
			if (assignment.getChild() != null && assignment.getChild().getAbolishDate().after(new Date())) {
				assignment.getChild().setAbolishDate(new Date());
				removeResourceLineAssignment(assignment.getChild().getId());
			}
		}
	}

	/**
	 * 为资源分配父资源
	 * @param parent
	 */
	public void assignParent(Resource parent) {
		ResourceLineAssignment resLineAssignment = new ResourceLineAssignment();
		resLineAssignment.setCreateDate(new Date());
		resLineAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resLineAssignment.setScheduledAbolishDate(new Date());
		resLineAssignment.setParent(parent);
		resLineAssignment.setChild(this);
		resLineAssignment.save();
	}

	/**
	 * 为资源分配子资源
	 * @param child
	 */
	public void assignChild(Resource child) {
		ResourceLineAssignment resLineAssignment = new ResourceLineAssignment();
		resLineAssignment.setCreateDate(new Date());
		resLineAssignment.setScheduledAbolishDate(new Date());
		resLineAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resLineAssignment.setParent(this);
		resLineAssignment.setChild(child);
		resLineAssignment.save();
	}

	/**
	 * 根据资源标识符查找角色
	 * @param identifier
	 * @return
	 */
	public static List<Role> findRoleByResource(String identifier) {
		return getRepository().createNamedQuery("findRoleByResource").addParameter("identifier", identifier).list();
	}

	/**
	 * 判断一个角色是否有权限访问某一个资源
	 * @param resId
	 * @param roleId
	 * @return
	 */
	public static boolean hasPrivilegeByRole(Long resId, Long roleId) {
		List<IdentityResourceAuthorization> ls = getRepository().createNamedQuery("queryPrivilegeByRole").addParameter("resId", resId)
				.addParameter("roleId", roleId).addParameter("abolishDate", new Date()).list();
				
		return !ls.isEmpty();
	}

	/**
	 * 判断用户是否有权限
	 * @param identifier
	 * @param userAccount
	 * @return
	 */
	public static boolean hasPrivilegeByUser(String identifier, String userAccount) {
		
		List<IdentityResourceAuthorization> ls = getRepository().createNamedQuery("queryPrivilegeByUser").addParameter("identifier", identifier)
				.addParameter("userAccount", userAccount).list();
		return !ls.isEmpty();
	}

	/**
	 * 判断一个资源是否有子资源
	 * @param parentId
	 * @return
	 */
	public static boolean hasChildByParent(Long parentId) {
		return !getRepository().createNamedQuery("hasChildByParent").addParameter("parentId", parentId).list().isEmpty();
	}

	/**
	 * 是否是菜单资源
	 * 
	 * @param resource
	 * @return
	 */
	public static boolean isMenu(Resource resource) {
		
		List<Resource> resources = getRepository().createNamedQuery("findResourceById").addParameter("name1", "KOALA_MENU")
				.addParameter("name2", "KOALA_DIRETORY").addParameter("id", resource.getId()).list();
		if (resources != null && !resources.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 创建资源
	 * @param name			资源名称
	 * @param identifier	资源标识
	 * @param level			级别
	 * @param menuIcon		菜单图标
	 * @return
	 */
	public static Resource newResource(String name, String identifier, String level, String menuIcon) {
	    Resource resource  = null;
	    String jpql = "select r from Resource r where r.name =:name " + //
	    		"and r.identifier = :identifier";
	    
	    List<Resource> resources = getRepository().createJpqlQuery(jpql).addParameter("name", name)
	    		.addParameter("identifier", identifier).list();
	    if (resources!=null && resources.size()>0){
	        resource = resources.get(0);
	    }
	    
	    if (resource == null) {
	        resource = new Resource();
		    resource.setName(name);
		    resource.setDesc(name);
		    resource.setCreateDate(new Date());
		    resource.setIdentifier(identifier);
		    resource.setLevel(level);
		    resource.setMenuIcon(menuIcon);
		    resource.setValid(true);
		    resource.setAbolishDate(DateUtils.MAX_DATE);
	    }
	    return resource;
	}

	/**
	 * 资源名称是否已经存在
	 * 
	 * @return
	 */
	@Transient
	public boolean isNameExist() {
		return !getRepository().createNamedQuery("isResouceNameExist").addParameter("name", getName())
				.addParameter("abolishDate", new Date()).list().isEmpty();
	}
	
	/**
	 * 删除所有资源
	 */
	public static void removeAll(){
		String sql = "DELETE FROM KS_RESOURCE";
		getRepository().createSqlQuery(sql).executeUpdate();
	}

	/**
	 * 资源标识是否已经存在
	 * 
	 * @return
	 */
	@Transient
	public boolean isIdentifierExist() {
		return !getRepository().createNamedQuery("isResourceIdentifierExist").addParameter("abolishDate", new Date()).addParameter("identifier", getIdentifier() ).list().isEmpty();
	}
	
	public static List<Resource> getRootResources() {
		List<Resource> results = new ArrayList<Resource>();
		List<Resource> all = Resource.findAll(Resource.class);
		List<Resource> hasParent = hasParent();
		all.removeAll(hasParent);
		
		Map<Long, Resource> map = new HashMap<Long, Resource>();
		for (ResourceLineAssignment each : ResourceLineAssignment.findAllResourceLine()) {
			Resource parent = map.get(each.getParent().getId());
			if (parent == null) {
				parent = each.getParent();
				map.put(parent.getId(), parent);
			}
			Resource child = map.get( each.getChild().getId());
			if (child == null) {
				child = each.getChild();
				map.put(child.getId(), child);
			}
			
			parent.addChild(child);
			if (all.contains(parent)) {
				results.add(parent);
			}
		}
		return results;
	}


	@Override
	public String toString() {
		return "Resource [isValid=" + isValid + ", identifier=" + identifier
				+ ", level=" + level + ", menuIcon=" + menuIcon + ", desc="
				+ desc + ", authorizations=" + authorizations + ", parent="
				+ parent + ", children=" + children + "]";
	}

	public static List<Resource> hasParent() {
		return getRepository().createNamedQuery("hasParent").addParameter("abolishDate", new Date()).list();
	}

	@Override
	public String[] businessKeys() {
		return new String[]{identifier};
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getAbolishDate() == null) ? 0 : getAbolishDate().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Resource))
			return false;
		Resource other = (Resource) obj;
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
