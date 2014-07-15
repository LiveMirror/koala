package org.openkoala.security.core.domain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.UrlIsExistedException;

import com.google.common.collect.Sets;

import static org.openkoala.security.core.util.EntitiesHelper.*;

public class UrlAccessResourceTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testSave() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
	}

	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000000", "/auth/test/**********");
		urlAccessResource2.save();
	}
	
	@Test(expected = UrlIsExistedException.class)
	public void testSaveUrlExisted() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000001", "/auth/test/**********");
		urlAccessResource2.save();
	}

	@Test
	public void testUpdate() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource updateUrlAccessResource = new UrlAccessResource("测试管理0000000002 update", "/auth/test/**********12");
		updateUrlAccessResource.setId(urlAccessResource.getId());
		updateUrlAccessResource.update();
		UrlAccessResource loadUrlAccessResource = UrlAccessResource.getBy(updateUrlAccessResource.getId());
		assertNotNull(loadUrlAccessResource);
		assertUrlAccessResource(updateUrlAccessResource,loadUrlAccessResource);
	}
	
	@Test(expected = NameIsExistedException.class)
	public void testUpdateNameExisted() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000001", "/auth/test/**********11");
		urlAccessResource2.save();
		UrlAccessResource updateUrlAccessResource = new UrlAccessResource("测试管理0000000001", "/auth/test/**********12");
		updateUrlAccessResource.setId(urlAccessResource.getId());
		updateUrlAccessResource.update();
	}
	
	@Test(expected = UrlIsExistedException.class)
	public void testUpdateUrlExisted() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000001", "/auth/test/**********11");
		urlAccessResource2.save();
		UrlAccessResource updateUrlAccessResource = new UrlAccessResource("测试管理0000000002", "/auth/test/**********11");
		updateUrlAccessResource.setId(urlAccessResource.getId());
		updateUrlAccessResource.update();
	}
	
	@Test
	public void testFindByName() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource loadUrlAccessResource = (UrlAccessResource) urlAccessResource.findByName(urlAccessResource.getName());
		assertNotNull(loadUrlAccessResource);
		assertUrlAccessResource(urlAccessResource, loadUrlAccessResource);
	}
	
	@Test
	public void testFindByUrl() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource loadUrlAccessResource = (UrlAccessResource) urlAccessResource.findByUrl(urlAccessResource.getUrl());
		assertNotNull(loadUrlAccessResource);
		assertUrlAccessResource(urlAccessResource, loadUrlAccessResource);
	}

	@Test
	public void testGetById() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource loadUrlAccessResource = UrlAccessResource.getBy(urlAccessResource.getId());
		assertNotNull(loadUrlAccessResource);
	}

	@Test
	public void testGetRoleNames() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		Authority role = initRole();
		role.save();
		role.addSecurityResource(urlAccessResource);
		
		Authority role1 = new Role("testRole00000000000000000");
		role1.save();
		role1.addSecurityResource(urlAccessResource);
		
		assertNotNull(urlAccessResource.getAuthorities());
		List<String> names = UrlAccessResource.getRoleNames(Sets.newHashSet(role,role1));
		assertNotNull(names);
		assertTrue(names.size() == 2);
	}

	@Test
	public void testGetPermissionIdentifiers() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		Authority permission1 = initPermission();
		permission1.save();
		Authority permission2 = new Permission("测试权限000002","testPermission000002");
		permission2.save();
		
		permission1.addSecurityResource(urlAccessResource);
		permission2.addSecurityResource(urlAccessResource);
		assertNotNull(urlAccessResource.getAuthorities());
		
		List<String> identifiers = UrlAccessResource.getPermissionIdentifiers(Sets.newHashSet(permission1,permission2));
		assertNotNull(identifiers);
		assertTrue(identifiers.size() == 2);
	}

	@Test
	public void testFindAllUrlAccessResources() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		List<UrlAccessResource> urlAccessResources = UrlAccessResource.findAllUrlAccessResources();
		assertNotNull(urlAccessResources);
		assertTrue(urlAccessResources.size() == 1);
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("urlAccessResource2adad", "/urlAccessResource2/urlAccessResource2");
		urlAccessResource2.save();
		urlAccessResources = UrlAccessResource.findAllUrlAccessResources();
		assertTrue(urlAccessResources.size() == 2);
	}

}
