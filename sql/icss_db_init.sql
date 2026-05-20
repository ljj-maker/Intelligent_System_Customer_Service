-- 数据库: icss_db
-- 字符集: utf8mb4
-- 创建时间: 2026-05-20

CREATE DATABASE IF NOT EXISTS `icss_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `icss_db`;

-- ====================
-- 1. 管理员表 (administrator)
-- ====================
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator` (
    `id`              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `name`            VARCHAR(20)  NOT NULL COMMENT '姓名',
    `username`        VARCHAR(20)  NOT NULL UNIQUE COMMENT '账号',
    `password`        VARCHAR(255) NOT NULL COMMENT '密码',
    `gender`          TINYINT UNSIGNED COMMENT '性别: 1男, 2女',
    `phone`           CHAR(11)     NOT NULL UNIQUE COMMENT '手机号',
    `status`          TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '账号状态: 1启用, 2禁用',
    `is_deleted`      TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否注销: 0正常, 1删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- 管理员数据 (2条)
INSERT INTO `administrator` (`name`, `username`, `password`, `gender`, `phone`, `status`, `is_deleted`) VALUES
('张三', 'admin01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 1, '13800138001', 1, 0),
('李四', 'admin02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 2, '13800138002', 1, 0);


-- ====================
-- 2. 客服表 (customer_service)
-- ====================
DROP TABLE IF EXISTS `customer_service`;
CREATE TABLE `customer_service` (
    `id`              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `staff_no`        VARCHAR(10)  NOT NULL UNIQUE COMMENT '工号(内部编号)',
    `name`            VARCHAR(10)  NOT NULL COMMENT '姓名',
    `username`        VARCHAR(20)  NOT NULL UNIQUE COMMENT '用户名',
    `password`        VARCHAR(255) NOT NULL COMMENT '密码',
    `phone`           CHAR(11)     NOT NULL UNIQUE COMMENT '手机号',
    `gender`          TINYINT UNSIGNED COMMENT '性别: 1男, 2女',
    `max_concurrent`  TINYINT UNSIGNED NOT NULL DEFAULT 10 COMMENT '最大并发接待数',
    `level`           TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '职位等级: 1-5',
    `image`           VARCHAR(255) DEFAULT '......' COMMENT '头像',
    `entry_date`      DATE COMMENT '入职日期',
    `status`          TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '账号状态: 1启用, 2禁用, 3离职',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `create_by`       BIGINT UNSIGNED COMMENT '创建人(管理员id)',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted`      TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否注销: 0正常, 1删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客服表';

-- 客服数据 (5条)
INSERT INTO `customer_service` 
(`staff_no`, `name`, `username`, `password`, `phone`, `gender`, `max_concurrent`, `level`, `image`, `entry_date`, `status`, `create_by`, `create_time`, `update_time`, `is_deleted`) 
VALUES
('CS001', '王五', 'staff01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13900139001', 1, 10, 1, 'https://example.com/avatar/staff01.jpg', '2024-01-15', 1, 1, '2024-01-15 09:00:00', '2024-01-15 09:00:00', 0),
('CS002', '赵六', 'staff02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13900139002', 2, 15, 2, 'https://example.com/avatar/staff02.jpg', '2024-03-01', 1, 1, '2024-03-01 10:00:00', '2024-03-01 10:00:00', 0),
('CS003', '孙七', 'staff03', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13900139003', 1, 8,  1, 'https://example.com/avatar/staff03.jpg', '2024-05-20', 1, 1, '2024-05-20 14:00:00', '2024-05-20 14:00:00', 0),
('CS004', '周八', 'staff04', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13900139004', 2, 20, 3, 'https://example.com/avatar/staff04.jpg', '2023-11-10', 2, 1, '2023-11-10 08:30:00', '2024-06-01 16:00:00', 0),
('CS005', '吴九', 'staff05', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13900139005', 1, 12, 2, 'https://example.com/avatar/staff05.jpg', '2024-02-28', 3, 1, '2024-02-28 11:00:00', '2024-07-15 09:00:00', 0);


-- ====================
-- 3. 用户表 (user)
-- ====================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `username`    VARCHAR(20)  NOT NULL UNIQUE COMMENT '用户名',
    `password`    VARCHAR(255) COMMENT '密码',
    `phone`       CHAR(11) UNIQUE COMMENT '手机号',
    `avatar`      VARCHAR(255) DEFAULT '......' COMMENT '头像URL',
    `gender`      TINYINT UNSIGNED COMMENT '性别: 1男, 2女',
    `status`      TINYINT DEFAULT 1 COMMENT '账号状态: 1正常, 2禁用, 3注销',
    `is_deleted`  TINYINT DEFAULT 0 COMMENT '是否注销: 0正常, 1删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户数据 (10条)
INSERT INTO `user` (`username`, `password`, `phone`, `avatar`, `gender`, `status`, `is_deleted`, `create_time`, `update_time`) VALUES
('user01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137001', 'https://example.com/avatar/user01.jpg', 1, 1, 0, '2024-01-10 08:00:00', '2024-01-10 08:00:00'),
('user02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137002', 'https://example.com/avatar/user02.jpg', 2, 1, 0, '2024-02-15 09:30:00', '2024-02-15 09:30:00'),
('user03', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137003', 'https://example.com/avatar/user03.jpg', 1, 1, 0, '2024-03-20 10:00:00', '2024-03-20 10:00:00'),
('user04', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137004', 'https://example.com/avatar/user04.jpg', 2, 2, 0, '2024-04-05 11:15:00', '2024-05-01 14:20:00'),
('user05', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137005', 'https://example.com/avatar/user05.jpg', 1, 1, 0, '2024-05-10 12:00:00', '2024-05-10 12:00:00'),
('user06', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137006', 'https://example.com/avatar/user06.jpg', 2, 1, 0, '2024-06-18 13:45:00', '2024-06-18 13:45:00'),
('user07', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137007', 'https://example.com/avatar/user07.jpg', 1, 3, 0, '2024-07-22 15:00:00', '2024-08-10 09:00:00'),
('user08', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137008', 'https://example.com/avatar/user08.jpg', 2, 1, 0, '2024-08-30 16:30:00', '2024-08-30 16:30:00'),
('user09', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137009', 'https://example.com/avatar/user09.jpg', 1, 1, 0, '2024-09-12 17:00:00', '2024-09-12 17:00:00'),
('user10', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '13700137010', 'https://example.com/avatar/user10.jpg', 2, 1, 0, '2024-10-25 18:00:00', '2024-10-25 18:00:00');


-- ====================
-- 4. 对话表 (chat_message)
-- ====================
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id`              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `user_id`         BIGINT UNSIGNED NOT NULL COMMENT '用户id',
    `staff_id`        TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '客服id: 0机器人/系统通知',
    `sender_type`     TINYINT UNSIGNED NOT NULL COMMENT '发送人: 1用户, 2客服, 3机器人, 4系统消息',
    `sender_name`     VARCHAR(20) COMMENT '发送者名称',
    `sender_avatar`   VARCHAR(255) COMMENT '发送者头像',
    `msg_type`        TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '消息类型: 1文本, 2图片',
    `content`         TEXT COMMENT '文本内容',
    `file_url`        VARCHAR(255) COMMENT '图片地址',
    `is_read`         TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已读: 0未读, 1已读',
    `read_time`       DATETIME COMMENT '已读时间',
    `client_msg_id`   VARCHAR(64) COMMENT '消息幂等id',
    `quote_msg_id`    BIGINT UNSIGNED COMMENT '引用消息id',
    `is_recalled`     TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否撤回: 0正常, 1撤回',
    `send_time`       DATETIME NOT NULL COMMENT '消息发送时间',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_staff_id` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对话表';

-- 对话记录暂不添加数据

