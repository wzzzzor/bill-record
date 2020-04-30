package com.wzzzzor.billrecord.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "背景颜色实体类")
public class Color implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 4048829796176900386L;

    private String id;
    @ApiModelProperty(value = "颜色名称")
    private String name;
    @ApiModelProperty(value = "颜色代码")
    private String code;
    @ApiModelProperty(value = "颜色值")
    private String value;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
