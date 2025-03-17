package com.sca.notesspringboot.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/auth-check")
    public ResponseEntity<String> checkAuth(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 如果头部有 Bearer token, 进行处理
        System.out.println("🔍 服务器收到的 Authorization 头: '" + authHeader + "'");

        if (authHeader != null && authHeader.trim().startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim(); // 移除 "Bearer " 前缀并去除空格
            System.out.println("🔍 服务器收到的 Token: " + token);
        } else {
            System.out.println("🔍 Authorization 头无效");
        }

        return ResponseEntity.ok("Header received: " + authHeader);
    }
}
