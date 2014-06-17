package org.openkoala.businesslog.utils;

import groovy.lang.GroovyObject;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.groovy.runtime.GStringImpl;
import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogBaseException;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.KoalaBusinessLogConfigException;

/**
 * 日志处理线程
 * User: zjzhai
 * Date: 12/15/13
 * Time: 6:45 PM
 */
public class BusinessLogThread implements Runnable {

    private static final String LOG_KEY = "log";

    private static final String CATEGORY_KEY = "category";

    private Map<String, Object> context;

    private String BLMappingValue;

    /**
     * 日志导出器
     */
    private BusinessLogExporter businessLogExporter;

    public BusinessLogThread(Map<String, Object> context, String BLMappingValue, BusinessLogExporter businessLogExporter) {
        this.context = context;
        this.BLMappingValue = BLMappingValue;
        this.businessLogExporter = businessLogExporter;
    }

    @Override
    public void run() {
        ThreadLocalBusinessLogContext.putBusinessLogMethod(BLMappingValue);
        try {
            GroovyObject groovyObject = getGroovyConfig(BLMappingValue);

            if (groovyObject == null) return;

            groovyObject.setProperty("context", Collections.unmodifiableMap(context));

            businessLogExporter.export(
                    createBusinessLog(groovyObject.invokeMethod(BLMappingValue, null))
            );
        } catch (IOException e) {
            throw new KoalaBusinessLogConfigException("There's a failure when read BusinesslogConfig.groovy", e);
        }
    }

    private BusinessLog createBusinessLog(Object object) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.addContext(context);
        if (object instanceof LinkedHashMap) {
            Map<String, String> map = (LinkedHashMap<String, String>) object;
            businessLog.setLog(map.get(LOG_KEY));
            businessLog.setCategory(map.get(CATEGORY_KEY));
        } else if (object instanceof GStringImpl || object instanceof String) {
            businessLog.setLog(object.toString());
        } else {
            throw new BusinessLogBaseException("failure to execute groovy");
        }
        return businessLog;
    }

    private GroovyObject getGroovyConfig(String businessMethod) throws IOException {
    	GroovyObjectClassCache groovyObjectClassCache = new GroovyObjectClassCache();
    	
        if (groovyObjectClassCache.isStandaloneConfig()) return getGroovyObject(groovyObjectClassCache.getStandaloneGroovyObjectClass());

        for (Class each : groovyObjectClassCache.getGroovyObjectClasses()) {
            try {
                if (each.getMethod(businessMethod) == null) continue;

                return getGroovyObject(each);

            } catch (NoSuchMethodException e) {
                continue;
            }
        }

        return null;
    }

	private GroovyObject getGroovyObject(Class clazz) {
        try {
            if (null == clazz) throw new KoalaBusinessLogConfigException("The config must be a groovy class");

            return (GroovyObject) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new KoalaBusinessLogConfigException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new KoalaBusinessLogConfigException("IllegalAccessException", e);
        }
    }

}
