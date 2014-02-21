package org.openkoala.openci.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.inject.Inject;

import org.dayatang.domain.AbstractEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.openci.AbstractIntegrationTest;
import org.openkoala.openci.EntityNullException;
import org.openkoala.openci.application.OpenciApplication;
import org.openkoala.openci.core.Developer;

public class OpenciApplicationImplTest extends AbstractIntegrationTest {
	
	@Inject
	private OpenciApplication openciApplication;

	private static final String NAME = "test";
	private Developer developer;
	
	@Test
	public void testSave() {
		openciApplication.saveEntity(developer);
		assertNotNull(developer.getId());
		assertEquals(developer, Developer.get(Developer.class, developer.getId()));
	}
	
	@Test(expected = EntityNullException.class)
	public void testSaveIfEntityIsNull() {
		openciApplication.saveEntity(null);
	}
	
	@Test
	public void testRemove() {
		openciApplication.saveEntity(developer);
		developer.remove();
		assertNull(AbstractEntity.get(Developer.class, developer.getId()));
	}
	
	@Test(expected = EntityNullException.class)
	public void testRemoveIfEntityIsNull() {
		openciApplication.removeEntity(null);
	}
	
	@Before
	public void init() {
		developer = new Developer(NAME, NAME, NAME, NAME);
	}
	
	@After
	public void destory() {
		List<Developer> developers = Developer.findAll(Developer.class);
		if (developers.size() > 0) {
			for (Developer each : developers) {
				each.remove();
			}
		}
	}
	
}
