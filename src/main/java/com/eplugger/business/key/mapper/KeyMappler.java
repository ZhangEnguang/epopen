package com.eplugger.business.key.mapper;

import com.eplugger.business.key.model.Key;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KeyMappler {
    
    @Insert("INSERT INTO biz_key (id, `key`) VALUES (#{id}, #{key})")
    int insert(Key key);

    @Update("UPDATE biz_key SET `key` = #{key} WHERE id = #{id}")
    int update(Key key);

    @Select("SELECT id, `key` FROM biz_key WHERE `key` = #{key}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "key", column = "key")
    })
    Key findByKey(String key);

    @Select("SELECT id, `key` FROM biz_key")
    List<Key> findAll();
}
