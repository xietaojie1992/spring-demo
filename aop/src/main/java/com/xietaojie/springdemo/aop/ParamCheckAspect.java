package com.xietaojie.springdemo.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面类，定义切点和通知
 *
 * @author xietaojie1992
 */
@Aspect
@Component
public class ParamCheckAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 切点（对连接点进行拦截的定义），可以是一个表达式，也可以是一个注解
     * 这里的切点是 HelloController 类的 hello 方法
     */
    @Pointcut("execution (public * com.xietaojie.springdemo.rest.HelloController.hello(..))")
    public void checkParam() {
    }

    @Before("checkParam()")
    public void doBefore() {
        logger.info("doBefore");
    }

    @After("checkParam()")
    public void doAfter() {
        logger.info("doAfter");
    }

    @AfterReturning("checkParam()")
    public void doAfterReturning() {
        logger.info("doAfterReturning");
    }

    @AfterThrowing("checkParam()")
    public void doAfterThrowing() {
        logger.info("doAfterThrowing");
    }

    @Around("checkParam()")
    public void doAroud() {

    }
}
