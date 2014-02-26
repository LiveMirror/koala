package org.openkoala.openci.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.openkoala.openci.AbstractIntegrationTest;
import org.openkoala.openci.application.DeveloperApplication;
import org.openkoala.openci.core.Developer;

public class DeveloperApplicationImplTest extends AbstractIntegrationTest {

	@Inject
	private DeveloperApplication developerApplication;
	
	@Test
	public void testCreateDeveloper() {
		Developer developer = new Developer("zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao@foreveross.com");
		developerApplication.createDeveloper(developer);
		assertEquals(developer, Developer.get(Developer.class, developer.getId()));
		Developer.findAll(Developer.class);
	}

	@Test
	public void testUpdateDeveloper() {
		Developer developer = new Developer("zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao@foreveross.com");
		developer.save();
		assertEquals("zhuyuanbiao", Developer.get(Developer.class, developer.getId()).getName());
		
		developer.setName("zhuyuanbiao2");
		developerApplication.updateDeveloperInfo(developer);
		assertEquals("zhuyuanbiao2", Developer.get(Developer.class, developer.getId()).getName());
	}
	
	@Test
	public void testPagingQueryDeveloper() {
		Developer developer1 = new Developer("zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao1@foreveross.com");
		Developer developer2 = new Developer("zhuyuanbiao2", "zhuyuanbiao2", "zhuyuanbiao", "zhuyuanbiao2@foreveross.com");
		developer1.save();
		developer2.save();
		
		List<Developer> developersInRepository = new ArrayList<Developer>();
		developersInRepository.add(developer1);
		developersInRepository.add(developer2);
		
		List<Developer> developersFromPagingQuery = developerApplication.pagingQeuryDevelopers(developer1, 0, 10).getData();
		assertEquals(1, developersFromPagingQuery.size());
//		assertEquals(developersInRepository, developersFromPagingQuery);
	}
	
	@Test
	public void testAbolishDeveloper() {
		Developer developer = new Developer("zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao@foreveross.com");
		developer.save();
		assertEquals(developer, Developer.get(Developer.class, developer.getId()));
		
		developerApplication.abolishDeveloper(developer);
		assertNull(Developer.get(Developer.class, developer.getId()));
	}
	
	@Test
	public void testAbolishDevelopers() {
		Developer developer1 = new Developer("zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao", "zhuyuanbiao1@foreveross.com");
		Developer developer2 = new Developer("zhuyuanbiao2", "zhuyuanbiao2", "zhuyuanbiao", "zhuyuanbiao2@foreveross.com");
		developer1.save();
		developer2.save();
		assertTrue(Developer.findAll(Developer.class).contains(developer1));
		assertTrue(Developer.findAll(Developer.class).contains(developer2));

		List<Developer> developers = new ArrayList<Developer>();
		developers.add(developer1);
		developers.add(developer2);

		
		developerApplication.abolishDevelopers(developers);
		assertNull(Developer.get(Developer.class, developer1.getId()));
		assertNull(Developer.get(Developer.class, developer2.getId()));
	}
	
}
