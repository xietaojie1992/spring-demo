package com.xietaojie.springdemo.aop;

import com.xietaojie.springdemo.aop.annotation.ParamCheck;
import com.xietaojie.springdemo.aop.exception.ParamCheckException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
    @Pointcut("execution (public * com.xietaojie.springdemo.cron..*.*(..))")
    public void checkParam() {
    }

    @Before("checkParam()")
    public void doBefore() {
        logger.info("doBefore");
    }

    @Before("checkParam() && @annotation(paramCheck)")
    public void doBefore2(JoinPoint joinPoint, ParamCheck paramCheck) {
        logger.info("doBefore, joinPoint, paramCheck");
    }

    @After("checkParam()")
    public void doAfter(JoinPoint joinPoint) {
        logger.info("doAfter");
    }

    @Around("checkParam() && @annotation(paramCheck)")
    public Object doAround(ProceedingJoinPoint joinPoint, ParamCheck paramCheck) throws Throwable {
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        //得到拦截的方法
        Method method = signature.getMethod();
        //获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        logger.info("@Around checkParam() && @annotation(paramCheck)");
        logger.info("doAround, method={}", signature);

        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }
        //获取方法参数名
        String[] paramNames = signature.getParameterNames();
        //获取参数值
        Object[] paranValues = joinPoint.getArgs();
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                //如果该参数前面的注解是ParamCheck的实例，并且notNull()=true,则进行非空校验
                if (parameterAnnotations[i][j] != null && parameterAnnotations[i][j] instanceof ParamCheck
                        && ((ParamCheck) parameterAnnotations[i][j]).notNull()) {
                    paramIsNull(paramNames[i], paranValues[i], parameterTypes[i] == null ? null : parameterTypes[i].getName());
                    break;
                }
            }
        }
        return joinPoint.proceed();
    }

    @Around("checkParam()")
    public Object doAround2(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        //得到拦截的方法
        Method method = signature.getMethod();
        //获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        logger.info("@Around checkParam()");
        logger.info("doAround, method={}", signature);

        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }
        //获取方法参数名
        String[] paramNames = signature.getParameterNames();
        //获取参数值
        Object[] paranValues = joinPoint.getArgs();
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                //如果该参数前面的注解是ParamCheck的实例，并且notNull()=true,则进行非空校验
                if (parameterAnnotations[i][j] != null && parameterAnnotations[i][j] instanceof ParamCheck
                        && ((ParamCheck) parameterAnnotations[i][j]).notNull()) {
                    paramIsNull(paramNames[i], paranValues[i], parameterTypes[i] == null ? null : parameterTypes[i].getName());
                    break;
                }
            }
        }
        return joinPoint.proceed();
    }

    @Around("@annotation(paramCheck)")
    public Object doAround3(ProceedingJoinPoint joinPoint, ParamCheck paramCheck) throws Throwable {
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        //得到拦截的方法
        Method method = signature.getMethod();
        //获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        logger.info("@Around @annotation(paramCheck)");
        logger.info("doAround, method={}", signature);

        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }
        //获取方法参数名
        String[] paramNames = signature.getParameterNames();
        //获取参数值
        Object[] paranValues = joinPoint.getArgs();
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                //如果该参数前面的注解是ParamCheck的实例，并且notNull()=true,则进行非空校验
                if (parameterAnnotations[i][j] != null && parameterAnnotations[i][j] instanceof ParamCheck
                        && ((ParamCheck) parameterAnnotations[i][j]).notNull()) {
                    paramIsNull(paramNames[i], paranValues[i], parameterTypes[i] == null ? null : parameterTypes[i].getName());
                    break;
                }
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 参数非空校验，如果参数为空，则抛出ParamIsNullException异常
     * @param paramName
     * @param value
     * @param parameterType
     */
    private void paramIsNull(String paramName, Object value, String parameterType) {
        if (value == null || "".equals(value.toString().trim())) {
            logger.error("paramIsNull, paramName={}, paramterType={}", paramName, parameterType);
            throw new ParamCheckException(paramName, parameterType);
        }
    }
}
