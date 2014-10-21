package org.openkoala.framework.i18n;

import java.io.File;
import java.io.IOException;

import org.openkoala.koalacommons.resourceloader.Resource;
import org.openkoala.koalacommons.resourceloader.impl.classpath.ClassPathResource;

/**
 * 资源加载器
 * @author zhuyuanbiao
 * @date 2014年1月8日 上午10:58:47
 *
 */
public class ResourceLoaderUtils {

    public static File getResource(String resourceName, Class<?> callingClass) {
        Resource resource = new ClassPathResource(resourceName, callingClass.getClassLoader());
        try {
			return resource.getFile();
		} catch (IOException e) {
			throw new FileNotFoundException(e);
		}
    }

}
