package com.eplugger.business.user.service.impl;

import com.eplugger.business.pub.service.impl.BusinessServiceImpl;
import com.eplugger.business.user.mapper.UserMapper;
import com.eplugger.business.user.model.User;
import com.eplugger.business.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends BusinessServiceImpl<User, UserMapper> implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    @Transactional
    public User save(User user) {
        boolean isNew = StringUtils.isBlank(user.getId());
        // 保存数据
        if (isNew) {
            insert(user);
        } else {
            mapper.update(user);
        }

        return user;
    }

    @Override
    @Transactional
    public int changePassword(String id, String password) {
        String newPassword = passwordEncoder.encode(password);
        return userMapper.changePassword(id, newPassword);
    }

    @Override
    public boolean validate(String id, String oldPassword) {
        User user = userMapper.findById(id);
        if (user == null) {
            return false;
        }
        // 使用matches方法进行密码比对，而不是重新加密比较
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public User insert(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }
} 