package com.sca.notesspringboot.web.controller;

import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.config.JwtUtil;
import com.sca.notesspringboot.entity.LoginRequest;
import com.sca.notesspringboot.entity.User;
import com.sca.notesspringboot.service.UserService;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户登录
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;  // 注入 JwtUtil

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest ,@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.getUserByUsername(loginRequest.getUsername());

        if (user != null && bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // 登录成功，生成 JWT Token
            String token = jwtUtil.generateToken(user.getUsername());
            response.put("code", "200");
            response.put("message", "登录成功");
            response.put("user", user);
            response.put("token", token); // 返回 token
            System.out.println("🔍 服务器收到的 Authorization 头: '" + authHeader + "'");
        } else {
            response.put("code", "401");
            response.put("message", "用户名或密码错误");
        }

        System.out.println("登录响应数据：" + response);  // 调试输出
        return response;
    }

    // 用户注册
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        boolean success = userService.register(user);

        if (success) {
            response.put("code", "200");
            response.put("message", "注册成功");
        } else {
            response.put("code", "400");
            response.put("message", "用户名已存在");
        }
        return response;
    }
    //修改密码
    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String oldPassword = requestBody.get("oldPassword");
        String newPassword = requestBody.get("newPassword");

        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error("旧密码错误");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userService.updateUser(user);
        return Result.success("密码修改成功");
    }
    //注销账户
    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("无效的Token");
        }
        // 这里可以添加Token黑名单逻辑以确保Token失效
        return Result.success("注销成功");
    }

    @PostMapping("/deregister")
    public Result deregister(@RequestBody Map<String, String> requestBody, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("无效的Token");
        }

        try {
            // 从请求体中获取 userid
            String userIdStr = requestBody.get("userid");
            if (userIdStr == null || userIdStr.isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            Integer userid = Integer.parseInt(userIdStr);

            // 根据 userid 删除用户
            User user = userService.selectByUserId(userid);
            if (user == null) {
                return Result.error("用户不存在");
            }

            userService.deleteUserByUserId(userid);
            return Result.success("账户已成功注销");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注销失败: " + e.getMessage());
        }
    }

}






