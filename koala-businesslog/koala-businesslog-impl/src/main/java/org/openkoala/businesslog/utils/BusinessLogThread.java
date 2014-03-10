package org.openkoala.businesslog.utils;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.openkoala.businesslog.*;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_METHOD;

/**
 * 日志处理线程
 * User: zjzhai
 * Date: 12/15/13
 * Time: 6:45 PM
 */
public class BusinessLogThread implements Runnable {


    private Map<String, Object> context;

    private String BLMappingValue;

    public BusinessLogThread(Map<String, Object> context, String BLMappingValue) {
        this.context = context;
        this.BLMappingValue = BLMappingValue;
    }

    @Override
    public void run() {
        ThreadLocalBusinessLogContext.put(BUSINESS_METHOD, BLMappingValue);
        GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader());
        try {

            Class groovyClass = loader.parseClass(new File(getClass().getResource("/BusinesslogConfig.groovy").getFile()));

            // 调用实例中的某个方法
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

            if (groovyClass.getMethod(BLMappingValue) != null) {
                groovyObject.setProperty("context", context);
                System.out.println(groovyObject.invokeMethod(BLMappingValue, null));
            }
        } catch (IOException e) {
            throw new KoalaBusinessLogConfigException("There's a failure when read BusinesslogConfig.groovy", e);
        } catch (InstantiationException e) {
            throw new KoalaBusinessLogConfigException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new KoalaBusinessLogConfigException("IllegalAccessException", e);
        } catch (NoSuchMethodException e) {
            return;
        }
    }


}
