package com.eplugger.business.pub.mapper;

import com.eplugger.common.entity.EntityImpl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 基础Mapper接口，提供通用的CRUD操作
 * @param <E> 实体类型，必须继承自EntityImpl
 */
@Mapper
public interface BusinessMapper<E extends EntityImpl> {
    E findById(String id);
    int insert(E entity);
    int update(E entity);
    int delete(String id);
    
    /**
     * 获取所有记录
     * @return 所有记录的列表
     */
    List<E> findAll();
}
