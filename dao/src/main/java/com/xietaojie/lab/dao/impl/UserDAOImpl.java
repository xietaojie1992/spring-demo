package com.xietaojie.lab.dao.impl;

import com.xietaojie.lab.dao.UserDAO;
import com.xietaojie.lab.dao.dataobject.UserDO;
import com.xietaojie.lab.dao.mapper.auto.UserDOMapper;
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
