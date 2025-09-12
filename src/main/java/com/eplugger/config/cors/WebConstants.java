package com.eplugger.config.cors;

import java.util.Arrays;
import java.util.List;

/**
 * Web相关常量配置
 */
public class WebConstants {

    /**
     * 允许的源列表
     */
    public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:3839", // 本地管理端
            "http://172.16.8.7:3839" // 正式用户端
    );

    /**
     * 允许的HTTP方法
     */
    public static final List<String> ALLOWED_METHODS = Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
    );

    /**
     * 允许的头部
     */
    public static final List<String> ALLOWED_HEADERS = Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With"
    );

    /**
     * 暴露的头部
     */
    public static final List<String> EXPOSED_HEADERS = List.of("Authorization");

    /**
     * 最大缓存时间（秒）
     */
    public static final long MAX_AGE = 3600L;

    /**
     * 公开API路径
     */
    public static final String[] PUBLIC_PATHS = {
            "/api/auth/**",
            "/api/public/**"
    };
} 