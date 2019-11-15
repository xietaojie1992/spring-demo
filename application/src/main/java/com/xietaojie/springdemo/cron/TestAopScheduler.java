package com.xietaojie.springdemo.cron;

import com.xietaojie.springdemo.aop.annotation.ControllerWebLog;
import com.xietaojie.springdemo.aop.annotation.ParamCheck;
import com.xietaojie.springdemo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author xietaojie1992
 */
@Slf4j
@Component
public class TestAopScheduler {

    @Autowired
    private TestService testService_1;

    @Scheduled(fixedRateString = "10000")
    @ControllerWebLog(name = "cron")
    public void schedule() {
        log.info("{}", testAop("hello ", null));
    }

    @ControllerWebLog(name = "cron")
    @ParamCheck
    public String testAop(String content1, String content2) {
        return content1 + content2;
    }

    private void test() {

    }

    @Scheduled(fixedRateString = "10000")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void schedule2() {
        test();
        testService_1.say(null);
    }

}
