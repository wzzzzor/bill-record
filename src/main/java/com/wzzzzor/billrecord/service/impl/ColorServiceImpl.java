package com.wzzzzor.billrecord.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.wzzzzor.billrecord.domain.Color;
import com.wzzzzor.billrecord.service.ColorService;

@Service("colorService")
public class ColorServiceImpl implements ColorService{

    public static final String SQLNAME_SEPARATOR = ".";

    @Resource
    private SqlSession sqlSessionTemplate;

    private static final String SQL_ID_FIND_ALL = "findAll";

    protected String getSqlName(String sqlName) {
        return Color.class.getName() + SQLNAME_SEPARATOR + sqlName;
    }

    @Override
    public List<Color> findAll() {
        return sqlSessionTemplate.selectList(getSqlName(SQL_ID_FIND_ALL));
    }

}
