package com.xietaojie.springdemo.aop;

import com.xietaojie.springdemo.aop.annotation.ControllerWebLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面
 *
 * @author xietaojie1992
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    private static final String START_TIME = "startTime";

    private static final String REQUEST_PARAMS = "requestParams";

    /**
     * execution（）                  ：表达式的主体
     * 第一个 * 符号                   ：表示返回值的类型，* 代表所有返回类型
     * com.xietaojie.springdemo.cron ：AOP 所切的服务的包名，即需要进行横切的业务类
     * 包名后面的 ..                   ：表示当前包及子包
     * 第二个 *                       ：表示类名，* 表示所有类
     * 最后的 .*(..)	                 ：第一个 . 表示任何方法名，括号内为参数类型，.. 代表任何类型参数
     */
    @Pointcut("execution (* com.xietaojie.springdemo.cron..*.*(..))")
    //@Pointcut("execution (* com.xietaojie.springdemo.cron.HelloScheduler.schedule(..))")
    public void webLog() {
    }

    @Before(value = "webLog()")
    @Order(10)
    public void doBefore(JoinPoint joinPoint) {
        logger.info("doBefore");
        logger.info("JoinPoint, method={}", joinPoint.getSignature());
    }

    @Before(value = "webLog() and @annotation(controllerWebLog)")
    @Order(9)
    public void doBefore(JoinPoint joinPoint, ControllerWebLog controllerWebLog) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        Map<String, Object> threadInfo = new HashMap<>();
        threadInfo.put(START_TIME, startTime);
        // 请求参数
        StringBuilder requestStr = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                requestStr.append(arg.toString());
            }
        }
        threadInfo.put(REQUEST_PARAMS, requestStr.toString());
        threadLocal.set(threadInfo);
        logger.info("{}接口开始调用:requestData={}", controllerWebLog.name(), threadInfo.get(REQUEST_PARAMS));
    }

    @AfterReturning(value = "webLog()&& @annotation(controllerWebLog)", returning = "res")
    public void doAfterReturning(ControllerWebLog controllerWebLog, Object res) {
        Map<String, Object> threadInfo = threadLocal.get();
        long takeTime = System.currentTimeMillis() - (long) threadInfo.getOrDefault(START_TIME, System.currentTimeMillis());
        if (controllerWebLog.intoDb()) {
            // TODO 日志持久化
        }
        threadLocal.remove();
        logger.info("{}接口结束调用:耗时={}ms,result={}", controllerWebLog.name(), takeTime, res);
    }

    @AfterThrowing(value = "webLog()&& @annotation(controllerWebLog)", throwing = "throwable")
    public void doAfterThrowing(ControllerWebLog controllerWebLog, Throwable throwable) {
        Map<String, Object> threadInfo = threadLocal.get();
        if (controllerWebLog.intoDb()) {
            //TODO 日志持久化
        }
        threadLocal.remove();
        logger.error("{}接口调用异常，异常信息{}", controllerWebLog.name(), throwable);
    }

    @Around("webLog()&& @annotation(controllerWebLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, ControllerWebLog controllerWebLog) throws Throwable {
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        //得到拦截的方法
        Method method = signature.getMethod();
        //获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        logger.info("@Around checkParam()&& @annotation(controllerWebLog), {}", controllerWebLog.name());
        logger.info("doAround, method={}", signature);

        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }
        return joinPoint.proceed();
    }

}
