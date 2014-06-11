package org.openkoala.security.application;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 权限集成测试基类
 * 
 * @author luzhao
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public abstract class AbstractSecurityIntegrationTestCase extends KoalaBaseSpringTestCase {
}
