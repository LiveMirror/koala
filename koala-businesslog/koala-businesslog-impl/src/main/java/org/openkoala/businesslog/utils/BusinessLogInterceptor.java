package org.openkoala.businesslog.utils;

import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_METHOD;
import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_METHOD_EXECUTE_ERROR;
import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_METHOD_RETURN_VALUE_KEY;
import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_OPERATION_IP;
import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_OPERATION_TIME;
import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_OPERATION_USER;
import static org.openkoala.businesslog.ContextKeyConstant.PRE_OPERATOR_OF_METHOD_KEY;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.aspectj.lang.JoinPoint;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.KoalaBusinessLogConfigException;
import org.openkoala.businesslog.MethodAlias;
import org.openkoala.koalacommons.resourceloader.impl.classpath.ClassPathResource;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * User: zjzhai Date: 11/28/13 Time: 11:38 AM
 */
public class BusinessLogInterceptor {

    private static final String BUSINESS_LOG_CONFIG_PROPERTIES_NAME = "koala-businesslog.properties";

    private static final String LOG_ENABLE = "kaola.businesslog.enable";

    private static Boolean isLogEnabled;
    
    @Inject
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Inject
    private BusinessLogExporter businessLogExporter;

    public void logAfter(JoinPoint joinPoint, Object result) {
        log(joinPoint, result, null);
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable error) {
        log(joinPoint, null, error);
    }

    public void log(JoinPoint joinPoint, Object result, Throwable error) {

        String BLMappingValue = getBLMapping(joinPoint);

        /**
         * 日志开关及防止重复查询
         */
        if (!isLogEnabled() || ThreadLocalBusinessLogContext.get().get(BUSINESS_METHOD) != null) {
            return;
        }

        BusinessLogThread businessLogThread = new BusinessLogThread(
                Collections.unmodifiableMap(createDefaultContext(joinPoint, result, error)),
                BLMappingValue,
                getBusinessLogExporter());

        if (null == getThreadPoolTaskExecutor()) {
            System.err.println("ThreadPoolTaskExecutor is not set or null");
            businessLogThread.run();
        } else {
            getThreadPoolTaskExecutor().execute(businessLogThread);
        }
    }

    private boolean isLogEnabled() {
    	if (isLogEnabled != null) {
    		return isLogEnabled;
    	}
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new ClassPathResource(BUSINESS_LOG_CONFIG_PROPERTIES_NAME).getFile()));
            isLogEnabled = Boolean.valueOf(properties.getProperty(LOG_ENABLE, "true"));
            return isLogEnabled;
        } catch (IOException e) {
            throw new KoalaBusinessLogConfigException("failure when read " + BUSINESS_LOG_CONFIG_PROPERTIES_NAME, e);
        }

    }

    private Map<String, Object> createDefaultContext(JoinPoint joinPoint,
                                                     Object result, Throwable error) {
        Map<String, Object> context = ThreadLocalBusinessLogContext.get();

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.put(PRE_OPERATOR_OF_METHOD_KEY + i, args[i]);
        }

        context.put(BUSINESS_METHOD_RETURN_VALUE_KEY, result);

        if (null != error) {
            context.put(BUSINESS_METHOD_EXECUTE_ERROR, error.getCause());
        }
        context.put(BUSINESS_OPERATION_USER, ThreadLocalBusinessLogContext.get().get(BUSINESS_OPERATION_USER));
        context.put(BUSINESS_OPERATION_IP, ThreadLocalBusinessLogContext.get().get(BUSINESS_OPERATION_IP));
        context.put(BUSINESS_METHOD, getBLMapping(joinPoint));
        context.put(BUSINESS_OPERATION_TIME, new Date());
        return context;

    }

    private String getBLMapping(JoinPoint joinPoint) {
        Method method = invocationMethod(joinPoint);
        if (method.isAnnotationPresent(MethodAlias.class)) {
            return method.getAnnotation(MethodAlias.class).value();
        }
        return joinPoint.getSignature().toString();
    }

    private Method invocationMethod(JoinPoint joinPoint) {
        try {
            Field methodInvocationField = MethodInvocationProceedingJoinPoint.class.getDeclaredField("methodInvocation");
            methodInvocationField.setAccessible(true);
            ProxyMethodInvocation methodInvocation = (ProxyMethodInvocation) methodInvocationField.get(joinPoint);
            return methodInvocation.getMethod();
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }


    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        if (null == threadPoolTaskExecutor) {
            threadPoolTaskExecutor = InstanceFactory.getInstance(ThreadPoolTaskExecutor.class, "threadPoolTaskExecutor");
        }

        return threadPoolTaskExecutor;
    }

    public BusinessLogExporter getBusinessLogExporter() {
        if (null == businessLogExporter) {
            businessLogExporter = InstanceFactory.getInstance(BusinessLogExporter.class, "businessLogExporter");
        }
        return businessLogExporter;
    }


}
