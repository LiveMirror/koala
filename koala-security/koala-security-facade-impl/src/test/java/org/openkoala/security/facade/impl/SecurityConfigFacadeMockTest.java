package org.openkoala.security.facade.impl;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.application.SecurityConfigApplication;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.impl.assembler.UserAssembler;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TODO Mock
 * 
 * @author luzhao
 * 
 */
@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserAssembler.class })
public class SecurityConfigFacadeMockTest {

	@Mock
	private SecurityConfigApplication securityConfigApplication;

	@Mock
	private SecurityAccessApplication securityAccessApplication;

	private SecurityConfigFacade securityConfigFacade = new SecurityConfigFacadeImpl();

	/*@Test
	public void testCreateUser() throws Exception {
		CreateUserCommand command = initCreateUserCommand();
		User user = new User("张三", "zhangsan");
		when(UserAssembler.toUser(command)).thenReturn(user);
		Mockito.doThrow(new UserAccountIsExistedException()).when(UserAssembler.toUser(command));
		Assert.assertEquals("用户账号:" + command.getUserAccount() + "已经存在。",securityConfigFacade.createUser(command).getMessage());
	}*/
}

















