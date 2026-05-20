package com.kefu.administrator.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员列表实体")
public class AdminDto {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "名字")
    private String name;
    @Schema(description = "账号")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "性别")
    private Integer gender;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "账号状态")
    private Integer status;
}
