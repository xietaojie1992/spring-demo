package com.xietaojie.springdemo.service.impl;

import com.xietaojie.springdemo.aop.annotation.ControllerWebLog;
import com.xietaojie.springdemo.aop.annotation.ParamCheck;
import com.xietaojie.springdemo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xietaojie1992
 */
public class TestServiceImpl_1 implements TestService {
    private Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl_1.class);

    @Override
    @ControllerWebLog(name = "test say")
    @ParamCheck
    public void say(String content) {
        LOGGER.info("I'm TestServiceImpl_1");
    }
}
