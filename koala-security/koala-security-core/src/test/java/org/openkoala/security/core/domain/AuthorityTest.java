package org.openkoala.security.core.domain;

import java.util.List;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import static org.openkoala.security.core.util.EntitiesHelper.*;


public class AuthorityTest extends AbstractDomainIntegrationTestCase {

    @Test
    public void testAddSecurityResources() {
        Role role = initRole();
        role.save();
        UrlAccessResource urlAccessResource = initUrlAccessResource();
        urlAccessResource.save();
        MenuResource menuResource = initMenuResource();
        menuResource.save();
        role.addSecurityResources(Lists.newArrayList(urlAccessResource, menuResource));

        List<UrlAccessResource> actualUrlAccessResources = role.findUrlAccessResourceByAuthority();
        assertFalse(actualUrlAccessResources.isEmpty());
        assertTrue(actualUrlAccessResources.size() == 1);
        UrlAccessResource actualUrlAccessResource = actualUrlAccessResources.get(0);
        assertNotNull(actualUrlAccessResource);
        assertNotNull(actualUrlAccessResource.getId());
        assertUrlAccessResource(urlAccessResource, actualUrlAccessResource);

        List<MenuResource> actualMenuResources = role.findMenuResourceByAuthority();
        assertFalse(actualMenuResources.isEmpty());
        assertTrue(actualMenuResources.size() == 1);
        MenuResource actualMenuResource = actualMenuResources.get(0);
        assertNotNull(actualMenuResource);
        assertNotNull(actualMenuResource.getId());
        assertMenuResource(menuResource, actualMenuResource);
    }

    @Test
    public void testTerminateSecurityResources() {
        Role role = initRole();
        role.save();
        UrlAccessResource urlAccessResource = initUrlAccessResource();
        urlAccessResource.save();
        MenuResource menuResource = initMenuResource();
        menuResource.save();
        role.addSecurityResources(Lists.newArrayList(urlAccessResource, menuResource));
        role.terminateSecurityResources(Sets.newHashSet(urlAccessResource, menuResource));

        List<MenuResource> actualMenuResources = role.findMenuResourceByAuthority();
        assertTrue(actualMenuResources.isEmpty());

        List<UrlAccessResource> actualUrlAccessResources = role.findUrlAccessResourceByAuthority();
        assertTrue(actualUrlAccessResources.isEmpty());
    }

}
