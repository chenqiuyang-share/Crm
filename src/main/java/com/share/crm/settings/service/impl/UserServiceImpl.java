package com.share.crm.settings.service.impl;

import com.share.crm.exception.LoginException;
import com.share.crm.settings.dao.UserDao;
import com.share.crm.settings.domain.User;
import com.share.crm.settings.service.UserService;
import com.share.crm.utils.DateTimeUtil;
import com.share.crm.utils.SqlSessionUtil;
import jdk.nashorn.internal.ir.CallNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 这是业务层service，用来调用dao层，这边是处理业务逻辑的
 * 一定要去走事物
 */
public class UserServiceImpl implements UserService {


    private final UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        //利用map传值
        Map<String,String> map = new HashMap<String,String>();
        //是以健值对的形式来传值
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);

        if(user == null){
            throw new LoginException("账号密码错误");
        }
        //程序能够成功的执行到该行，说明账号密码正确
        //需要继续向下验证其他三项信息


        /**
         * 1,验证失效时间
         */
        String expireTime = user.getExpireTime();
        //这是得到现在的时间，并且规范时间格式
        String currentTime = DateTimeUtil.getSysTime();

        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("该账号已经失效");
        }

        /**
         * 2,判断锁定状态
         */
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("该账号已锁定");
        }

        /**
         * 3,判断id地址是否受限
         */
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("IP地址受限");

        }

        return null;
    }
}
