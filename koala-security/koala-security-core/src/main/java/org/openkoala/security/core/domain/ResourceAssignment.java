package org.openkoala.security.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.NullArgumentException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 资源分配
 * Created by luzhao on 14-8-19.
 */
@Entity
@Table(name = "KS_RESOURCEASSIGNMENTS")
@NamedQueries({
        @NamedQuery(name="ResourceAssignment.findSecurityResourcesByAuthorities",query = "SELECT _resource FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _authority in (:authorities) AND TYPE(_resource)= :resourceType GROUP BY _resource.id"),
        @NamedQuery(name="ResourceAssignment.findAuthoritiesBySecurityResource",query = "SELECT _authority FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _resource = :resource AND TYPE(_authority)= :authorityType GROUP BY _authority.id")
})
public class ResourceAssignment extends SecurityAbstractEntity {

    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    @ManyToOne
    @JoinColumn(name = "SECURITYRESOURCE_ID")
    private SecurityResource resource;

    protected ResourceAssignment() { }

    public ResourceAssignment( Authority authority,SecurityResource resource) {
        if (authority == null) {
            throw new NullArgumentException("authority");
        }

        if (resource == null) {
            throw new NullArgumentException("resource");
        }
        this.authority = authority;
        this.resource = resource;

    }

    @Override
    public void save() {
        if(exists(authority,resource)){
            return;
        }
        super.save();
    }

    @Override
    public void remove() {
        super.remove();
    }

    public static ResourceAssignment findByResourceInAuthority(Authority authority, SecurityResource resource) {
        ResourceAssignment result =  getRepository()//
                .createCriteriaQuery(ResourceAssignment.class)//
                .eq("authority", authority)//
                .eq("resource", resource)//
                .singleResult();
        return result;
    }

    public static List<ResourceAssignment> findByAuthority(Authority authority) {
        return getRepository()//
                .createCriteriaQuery(ResourceAssignment.class)//
                .eq("authority", authority)//
                .list();

    }

    public static List<ResourceAssignment> findByResource(SecurityResource resource) {
        return getRepository()//
                .createCriteriaQuery(ResourceAssignment.class)//
                .eq("resource", resource)//
                .list();
    }

    //~ 查询

    public static List<MenuResource> findMenuResourceByAuthorities(Set<? extends Authority> authorities) {
        return getRepository()//
                .createNamedQuery("ResourceAssignment.findSecurityResourcesByAuthorities")//
                .addParameter("authorities", authorities)//
                .addParameter("resourceType", MenuResource.class)//
                .list();
    }

    public static List<MenuResource> findMenuResourceByAuthority(Authority authority) {
        Set<Authority> authorities =  getAuthoritiesByAuthority(authority);
        return findMenuResourceByAuthorities(authorities);
    }

    public static List<UrlAccessResource> findUrlAccessResourcesByAuthority(Authority authority){
        Set<Authority> authorities =  getAuthoritiesByAuthority(authority);
        return findUrlAccessResourcesByAuthorities(authorities);
    }

    public static List<UrlAccessResource> findUrlAccessResourcesByAuthorities(Set<Authority> authorities) {
        return getRepository()//
                .createNamedQuery("ResourceAssignment.findSecurityResourcesByAuthorities")//
                .addParameter("authorities", authorities)//
                .addParameter("resourceType", UrlAccessResource.class)//
                .list();
    }

    public static List<Role> findRoleBySecurityResource(SecurityResource resource){
        return getRepository().createNamedQuery("ResourceAssignment.findAuthoritiesBySecurityResource")
                .addParameter("resource",resource)
                .addParameter("authorityType",Role.class)//
                .list();
    }

    public static List<Permission> findPermissionBySecurityResource(SecurityResource resource){
        return getRepository().createNamedQuery("ResourceAssignment.findAuthoritiesBySecurityResource")
                .addParameter("resource",resource)
                .addParameter("authorityType",Permission.class)//
                .list();
    }

    // TODO ...
    public static List<ResourceAssignment> findAll(){
        return getRepository().createJpqlQuery("SELECT _resource FROM ResourceAssignment resourceAssignment JOIN r.resource _resource GROUP BY _resource").list();
    }

    private static Set<Authority> getAuthoritiesByAuthority(Authority authority) {
        Set<Authority> results = new HashSet<Authority>();
        results.add(authority);
        if(authority instanceof Role){
            Role role = (Role)authority;
            results.addAll(role.getPermissions());
        }
        return results;
    }

    private boolean exists(Authority authority,SecurityResource resource){
       return findByResourceInAuthority(authority, resource) != null;
    }

    @Override
    public String[] businessKeys() {
        return new String[]{"resource", "authority"};
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)//
                .append(resource)//
                .append(authority)//
                .toString();
    }

    public SecurityResource getResource() {
        return resource;
    }

    public Authority getAuthority() {
        return authority;
    }
}
