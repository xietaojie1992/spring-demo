package com.xietaojie.springdemo.cron;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xietaojie1992
 */
@Slf4j
@Component
public class HelloScheduler {

    //@Scheduled(fixedRateString = "10000")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void schedule() {
        log.info("HelloScheduler ... schedule");
        return;
    }
}
