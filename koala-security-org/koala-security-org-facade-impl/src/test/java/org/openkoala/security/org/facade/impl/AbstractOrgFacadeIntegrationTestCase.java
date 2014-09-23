package org.openkoala.security.org.facade.impl;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by luzhao on 14-8-29.
 */
@ContextConfiguration(locations = {"classpath*:META-INF/spring/security-org-root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public abstract class AbstractOrgFacadeIntegrationTestCase extends KoalaBaseSpringTestCase {
}
