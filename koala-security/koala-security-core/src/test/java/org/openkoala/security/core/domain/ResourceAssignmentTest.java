package org.openkoala.security.core.domain;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.openkoala.security.core.util.EntitiesHelper.*;

import static org.junit.Assert.*;

public class ResourceAssignmentTest extends AbstractDomainIntegrationTestCase {

    private Role role1;

    private Permission permission;

    private UrlAccessResource urlAccessResource1;

    private MenuResource menuResource;

    private PageElementResource pageElementResource;

    @Before
    public void setUp() {
        role1 = initRole();
        role1.save();
        urlAccessResource1 = initUrlAccessResource();
        urlAccessResource1.save();
        permission = initPermission();
        permission.save();
        menuResource = initMenuResource();
        menuResource.save();
        pageElementResource = initPageElementResource();
        pageElementResource.save();
    }

    @Test
    public void testSave() throws Exception {
        ResourceAssignment resourceAssignment1 = new ResourceAssignment(role1, urlAccessResource1);
        resourceAssignment1.save();
        assertNotNull(resourceAssignment1.getId());
        ResourceAssignment result1 = ResourceAssignment.getById(resourceAssignment1.getId());
        assertNotNull(result1.getId());
        assertNotNull(result1);
        assertRole(role1, (Role) result1.getAuthority());
        assertUrlAccessResource(urlAccessResource1, (UrlAccessResource) result1.getResource());

        ResourceAssignment resourceAssignment2 = new ResourceAssignment(role1, menuResource);
        resourceAssignment2.save();
        assertNotNull(resourceAssignment2.getId());
        ResourceAssignment result2 = ResourceAssignment.getById(resourceAssignment2.getId());
        assertNotNull(result2);
        assertMenuResource(menuResource, (MenuResource) result2.getResource());

        ResourceAssignment resourceAssignment3 = new ResourceAssignment(role1, pageElementResource);
        resourceAssignment3.save();
        assertNotNull(resourceAssignment3.getId());
        ResourceAssignment result3 = ResourceAssignment.getById(resourceAssignment3.getId());
        assertNotNull(result3);
        assertPageElementResource(pageElementResource, (PageElementResource) result3.getResource());

        ResourceAssignment resourceAssignment4 = new ResourceAssignment(permission, urlAccessResource1);
        resourceAssignment4.save();
        assertNotNull(resourceAssignment4.getId());
        ResourceAssignment result4 = ResourceAssignment.getById(resourceAssignment4.getId());
        assertNotNull(result4);
        assertPermission(permission, (Permission) result4.getAuthority());
        assertUrlAccessResource(urlAccessResource1, (UrlAccessResource) result4.getResource());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAuthorityIsNull() throws Exception {
        new ResourceAssignment(null, urlAccessResource1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveResourceIsNull() throws Exception {
        new ResourceAssignment(role1, null);
    }

    @Test
    public void testSaveExisted() throws Exception {
        ResourceAssignment resourceAssignment1 = new ResourceAssignment(role1, urlAccessResource1);
        resourceAssignment1.save();
        assertNotNull(resourceAssignment1.getId());

        ResourceAssignment resourceAssignment2 = new ResourceAssignment(role1, urlAccessResource1);
        resourceAssignment2.save();
        assertNull(resourceAssignment2.getId());
    }

    @Test
    public void testRemove() throws Exception {
        ResourceAssignment resourceAssignment1 = new ResourceAssignment(role1, urlAccessResource1);
        resourceAssignment1.save();
        System.out.println("resourceAssignment1 = " + resourceAssignment1);
        assertNotNull(resourceAssignment1.getId());
        resourceAssignment1.remove();
        ResourceAssignment result1 = ResourceAssignment.getById(resourceAssignment1.getId());
        assertNull(result1);
    }

    @Test
    public void testFindByResourceInAuthority() throws Exception {
        ResourceAssignment resourceAssignment1 = new ResourceAssignment(role1, urlAccessResource1);
        resourceAssignment1.save();
        ResourceAssignment resourceAssigment = ResourceAssignment.findByResourceInAuthority(role1, urlAccessResource1);
        assertNotNull(resourceAssigment);
        assertNotNull(resourceAssigment.getId());
        assertRole(role1, (Role) resourceAssigment.getAuthority());
        assertUrlAccessResource(urlAccessResource1, (UrlAccessResource) resourceAssigment.getResource());
    }

    @Test
    public void testFindByAuthority() throws Exception {
        testSave();
        List<ResourceAssignment> results = ResourceAssignment.findByAuthority(role1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 3);
        ResourceAssignment result = results.get(0);
        assertNotNull(result.getId());
        Role acutal = (Role) result.getAuthority();
        assertNotNull(acutal);
        assertRole(role1, acutal);
    }

    @Test
    public void testFindByAuthorityHasPermission() throws Exception {
        testSave();
        role1.addPermission(permission);
        List<ResourceAssignment> results = ResourceAssignment.findByAuthority(role1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 4);
        ResourceAssignment result = results.get(0);
        assertNotNull(result.getId());
        Role acutal = (Role) result.getAuthority();
        assertNotNull(acutal);
        assertRole(role1, acutal);

        ResourceAssignment result4 = results.get(3);
        assertNotNull(result4);
        assertNotNull(result4.getId());
        Permission acutalPermission = (Permission) result4.getAuthority();
        assertNotNull(acutalPermission);
        assertNotNull(acutalPermission.getId());
        assertPermission(permission, acutalPermission);

    }

    @Test
    public void testFindByResource() throws Exception {
        testSave();
        List<ResourceAssignment> results = ResourceAssignment.findByResource(urlAccessResource1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 2);
        ResourceAssignment result1 = results.get(0);
        UrlAccessResource acutalUrlAccessResource = (UrlAccessResource) result1.getResource();
        assertNotNull(acutalUrlAccessResource);
        assertUrlAccessResource(urlAccessResource1, acutalUrlAccessResource);
        Role actualRole = (Role) result1.getAuthority();
        assertNotNull(actualRole);
        assertRole(role1, actualRole);

        ResourceAssignment result2 = results.get(1);
        assertNotNull(result2);
        Permission actualPermission = (Permission) result2.getAuthority();
        assertNotNull(actualPermission);
    }

    @Test
    public void testFindMenuResourceByAuthorities() throws Exception {
        testSave();
        List<MenuResource> results = ResourceAssignment.findMenuResourceByAuthorities(Sets.newHashSet(role1));
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 1);
        MenuResource result = results.get(0);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void testFindMenuResourceByAuthority() throws Exception {
        testSave();
        List<MenuResource> results = ResourceAssignment.findMenuResourceByAuthority(role1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 1);
        MenuResource result = results.get(0);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void testFindMenuResourceByAuthorityHasPermission() throws Exception {
        testSave();
        role1.addPermission(permission);
        MenuResource menuResource2 = new MenuResource("菜单testFindMenuResourceByAuthorityHasPermission");
        menuResource2.save();

        new ResourceAssignment(permission, menuResource2).save();

        List<MenuResource> results = ResourceAssignment.findMenuResourceByAuthority(role1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 2);
        MenuResource result1 = results.get(0);
        assertNotNull(result1);
        assertNotNull(result1.getId());
        assertMenuResource(menuResource, result1);
        MenuResource result2 = results.get(1);
        assertNotNull(result2);
        assertMenuResource(menuResource2, result2);
    }


    @Test
    public void testFindUrlAccessResourcesByAuthority() throws Exception {
        testSave();
        List<UrlAccessResource> results = ResourceAssignment.findUrlAccessResourcesByAuthority(role1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 1);
        UrlAccessResource result = results.get(0);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void testFindUrlAccessResourcesByAuthorityHasPermission() throws Exception {
        testSave();
        UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试", "testFindUrlAccessResourcesByAuthorityHasPermission");
        urlAccessResource2.save();
        ResourceAssignment resourceAssignment2 = new ResourceAssignment(permission, urlAccessResource2);
        resourceAssignment2.save();
        role1.addPermission(permission);
        List<UrlAccessResource> results = ResourceAssignment.findUrlAccessResourcesByAuthority(role1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 2);

        UrlAccessResource result1 = results.get(0);
        assertNotNull(result1);
        assertNotNull(result1.getId());
        assertUrlAccessResource(urlAccessResource1, result1);

        UrlAccessResource result2 = results.get(1);
        assertNotNull(result2);
        assertNotNull(result2.getId());
        assertUrlAccessResource(urlAccessResource2, result2);
    }

    @Test
    public void testFindUrlAccessResourcesByAuthorities() throws Exception {
        testSave();
        List<UrlAccessResource> results = ResourceAssignment.findUrlAccessResourcesByAuthorities(Sets.newHashSet(role1));
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 1);
        UrlAccessResource result = results.get(0);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void testFindRoleBySecurityResource() throws Exception {
        testSave();
        List<Role> results = ResourceAssignment.findRoleBySecurityResource(urlAccessResource1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 1);
        Role actualRole = results.get(0);
        assertNotNull(actualRole);
        assertRole(role1, actualRole);
    }

    @Test
    public void testFindPermissionBySecurityResource() throws Exception {
        testSave();
        List<Permission> results = ResourceAssignment.findPermissionBySecurityResource(urlAccessResource1);
        assertFalse(results.isEmpty());
        assertTrue(results.size() == 1);
        Permission permission = results.get(0);
        assertNotNull(permission.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByIdIsNull() {
        ResourceAssignment.getById(null);
    }

    @Test
    public void testGetById() {
        ResourceAssignment resourceAssignment = new ResourceAssignment(role1, urlAccessResource1);
        resourceAssignment.save();
        assertNotNull(resourceAssignment.getId());
        ResourceAssignment actualResourceAssignment = ResourceAssignment.getById(resourceAssignment.getId());
        assertNotNull(actualResourceAssignment);
    }

}