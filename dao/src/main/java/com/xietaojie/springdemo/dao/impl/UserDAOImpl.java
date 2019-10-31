package com.xietaojie.springdemo.dao.impl;

import com.xietaojie.springdemo.dao.UserDAO;
import com.xietaojie.springdemo.dao.dataobject.UserDO;
import com.xietaojie.springdemo.dao.mapper.auto.UserDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author xietaojie1992
 */
@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private UserDOMapper userDOMapper;

    @Override
    public int insert(UserDO userDO) {
        return userDOMapper.insertSelective(userDO);
    }

    @Override
    public int update(UserDO userDO) {
        return userDOMapper.updateByPrimaryKeySelective(userDO);
    }

    @Override
    public UserDO queryById(Integer id) {
        return userDOMapper.selectByPrimaryKey(id);
    }
}
