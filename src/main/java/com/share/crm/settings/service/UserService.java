package com.share.crm.settings.service;

import com.share.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd);
}
