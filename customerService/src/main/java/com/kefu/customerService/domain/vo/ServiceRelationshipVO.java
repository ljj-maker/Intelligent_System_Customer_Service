package com.kefu.customerService.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客服服务关系返回实体")
public class ServiceRelationshipVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "客服id")
    private Integer staffId;
}
