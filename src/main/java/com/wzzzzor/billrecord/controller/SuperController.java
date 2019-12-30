package com.wzzzzor.billrecord.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wzzzzor.billrecord.exception.RestException;
import com.wzzzzor.billrecord.resultful.RestResult;
import com.wzzzzor.billrecord.resultful.RestResult.OperatorType;
import com.wzzzzor.billrecord.service.ISuperService;
import com.wzzzzor.billrecord.utils.Utility;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

public abstract class SuperController<T> {

    protected abstract ISuperService<T> getService();

    @ApiOperation(value = "根据id获取数据")
    @ApiImplicitParam(name = "id", required = true, dataType = "string", paramType = "path", value = "id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResult get(@PathVariable("id") String id) {
        RestResult result = new RestResult();
        HttpStatus status = null;
        T t = null;
        String message = "";
        t = getService().selectById(id);
        if(t == null) {
            message = "数据不存在（id：" + id + "）";
            status = HttpStatus.NOT_FOUND;
        }else {
            result.addData(OperatorType.QUERY, t);
        }
        if(Utility.isEmpty(status)) {
            result.addOK(t);
        }else {
            throw new RestException(status,message);
        }
        return result;
    }
}
