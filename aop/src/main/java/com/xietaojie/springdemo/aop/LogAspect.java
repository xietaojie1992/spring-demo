package com.xietaojie.springdemo.aop;

import com.xietaojie.springdemo.aop.annotation.ControllerWebLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面类，打印所有 HTTP 请求和相应结果
 *
 * @author xietaojie1992
 */
@Aspect
@Component
public class LogAspect {

    private Logger webLogger  = LoggerFactory.getLogger("log.web");
    private Logger cronLogger = LoggerFactory.getLogger("log.cron");

    /**************** Web Log *****************/

    /**
     * 定义切点
     *
     * execution（）                  ：表达式的主体
     * 第一个 * 符号                   ：表示返回值的类型，* 代表所有返回类型
     * com.xietaojie.springdemo.cron ：AOP 所切的服务的包名，即需要进行横切的业务类
     * 包名后面的 ..                   ：表示当前包及子包
     * 第二个 *                       ：表示类名，* 表示所有类
     * 最后的 .*(..)	                 ：第一个 . 表示任何方法名，括号内为参数类型，.. 代表任何类型参数
     */
    /**
     *
     */
    //@Pointcut("execution (public * com.xietaojie.springdemo.rest..*.*(..))") // 匹配 com.xietaojie.springdemo.rest 包下所有方法
    @Pointcut("bean (*Controller)") // 匹配名字后缀为 Controller 的 bean 下的所有方法
    public void webLog() {
    }

    @Before(value = "webLog()")
    @Order(10)
    public void doBeforeWeb(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        webLogger.info("{}", joinPoint.getSignature());
        webLogger.info("Request received, URL={}, METHOD={}, RemoteAddress={}, ARGS={}", request.getRequestURL().toString(),
                request.getMethod(), request.getRemoteAddr(), Arrays.toString(joinPoint.getArgs()));
        //logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturningWeb(JoinPoint joinPoint, Object ret) throws Throwable {
        // 处理完请求，返回内容
        webLogger.info("Request completed, response={}", ret);
    }

    /************** Cron Log *****************/

    /**
     * execution，匹配方法执行的连接点
     * within，匹配指定类型内的方法，匹配当前 AOP 代理对象类型的执行方法
     */
    //@Pointcut("execution (* com.xietaojie.springdemo.cron..*.*(..))")
    // @Pointcut("within(com.xietaojie.springdemo.cron.*)")
    // public void cronLog() {
    // }
    @Pointcut("@annotation(controllerWebLog)")
    public void cronLog(ControllerWebLog controllerWebLog) {
    }

    @Before(value = "cronLog(controllerWebLog)")
    public void doBeforeCron(JoinPoint joinPoint, ControllerWebLog controllerWebLog) {
        // 记录下请求内容
        cronLogger.info("cron job started, {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "cronLog(controllerWebLog)")
    public void doAfterReturningCron(JoinPoint joinPoint, ControllerWebLog controllerWebLog) throws Throwable {
        // 处理完请求，返回内容
        cronLogger.info("cron job finished, {}", joinPoint.getSignature());
    }

}
