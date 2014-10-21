package org.openkoala.koalacommons.i18n;

import java.util.Locale;

import org.junit.Test;
import org.junit.Ignore;
import org.openkoala.framework.i18n.I18NManager;
import static org.junit.Assert.*;

/**
 * 国际化测试
 * @author lingen
 *
 */
public class I18nTest {


	@Test
	public void shouldUsingDefaultLocale(){
        String value = I18NManager.getMessage("name","zh_cn");
		//assertTrue(value.equals("考拉"));
        assertTrue(I18NManager.getMessage("login.msg","zh_TW").equals("陳剛"));
        assertTrue(I18NManager.getMessage("login.msg","en").equals("Glies"));
        assertTrue("Koala".equals( I18NManager.getMessage("name","en")));


    }
	
}
