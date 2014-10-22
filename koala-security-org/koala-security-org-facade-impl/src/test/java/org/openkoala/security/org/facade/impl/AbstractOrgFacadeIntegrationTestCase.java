package org.openkoala.security.org.facade.impl;

import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = {"classpath*:META-INF/spring/security-org-root.xml","classpath*:META-INF/spring/security-extend.xml"})
@TransactionConfiguration(transactionManager = "transactionManager_org", defaultRollback = true)
public abstract class AbstractOrgFacadeIntegrationTestCase extends KoalaBaseSpringTestCase {
}
