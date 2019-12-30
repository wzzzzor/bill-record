package com.wzzzzor.billrecord.service;

import java.util.List;

import com.wzzzzor.billrecord.domain.Color;

public interface ColorService {

    /**
     * 获取所有的背景颜色列表
     * @return
     */
    public List<Color> findAll();
}
