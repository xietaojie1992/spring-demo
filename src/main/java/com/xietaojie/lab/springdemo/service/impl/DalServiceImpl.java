package com.xietaojie.lab.springdemo.service.impl;

import com.xietaojie.lab.springdemo.common.dal.mapper.UserMapper;
import com.xietaojie.lab.springdemo.common.dal.model.User;
import com.xietaojie.lab.springdemo.service.DalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author xietaojie1992
 */
@Repository
public class DalServiceImpl implements DalService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }
}
