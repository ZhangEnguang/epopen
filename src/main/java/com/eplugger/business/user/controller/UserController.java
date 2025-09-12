package com.eplugger.business.user.controller;

import com.eplugger.business.pub.controller.BusinessController;
import com.eplugger.business.user.model.User;
import com.eplugger.business.user.service.UserService;
import com.eplugger.common.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends BusinessController<User, UserService> {

    private final UserService service;

    @PostMapping("/save")
    public Result<User> save(@RequestBody User user) {
        return Result.success(service.save(user));
    }

    @PostMapping("/changePassword/{id}")
    public Result<Integer> changePassword(@PathVariable String id, @RequestParam String password, @RequestParam String oldPassword) {
        if (!service.validate(id, oldPassword)) {
            return Result.failure("旧密码错误");
        }
        return Result.success(service.changePassword(id, password));
    }
}
