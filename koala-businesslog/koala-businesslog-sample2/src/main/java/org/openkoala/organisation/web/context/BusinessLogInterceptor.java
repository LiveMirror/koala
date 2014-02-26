package org.openkoala.organisation.web.context;

import org.aspectj.lang.JoinPoint;

/**
 * User: zjzhai
 * Date: 2/26/14
 * Time: 4:18 PM
 */
public class BusinessLogInterceptor {
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("++++++++++++++++++");
        System.out.println(joinPoint.getSignature().toString());

    }

    public void afterThrowing(JoinPoint joinPoint, Throwable error) {

    }
}
