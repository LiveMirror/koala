package org.openkoala.security.core.domain;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.*;

/**
 * 角色是权限的集合，它可以可以是逻辑上的权限{@link Permission}，也可以是物理上的权限{@link SecurityResource}。
 * 它代表一系列可执行操作或责任，因此它是授权的粗粒度。
 *
 * @author luzhao
 */
@Entity
@DiscriminatorValue("ROLE")
public class Role extends Authority {

    private static final long serialVersionUID = 4327840654680779887L;

    /**
     * 查询Role需要级联的查询出Permission
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "KS_ROLE_PERMISSION_MAP", //
            joinColumns = @JoinColumn(name = "ROLE_ID"), //
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
    private Set<Permission> permissions = new HashSet<Permission>();

    protected Role() {
    }

    public Role(String name) {
        super(name);
    }

    public static boolean checkName(String roleName) {
        return getRoleBy(roleName) != null;
    }

    public static List<String> getNames(Set<Authority> authorities) {
        List<String> results = new ArrayList<String>();
        for (Authority authority : authorities) {
            if (authority instanceof Role) {
                results.add(((Role) authority).getName().trim());
            }
        }
        return results;
    }

    public static Set<Role> findByUser(User user) {
        Set<Role> results = new HashSet<Role>();
        List<Authorization> authorizations = Authorization.findByActor(user);
        for (Authorization authorization : authorizations) {
            Authority authority = authorization.getAuthority();
            if (authority instanceof Role) {
                results.add((Role) authority);
            }
        }
        return results;
    }

    public Set<Authority> findAuthoritiesBy() {
        Set<Authority> results = new HashSet<Authority>();
        results.add(this);
        results.addAll(this.getPermissions());
        return results;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.addRole(this);
        this.save();
    }

    public void addPermissions(List<Permission> permissions) {
        this.permissions.addAll(permissions);
        for (Permission permission : permissions) {
            permission.addRole(this);
        }
        this.save();
    }

    public void terminatePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.terminateRole(this);
        this.save();
    }

    public void terminatePermissions(List<Permission> permissions) {
        this.permissions.removeAll(permissions);
        for (Permission permission : permissions) {
            permission.terminateRole(this);
        }
        this.save();
    }

    public static List<Role> findAll() {
        return Role.findAll(Role.class);
    }

    // TODO 名称应该放在基类上面。
    public static Role getRoleBy(String name) {
        return getRepository()//
                .createCriteriaQuery(Role.class)//
                .eq("name", name)//
                .singleResult();
    }

    @Override
    public Authority getBy(String name) {
        return getRepository()//
                .createNamedQuery("Authority.getAuthorityByName")//
                .addParameter("authorityType", Role.class)//
                .addParameter("name", name)//
                .singleResult();
    }

    public static Role getBy(Long id) {
        return Role.get(Role.class, id);
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<MenuResource> findMenuResources() {
        List<MenuResource> results = getRepository()//
                .createNamedQuery("ResourceAssignment.findSecurityResourcesByAuthority")//
                .addParameter("authority", this)//
                .addParameter("resourceType", MenuResource.class)//
                .addParameter("authorityType", Role.class)//
                .list();
        return Sets.newHashSet(results);

    }

    public Set<UrlAccessResource> findUrlAccessResources() {
        List<UrlAccessResource> results = getRepository()//
                .createNamedQuery("ResourceAssignment.findSecurityResourcesByAuthority")//
                .addParameter("authority", this)//
                .addParameter("resourceType", UrlAccessResource.class)//
                .addParameter("authorityType", Role.class)//
                .list();
        return Sets.newHashSet(results);
    }

    public Set<PageElementResource> findPageElementResources() {
        List<PageElementResource> results = getRepository()//
                .createNamedQuery("ResourceAssignment.findSecurityResourcesByAuthority")//
                .addParameter("authority", this)//
                .addParameter("resourceType", PageElementResource.class)//
                .addParameter("authorityType", Role.class)//
                .list();
        return Sets.newHashSet(results);

    }
}
