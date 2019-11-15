package com.xietaojie.lab.service.impl;

import com.xietaojie.lab.aop.annotation.ControllerWebLog;
import com.xietaojie.lab.aop.annotation.ParamCheck;
import com.xietaojie.lab.service.TestService;
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
