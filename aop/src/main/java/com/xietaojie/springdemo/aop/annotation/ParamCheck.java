package com.xietaojie.springdemo.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数不为空
 *
 * @author xietaojie1992
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamCheck {

    /**
     * 是否为空，默认不为空
     */
    boolean notNull() default true;
}
