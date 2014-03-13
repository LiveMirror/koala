package org.openkoala.businesslog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务方法别名
 * User: zjzhai
 * Date: 2/27/14
 * Time: 2:29 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAlias {
    public String value();
}
