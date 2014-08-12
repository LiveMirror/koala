package org.openkoala.security.core.domain;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.openkoala.security.core.CorrelationException;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.NullArgumentException;
import org.openkoala.security.core.UrlIsExistedException;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.openkoala.security.core.util.EntitiesHelper.*;

public class UrlAccessResourceTest extends AbstractDomainIntegrationTestCase {

	// ~ UrlAccessResource Save Test
	// ========================================================================================================

	@Test
	public void testSave() throws Exception {
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		assertNotNull(expected.getId());
		UrlAccessResource actual = UrlAccessResource.getBy(expected.getId());
		assertUrlAccessResource(expected, actual);
	}

	@Test(expected = NullArgumentException.class)
	public void testSaveNameIsNull() throws Exception {
		new UrlAccessResource(null, "/auth/test/**********");
	}

	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000000", "/auth/test/**********");
		urlAccessResource2.save();
	}

	@Test(expected = NullArgumentException.class)
	public void testSaveUrlIsNull() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000001", null);
		urlAccessResource2.save();
	}

	@Test(expected = UrlIsExistedException.class)
	public void testSaveUrlExisted() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("测试管理0000000001", "/auth/test/**********");
		urlAccessResource2.save();
	}

	@Test(expected = NullArgumentException.class)
	public void testChangeUrlIsNull() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		urlAccessResource.changeUrl(null);
	}

	@Test(expected = UrlIsExistedException.class)
	public void testChangeUrlisExisted() throws Exception {
		String url = "/auth/test01/**********";
		UrlAccessResource result1 = initUrlAccessResource();
		result1.save();
		assertNotNull(result1.getId());
		UrlAccessResource result2 = new UrlAccessResource("测试URL2", url);
		result2.save();
		assertNotNull(result2.getId());
		result1.changeUrl(url);
	}

	@Test
	public void testChangeUrl() throws Exception {
		String url = "/auth/test01/**********";
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		assertNotNull(expected.getId());
		expected.changeUrl(url);
		UrlAccessResource actual = UrlAccessResource.getBy(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = NullArgumentException.class)
	public void testChangeNameIsNull() throws Exception {
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		expected.changeName(null);
	}

	@Test(expected = NameIsExistedException.class)
	public void testChangeNameIsExisted() throws Exception {
		String name = "改变名称";
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		UrlAccessResource urlAccessResource2 = new UrlAccessResource(name, "/auth/test01/**********");
		urlAccessResource2.save();
		expected.changeName(name);
	}

	@Test
	public void testChangeName() throws Exception {
		String name = "改变名称";
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		expected.changeName(name);
		UrlAccessResource actual = UrlAccessResource.getBy(expected.getId());
		assertNotNull(actual);
		assertUrlAccessResource(expected, actual);
	}

	@Test
	public void testRemove() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		urlAccessResource.remove();
	}

	@Test(expected = CorrelationException.class)
	public void testRemvehasAuthorities() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		Role role = initRole();
		role.addSecurityResource(urlAccessResource);
		urlAccessResource.remove();
	}

	@Test
	public void testFindByName() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource loadUrlAccessResource = (UrlAccessResource) urlAccessResource.findByName(urlAccessResource
				.getName());
		assertNotNull(loadUrlAccessResource);
		assertUrlAccessResource(urlAccessResource, loadUrlAccessResource);
	}

	@Test
	public void testFindByUrl() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		UrlAccessResource loadUrlAccessResource = urlAccessResource.findByUrl(urlAccessResource.getUrl());
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

	@Test(expected = NullArgumentException.class)
	public void testGetByUrlIsNull() throws Exception {
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		expected.findByUrl(null);
	}

	@Test
	public void testGetByUrl() throws Exception {
		UrlAccessResource expected = initUrlAccessResource();
		expected.save();
		UrlAccessResource actual = expected.findByUrl(expected.getUrl());
		assertUrlAccessResource(expected, actual);
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
		List<String> names = UrlAccessResource.getRoleNames(Sets.newHashSet(role, role1));
		assertNotNull(names);
		assertTrue(names.size() == 2);
	}

	@Test
	public void testGetPermissionIdentifiers() throws Exception {
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		Authority permission1 = initPermission();
		permission1.save();
		Authority permission2 = new Permission("测试权限000002", "testPermission000002");
		permission2.save();

		permission1.addSecurityResource(urlAccessResource);
		permission2.addSecurityResource(urlAccessResource);
		assertNotNull(urlAccessResource.getAuthorities());

		List<String> identifiers = UrlAccessResource
				.getPermissionIdentifiers(Sets.newHashSet(permission1, permission2));
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
		UrlAccessResource urlAccessResource2 = new UrlAccessResource("urlAccessResource2adad",
				"/urlAccessResource2/urlAccessResource2");
		urlAccessResource2.save();
		urlAccessResources = UrlAccessResource.findAllUrlAccessResources();
		assertTrue(urlAccessResources.size() == 2);
	}

	@Test
	public void testFindAllAndRoles() throws Exception {
		init();
		List<UrlAccessResource> urlAccessResources = UrlAccessResource.findAllUrlAccessResources();
		assertFalse(urlAccessResources.isEmpty());
		Set<Authority> authorities = urlAccessResources.get(0).getAuthorities();
		assertFalse(authorities.isEmpty());
		for (Authority authority : authorities) {
			assertEquals("testRole0000000000", authority.getName());
		}
	}

	private void init() {
		Role role = initRole();
		role.save();
		UrlAccessResource urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		role.addSecurityResource(urlAccessResource);
	}
}
