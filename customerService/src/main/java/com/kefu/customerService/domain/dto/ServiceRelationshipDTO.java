package com.kefu.customerService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客服服务关系接收实体")
public class ServiceRelationshipDTO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "客服id")
    private Long staffId;
}
