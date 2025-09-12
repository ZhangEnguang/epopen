package com.eplugger.business.pub.service;

import com.eplugger.common.entity.EntityImpl;

import java.util.List;

public interface BusinessService<E extends EntityImpl> {
    E findById(String id);
    E insert(E entity);
    int update(E entity);
    int delete(String id);

    /**
     * 获取所有记录
     * @return 所有记录的列表
     */
    List<E> findAll();
}
