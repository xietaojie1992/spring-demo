package com.xietaojie.lab.rest;

import com.xietaojie.lab.aop.annotation.ParamCheck;
import com.xietaojie.lab.config.ProjectProperties;
import com.xietaojie.lab.dao.UserDAO;
import com.xietaojie.lab.dao.dataobject.UserDO;
import com.xietaojie.lab.service.TestService;
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
        testService_1.say("fff");
        testService_2.say("fff");

        userDAO.insert(user);
        log.info(testAop("echo aop"));
        log.info(testAop(null));

        return projectProperties.toString();
    }

    public String testAop(@ParamCheck String content) {
        return content;
    }

    @GetMapping(value = "/aop/{content}")
    @ParamCheck
    public String echo(@ParamCheck @PathVariable("content") String content) {
        return content;
    }

    @GetMapping(value = "/echo")
    @ParamCheck
    public String echo2(@ParamCheck @RequestParam(value = "content", defaultValue = "echo", required = false) String content) {
        return content;
    }
}
