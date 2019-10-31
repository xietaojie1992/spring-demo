package com.xietaojie.springdemo.service.impl;

import com.xietaojie.springdemo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xietaojie1992
 */
public class TestServiceImpl_2 implements TestService {
    private Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl_2.class);

    @Override
    public void say() {
        LOGGER.info("I'm TestServiceImpl_2");
    }
}
