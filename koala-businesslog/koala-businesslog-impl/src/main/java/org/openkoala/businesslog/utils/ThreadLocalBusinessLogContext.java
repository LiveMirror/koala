package org.openkoala.businesslog.utils;

import java.util.HashMap;
import java.util.Map;

import static org.openkoala.businesslog.ContextKeyConstant.BUSINESS_METHOD;

/**
 * 线程变量
 * User: zjzhai
 * Date: 11/28/13
 * Time: 2:18 PM
 */
public class ThreadLocalBusinessLogContext {

    private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
        protected synchronized Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static Map<String, Object> get() {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : context.get().keySet()) {
            result.put(key, context.get().get(key));
        }
        return result;
    }


    public static void put(String key, Object value) {
        context.get().put(key, value);
    }

    public static Object getBusinessMethod(){
        return get().get(BUSINESS_METHOD);
    }


    public static void clear() {
        //contextMap.clear();
        context.set(new HashMap<String, Object>());
    }

    public static void putBusinessLogMethod(String blMappingValue) {
        put(BUSINESS_METHOD, blMappingValue);
    }
}
