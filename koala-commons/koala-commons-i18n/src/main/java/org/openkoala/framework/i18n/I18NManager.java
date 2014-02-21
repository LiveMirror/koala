package org.openkoala.framework.i18n; 

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.dayatang.i18n.NoSuchMessageException;
import org.dayatang.i18n.support.I18nServiceAccessor;
import org.openkoala.framework.i18n.service.ResourceBundleI18nServiceImpl;

/**
 * I18NManager
 * @author Ken
 * @since 2013-01-11
 *
 */
public class I18NManager {
	
	
	// 国际资源文件存放路径
	private static final String I18N_PATH = "i18n";
	
	// 默认地区
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    // 用于保存basename
	private static Set<String> basename = new HashSet<String>();

	private static ResourceBundleI18nServiceImpl resourceBundleI18nServiceImpl;
	
	private I18NManager() {
		
	}
	
	static {
		init();
	}
	
	/**
	 * 初始化
	 */
	private static void init() {
		if (getURL() != null) {
			File file = new File(getURL().getFile());
			handleBasename(file, I18N_PATH);
		}
	}
	
	/**
	 * 递归处理basename
	 * @param file
	 * @param sb
	 */
	private static void handleBasename(File file, String sb) {
		for (File f : file.listFiles()) {
			if (f.getName().lastIndexOf("properties") != -1) {
				if (f.getName().indexOf("_") == -1) {
					basename.add(sb + "." + f.getName().substring(0, f.getName().indexOf(".")));
				} else {
					basename.add(sb + "." + f.getName().substring(0, f.getName().indexOf("_")));
				}
			}
			if (f.isDirectory()) {
				handleBasename(f, sb + "." + f.getName());
			}
		}
	}
	
	private static URL getURL() {
		return ResourceLoaderUtils.getResource(I18N_PATH, I18NManager.class);
	}
	

	/**
	 * 获取国际化信息
	 * @param key
	 * @param locale
	 * @return
	 */
	public static String getMessage(String key, String locale) {
		initResourceBundleI18nServiceImpl();

		resourceBundleI18nServiceImpl.setBasenames(basename.toArray(new String[basename.size()]));
		I18nServiceAccessor accessor = resourceBundleI18nServiceImpl.getAccessor();

		// 如果没有指定locale属性，就用默认的Locale
		if (locale == null || "".equals(locale)) {
			String message;

			try {
				message = accessor.getMessage(key, DEFAULT_LOCALE);
			} catch (NoSuchMessageException e) {
				// 如果key在资源文件中不存在，就直接把key作为默认值返回
				return accessor.getMessage(key, key);
			}

			return message;
		}

		return accessor.getMessage(key, new Locale(locale));
	}

	private static void initResourceBundleI18nServiceImpl() {
		if (resourceBundleI18nServiceImpl == null) {
			resourceBundleI18nServiceImpl = new ResourceBundleI18nServiceImpl();
		}
	}
	
	/**
	 * 获取国际化信息（使用默认的Locale）
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
	
		return getMessage(key, null);
	}
	
}
