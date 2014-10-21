package org.openkoala.security.facade.impl;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 权限集成测试基类
 * 
 * @author lucas
 * 
 */
@ContextConfiguration(locations = {"classpath*:META-INF/spring/security-root.xml","classpath*:META-INF/spring/security-extend.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public abstract class AbstractFacadeIntegrationTestCase extends KoalaBaseSpringTestCase {
	
}
