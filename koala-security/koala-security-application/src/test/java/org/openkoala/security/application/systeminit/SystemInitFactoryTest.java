package org.openkoala.security.application.systeminit;

import org.junit.Test;
import org.openkoala.security.application.systeminit.SystemInit;
import org.openkoala.security.application.systeminit.SystemInitFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SystemInitFactoryTest {

    @Test
    public void testGetSystemInit() {
        SystemInit systemInit = SystemInitFactory.INSTANCE.getSystemInit();
        assertTrue(systemInit != null);
        assertEquals("考拉", systemInit.getUser().getName());
    }
}
