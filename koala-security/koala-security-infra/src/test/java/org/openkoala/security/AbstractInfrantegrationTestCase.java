package org.openkoala.security;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = {"classpath*:META-INF/spring/security-root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public abstract class AbstractInfrantegrationTestCase extends KoalaBaseSpringTestCase {
}
