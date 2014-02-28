package org.openkoala.businesslog.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务日志映射
 * User: zjzhai
 * Date: 2/27/14
 * Time: 2:29 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BLMapping {
    public String value();
}
