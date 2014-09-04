package org.openkoala.koala.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openkoala.koala.annotation.parse.ParseGeneratedSourceCode;

/**
 * 标志Annotation,由ParseGeneratedSourceCode
 * 
 * 标志于类上，当此类有此注解，进行解析
 * @author lingen.liu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ParseAnnotation(type=ParseGeneratedSourceCode.class)
public @interface GeneratedSourceCode {

}
