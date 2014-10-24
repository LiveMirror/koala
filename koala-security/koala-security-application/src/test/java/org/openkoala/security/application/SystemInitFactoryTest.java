package org.openkoala.security.application;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.security.application.systeminit.SystemInit;
import org.openkoala.security.application.systeminit.SystemInitFactory;
import org.openkoala.security.core.domain.*;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

@Ignore
public class SystemInitFactoryTest extends AbstractApplicationIntegrationTestCase{

    private SystemInit init;

    @Before
    public void setup() {
        init = SystemInitFactory.INSTANCE.getSystemInit("/META-INF/systemInit/systemInit.xml");
    }

    @Test
    public void testGetSystemInit() {
        assertNotNull(init);
        assertEquals("考拉", init.getUser().getName());
    }

    @Test
    public void testInitUser() throws Exception {
        User user = init.createUser();
        assertNotNull(user);
        assertNotNull(user.getId());
    }

    @Test
    public void testInitRole() throws Exception {
        Role role = init.createRole();
        assertNotNull(role);
        assertNotNull(role.getId());
    }

    @Test
    public void testInitMenuResources() throws Exception {
        List<MenuResource> resources = init.createMenuResourceAndReturnNeedGrant();
        assertFalse(resources.isEmpty());
        assertTrue(resources.size() == 7); // 8 - 1
    }

    @Test
    public void testInitPageElementResources() throws Exception {
        List<PageElementResource> resources = init.createPageElementResources();
        assertFalse(resources.isEmpty());
        assertTrue(resources.size() == 26); //35 - 9
    }

    @Test
    public void testInitUrlAccessResources() throws Exception {
        List<UrlAccessResource> resources = init.createUrlAccessResources();
        assertFalse(resources.isEmpty());
        assertTrue(resources.size() == 51);
    }
}
