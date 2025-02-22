package com.sca.notesspringboot.service.impl;

import com.sca.notesspringboot.entity.User;
import com.sca.notesspringboot.exception.ServiceException;
import com.sca.notesspringboot.mapper.UserMapper;
import com.sca.notesspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public void insertUser(User user){
        userMapper.insert(user);

    }


    public User login(User user) {
        //根据用户名查找用户信息
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser == null){
            //抛出自定义异常类
            throw new ServiceException("用户名或密码错误");
        }
        if(!user.getPassword().equals(dbUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        return dbUser;
    }

}
