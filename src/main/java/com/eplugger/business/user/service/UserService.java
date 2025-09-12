package com.eplugger.business.user.service;


import com.eplugger.business.pub.service.BusinessService;
import com.eplugger.business.user.model.User;

public interface UserService extends BusinessService<User> {
    User findByUsername(String username);

    User save(User user);

    int changePassword(String id, String password);

    boolean validate(String id, String oldPassword);
}