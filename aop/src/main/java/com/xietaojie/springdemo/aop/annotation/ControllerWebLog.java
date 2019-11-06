package com.xietaojie.springdemo.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xietaojie1992
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerWebLog {

    /**
     * 所调用接口的名称
     */
    String name();

    /**
     * 操作日志是否需要持久化存储
     */
    boolean intoDb() default false;
}
