package org.openkoala.koalacommons.i18n;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import org.openkoala.framework.i18n.I18NManager;

/**
 * 国际化测试
 * @author lingen
 *
 */
public class I18nTest {


    @Ignore
	@Test
	public void shouldUsingDefaultLocale(){
		System.out.println(Locale.CHINA.getDisplayLanguage());
		String value = I18NManager.getMessage("name","zh_cn");
		Assert.assertTrue(value.equals("考拉"));
	}
	
	@Test
	public void shouldUsingENLocale(){
		String value = I18NManager.getMessage("name","en");
		Assert.assertTrue("Koala".equals(value));
	}
}
