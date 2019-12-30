package com.wzzzzor.billrecord.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

@Api(value = "登陆接口",tags = {"登录接口"})
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "index";
    }
}
