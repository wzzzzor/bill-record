package com.wzzzzor.billrecord.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import com.wzzzzor.billrecord.exception.ServiceException;
import com.wzzzzor.billrecord.service.ISuperService;
import com.wzzzzor.billrecord.utils.BeanUtils;
import com.wzzzzor.billrecord.utils.SqlId;
import com.wzzzzor.billrecord.utils.UUIDUtils;

public abstract class MybatisSuperService<T> implements ISuperService<T>{

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    @Autowired(required = true)
    protected SqlSession sqlSessionTemplate;
    
    /**
     * 空间属性分割符
     */
    public static final String SQLNAME_SEPARATOR = ".";

    /**
     * SqlMapping命名空间 
     */
    private String sqlNamespace = getDefaultSqlNamespace();

    /**
     * 获取泛型类型的实体对象类全名
     */
    protected String getDefaultSqlNamespace() {
        Class<?> genericClass = BeanUtils.getGenericClass(this.getClass());
        return genericClass == null ? null : genericClass.getName();
    }

    /**
     * 获取SqlMapping命名空间 
     * @return SqlMapping命名空间 
     */
    @Override
    public String getSqlNamespace() {
        return sqlNamespace;
    }

    /**
     * 设置SqlMapping命名空间。 以改变默认的SqlMapping命名空间，
     * <br>不能滥用此方法随意改变SqlMapping命名空间。 
     * @param sqlNamespace SqlMapping命名空间 
     */
    @Override
    public void setSqlNamespace(String sqlNamespace) {
        this.sqlNamespace = sqlNamespace;
    }

    /**
     * 将SqlMapping命名空间与给定的SqlMapping名组合在一起。
     * @param sqlName SqlMapping名 
     * @return 组合了SqlMapping命名空间后的完整SqlMapping名 
     */
    protected String getSqlName(String sqlName) {
        return sqlNamespace + SQLNAME_SEPARATOR + sqlName;
    }

    /**
     * 生成主键值。 默认使用方法
     * 如果需要生成主键，需要由子类重写此方法根据需要的方式生成主键值。 
     * @param entity 要持久化的对象 
     */
    protected String generateId() {
        return UUIDUtils.create();
    }

    @Override
    public T selectById(String id) {
        Assert.notNull(id);
        try {
            return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_BY_ID), id);
        } catch (Exception e) {
            throw new ServiceException(String.format("根据ID查询对象出错！语句：%s", getSqlName(SqlId.SQL_SELECT_BY_ID)), e);
        }
        
    }

    
}
