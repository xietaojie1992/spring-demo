package com.xietaojie.lab.springdemo.service.impl;

import com.xietaojie.lab.springdemo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xietaojie1992
 */
public class TestServiceImpl_1 implements TestService {
    private Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl_1.class);

    @Override
    public void say() {
        LOGGER.info("I'm TestServiceImpl_1");
    }
}
