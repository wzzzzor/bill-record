package com.wzzzzor.billrecord.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "demo接口", tags = {"demo-Tag接口"})
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get() {
        return "demo的get方法";
    }
}
