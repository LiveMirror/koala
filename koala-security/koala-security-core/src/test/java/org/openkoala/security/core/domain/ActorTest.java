package org.openkoala.security.core.domain;


import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ActorTest extends AbstractSecurityIntegrationTestCase{
	
	@Test
	public void testAddActor() throws Exception {
		System.out.println("add Actor");
		User user = new User();
		user.setCreateOwner("admin");
		user.setDescription("测试");
		user.setEmail("test@foreveross.com");
		user.setName("测试");
		user.setUserAccount("test01");
		user.setPassword("000000");
		user.save();
	}
	
	@Test
	public void testExportAllData() throws Exception {
//		DbUnitUtils dbUnitUtils = DbUnitUtils.configFromClasspath("/jdbc.properties");
//		dbUnitUtils.exportData("dbunit", "data.xml");
	}
	
	@Test
	public void testFindAllRolesBy() throws Exception {
		List<Role> roles = User.findAllRolesBy("zhangsan");
		System.out.println(roles);
	}
}
