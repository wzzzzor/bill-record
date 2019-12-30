package com.wzzzzor.billrecord.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wzzzzor.billrecord.domain.Color;
import com.wzzzzor.billrecord.service.ColorService;

@Service("colorService")
public class ColorServiceImpl extends MybatisSuperService<Color> implements ColorService{

    /*public static final String SQLNAME_SEPARATOR = ".";

    @Resource
    private SqlSession sqlSessionTemplate;*/

    private static final String SQL_ID_FIND_ALL = "findAll";

    /*protected String getSqlName(String sqlName) {
        return Color.class.getName() + SQLNAME_SEPARATOR + sqlName;
    }*/

    @Override
    public List<Color> findAll() {
        return this.sqlSessionTemplate.selectList(getSqlName(SQL_ID_FIND_ALL));
    }

}
