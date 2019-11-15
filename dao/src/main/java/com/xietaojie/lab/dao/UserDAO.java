package com.xietaojie.lab.dao;

import com.xietaojie.lab.dao.dataobject.UserDO;

/**
 * @author xietaojie1992
 */
public interface UserDAO {

    int insert(UserDO userDO);

    int update(UserDO userDO);

    UserDO queryById(Integer id);
}
