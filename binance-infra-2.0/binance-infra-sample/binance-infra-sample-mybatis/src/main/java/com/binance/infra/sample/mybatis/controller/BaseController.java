package com.binance.infra.sample.mybatis.controller;

import com.binance.infra.sample.mybatis.model.BaseModel;
import com.binance.infra.sample.mybatis.model.Response;
import com.binance.infra.sample.mybatis.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 提供简单的、通用的增删改查
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
public abstract class BaseController<T extends BaseModel> {

    @Autowired
    private BaseService<T> service;

    @GetMapping("{id}")
    public Response<T> findById(@PathVariable String id){
        return Response.success(service.findById(id));
    }

    @GetMapping
    public Response<List<T>> findAll(){
        return Response.success(service.findAll());
    }

    @PostMapping
    public Response<T> insert(@RequestBody T entity){
        return Response.success(service.insert(entity));
    }

    @PutMapping
    public Response<T> update(@RequestBody T entity){
        return Response.success(service.update(entity));
    }

    @DeleteMapping("{id}")
    public Response<Boolean> delete(@PathVariable String id){
        return Response.success(service.delete(id));
    }
}
