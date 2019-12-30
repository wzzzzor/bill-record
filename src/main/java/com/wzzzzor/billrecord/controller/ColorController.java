package com.wzzzzor.billrecord.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wzzzzor.billrecord.domain.Color;
import com.wzzzzor.billrecord.service.ColorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "背景颜色接口", tags = {"背景颜色接口"})
@RestController
@RequestMapping("/rest/color")
public class ColorController {

    @Resource
    private ColorService colorService;

    @ApiOperation(value = "获取所有背景颜色列表", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<Color> findAll(){
        return colorService.findAll();
    }
}
