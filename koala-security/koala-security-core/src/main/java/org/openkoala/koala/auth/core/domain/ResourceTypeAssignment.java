package org.openkoala.koala.auth.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dayatang.utils.DateUtils;

/**
 * Resource与ResourceType的关系
 * 
 * @author Ken
 * @since 2013-3-23 下午2:11:01
 */
@Entity
@Table(name = "KS_RESOURCETYPE_ASSIGNMENT")
public class ResourceTypeAssignment extends Accountability {
	
    private static final long serialVersionUID = -1583999041453970769L;

    
    private Resource resource;

   
    private ResourceType resourceType;
    
    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID", nullable = false)
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @ManyToOne
    @JoinColumn(name = "RESOURCETYPE_ID", nullable = false)
    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * 根据资源ID查找资源与资源类型的关系
     * @param resourceId
     * @return
     */
    public static ResourceTypeAssignment findByResource(long resourceId) {
    	return
    			getRepository().createNamedQuery("findByResource").addParameter("resourceId", resourceId)
    			.addParameter("abolishDate", new Date()).singleResult();
   
    }

    /**
     * 创建资源与资源类型的对应关系 
     * @param resource			资源
     * @param resourceType		资源类型
     * @return
     */
    public static ResourceTypeAssignment newResourceTypeAssignment(Resource resource, ResourceType resourceType) {
        ResourceTypeAssignment assign = null;
        String jpql = "select r from ResourceTypeAssignment r where r.resource = :resource and r.resourceType = :resourceType";
        
        assign = getRepository().createJpqlQuery(jpql).addParameter("resource", resource)
        		.addParameter("resourceType", resourceType).singleResult();
        		
        
        if (assign == null) {
            assign = new ResourceTypeAssignment();
            assign.setResource(resource);
            assign.setResourceType(resourceType);
            assign.setCreateDate(new Date());
            assign.setAbolishDate(DateUtils.MAX_DATE);
        }
        return assign;
    }
    
    /**
     * 根据资源类型查找与资源的关系
     * @param typeId
     * @return
     */
    public static List<ResourceTypeAssignment> findResourceByType(long typeId) {
    	return getRepository().createNamedQuery("findResourceByType").addParameter("abolishDate", new Date()).addParameter("resourceTypeId", typeId).list();
    }

	@Override
	public String[] businessKeys() {
		return new String[]{resource.getName(),resourceType.getName(),getAbolishDate().toString()};
	}
}
