package com.xietaojie.springdemo.dao;

import com.xietaojie.springdemo.dao.dataobject.UserDO;

/**
 * @author xietaojie1992
 */
public interface UserDAO {

    int insert(UserDO userDO);

    int update(UserDO userDO);

    UserDO queryById(Integer id);
}
