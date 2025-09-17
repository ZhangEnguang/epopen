-- 创建数据库
create
database epopen;
ALTER
DATABASE epopen CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE `sys_user`
(
    `id`       VARCHAR(36)   NOT NULL COMMENT '主键',
    `username` VARCHAR(100)  NOT NULL COMMENT '用户名',
    `password` VARCHAR(2000) NOT NULL COMMENT '密码',
    `enabled`  TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

INSERT INTO sys_user (id, username, password, enabled)
VALUES ('79d266a6b79444a6a71a02c550c06184', 'admin', '$2a$10$v/OHOrW/wbp62B4/xMJgt.SDVqPqxyX4UBp2d65vrsgULegHNMYcq', 1);

-- biz_key 表建表语句
CREATE TABLE `biz_key`
(
    `id`          VARCHAR(64)  NOT NULL COMMENT '主键ID',
    `key`         VARCHAR(255) NOT NULL COMMENT '密钥内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='密钥表';
