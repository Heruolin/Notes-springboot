package com.sca.notesspringboot.web.controller;

import cn.hutool.core.util.StrUtil;
import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.entity.User;
import com.sca.notesspringboot.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class LoginController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername() )|| StrUtil.isBlank(user.getPassword())) {
            return Result.error("输入不合法");
        }
        user = userService.login(user);
        return Result.success(user);
    }

}
