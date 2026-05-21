USE `icss_db`;

-- ====================
-- 5. 客服-用户服务关系表 (service_relationship)
-- ====================
DROP TABLE IF EXISTS `service_relationship`;
CREATE TABLE `service_relationship` (
    `id`            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `user_id`       BIGINT UNSIGNED NOT NULL COMMENT '用户id',
    `staff_id`      TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '客服id: 0机器人/系统通知',
    
    -- 联合唯一索引：允许一个用户绑定多个客服，也允许多个用户绑定同一客服，
    -- 仅禁止同一组(user_id, staff_id)重复。后端插入时捕获唯一冲突异常即可
    UNIQUE KEY `uk_user_staff` (`user_id`, `staff_id`) COMMENT '用户与客服联合唯一索引'
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客服-用户服务关系表';