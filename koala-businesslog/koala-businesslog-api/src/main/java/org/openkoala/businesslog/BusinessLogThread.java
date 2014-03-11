package org.openkoala.businesslog;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.runtime.GStringImpl;
import org.openkoala.businesslog.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志处理线程
 * User: zjzhai
 * Date: 12/15/13
 * Time: 6:45 PM
 */
public class BusinessLogThread implements Runnable {

    private static final String LOG_KEY = "log";

    private static final String CATEGORY_KEY = "category";

    private static final String STANDALONE_GROOVY_CONFIG_NAME = "/BusinessLogConfig.groovy";

    private static final String GROOVY_CONFIG_DIR = "/businessLogConfig";

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

        if (isStandaloneConfig()) return getGroovyObject(getGroovyClass(getStandaloneConfigFile()));

        if (getClass().getResource(GROOVY_CONFIG_DIR) == null)
            throw new KoalaBusinessLogConfigException("Not found any businesslog config, you need a " + STANDALONE_GROOVY_CONFIG_NAME + " or businessLogConfig director");

        File configDir = new File(getClass().getResource(GROOVY_CONFIG_DIR).getFile());

        if (!configDir.exists() || !configDir.isDirectory())
            throw new KoalaBusinessLogConfigException("Not found any businesslog config, you need a " + STANDALONE_GROOVY_CONFIG_NAME + " or businessLogConfig director");


        for (File each : configDir.listFiles(new GroovyFileNameFilter())) {
            try {

                Class clazz = getGroovyClass(each);

                if (getGroovyClass(each).getMethod(businessMethod) == null) continue;

                return getGroovyObject(clazz);

            } catch (NoSuchMethodException e) {
                continue;
            }
        }

        return null;
    }

    private boolean isStandaloneConfig() {
        return getStandaloneConfigFile() != null && getStandaloneConfigFile().exists();
    }

    private File getStandaloneConfigFile() {
        if (getClass().getResource(STANDALONE_GROOVY_CONFIG_NAME) == null) return null;

        return new File(getClass().getResource(STANDALONE_GROOVY_CONFIG_NAME).getFile());
    }


    private Class getGroovyClass(File configFile) {
        try {
            return new GroovyClassLoader(getClass().getClassLoader()).parseClass(configFile);
        } catch (IOException e) {
            throw new KoalaBusinessLogConfigException("There's a failure when read BusinesslogConfig.groovy", e);
        }
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

    private class GroovyFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".groovy");
        }
    }

}
