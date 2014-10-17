package org.openkoala.security.application.systeminit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SystemInitFactoryTest {

    @Test
    public void testGetSystemInit() {
        SystemInit systemInit = SystemInitFactory.INSTANCE.getSystemInit();
        assertTrue(systemInit != null);
        assertEquals("考拉", systemInit.getUser().getName());
    }
}
