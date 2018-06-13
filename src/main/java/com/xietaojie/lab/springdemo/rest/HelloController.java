package com.xietaojie.lab.springdemo.rest;

import com.xietaojie.lab.springdemo.common.dal.mapper.UserMapper;
import com.xietaojie.lab.springdemo.common.dal.model.User;
import com.xietaojie.lab.springdemo.config.ProjectProperties;
import com.xietaojie.lab.springdemo.service.DalService;
import com.xietaojie.lab.springdemo.service.TestService;
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
public class HelloController {

    //@Value("${project.version}")
    //private String projectVersion;

    @Autowired
    private ProjectProperties projectProperties;

    @Autowired
    private DalService dalService;

    @Autowired
    private TestService testService_1;

    @Autowired
    private TestService testService_2;

    @RequestMapping(value = {"/hello", "/hi", "/"}, method = RequestMethod.GET)
    public String hello() {
        User user = new User();
        user.setName("aaa");
        user.setPassword("aaa");
        user.setPhoneNumber("1234567890");
        dalService.insert(user);
        testService_1.say();
        testService_2.say();
        return projectProperties.toString();
    }

    @GetMapping(value = "/echo/{content}")
    public String echo(@PathVariable("content") String content) {
        return content;
    }

    @GetMapping(value = "/echo")
    public String echo2(@RequestParam(value = "content", defaultValue = "echo", required = false) String content) {
        return content;
    }
}