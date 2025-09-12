package com.eplugger.business.pub.controller;

import com.eplugger.business.pub.service.BusinessService;
import com.eplugger.common.entity.EntityImpl;
import com.eplugger.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础控制器，提供通用的CRUD操作
 * @param <E> 实体类型，必须继承自EntityImpl
 * @param <S> 服务类型，必须实现BusinessService接口
 */
public abstract class BusinessController<E extends EntityImpl, S extends BusinessService<E>> {

    @Autowired
    protected S service;

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<E> getById(@PathVariable String id) {
        return Result.success(service.findById(id));
    }

    /**
     * 新增
     */
    @PostMapping()
    public Result<E> add(@RequestBody E entity) {
        service.insert(entity);
        return Result.success(entity);
    }

    /**
     * 更新
     */
    @PutMapping
    public Result<Integer> update(@RequestBody E entity) {
        return Result.success(service.update(entity));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable String id) {
        return Result.success(service.delete(id));
    }
    
    /**
     * 获取所有记录
     */
    @GetMapping
    public Result<List<E>> findAll() {
        return Result.success(service.findAll());
    }
} 