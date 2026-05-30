package com.kefu.userdto.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息接受实体")
public class UserDTO {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码，不传则不修改")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "性别 1男 2女")
    private Integer gender;

    @Schema(description = "账号状态 1正常 2禁用 3注销")
    private Integer status;
}
