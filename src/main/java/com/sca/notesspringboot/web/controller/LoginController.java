package com.sca.notesspringboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

}
