package com.kefu.administrator.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员信息返回实体")
public class AdminVO {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "账号")
    private String username;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "性别")
    private Integer gender;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "账号状态")
    private Integer status;
}
