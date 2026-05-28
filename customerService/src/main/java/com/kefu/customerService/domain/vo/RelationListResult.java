package com.kefu.customerService.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "客服服务关系列表返回实体")
public class RelationListResult {

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "列表")
    private List<ServiceRelationshipVO> rows;
}
