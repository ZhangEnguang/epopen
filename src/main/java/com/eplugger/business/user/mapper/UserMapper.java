package com.eplugger.business.user.mapper;

import com.eplugger.business.pub.mapper.BusinessMapper;
import com.eplugger.business.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BusinessMapper<User> {


    User findByUsername(@Param("username") String username);

    int changePassword(@Param("id") String id, @Param("newPassword") String newPassword);
}