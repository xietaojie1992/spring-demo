package com.xietaojie.lab.springdemo.config;

import com.xietaojie.lab.springdemo.service.TestService;
import com.xietaojie.lab.springdemo.service.impl.TestServiceImpl_1;
import com.xietaojie.lab.springdemo.service.impl.TestServiceImpl_2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xietaojie1992
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public TestService testService_1() {
        return new TestServiceImpl_1();
    }

    @Bean
    public TestService testService_2() {
        return new TestServiceImpl_2();
    }
}
