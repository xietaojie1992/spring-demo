package com.xietaojie.springdemo.cron;

import com.xietaojie.springdemo.aop.annotation.ControllerWebLog;
import com.xietaojie.springdemo.aop.annotation.ParamCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author xietaojie1992
 */
@Slf4j
@Component
public class TestAopScheduler {

    @Scheduled(fixedRateString = "10000")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void schedule() {
        log.info("{}", testAop("hello ", "world"));
    }

    @ControllerWebLog(name = "cron")
    public String testAop(@ParamCheck String content1, @ParamCheck String content2) {
        return content1 + content2;
    }

    private void test() {

    }

    public void test2() {

    }

    @Scheduled(fixedRateString = "10000")
    @ControllerWebLog(name = "cron")
    public void schedule2() {
        test();
        test2();
    }

}
