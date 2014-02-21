package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.dayatang.utils.DateUtils;

/**
 * 资源类型
 * 
 * @author Ken
 * @since 2013-03-12 09:25
 * 
 */
@Entity
@Table(name = "KS_RESOURCE_TYPE")
public class ResourceType extends Party {

	private static final long serialVersionUID = -5507294042279204096L;

	/**
	 * 查找所有资源类型
	 * 
	 * @return
	 */
	public static List<ResourceType> findAllResourceType() {
		return getRepository().createNamedQuery("findAllResourceType").addParameter("name1", "KOALA_MENU").addParameter("name2", "KOALA_DIRETORY")
				.addParameter("abolishDate", new Date()).list();
	}
	
	/**
	 * 查找菜单类型
	 * @return
	 */
	public static List<ResourceType> findMenuType() {
		return getRepository().createNamedQuery("findMenuType").addParameter("name1", "KOALA_MENU").addParameter("name2", "KOALA_DIRETORY")
		.addParameter("abolishDate", new Date()).list();
	}

	/**
	 * 创建资源类型
	 * @param name	资源类型名称
	 * @return
	 */
	public static ResourceType newResourceType(String name){
	    
	    ResourceType type  = null;
	    String jpql = "select r from ResourceType r where r.name=:name";
	    
	    type = getRepository().createJpqlQuery(jpql).addParameter("name", name).singleResult();

        
        if (type == null) {
	      type = new ResourceType();
	      type.setName(name);
	      type.setCreateDate(new Date());
	      type.setAbolishDate(DateUtils.MAX_DATE);
        }
	    return type;
	}
	
	/**
	 * 根据资源类型ID获取资源类型与资源的关系
	 * @return
	 */
	@Transient
	public List<ResourceTypeAssignment> getResources() {
		return ResourceTypeAssignment.findResourceByType(getId());
	}
	
	/**
	 * 删除所有资源类型
	 */
	public static void removeAll(){
		
		getRepository().createSqlQuery("DELETE FROM ResourceType").executeUpdate();
	}

	@Override
	public String[] businessKeys() {
		return new String[]{getName(),getAbolishDate().toString()};
	}

}
