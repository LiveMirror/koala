package org.openkoala.security.infra.credential;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by luzhao on 14-9-11.
 */
@ContextConfiguration(locations = {"classpath*:META-INF/spring/security-root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = true)
public abstract class AbstractInfrantegrationTestCase extends KoalaBaseSpringTestCase {
}
