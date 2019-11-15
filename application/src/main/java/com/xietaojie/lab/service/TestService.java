package com.xietaojie.lab.service;

import com.xietaojie.lab.aop.annotation.ParamCheck;

/**
 * @author xietaojie1992
 */
public interface TestService {

    void say(@ParamCheck String content);
}
