package com.wzzzzor.billrecord.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "demo接口", tags = {"demo-Tag接口"})
@RestController
@RequestMapping("/demo")
public class DemoController {

    @ApiOperation(value = "获取demo的get方法", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map get() {
        Map map = new HashMap<>();
        map.put("key1", "键一");
        return map;
    }
}
