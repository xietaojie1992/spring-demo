package com.xietaojie.springdemo.service;

import com.xietaojie.springdemo.aop.annotation.ParamCheck;

/**
 * @author xietaojie1992
 */
public interface TestService {

    void say(@ParamCheck String content);
}
