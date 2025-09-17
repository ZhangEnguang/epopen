package com.eplugger.business.key.controller;

import com.eplugger.business.key.model.Key;
import com.eplugger.business.key.service.KeyService;
import com.eplugger.common.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/key")
public class KeyController {
    private final KeyService service;

    @PostMapping("/generate")
    public Result<Key> generate() {
        return Result.success(service.generate());
    }

    @PostMapping("/compare")
    public Result<Boolean> compareKey(@RequestBody Key key) {
        return Result.success(service.compare(key));
    }
}
