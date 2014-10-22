package org.openkoala.security.core.domain;

import org.junit.Before;
import org.mockito.Mockito;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 权限集成测试基类
 *
 * @author lucas
 */
@ContextConfiguration(locations = {"classpath*:META-INF/spring/security-root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public abstract class AbstractDomainIntegrationTestCase extends KoalaBaseSpringTestCase {

	protected EncryptService passwordEncryptService;

	@Before
	public void setUp() {
		passwordEncryptService = Mockito.mock(EncryptService.class);
		User.setPasswordEncryptService(passwordEncryptService);
	}
}
