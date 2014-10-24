package org.openkoala.security.application;

import org.junit.Test;
import org.openkoala.security.core.domain.*;

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.*;

public class SecurityDBInitApplicationTest extends AbstractApplicationIntegrationTestCase {

    @Inject
    private SecurityDBInitApplication securityDBInitApplication;

    @Test
    public void testInitUser() throws Exception {
        User user = securityDBInitApplication.initUser();
        assertNotNull(user);
        assertNotNull(user.getId());
    }

    @Test
    public void testInitRole() throws Exception {
        Role role = securityDBInitApplication.initRole();
        assertNotNull(role);
        assertNotNull(role.getId());
    }

    @Test
    public void testInitMenuResources() throws Exception {
        List<MenuResource> resources = securityDBInitApplication.initMenuResources();
        assertFalse(resources.isEmpty());
        assertTrue(resources.size() == 7); // 8 - 1
    }

    @Test
    public void testInitPageElementResources() throws Exception {
        List<PageElementResource> resources = securityDBInitApplication.initPageElementResources();
        assertFalse(resources.isEmpty());
        assertTrue(resources.size() == 26); //35 - 9
    }

    @Test
    public void testInitUrlAccessResources() throws Exception {
        List<UrlAccessResource> resources = securityDBInitApplication.initUrlAccessResources();
        assertFalse(resources.isEmpty());
        assertTrue(resources.size() == 51);
    }
}