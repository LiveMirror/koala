package org.openkoala.security.application.systeminit;

import org.junit.Test;

public class SystemInitFactoryTest {

	@Test
	public void testGetSystemInit() {
		SystemInit systemInit = SystemInitFactory.INSTANCE.getSystemInit();
		assert systemInit != null;
		assert systemInit.getUser().getName().equals("超级管理员");
	}
	
}
