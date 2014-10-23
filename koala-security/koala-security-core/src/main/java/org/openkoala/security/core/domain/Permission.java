package org.openkoala.security.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.CorrelationException;
import org.openkoala.security.core.IdentifierIsExistedException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import java.util.*;

/**
 * 权限是一个抽象的概念，代表一项操作或责任，因此是授权的细粒度表现。
 *
 * @author lucas
 */
@Entity
@DiscriminatorValue("PERMISSION")
public class Permission extends Authority {

    private static final long serialVersionUID = 4631351008490511334L;

    /**
     * 权限标识符 例如：user:create
     */
    @NotNull
    @Column(name = "IDENTIFIER")
    private String identifier;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<Role>();

    protected Permission() {
    }

    public Permission(String name, String identifier) {
        super(name);
        checkArgumentIsNull("identifier", identifier);
        isIdentifierExisted(identifier);
        this.identifier = identifier;
    }

    public Permission getPermissionBy(String identifier) {
        return getRepository()//
                .createCriteriaQuery(Permission.class)//
                .eq("identifier", identifier)//
                .singleResult();
    }

    public static List<String> getIdentifiers(Set<Authority> authorities) {
        List<String> results = new ArrayList<String>();
        for (Authority authority : authorities) {
            if (authority instanceof Permission) {
                results.add(((Permission) authority).getIdentifier().trim());
            }
        }
        return results;
    }


    @Override
    public Authority getBy(String name) {
        return getRepository()//
                .createNamedQuery("Authority.getAuthorityByName")//
                .addParameter("authorityType", Permission.class)//
                .addParameter("name", name)//
                .singleResult();
    }

    /**
     * 由于permission 是细粒度权限。因此需要判断Permission 是否已经分配给SecurityResource子类，如果已经分配就不能再分配
     */
    @Override
    public void addSecurityResource(SecurityResource securityResource) {
        ResourceAssignment resourceAssignment = getRepository()
                .createNamedQuery("ResourceAssignment.findByResourceTypeAndAuthority")
                .addParameter("resourceType", securityResource.getClass()) // 能够拿到真实类型，例如传递的是menuResource 那么securityResource就是MenuResource类型。
                .addParameter("authorityType", this.getClass())
                .singleResult();
        if (resourceAssignment != null) {
            throw new CorrelationException("Permission canot grant twice SecurityResources that kind of children.");
        }
        new ResourceAssignment(this, securityResource).save();
    }

    public static Permission getBy(Long id) {
        return Permission.get(Permission.class, id);
    }

    private void isIdentifierExisted(String identifier) {
        Permission permission = getPermissionBy(identifier);
        if (permission != null) {
            throw new IdentifierIsExistedException("permission.identifier.existed");
        }

    }

    public void changeIdentifier(String identifier) {
        checkArgumentIsNull("identifier", identifier);
        if (!identifier.equals(this.getIdentifier())) {
            isIdentifierExisted(identifier);
            this.identifier = identifier;
            this.save();
        }
    }

    public void addRole(Role role) {
        this.roles.add(role);
        this.save();
    }

    public void addRoles(Set<Role> roles) {
        this.roles.addAll(roles);
        this.save();
    }

    public void terminateRole(Role role) {
        this.roles.remove(role);
        this.save();
    }

    public void terminateRoles(Set<Role> roles) {
        this.roles.removeAll(roles);
        this.save();
    }

    @Override
    public void remove() {

        if (!this.getRoles().isEmpty()) {
            throw new CorrelationException("permission has role, so can't remove it.");
        }
        super.remove();
    }

    public String[] businessKeys() {
        return new String[]{"name", "identifier"};
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getName())
                .append(identifier)
                .append(getDescription())
                .build();
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<SecurityResource> findResources() {
        return getRepository()
                .createNamedQuery("ResourceAssignment.findSecurityResourcesByAuthorityNoResourcesType")
                .addParameter("authority", this)
                .list();
    }
}