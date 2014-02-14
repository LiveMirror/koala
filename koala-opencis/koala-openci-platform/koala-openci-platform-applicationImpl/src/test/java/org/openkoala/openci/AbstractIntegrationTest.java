package org.openkoala.openci;

import org.junit.After;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.domain.InstanceFactory;

@TransactionConfiguration(transactionManager = "transactionManager_opencis", defaultRollback = true) 
public abstract class AbstractIntegrationTest extends KoalaBaseSpringTestCase {

	protected static final String TEST_USERNAME = "test";
	
	@After
	public void tearDown() throws Exception {
		InstanceFactory.setInstanceProvider(null);
	}

}
