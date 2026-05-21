package com.kefu.administrator.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "客服信息接受实体")
public class CustomerServiceDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "工号")
    private String staffNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别 1男 2女")
    private Integer gender;

    @Schema(description = "最大并发接待数")
    private Integer maxConcurrent;

    @Schema(description = "职位等级 1-5")
    private Integer level;

    @Schema(description = "头像")
    private String image;

    @Schema(description = "入职日期")
    private LocalDate entryDate;

    @Schema(description = "账号状态 1启用 2禁用 3离职")
    private Integer status;
}
