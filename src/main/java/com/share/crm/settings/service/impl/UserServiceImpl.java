package com.share.crm.settings.service.impl;

import com.share.crm.settings.dao.UserDao;
import com.share.crm.settings.domain.User;
import com.share.crm.settings.service.UserService;
import com.share.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {


    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    public User login(String loginAct, String loginPwd) {

        return null;
    }
}
