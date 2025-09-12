package com.eplugger.common.entity;

import org.springframework.beans.factory.BeanNameAware;

import java.io.Serializable;

public interface Entity extends Serializable {

    String PRIMARY_KEY = "id";

    /**
     * 获取id
     */
    String getId();

    /**
     * 主键编号
     */
    void setId(String id);

}