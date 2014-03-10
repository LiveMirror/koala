package org.openkoala.businesslog;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 集成测试基类。
 * 
 * @author xmfang
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManager",
        defaultRollback = true)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/root.xml"})
public abstract class AbstractIntegrationTest extends KoalaBaseSpringTestCase {
	
}
