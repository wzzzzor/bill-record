package com.wzzzzor.billrecord.service;

public interface ISuperService<T> {

    public String getSqlNamespace();
    public void setSqlNamespace(String sqlNamespace);

    public T selectById(String id);
}
