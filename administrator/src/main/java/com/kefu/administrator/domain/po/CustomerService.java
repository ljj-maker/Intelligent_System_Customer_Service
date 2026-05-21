package com.kefu.administrator.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer_service")
public class CustomerService {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工号(内部编号)
     */
    private String staffNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 1男 2女
     */
    private Integer gender;

    /**
     * 最大并发接待数 默认10
     */
    private Integer maxConcurrent;

    /**
     * 职位等级 1-5 默认1
     */
    private Integer level;

    /**
     * 头像
     */
    private String image;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 账号状态 1启用 2禁用 3离职 默认1
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建人(管理员id)
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否注销 0正常 1删除
     */
    private Integer isDeleted;
}
