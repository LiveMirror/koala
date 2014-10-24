package org.openkoala.security.core.domain;

import org.junit.Test;
import org.openkoala.security.core.IdentifierIsExistedException;
import org.openkoala.security.core.NameIsExistedException;

import static org.junit.Assert.assertNotNull;
import static org.openkoala.security.core.util.EntitiesHelper.assertPageElementResource;
import static org.openkoala.security.core.util.EntitiesHelper.initPageElementResource;

/**
 * @author luzhao
 */
public class PageElementResourceTest extends AbstractDomainIntegrationTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNameIsNull() throws Exception {
        new PageElementResource(null, "aaaa");
    }

    @Test(expected = NameIsExistedException.class)
    public void testSaveNameExisted() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        new PageElementResource("用户添加0000000000", "aaaa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveIdentifierIsNull() throws Exception {
        new PageElementResource("用户添加0000000000", null);
    }

    @Test(expected = IdentifierIsExistedException.class)
    public void testSaveIdentifierExisted() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        new PageElementResource("用户添加0000000001", "userAdd");
    }

    @Test
    public void testSave() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        assertNotNull(pageElementResource.getId());
        PageElementResource loadPageElementResource = PageElementResource.getBy(pageElementResource.getId());
        assertNotNull(loadPageElementResource);
        assertPageElementResource(pageElementResource, loadPageElementResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeNameIsNull() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        pageElementResource.changeName(null);
    }

    @Test(expected = NameIsExistedException.class)
    public void testChangeNameIsExisted() throws Exception {
        String name = "update用户添加0000000000";
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        PageElementResource updatePageElementResource = new PageElementResource(name, "aaaa");
        updatePageElementResource.save();
        pageElementResource.changeName(name);
    }

    @Test
    public void testChangeName() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        pageElementResource.changeName("update用户添加0000000000");
        PageElementResource loadPageElementResource = PageElementResource.getBy(pageElementResource.getId());
        assertNotNull(loadPageElementResource);
        assertPageElementResource(pageElementResource, loadPageElementResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeIdentifierIsNull() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        pageElementResource.changeIdentifier(null);
    }

    @Test(expected = IdentifierIsExistedException.class)
    public void testChangeIdentifierIsExisted() throws Exception {
        String identifier = "userManagerAdd";
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        PageElementResource updatePageElementResource = new PageElementResource("用户管理添加", identifier);
        updatePageElementResource.save();
        pageElementResource.changeIdentifier(identifier);
    }

    @Test
    public void testChangeIdentifier() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        pageElementResource.changeIdentifier("userManagerAdd");
        PageElementResource loadPageElementResource = PageElementResource.getBy(pageElementResource.getId());
        assertNotNull(loadPageElementResource);
        assertPageElementResource(pageElementResource, loadPageElementResource);
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
        PageElementResource loadPageElementResource = PageElementResource.getBy(pageElementResource.getName());
        assertNotNull(loadPageElementResource);
        assertNotNull(loadPageElementResource.getId());
        assertPageElementResource(pageElementResource, loadPageElementResource);
    }

    @Test
    public void testGetById() throws Exception {
        PageElementResource pageElementResource = initPageElementResource();
        pageElementResource.save();
        PageElementResource loadPageElementResource = PageElementResource.getBy(pageElementResource.getId());
        assertNotNull(loadPageElementResource);
        assertPageElementResource(pageElementResource, loadPageElementResource);
    }
}
