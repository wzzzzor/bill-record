package com.wzzzzor.billrecord.service;

import java.util.List;

import com.wzzzzor.billrecord.base.DataTablePage;
import com.wzzzzor.billrecord.domain.Color;

public interface ColorService extends ISuperService<Color>{

    /**
     * 获取所有的背景颜色列表
     * @return
     */
    public DataTablePage findAll(DataTablePage page);
}
