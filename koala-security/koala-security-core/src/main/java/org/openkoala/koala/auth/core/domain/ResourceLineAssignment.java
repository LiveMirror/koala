package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dayatang.utils.DateUtils;

/**
 * 资源的垂直关系
 * 
 * @author zyb <a
 *         href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 16, 2013 9:14:59 AM
 */
@Entity
@Table(name = "KS_RESOURCE_LINEASSIGNMENT")
public class ResourceLineAssignment extends Accountability {

	private static final long serialVersionUID = 1L;

	
	private Resource parent;

	
	private Resource child;

	/**
	 * 查找资源关系
	 * 
	 * @param resourceId
	 * @return
	 */
	public static List<ResourceLineAssignment> findRelationByResource(
			Long resourceId) {
		return getRepository().createNamedQuery("findRelationByResource")
				.addParameter("childId", resourceId)
				.addParameter("parentId", resourceId).list();
	}

	@ManyToOne
	@JoinColumn(name = "PARENT_ID", nullable = false)
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	@ManyToOne
	@JoinColumn(name = "CHILD_ID", nullable = false)
	public Resource getChild() {
		return child;
	}

	public void setChild(Resource child) {
		this.child = child;
	}

	/**
	 * 删除所有的资源垂直关系
	 */
	public static void removeAll() {
		String sql = "DELETE FROM ResourceLineAssignment";
		getRepository().createSqlQuery(sql).executeUpdate();
	}

	/**
	 * 创建资源的垂直关系
	 * 
	 * @param parent
	 *            父资源
	 * @param child
	 *            子资源
	 * @return
	 */
	public static ResourceLineAssignment newResourceLineAssignment(
			Resource parent, Resource child) {
		ResourceLineAssignment assignment = null;
		String jpql = "select r from ResourceLineAssignment r where r.parent = :parent and r.child = :child";

		List<ResourceLineAssignment> resources = getRepository()
				.createJpqlQuery(jpql).addParameter("child", child)
				.addParameter("parent", parent).list();
		if (resources != null && resources.size() > 0) {
			assignment = resources.get(0);
		}

		if (assignment == null) {
			assignment = new ResourceLineAssignment();
			assignment.setParent(parent);
			assignment.setChild(child);
			assignment.setCreateDate(new Date());
			assignment.setAbolishDate(DateUtils.MAX_DATE);
		}
		return assignment;
	}

	public static List<ResourceLineAssignment> findAllResourceLineByUseraccount(
			String useraccount) {
		return getRepository().createNamedQuery("findAllResourceLineByUseraccount").addParameter("userAccount", useraccount)
				.addParameter("identityResourceAuthorizAtionAbolishDate", new Date()).addParameter("roleUserAuthorizationAbolishDate", new Date()).list();
		
	}

	public static List<ResourceLineAssignment> findAllResourceLine() {
		return getRepository().createNamedQuery("findAllResourceLine").addParameter("abolishDate", new Date()).list();
	}

	@Override
	public String[] businessKeys() {
		return new String[] { String.valueOf(parent.getId()),
				String.valueOf(child.getId()), getAbolishDate().toString() };
	}

}
