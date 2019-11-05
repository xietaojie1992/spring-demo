package com.xietaojie.springdemo.rest;

import com.xietaojie.springdemo.aop.annotation.ParamCheck;
import com.xietaojie.springdemo.config.ProjectProperties;
import com.xietaojie.springdemo.dao.UserDAO;
import com.xietaojie.springdemo.dao.dataobject.UserDO;
import com.xietaojie.springdemo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xietaojie1992
 */
@RestController
//@RequestMapping(value = "/")
@Slf4j
public class HelloController {

    //@Value("${project.version}")
    //private String projectVersion;

    @Autowired
    private ProjectProperties projectProperties;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TestService testService_1;

    @Autowired
    private TestService testService_2;

    @RequestMapping(value = {"/hello", "/hi", "/"}, method = RequestMethod.GET)
    public String hello() {
        UserDO user = new UserDO();
        user.setName("aaa");
        user.setPassword("aaa");
        user.setPhoneNumber("1234567890");
        testService_1.say();
        testService_2.say();

        userDAO.insert(user);
        log.info(testAop("echo aop"));
        log.info(testAop(null));

        return projectProperties.toString();
    }

    public String testAop(@ParamCheck String content) {
        return content;
    }

    @GetMapping(value = "/aop/{content}")
    public String echo(@PathVariable("content") String content) {
        return content;
    }

    @GetMapping(value = "/echo")
    public String echo2(@RequestParam(value = "content", defaultValue = "echo", required = false) String content) {
        return content;
    }
}
