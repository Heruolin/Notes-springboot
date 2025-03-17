package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.User;
import org.springframework.stereotype.Service;



public interface UserService {


    // 根据用户名查询用户
    User getUserByUsername(String username);

    // 登录逻辑
    User login(String username, String password);

    // 注册逻辑
    boolean register(User user);
}
