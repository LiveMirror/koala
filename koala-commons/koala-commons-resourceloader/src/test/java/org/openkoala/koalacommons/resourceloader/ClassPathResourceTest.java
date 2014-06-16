package org.openkoala.koalacommons.resourceloader;


import java.io.File;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;

import org.openkoala.koalacommons.resourceloader.impl.classpath.ClassPathResource;

public class ClassPathResourceTest {

	private static final String FILE_NAME = "test.properties";
	private Resource resource = null;
	
	@Test
	public void testGetFileName() {
		resource = new ClassPathResource(FILE_NAME);
		assertEquals(FILE_NAME, resource.getFilename());
	}
	
	@Test
	public void testGetFiles() throws IOException {
		resource = new ClassPathResource("i18n");
		File file = resource.getFile();
		assertEquals(2, file.listFiles().length);
	}
	
	@Test
	public void testGetFileNameByOtherClassLoader() {
		resource = new ClassPathResource(FILE_NAME, String.class);
		assertEquals(FILE_NAME, resource.getFilename());
	}
	
}
