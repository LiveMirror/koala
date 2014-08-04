package org.openkoala.security.core.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openkoala.security.core.NameIsExistedException;

import static org.openkoala.security.core.util.EntitiesHelper.*;

/**
 * @author luzhao
 * 
 */
public class PageElementResourceTest extends AbstractDomainIntegrationTestCase {

	@Test
	public void testSave() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		pageElementResource.save();
		assertNotNull(pageElementResource.getId());
		PageElementResource loadPageElementResource = PageElementResource.getBy(pageElementResource.getId());
		assertNotNull(loadPageElementResource);
		assertPageElementResource(pageElementResource, loadPageElementResource);
	}

	@Test(expected = NameIsExistedException.class)
	public void testSaveNameExisted() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		pageElementResource.save();
		PageElementResource pageElementResource2 = new PageElementResource("用户添加0000000000","aaaa");
		pageElementResource2.save();
	}

	@Test
	public void testUpdate() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		pageElementResource.save();
		PageElementResource updatePageElementResource = new PageElementResource("update用户添加0000000000","aaaa");
		updatePageElementResource.setId(pageElementResource.getId());
		updatePageElementResource.update();
	}

	@Test(expected = NameIsExistedException.class)
	public void testUpdateNameExisted() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		PageElementResource pageElementResource1 = new PageElementResource("update用户添加0000000000","aaaa");
		pageElementResource1.save();
		pageElementResource.save();
		PageElementResource updatePageElementResource = new PageElementResource("update用户添加0000000000","bbbb");
		updatePageElementResource.setId(pageElementResource.getId());
		updatePageElementResource.update();
		PageElementResource loadPageElementResource = PageElementResource.getBy(updatePageElementResource.getId());
		assertNotNull(loadPageElementResource);
		assertPageElementResource(updatePageElementResource, loadPageElementResource);
	}

	@Test
	public void testFindByName() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		pageElementResource.save();
		PageElementResource loadPageElementResource = (PageElementResource) pageElementResource.findByName(pageElementResource.getName());
		assertNotNull(loadPageElementResource);
		assertNotNull(loadPageElementResource.getId());
		assertPageElementResource(pageElementResource, loadPageElementResource);
	}

	@Test
	public void testGetByName() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		pageElementResource.save();
		PageElementResource loadPageElementResource =  PageElementResource.getBy(pageElementResource.getName());
		assertNotNull(loadPageElementResource);
		assertNotNull(loadPageElementResource.getId());
		assertPageElementResource(pageElementResource, loadPageElementResource);
	}

	@Test
	public void testGetById() throws Exception {
		PageElementResource pageElementResource = initPageElementResource();
		pageElementResource.save();
		PageElementResource loadPageElementResource =  PageElementResource.getBy(pageElementResource.getId());
		assertNotNull(loadPageElementResource);
		assertPageElementResource(pageElementResource, loadPageElementResource);
	}

}
