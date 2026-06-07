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
('李四', 'admin02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 2, '13800138002', 1, 0),
('a', 'a', '$2a$10$foK3JHI3/HIwqMDMyYij8uIMWiAmVjrcTOS/hpQzXPhLBzjxZbC2O', 1, '13800136002', 1, 0);


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
    `staff_id`        BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '客服id: 0机器人/系统通知',
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

-- nacos
create table if not exists config_info
(
    id                 bigint auto_increment comment 'id'
        primary key,
    data_id            varchar(255)                           not null comment 'data_id',
    group_id           varchar(128)                           null comment 'group_id',
    content            longtext                               not null comment 'content',
    md5                varchar(32)                            null comment 'md5',
    gmt_create         datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified       datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    src_user           text                                   null comment 'source user',
    src_ip             varchar(50)                            null comment 'source ip',
    app_name           varchar(128)                           null comment 'app_name',
    tenant_id          varchar(128) default ''                null comment '租户字段',
    c_desc             varchar(256)                           null comment 'configuration description',
    c_use              varchar(64)                            null comment 'configuration usage',
    effect             varchar(64)                            null comment '配置生效的描述',
    type               varchar(64)                            null comment '配置的类型',
    c_schema           text                                   null comment '配置的模式',
    encrypted_data_key text                                   not null comment '密钥',
    constraint uk_configinfo_datagrouptenant
        unique (data_id, group_id, tenant_id)
)
    comment 'config_info' collate = utf8mb3_bin;

create table if not exists config_info_aggr
(
    id           bigint auto_increment comment 'id'
        primary key,
    data_id      varchar(255)            not null comment 'data_id',
    group_id     varchar(128)            not null comment 'group_id',
    datum_id     varchar(255)            not null comment 'datum_id',
    content      longtext                not null comment '内容',
    gmt_modified datetime                not null comment '修改时间',
    app_name     varchar(128)            null comment 'app_name',
    tenant_id    varchar(128) default '' null comment '租户字段',
    constraint uk_configinfoaggr_datagrouptenantdatum
        unique (data_id, group_id, tenant_id, datum_id)
)
    comment '增加租户字段' collate = utf8mb3_bin;

create table if not exists config_info_beta
(
    id                 bigint auto_increment comment 'id'
        primary key,
    data_id            varchar(255)                           not null comment 'data_id',
    group_id           varchar(128)                           not null comment 'group_id',
    app_name           varchar(128)                           null comment 'app_name',
    content            longtext                               not null comment 'content',
    beta_ips           varchar(1024)                          null comment 'betaIps',
    md5                varchar(32)                            null comment 'md5',
    gmt_create         datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified       datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    src_user           text                                   null comment 'source user',
    src_ip             varchar(50)                            null comment 'source ip',
    tenant_id          varchar(128) default ''                null comment '租户字段',
    encrypted_data_key text                                   not null comment '密钥',
    constraint uk_configinfobeta_datagrouptenant
        unique (data_id, group_id, tenant_id)
)
    comment 'config_info_beta' collate = utf8mb3_bin;

create table if not exists config_info_tag
(
    id           bigint auto_increment comment 'id'
        primary key,
    data_id      varchar(255)                           not null comment 'data_id',
    group_id     varchar(128)                           not null comment 'group_id',
    tenant_id    varchar(128) default ''                null comment 'tenant_id',
    tag_id       varchar(128)                           not null comment 'tag_id',
    app_name     varchar(128)                           null comment 'app_name',
    content      longtext                               not null comment 'content',
    md5          varchar(32)                            null comment 'md5',
    gmt_create   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    src_user     text                                   null comment 'source user',
    src_ip       varchar(50)                            null comment 'source ip',
    constraint uk_configinfotag_datagrouptenanttag
        unique (data_id, group_id, tenant_id, tag_id)
)
    comment 'config_info_tag' collate = utf8mb3_bin;

create table if not exists config_tags_relation
(
    id        bigint                  not null comment 'id',
    tag_name  varchar(128)            not null comment 'tag_name',
    tag_type  varchar(64)             null comment 'tag_type',
    data_id   varchar(255)            not null comment 'data_id',
    group_id  varchar(128)            not null comment 'group_id',
    tenant_id varchar(128) default '' null comment 'tenant_id',
    nid       bigint auto_increment comment 'nid, 自增长标识'
        primary key,
    constraint uk_configtagrelation_configidtag
        unique (id, tag_name, tag_type)
)
    comment 'config_tag_relation' collate = utf8mb3_bin;

create index idx_tenant_id
    on config_tags_relation (tenant_id);

create table if not exists group_capacity
(
    id                bigint unsigned auto_increment comment '主键ID'
        primary key,
    group_id          varchar(128) default ''                not null comment 'Group ID，空字符表示整个集群',
    quota             int unsigned default '0'               not null comment '配额，0表示使用默认值',
    `usage`           int unsigned default '0'               not null comment '使用量',
    max_size          int unsigned default '0'               not null comment '单个配置大小上限，单位为字节，0表示使用默认值',
    max_aggr_count    int unsigned default '0'               not null comment '聚合子配置最大个数，，0表示使用默认值',
    max_aggr_size     int unsigned default '0'               not null comment '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    max_history_count int unsigned default '0'               not null comment '最大变更历史数量',
    gmt_create        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified      datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    constraint uk_group_id
        unique (group_id)
)
    comment '集群、各Group容量信息表' collate = utf8mb3_bin;

create table if not exists his_config_info
(
    id                 bigint unsigned                        not null comment 'id',
    nid                bigint unsigned auto_increment comment 'nid, 自增标识'
        primary key,
    data_id            varchar(255)                           not null comment 'data_id',
    group_id           varchar(128)                           not null comment 'group_id',
    app_name           varchar(128)                           null comment 'app_name',
    content            longtext                               not null comment 'content',
    md5                varchar(32)                            null comment 'md5',
    gmt_create         datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified       datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    src_user           text                                   null comment 'source user',
    src_ip             varchar(50)                            null comment 'source ip',
    op_type            char(10)                               null comment 'operation type',
    tenant_id          varchar(128) default ''                null comment '租户字段',
    encrypted_data_key text                                   not null comment '密钥'
)
    comment '多租户改造' collate = utf8mb3_bin;

create index idx_did
    on his_config_info (data_id);

create index idx_gmt_create
    on his_config_info (gmt_create);

create index idx_gmt_modified
    on his_config_info (gmt_modified);

create table if not exists permissions
(
    role     varchar(50)  not null comment 'role',
    resource varchar(255) not null comment 'resource',
    action   varchar(8)   not null comment 'action',
    constraint uk_role_permission
        unique (role, resource, action)
);

create table if not exists roles
(
    username varchar(50) not null comment 'username',
    role     varchar(50) not null comment 'role',
    constraint idx_user_role
        unique (username, role)
);

create table if not exists tenant_capacity
(
    id                bigint unsigned auto_increment comment '主键ID'
        primary key,
    tenant_id         varchar(128) default ''                not null comment 'Tenant ID',
    quota             int unsigned default '0'               not null comment '配额，0表示使用默认值',
    `usage`           int unsigned default '0'               not null comment '使用量',
    max_size          int unsigned default '0'               not null comment '单个配置大小上限，单位为字节，0表示使用默认值',
    max_aggr_count    int unsigned default '0'               not null comment '聚合子配置最大个数',
    max_aggr_size     int unsigned default '0'               not null comment '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    max_history_count int unsigned default '0'               not null comment '最大变更历史数量',
    gmt_create        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified      datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    constraint uk_tenant_id
        unique (tenant_id)
)
    comment '租户容量信息表' collate = utf8mb3_bin;

create table if not exists tenant_info
(
    id            bigint auto_increment comment 'id'
        primary key,
    kp            varchar(128)            not null comment 'kp',
    tenant_id     varchar(128) default '' null comment 'tenant_id',
    tenant_name   varchar(128) default '' null comment 'tenant_name',
    tenant_desc   varchar(256)            null comment 'tenant_desc',
    create_source varchar(32)             null comment 'create_source',
    gmt_create    bigint                  not null comment '创建时间',
    gmt_modified  bigint                  not null comment '修改时间',
    constraint uk_tenant_info_kptenantid
        unique (kp, tenant_id)
)
    comment 'tenant_info' collate = utf8mb3_bin;

create index idx_tenant_id
    on tenant_info (tenant_id);

create table if not exists users
(
    username varchar(50)  not null comment 'username'
        primary key,
    password varchar(500) not null comment 'password',
    enabled  tinyint(1)   not null comment 'enabled'
);


