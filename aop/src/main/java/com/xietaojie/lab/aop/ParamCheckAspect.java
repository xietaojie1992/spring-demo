package com.xietaojie.lab.aop;

import com.xietaojie.lab.aop.annotation.ParamCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
    @Pointcut("execution (public * com.xietaojie.lab.cron..*.*(..))")
    public void check() {
    }

    /**
     * 以注解作为切点
     */
    @Pointcut("@annotation(com.xietaojie.lab.aop.annotation.ParamCheck)")
    public void check2() {
    }

    @Around("check2()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
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

        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = signature.getMethod().getParameters();

        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(ParamCheck.class)) {
                logger.info("{}", parameter);
                //paramIsNull(paramNames[i], paranValues[i], parameterTypes[i] == null ? null : parameterTypes[i].getName());
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 参数非空校验，如果参数为空，则抛出ParamIsNullException异常
     *
     * @param paramName
     * @param value
     * @param parameterType
     */
    private void paramIsNull(String paramName, Object value, String parameterType) {
        if (value == null || "".equals(value.toString().trim())) {
            logger.error("paramIsNull, paramName={}, paramterType={}", paramName, parameterType);
            //            throw new ParamCheckException(paramName, parameterType);
        }
    }
}
