package org.openkoala.security.infra.credential;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.security.AbstractInfrantegrationTestCase;
import org.openkoala.security.core.domain.User;
import org.springframework.test.context.transaction.TransactionConfiguration;

@Ignore
@TransactionConfiguration(transactionManager = "transactionManager_security", defaultRollback = false)
public class BigDataTest extends AbstractInfrantegrationTestCase {

    @Test
    public void testUserBigData10000() {
        for (int i = 1; i < 500; i++) {
            User result = new User("自动生成测试用户" + i, "autoTest" + i);
            result.setCreateOwner("admin");
            result.setDescription("测试");
            result.save();
        }
    }
}
