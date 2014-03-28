package org.openkoala.koalacommons.i18n;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.framework.i18n.I18NManager;

/**
 * 国际化测试
 * @author lingen
 *
 */
@Ignore
public class I18nTest {

	@Test
	public void testI18n(){
		String value = I18NManager.getMessage("name");
		Assert.assertTrue(value.equals("考拉"));
	}
	
	@Test
	public void testUsI18n(){
		String value = I18NManager.getMessage("name","en");
		Assert.assertTrue("Koala".equals(value));
	}
}
