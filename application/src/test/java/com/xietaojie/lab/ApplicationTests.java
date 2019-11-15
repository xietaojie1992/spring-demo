package com.xietaojie.lab;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xietaojie1992
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTests {

    final static String  DEFAULT_HOST     = "172.16.5.38";
    final static Integer DEFAULT_PORT     = 5672;
    final static String  DEFAULT_USERNAME = "guest";
    final static String  DEFAULT_PASSWORD = "guest";

    @Test
    public void contextLoads() {
    }
}
