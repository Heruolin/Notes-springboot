package com.sca.notesspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sca.notesspringboot.entity.User;
import com.sca.notesspringboot.entity.UserRepository;
import com.sca.notesspringboot.mapper.UserMapper;
import com.sca.notesspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 确保它是 @Bean 方式注入的

    @Autowired
    private UserRepository userRepository;

    // 根据用户名查询用户
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user != null) {
            System.out.println("数据库密码：" + user.getPassword());
            System.out.println("输入密码：" + password);
        } else {
            System.out.println("用户未找到");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user; // 密码匹配成功
        } else {
            System.out.println("密码不匹配");
        }
        return null; // 登录失败
    }

    @Override
    public boolean register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            return false;
        }

        // **确保密码加密存储**
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("加密后密码：" + encryptedPassword);
        user.setPassword(encryptedPassword);

        return userMapper.insert(user) > 0;
    }
}

