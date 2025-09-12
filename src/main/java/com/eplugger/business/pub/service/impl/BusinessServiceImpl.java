package com.eplugger.business.pub.service.impl;

import com.eplugger.business.pub.mapper.BusinessMapper;
import com.eplugger.business.pub.service.BusinessService;
import com.eplugger.common.entity.EntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务服务实现基类，提供通用的CRUD操作实现
 * @param <E> 实体类型
 * @param <M> Mapper类型
 */
public abstract class BusinessServiceImpl<E extends EntityImpl, M extends BusinessMapper<E>> implements BusinessService<E> {
    
    @Autowired
    protected M mapper;

    @Override
    public E findById(String id) {
        return mapper.findById(id);
    }

    @Override
    @Transactional
    public E insert(E entity) {
        mapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional
    public int update(E entity) {
        return mapper.update(entity);
    }

    @Override
    @Transactional
    public int delete(String id) {
        return mapper.delete(id);
    }
    
    @Override
    public List<E> findAll() {
        return mapper.findAll();
    }
}
